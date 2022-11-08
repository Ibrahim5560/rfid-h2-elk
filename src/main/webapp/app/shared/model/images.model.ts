export interface IImages {
  id?: number;
  guid?: string;
  plate?: string | null;
  anpr?: string | null;
  rfid?: string | null;
  dataStatus?: string;
  gantry?: number;
  lane?: number;
  kph?: number | null;
  ambush?: number | null;
  direction?: number | null;
  vehicle?: number;
  imageLp?: string | null;
  issue?: string | null;
  status?: string | null;
}

export const defaultValue: Readonly<IImages> = {};
