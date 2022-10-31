import { IGateway, NewGateway } from './gateway.model';

export const sampleWithRequiredData: IGateway = {
  id: 91474,
  serial_number: 'up Refined',
  name: 'grow time-frame',
  ip_address: '10.2.6.9',
};

export const sampleWithPartialData: IGateway = {
  id: 80638,
  serial_number: 'Granite',
  name: 'hour Lesotho Plastic',
  ip_address: '10.2.5.9',
};

export const sampleWithFullData: IGateway = {
  id: 54569,
  serial_number: 'Spur Rubber SSL',
  name: 'Connecticut 24',
  ip_address: '10.0.0.1',
};

export const sampleWithNewData: NewGateway = {
  serial_number: 'Jewelery',
  name: 'Parkways Cotton Chips',
  ip_address: '10.5.9.72',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
