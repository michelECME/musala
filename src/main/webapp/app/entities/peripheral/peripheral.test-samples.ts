import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IPeripheral, NewPeripheral } from './peripheral.model';

export const sampleWithRequiredData: IPeripheral = {
  id: 1339,
  uid: 96596,
  vendor: 'bus Unbranded',
  date_created: dayjs('2022-10-31'),
  status: Status['OFFICE'],
};

export const sampleWithPartialData: IPeripheral = {
  id: 44419,
  uid: 19473,
  vendor: 'Internal edge',
  date_created: dayjs('2022-10-31'),
  status: Status['OFFICE'],
};

export const sampleWithFullData: IPeripheral = {
  id: 5697,
  uid: 18381,
  vendor: 'Forks Cheese',
  date_created: dayjs('2022-10-31'),
  status: Status['ONLINE'],
};

export const sampleWithNewData: NewPeripheral = {
  uid: 9025,
  vendor: 'Kyat',
  date_created: dayjs('2022-10-30'),
  status: Status['ONLINE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
