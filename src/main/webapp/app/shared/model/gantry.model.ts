export interface IGantry {
  id?: number;
  guid?: string;
  nameEn?: string;
  nameAr?: string;
  status?: number | null;
  code?: string | null;
}

export const defaultValue: Readonly<IGantry> = {};
