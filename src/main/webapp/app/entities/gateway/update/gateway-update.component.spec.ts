import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GatewayFormService } from './gateway-form.service';
import { GatewayService } from '../service/gateway.service';
import { IGateway } from '../gateway.model';

import { GatewayUpdateComponent } from './gateway-update.component';

describe('Gateway Management Update Component', () => {
  let comp: GatewayUpdateComponent;
  let fixture: ComponentFixture<GatewayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gatewayFormService: GatewayFormService;
  let gatewayService: GatewayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GatewayUpdateComponent],
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
      .overrideTemplate(GatewayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GatewayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gatewayFormService = TestBed.inject(GatewayFormService);
    gatewayService = TestBed.inject(GatewayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gateway: IGateway = { id: 456 };

      activatedRoute.data = of({ gateway });
      comp.ngOnInit();

      expect(comp.gateway).toEqual(gateway);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGateway>>();
      const gateway = { id: 123 };
      jest.spyOn(gatewayFormService, 'getGateway').mockReturnValue(gateway);
      jest.spyOn(gatewayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gateway });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gateway }));
      saveSubject.complete();

      // THEN
      expect(gatewayFormService.getGateway).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gatewayService.update).toHaveBeenCalledWith(expect.objectContaining(gateway));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGateway>>();
      const gateway = { id: 123 };
      jest.spyOn(gatewayFormService, 'getGateway').mockReturnValue({ id: null });
      jest.spyOn(gatewayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gateway: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gateway }));
      saveSubject.complete();

      // THEN
      expect(gatewayFormService.getGateway).toHaveBeenCalled();
      expect(gatewayService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGateway>>();
      const gateway = { id: 123 };
      jest.spyOn(gatewayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gateway });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gatewayService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
