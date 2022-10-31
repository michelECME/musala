import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GatewayFormService, GatewayFormGroup } from './gateway-form.service';
import { IGateway } from '../gateway.model';
import { GatewayService } from '../service/gateway.service';

@Component({
  selector: 'jhi-gateway-update',
  templateUrl: './gateway-update.component.html',
})
export class GatewayUpdateComponent implements OnInit {
  isSaving = false;
  gateway: IGateway | null = null;

  editForm: GatewayFormGroup = this.gatewayFormService.createGatewayFormGroup();

  constructor(
    protected gatewayService: GatewayService,
    protected gatewayFormService: GatewayFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gateway }) => {
      this.gateway = gateway;
      if (gateway) {
        this.updateForm(gateway);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gateway = this.gatewayFormService.getGateway(this.editForm);
    if (gateway.id !== null) {
      this.subscribeToSaveResponse(this.gatewayService.update(gateway));
    } else {
      this.subscribeToSaveResponse(this.gatewayService.create(gateway));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGateway>>): void {
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

  protected updateForm(gateway: IGateway): void {
    this.gateway = gateway;
    this.gatewayFormService.resetForm(this.editForm, gateway);
  }
}
