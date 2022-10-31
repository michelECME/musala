import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeripheral, NewPeripheral } from '../peripheral.model';

export type PartialUpdatePeripheral = Partial<IPeripheral> & Pick<IPeripheral, 'id'>;

type RestOf<T extends IPeripheral | NewPeripheral> = Omit<T, 'date_created'> & {
  date_created?: string | null;
};

export type RestPeripheral = RestOf<IPeripheral>;

export type NewRestPeripheral = RestOf<NewPeripheral>;

export type PartialUpdateRestPeripheral = RestOf<PartialUpdatePeripheral>;

export type EntityResponseType = HttpResponse<IPeripheral>;
export type EntityArrayResponseType = HttpResponse<IPeripheral[]>;

@Injectable({ providedIn: 'root' })
export class PeripheralService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/peripherals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(peripheral: NewPeripheral): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peripheral);
    return this.http
      .post<RestPeripheral>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(peripheral: IPeripheral): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peripheral);
    return this.http
      .put<RestPeripheral>(`${this.resourceUrl}/${this.getPeripheralIdentifier(peripheral)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(peripheral: PartialUpdatePeripheral): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(peripheral);
    return this.http
      .patch<RestPeripheral>(`${this.resourceUrl}/${this.getPeripheralIdentifier(peripheral)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPeripheral>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPeripheral[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPeripheralIdentifier(peripheral: Pick<IPeripheral, 'id'>): number {
    return peripheral.id;
  }

  comparePeripheral(o1: Pick<IPeripheral, 'id'> | null, o2: Pick<IPeripheral, 'id'> | null): boolean {
    return o1 && o2 ? this.getPeripheralIdentifier(o1) === this.getPeripheralIdentifier(o2) : o1 === o2;
  }

  addPeripheralToCollectionIfMissing<Type extends Pick<IPeripheral, 'id'>>(
    peripheralCollection: Type[],
    ...peripheralsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const peripherals: Type[] = peripheralsToCheck.filter(isPresent);
    if (peripherals.length > 0) {
      const peripheralCollectionIdentifiers = peripheralCollection.map(peripheralItem => this.getPeripheralIdentifier(peripheralItem)!);
      const peripheralsToAdd = peripherals.filter(peripheralItem => {
        const peripheralIdentifier = this.getPeripheralIdentifier(peripheralItem);
        if (peripheralCollectionIdentifiers.includes(peripheralIdentifier)) {
          return false;
        }
        peripheralCollectionIdentifiers.push(peripheralIdentifier);
        return true;
      });
      return [...peripheralsToAdd, ...peripheralCollection];
    }
    return peripheralCollection;
  }

  protected convertDateFromClient<T extends IPeripheral | NewPeripheral | PartialUpdatePeripheral>(peripheral: T): RestOf<T> {
    return {
      ...peripheral,
      date_created: peripheral.date_created?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPeripheral: RestPeripheral): IPeripheral {
    return {
      ...restPeripheral,
      date_created: restPeripheral.date_created ? dayjs(restPeripheral.date_created) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPeripheral>): HttpResponse<IPeripheral> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPeripheral[]>): HttpResponse<IPeripheral[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
