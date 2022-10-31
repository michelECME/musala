import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGateway, NewGateway } from '../gateway.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGateway for edit and NewGatewayFormGroupInput for create.
 */
type GatewayFormGroupInput = IGateway | PartialWithRequiredKeyOf<NewGateway>;

type GatewayFormDefaults = Pick<NewGateway, 'id'>;

type GatewayFormGroupContent = {
  id: FormControl<IGateway['id'] | NewGateway['id']>;
  serial_number: FormControl<IGateway['serial_number']>;
  name: FormControl<IGateway['name']>;
  ip_address: FormControl<IGateway['ip_address']>;
};

export type GatewayFormGroup = FormGroup<GatewayFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GatewayFormService {
  createGatewayFormGroup(gateway: GatewayFormGroupInput = { id: null }): GatewayFormGroup {
    const gatewayRawValue = {
      ...this.getFormDefaults(),
      ...gateway,
    };
    return new FormGroup<GatewayFormGroupContent>({
      id: new FormControl(
        { value: gatewayRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      serial_number: new FormControl(gatewayRawValue.serial_number, {
        validators: [Validators.required],
      }),
      name: new FormControl(gatewayRawValue.name, {
        validators: [Validators.required],
      }),
      ip_address: new FormControl(gatewayRawValue.ip_address, {
        validators: [
          Validators.required,
          Validators.pattern('^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$'),
        ],
      }),
    });
  }

  getGateway(form: GatewayFormGroup): IGateway | NewGateway {
    return form.getRawValue() as IGateway | NewGateway;
  }

  resetForm(form: GatewayFormGroup, gateway: GatewayFormGroupInput): void {
    const gatewayRawValue = { ...this.getFormDefaults(), ...gateway };
    form.reset(
      {
        ...gatewayRawValue,
        id: { value: gatewayRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GatewayFormDefaults {
    return {
      id: null,
    };
  }
}
