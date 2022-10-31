import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../peripheral.test-samples';

import { PeripheralFormService } from './peripheral-form.service';

describe('Peripheral Form Service', () => {
  let service: PeripheralFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PeripheralFormService);
  });

  describe('Service methods', () => {
    describe('createPeripheralFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPeripheralFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uid: expect.any(Object),
            vendor: expect.any(Object),
            date_created: expect.any(Object),
            status: expect.any(Object),
            gateway: expect.any(Object),
          })
        );
      });

      it('passing IPeripheral should create a new form with FormGroup', () => {
        const formGroup = service.createPeripheralFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uid: expect.any(Object),
            vendor: expect.any(Object),
            date_created: expect.any(Object),
            status: expect.any(Object),
            gateway: expect.any(Object),
          })
        );
      });
    });

    describe('getPeripheral', () => {
      it('should return NewPeripheral for default Peripheral initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPeripheralFormGroup(sampleWithNewData);

        const peripheral = service.getPeripheral(formGroup) as any;

        expect(peripheral).toMatchObject(sampleWithNewData);
      });

      it('should return NewPeripheral for empty Peripheral initial value', () => {
        const formGroup = service.createPeripheralFormGroup();

        const peripheral = service.getPeripheral(formGroup) as any;

        expect(peripheral).toMatchObject({});
      });

      it('should return IPeripheral', () => {
        const formGroup = service.createPeripheralFormGroup(sampleWithRequiredData);

        const peripheral = service.getPeripheral(formGroup) as any;

        expect(peripheral).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPeripheral should not enable id FormControl', () => {
        const formGroup = service.createPeripheralFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPeripheral should disable id FormControl', () => {
        const formGroup = service.createPeripheralFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
