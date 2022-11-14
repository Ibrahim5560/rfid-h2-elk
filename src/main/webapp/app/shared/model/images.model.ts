export interface IImages {
  id?: number;
  guid?: string;
  plate?: string | null;
  imageLpContentType?: string | null;
  imageLp?: string | null;
  imageThumbContentType?: string | null;
  imageThumb?: string | null;
  anpr?: string | null;
  rfid?: string | null;
  dataStatus?: string;
  gantry?: number;
  lane?: number;
  kph?: number | null;
  ambush?: number | null;
  direction?: number | null;
  vehicle?: number;
  issue?: string | null;
  status?: string | null;
}

export const defaultValue: Readonly<IImages> = {};
