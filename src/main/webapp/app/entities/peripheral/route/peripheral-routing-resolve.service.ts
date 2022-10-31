import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeripheral } from '../peripheral.model';
import { PeripheralService } from '../service/peripheral.service';

@Injectable({ providedIn: 'root' })
export class PeripheralRoutingResolveService implements Resolve<IPeripheral | null> {
  constructor(protected service: PeripheralService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeripheral | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((peripheral: HttpResponse<IPeripheral>) => {
          if (peripheral.body) {
            return of(peripheral.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
