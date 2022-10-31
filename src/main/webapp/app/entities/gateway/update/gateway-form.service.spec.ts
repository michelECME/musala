import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../gateway.test-samples';

import { GatewayFormService } from './gateway-form.service';

describe('Gateway Form Service', () => {
  let service: GatewayFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GatewayFormService);
  });

  describe('Service methods', () => {
    describe('createGatewayFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGatewayFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serial_number: expect.any(Object),
            name: expect.any(Object),
            ip_address: expect.any(Object),
          })
        );
      });

      it('passing IGateway should create a new form with FormGroup', () => {
        const formGroup = service.createGatewayFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            serial_number: expect.any(Object),
            name: expect.any(Object),
            ip_address: expect.any(Object),
          })
        );
      });
    });

    describe('getGateway', () => {
      it('should return NewGateway for default Gateway initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGatewayFormGroup(sampleWithNewData);

        const gateway = service.getGateway(formGroup) as any;

        expect(gateway).toMatchObject(sampleWithNewData);
      });

      it('should return NewGateway for empty Gateway initial value', () => {
        const formGroup = service.createGatewayFormGroup();

        const gateway = service.getGateway(formGroup) as any;

        expect(gateway).toMatchObject({});
      });

      it('should return IGateway', () => {
        const formGroup = service.createGatewayFormGroup(sampleWithRequiredData);

        const gateway = service.getGateway(formGroup) as any;

        expect(gateway).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGateway should not enable id FormControl', () => {
        const formGroup = service.createGatewayFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGateway should disable id FormControl', () => {
        const formGroup = service.createGatewayFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
