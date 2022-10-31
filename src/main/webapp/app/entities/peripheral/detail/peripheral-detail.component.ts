import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeripheral } from '../peripheral.model';

@Component({
  selector: 'jhi-peripheral-detail',
  templateUrl: './peripheral-detail.component.html',
})
export class PeripheralDetailComponent implements OnInit {
  peripheral: IPeripheral | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ peripheral }) => {
      this.peripheral = peripheral;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
