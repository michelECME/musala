import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeripheral } from '../peripheral.model';
import { PeripheralService } from '../service/peripheral.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './peripheral-delete-dialog.component.html',
})
export class PeripheralDeleteDialogComponent {
  peripheral?: IPeripheral;

  constructor(protected peripheralService: PeripheralService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.peripheralService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
