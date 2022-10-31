import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'gateway',
        data: { pageTitle: 'Gateways' },
        loadChildren: () => import('./gateway/gateway.module').then(m => m.GatewayModule),
      },
      {
        path: 'peripheral',
        data: { pageTitle: 'Peripherals' },
        loadChildren: () => import('./peripheral/peripheral.module').then(m => m.PeripheralModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
