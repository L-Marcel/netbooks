import {
  addDays,
  Duration,
  formatDuration,
  intervalToDuration,
  isAfter,
} from "date-fns";
import Decimal from "decimal.js";
import { fromZonedTime } from "date-fns-tz";
import { ptBR } from "date-fns/locale";

export enum PaymentStatus {
  SCHEDULED = "SCHEDULED",
  PAID = "PAID",
  CANCELED = "CANCELED",
}

export type ProductData = {
  name: string;
  duration: number;
};

export class Product {
  readonly name: string;
  readonly duration: Duration;

  constructor(data: ProductData) {
    this.name = data.name;
    this.duration = intervalToDuration({
      start: new Date(),
      end: addDays(new Date(), data.duration),
    });
  }

  public getDurationText(): string {
    return formatDuration(this.duration, {
      format: ["years", "months", "days"],
      zero: false,
      locale: ptBR,
    });
  }
}

export type PaymentData = {
  id: number;
  subscription: number;
  price: number;
  dueDate: Date;
  payDate: Date;
  createdAt: Date;
  paidAt: Date;
  status: PaymentStatus;
  product: ProductData;
};

export class Payment {
  readonly id: number;
  readonly subscription: number;
  readonly price: Decimal;
  readonly dueDate: Date;
  readonly payDate: Date;
  readonly createdAt: Date;
  readonly paidAt: Date;
  readonly status: PaymentStatus;
  readonly product: Product;

  constructor(data: PaymentData) {
    this.id = data.id;
    this.subscription = data.subscription;
    this.price = new Decimal(data.price);
    this.dueDate = new Date(data.dueDate);
    this.payDate = new Date(data.payDate);
    this.createdAt = new Date(data.createdAt);
    this.paidAt = new Date(data.paidAt);
    this.status = data.status;
    this.product = new Product(data.product);
  }

  public isOverdue(): boolean {
    return (
      isAfter(
        fromZonedTime(new Date().setHours(0, 0, 0, 0), "-03:00"),
        this.dueDate
      ) && this.status !== PaymentStatus.PAID
    );
  }
}
