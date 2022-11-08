export interface IEvents {
  id?: number;
  guid?: string;
  plate?: string | null;
  anpr?: string | null;
  rfid?: string | null;
  gantry?: number | null;
  wantedFor?: string | null;
  licenseIssue?: string | null;
  issue?: string | null;
  tagIssue?: string | null;
  statusName?: string | null;
  lane?: number | null;
  direction?: number | null;
  kph?: number | null;
  ambush?: number | null;
  toll?: number | null;
  fine?: number | null;
  wantedBy?: number | null;
  licenseFine?: number | null;
  speedFine?: number | null;
  handled?: number | null;
  processingTime?: number | null;
  ruleRcvd?: number | null;
  ruleIssue?: number | null;
  ruleProcessed?: number | null;
  ruleSent?: number | null;
  when?: number | null;
  vehicle?: number | null;
  stolen?: number | null;
  wanted?: boolean | null;
  gantryProcessed?: number | null;
  gantrySent?: number | null;
  status?: string | null;
  dataStatus?: string | null;
  threadRandNo?: string | null;
  handledBy?: string | null;
}

export const defaultValue: Readonly<IEvents> = {
  wanted: false,
};
