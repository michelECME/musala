import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPeripheral } from '../peripheral.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../peripheral.test-samples';

import { PeripheralService, RestPeripheral } from './peripheral.service';

const requireRestSample: RestPeripheral = {
  ...sampleWithRequiredData,
  date_created: sampleWithRequiredData.date_created?.format(DATE_FORMAT),
};

describe('Peripheral Service', () => {
  let service: PeripheralService;
  let httpMock: HttpTestingController;
  let expectedResult: IPeripheral | IPeripheral[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PeripheralService);
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

    it('should create a Peripheral', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const peripheral = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(peripheral).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Peripheral', () => {
      const peripheral = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(peripheral).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Peripheral', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Peripheral', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Peripheral', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPeripheralToCollectionIfMissing', () => {
      it('should add a Peripheral to an empty array', () => {
        const peripheral: IPeripheral = sampleWithRequiredData;
        expectedResult = service.addPeripheralToCollectionIfMissing([], peripheral);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(peripheral);
      });

      it('should not add a Peripheral to an array that contains it', () => {
        const peripheral: IPeripheral = sampleWithRequiredData;
        const peripheralCollection: IPeripheral[] = [
          {
            ...peripheral,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPeripheralToCollectionIfMissing(peripheralCollection, peripheral);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Peripheral to an array that doesn't contain it", () => {
        const peripheral: IPeripheral = sampleWithRequiredData;
        const peripheralCollection: IPeripheral[] = [sampleWithPartialData];
        expectedResult = service.addPeripheralToCollectionIfMissing(peripheralCollection, peripheral);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(peripheral);
      });

      it('should add only unique Peripheral to an array', () => {
        const peripheralArray: IPeripheral[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const peripheralCollection: IPeripheral[] = [sampleWithRequiredData];
        expectedResult = service.addPeripheralToCollectionIfMissing(peripheralCollection, ...peripheralArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const peripheral: IPeripheral = sampleWithRequiredData;
        const peripheral2: IPeripheral = sampleWithPartialData;
        expectedResult = service.addPeripheralToCollectionIfMissing([], peripheral, peripheral2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(peripheral);
        expect(expectedResult).toContain(peripheral2);
      });

      it('should accept null and undefined values', () => {
        const peripheral: IPeripheral = sampleWithRequiredData;
        expectedResult = service.addPeripheralToCollectionIfMissing([], null, peripheral, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(peripheral);
      });

      it('should return initial array if no Peripheral is added', () => {
        const peripheralCollection: IPeripheral[] = [sampleWithRequiredData];
        expectedResult = service.addPeripheralToCollectionIfMissing(peripheralCollection, undefined, null);
        expect(expectedResult).toEqual(peripheralCollection);
      });
    });

    describe('comparePeripheral', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePeripheral(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePeripheral(entity1, entity2);
        const compareResult2 = service.comparePeripheral(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePeripheral(entity1, entity2);
        const compareResult2 = service.comparePeripheral(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePeripheral(entity1, entity2);
        const compareResult2 = service.comparePeripheral(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
