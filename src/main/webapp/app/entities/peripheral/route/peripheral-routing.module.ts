import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PeripheralComponent } from '../list/peripheral.component';
import { PeripheralDetailComponent } from '../detail/peripheral-detail.component';
import { PeripheralUpdateComponent } from '../update/peripheral-update.component';
import { PeripheralRoutingResolveService } from './peripheral-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const peripheralRoute: Routes = [
  {
    path: '',
    component: PeripheralComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeripheralDetailComponent,
    resolve: {
      peripheral: PeripheralRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeripheralUpdateComponent,
    resolve: {
      peripheral: PeripheralRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeripheralUpdateComponent,
    resolve: {
      peripheral: PeripheralRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(peripheralRoute)],
  exports: [RouterModule],
})
export class PeripheralRoutingModule {}
