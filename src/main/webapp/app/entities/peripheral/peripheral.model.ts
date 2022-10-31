import dayjs from 'dayjs/esm';
import { IGateway } from 'app/entities/gateway/gateway.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IPeripheral {
  id: number;
  uid?: number | null;
  vendor?: string | null;
  date_created?: dayjs.Dayjs | null;
  status?: Status | null;
  gateway?: Pick<IGateway, 'id' | 'serial_number'> | null;
}

export type NewPeripheral = Omit<IPeripheral, 'id'> & { id: null };
