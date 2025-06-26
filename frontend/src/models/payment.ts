import { addDays, Duration, intervalToDuration } from "date-fns";
import Decimal from "decimal.js";
import { PaymentStatus } from "./payment_status";

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
}

export type PaymentData = {
  id: number;
  subscription: number;
  price: number;
  dueDate: Date;
  payDate: Date;
  createdAt: Date;
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
  readonly status: PaymentStatus;
  readonly product: Product;

  constructor(data: PaymentData) {
    this.id = data.id;
    this.subscription = data.subscription;
    this.price = new Decimal(data.price);
    this.dueDate = new Date(data.dueDate);
    this.payDate = new Date(data.payDate);
    this.createdAt = new Date(data.createdAt);
    this.status = data.status;
    this.product = new Product(data.product);
  }
}
