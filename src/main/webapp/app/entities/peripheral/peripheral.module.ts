import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PeripheralComponent } from './list/peripheral.component';
import { PeripheralDetailComponent } from './detail/peripheral-detail.component';
import { PeripheralUpdateComponent } from './update/peripheral-update.component';
import { PeripheralDeleteDialogComponent } from './delete/peripheral-delete-dialog.component';
import { PeripheralRoutingModule } from './route/peripheral-routing.module';

@NgModule({
  imports: [SharedModule, PeripheralRoutingModule],
  declarations: [PeripheralComponent, PeripheralDetailComponent, PeripheralUpdateComponent, PeripheralDeleteDialogComponent],
})
export class PeripheralModule {}
