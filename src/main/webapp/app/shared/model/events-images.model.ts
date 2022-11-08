export interface IEventsImages {
  id?: number;
  guid?: string;
  imageLp?: string | null;
  imageThumb?: string | null;
  processingTime?: number | null;
  ruleRcvd?: number | null;
  ruleSent?: number | null;
  when?: number | null;
  gantryProcessed?: number | null;
  gantrySent?: number | null;
  status?: string | null;
  dataStatus?: string | null;
  threadRandNo?: string | null;
  gantry?: number | null;
}

export const defaultValue: Readonly<IEventsImages> = {};
