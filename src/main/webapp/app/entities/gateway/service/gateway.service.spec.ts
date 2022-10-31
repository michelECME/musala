import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGateway } from '../gateway.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../gateway.test-samples';

import { GatewayService } from './gateway.service';

const requireRestSample: IGateway = {
  ...sampleWithRequiredData,
};

describe('Gateway Service', () => {
  let service: GatewayService;
  let httpMock: HttpTestingController;
  let expectedResult: IGateway | IGateway[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GatewayService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Gateway', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gateway = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gateway).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Gateway', () => {
      const gateway = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gateway).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Gateway', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Gateway', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Gateway', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGatewayToCollectionIfMissing', () => {
      it('should add a Gateway to an empty array', () => {
        const gateway: IGateway = sampleWithRequiredData;
        expectedResult = service.addGatewayToCollectionIfMissing([], gateway);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gateway);
      });

      it('should not add a Gateway to an array that contains it', () => {
        const gateway: IGateway = sampleWithRequiredData;
        const gatewayCollection: IGateway[] = [
          {
            ...gateway,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGatewayToCollectionIfMissing(gatewayCollection, gateway);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Gateway to an array that doesn't contain it", () => {
        const gateway: IGateway = sampleWithRequiredData;
        const gatewayCollection: IGateway[] = [sampleWithPartialData];
        expectedResult = service.addGatewayToCollectionIfMissing(gatewayCollection, gateway);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gateway);
      });

      it('should add only unique Gateway to an array', () => {
        const gatewayArray: IGateway[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gatewayCollection: IGateway[] = [sampleWithRequiredData];
        expectedResult = service.addGatewayToCollectionIfMissing(gatewayCollection, ...gatewayArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gateway: IGateway = sampleWithRequiredData;
        const gateway2: IGateway = sampleWithPartialData;
        expectedResult = service.addGatewayToCollectionIfMissing([], gateway, gateway2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gateway);
        expect(expectedResult).toContain(gateway2);
      });

      it('should accept null and undefined values', () => {
        const gateway: IGateway = sampleWithRequiredData;
        expectedResult = service.addGatewayToCollectionIfMissing([], null, gateway, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gateway);
      });

      it('should return initial array if no Gateway is added', () => {
        const gatewayCollection: IGateway[] = [sampleWithRequiredData];
        expectedResult = service.addGatewayToCollectionIfMissing(gatewayCollection, undefined, null);
        expect(expectedResult).toEqual(gatewayCollection);
      });
    });

    describe('compareGateway', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGateway(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGateway(entity1, entity2);
        const compareResult2 = service.compareGateway(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGateway(entity1, entity2);
        const compareResult2 = service.compareGateway(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGateway(entity1, entity2);
        const compareResult2 = service.compareGateway(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
