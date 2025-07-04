import { UUID } from "crypto";
import { PlanEdition, PlanEditionData } from "./plan_edition";

export type SubscriptionData = {
  id: number;
  subscriber: UUID;
  edition: PlanEditionData;
  startedIn: Date;
  closedIn?: Date;
  dueDate: Date;
  payDate: Date;
  numPayments: number;
  actived: boolean;
};

export class Subscription {
  readonly id: number;
  readonly subscriber: UUID;
  readonly edition: PlanEdition;
  readonly startedIn: Date;
  readonly closedIn?: Date;
  readonly dueDate: Date;
  readonly payDate: Date;
  readonly numPayments: number;
  readonly actived: boolean;

  constructor(data: SubscriptionData) {
    this.id = data.id;
    this.subscriber = data.subscriber;
    this.edition = new PlanEdition(data.edition);
    this.startedIn = new Date(data.startedIn);
    this.closedIn = data.closedIn ? new Date(data.closedIn) : undefined;
    this.dueDate = new Date(data.dueDate);
    this.payDate = new Date(data.payDate);
    this.numPayments = data.numPayments;
    this.actived = data.actived;
  }
}
