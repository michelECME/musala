import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGateway, NewGateway } from '../gateway.model';

export type PartialUpdateGateway = Partial<IGateway> & Pick<IGateway, 'id'>;

export type EntityResponseType = HttpResponse<IGateway>;
export type EntityArrayResponseType = HttpResponse<IGateway[]>;

@Injectable({ providedIn: 'root' })
export class GatewayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gateways');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gateway: NewGateway): Observable<EntityResponseType> {
    return this.http.post<IGateway>(this.resourceUrl, gateway, { observe: 'response' });
  }

  update(gateway: IGateway): Observable<EntityResponseType> {
    return this.http.put<IGateway>(`${this.resourceUrl}/${this.getGatewayIdentifier(gateway)}`, gateway, { observe: 'response' });
  }

  partialUpdate(gateway: PartialUpdateGateway): Observable<EntityResponseType> {
    return this.http.patch<IGateway>(`${this.resourceUrl}/${this.getGatewayIdentifier(gateway)}`, gateway, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGateway>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGateway[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGatewayIdentifier(gateway: Pick<IGateway, 'id'>): number {
    return gateway.id;
  }

  compareGateway(o1: Pick<IGateway, 'id'> | null, o2: Pick<IGateway, 'id'> | null): boolean {
    return o1 && o2 ? this.getGatewayIdentifier(o1) === this.getGatewayIdentifier(o2) : o1 === o2;
  }

  addGatewayToCollectionIfMissing<Type extends Pick<IGateway, 'id'>>(
    gatewayCollection: Type[],
    ...gatewaysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gateways: Type[] = gatewaysToCheck.filter(isPresent);
    if (gateways.length > 0) {
      const gatewayCollectionIdentifiers = gatewayCollection.map(gatewayItem => this.getGatewayIdentifier(gatewayItem)!);
      const gatewaysToAdd = gateways.filter(gatewayItem => {
        const gatewayIdentifier = this.getGatewayIdentifier(gatewayItem);
        if (gatewayCollectionIdentifiers.includes(gatewayIdentifier)) {
          return false;
        }
        gatewayCollectionIdentifiers.push(gatewayIdentifier);
        return true;
      });
      return [...gatewaysToAdd, ...gatewayCollection];
    }
    return gatewayCollection;
  }
}
