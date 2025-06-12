import { Decimal } from "decimal.js";

export type PlanEditionData = {
  id: number;
  plan: number;
  numSubscribers: number;
  price: number;
  startedIn: number;
  closedIn: number;
};

export class PlanEdition {
  readonly id: number;
  readonly plan: number;
  readonly numSubscribers: number;
  readonly price: Decimal;
  readonly startedIn: Date;
  readonly closedIn?: Date;

  constructor(data: PlanEditionData) {
    this.id = data.id;
    this.plan = data.plan;
    this.numSubscribers = data.numSubscribers;
    this.price = new Decimal(data.price);
    this.startedIn = new Date(data.startedIn);
    if (data.closedIn) this.closedIn = new Date(data.closedIn);
  }
}
