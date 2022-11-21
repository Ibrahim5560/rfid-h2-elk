export interface IEvents {
  id?: number;
  guid?: string;
  plate?: string | null;
  anpr?: string | null;
  rfid?: string | null;
  dataStatus?: string;
  gantry?: number;
  kph?: number | null;
  ambush?: number | null;
  direction?: number | null;
  vehicle?: number;
  issue?: string | null;
  status?: string | null;
  handledBy?: number | null;
  gantryProcessed?: number | null;
  gantrySent?: number | null;
  when?: number | null;
  toll?: number | null;
  ruleRcvd?: number | null;
  wantedFor?: string | null;
  fine?: number | null;
  licenseIssue?: string | null;
  wantedBy?: number | null;
  ruleProcessed?: number | null;
  speedFine?: number | null;
  lane?: number;
  tagIssue?: string | null;
  statusName?: string | null;
  licenseFine?: number | null;
  stolen?: number | null;
  wanted?: boolean | null;
  ruleSent?: number | null;
  handled?: number | null;
  ruleIssue?: string | null;
}

export const defaultValue: Readonly<IEvents> = {
  wanted: false,
};
