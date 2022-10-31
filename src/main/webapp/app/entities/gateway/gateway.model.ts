export interface IGateway {
  id: number;
  serial_number?: string | null;
  name?: string | null;
  ip_address?: string | null;
}

export type NewGateway = Omit<IGateway, 'id'> & { id: null };
