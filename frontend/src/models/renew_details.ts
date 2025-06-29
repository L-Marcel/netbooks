import { isAfter, isBefore, isEqual } from "date-fns";
import { fromZonedTime } from "date-fns-tz";

export type RenewDetailsData = {
  hidden: boolean;
  dueDate: Date;
  payDate: Date;
};

export class RenewDetails {
  readonly hidden: boolean;
  readonly dueDate: Date;
  readonly payDate: Date;

  constructor(data: RenewDetailsData) {
    this.hidden = data.hidden;
    this.dueDate = new Date(data.dueDate);
    this.payDate = new Date(data.payDate);
  }

  public needManualRenew(): boolean {
    const currentDate = fromZonedTime(
      new Date().setHours(0, 0, 0, 0),
      "-03:00"
    );

    return (
      this.hidden &&
      (isAfter(currentDate, this.payDate) ||
        isEqual(currentDate, this.payDate)) &&
      (isBefore(currentDate, this.dueDate) ||
        isEqual(currentDate, this.dueDate))
    );
  }
}
