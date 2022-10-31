import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PeripheralFormService } from './peripheral-form.service';
import { PeripheralService } from '../service/peripheral.service';
import { IPeripheral } from '../peripheral.model';
import { IGateway } from 'app/entities/gateway/gateway.model';
import { GatewayService } from 'app/entities/gateway/service/gateway.service';

import { PeripheralUpdateComponent } from './peripheral-update.component';

describe('Peripheral Management Update Component', () => {
  let comp: PeripheralUpdateComponent;
  let fixture: ComponentFixture<PeripheralUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let peripheralFormService: PeripheralFormService;
  let peripheralService: PeripheralService;
  let gatewayService: GatewayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PeripheralUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PeripheralUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeripheralUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    peripheralFormService = TestBed.inject(PeripheralFormService);
    peripheralService = TestBed.inject(PeripheralService);
    gatewayService = TestBed.inject(GatewayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Gateway query and add missing value', () => {
      const peripheral: IPeripheral = { id: 456 };
      const gateway: IGateway = { id: 93484 };
      peripheral.gateway = gateway;

      const gatewayCollection: IGateway[] = [{ id: 81554 }];
      jest.spyOn(gatewayService, 'query').mockReturnValue(of(new HttpResponse({ body: gatewayCollection })));
      const additionalGateways = [gateway];
      const expectedCollection: IGateway[] = [...additionalGateways, ...gatewayCollection];
      jest.spyOn(gatewayService, 'addGatewayToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ peripheral });
      comp.ngOnInit();

      expect(gatewayService.query).toHaveBeenCalled();
      expect(gatewayService.addGatewayToCollectionIfMissing).toHaveBeenCalledWith(
        gatewayCollection,
        ...additionalGateways.map(expect.objectContaining)
      );
      expect(comp.gatewaysSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const peripheral: IPeripheral = { id: 456 };
      const gateway: IGateway = { id: 96897 };
      peripheral.gateway = gateway;

      activatedRoute.data = of({ peripheral });
      comp.ngOnInit();

      expect(comp.gatewaysSharedCollection).toContain(gateway);
      expect(comp.peripheral).toEqual(peripheral);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeripheral>>();
      const peripheral = { id: 123 };
      jest.spyOn(peripheralFormService, 'getPeripheral').mockReturnValue(peripheral);
      jest.spyOn(peripheralService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peripheral });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peripheral }));
      saveSubject.complete();

      // THEN
      expect(peripheralFormService.getPeripheral).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(peripheralService.update).toHaveBeenCalledWith(expect.objectContaining(peripheral));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeripheral>>();
      const peripheral = { id: 123 };
      jest.spyOn(peripheralFormService, 'getPeripheral').mockReturnValue({ id: null });
      jest.spyOn(peripheralService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peripheral: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: peripheral }));
      saveSubject.complete();

      // THEN
      expect(peripheralFormService.getPeripheral).toHaveBeenCalled();
      expect(peripheralService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPeripheral>>();
      const peripheral = { id: 123 };
      jest.spyOn(peripheralService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ peripheral });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(peripheralService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGateway', () => {
      it('Should forward to gatewayService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gatewayService, 'compareGateway');
        comp.compareGateway(entity, entity2);
        expect(gatewayService.compareGateway).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
