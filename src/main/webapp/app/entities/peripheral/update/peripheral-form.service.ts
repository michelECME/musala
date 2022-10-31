import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPeripheral, NewPeripheral } from '../peripheral.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPeripheral for edit and NewPeripheralFormGroupInput for create.
 */
type PeripheralFormGroupInput = IPeripheral | PartialWithRequiredKeyOf<NewPeripheral>;

type PeripheralFormDefaults = Pick<NewPeripheral, 'id'>;

type PeripheralFormGroupContent = {
  id: FormControl<IPeripheral['id'] | NewPeripheral['id']>;
  uid: FormControl<IPeripheral['uid']>;
  vendor: FormControl<IPeripheral['vendor']>;
  date_created: FormControl<IPeripheral['date_created']>;
  status: FormControl<IPeripheral['status']>;
  gateway: FormControl<IPeripheral['gateway']>;
};

export type PeripheralFormGroup = FormGroup<PeripheralFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PeripheralFormService {
  createPeripheralFormGroup(peripheral: PeripheralFormGroupInput = { id: null }): PeripheralFormGroup {
    const peripheralRawValue = {
      ...this.getFormDefaults(),
      ...peripheral,
    };
    return new FormGroup<PeripheralFormGroupContent>({
      id: new FormControl(
        { value: peripheralRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      uid: new FormControl(peripheralRawValue.uid, {
        validators: [Validators.required],
      }),
      vendor: new FormControl(peripheralRawValue.vendor, {
        validators: [Validators.required],
      }),
      date_created: new FormControl(peripheralRawValue.date_created, {
        validators: [Validators.required],
      }),
      status: new FormControl(peripheralRawValue.status, {
        validators: [Validators.required],
      }),
      gateway: new FormControl(peripheralRawValue.gateway),
    });
  }

  getPeripheral(form: PeripheralFormGroup): IPeripheral | NewPeripheral {
    return form.getRawValue() as IPeripheral | NewPeripheral;
  }

  resetForm(form: PeripheralFormGroup, peripheral: PeripheralFormGroupInput): void {
    const peripheralRawValue = { ...this.getFormDefaults(), ...peripheral };
    form.reset(
      {
        ...peripheralRawValue,
        id: { value: peripheralRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PeripheralFormDefaults {
    return {
      id: null,
    };
  }
}
