import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PeripheralFormService, PeripheralFormGroup } from './peripheral-form.service';
import { IPeripheral } from '../peripheral.model';
import { PeripheralService } from '../service/peripheral.service';
import { IGateway } from 'app/entities/gateway/gateway.model';
import { GatewayService } from 'app/entities/gateway/service/gateway.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-peripheral-update',
  templateUrl: './peripheral-update.component.html',
})
export class PeripheralUpdateComponent implements OnInit {
  isSaving = false;
  peripheral: IPeripheral | null = null;
  statusValues = Object.keys(Status);

  gatewaysSharedCollection: IGateway[] = [];

  editForm: PeripheralFormGroup = this.peripheralFormService.createPeripheralFormGroup();

  constructor(
    protected peripheralService: PeripheralService,
    protected peripheralFormService: PeripheralFormService,
    protected gatewayService: GatewayService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGateway = (o1: IGateway | null, o2: IGateway | null): boolean => this.gatewayService.compareGateway(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peripheral }) => {
      this.peripheral = peripheral;
      if (peripheral) {
        this.updateForm(peripheral);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const peripheral = this.peripheralFormService.getPeripheral(this.editForm);
    if (peripheral.id !== null) {
      this.subscribeToSaveResponse(this.peripheralService.update(peripheral));
    } else {
      this.subscribeToSaveResponse(this.peripheralService.create(peripheral));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeripheral>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(peripheral: IPeripheral): void {
    this.peripheral = peripheral;
    this.peripheralFormService.resetForm(this.editForm, peripheral);

    this.gatewaysSharedCollection = this.gatewayService.addGatewayToCollectionIfMissing<IGateway>(
      this.gatewaysSharedCollection,
      peripheral.gateway
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gatewayService
      .query()
      .pipe(map((res: HttpResponse<IGateway[]>) => res.body ?? []))
      .pipe(
        map((gateways: IGateway[]) => this.gatewayService.addGatewayToCollectionIfMissing<IGateway>(gateways, this.peripheral?.gateway))
      )
      .subscribe((gateways: IGateway[]) => (this.gatewaysSharedCollection = gateways));
  }
}
