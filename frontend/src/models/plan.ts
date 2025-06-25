import {
  addDays,
  Duration,
  formatDuration,
  intervalToDuration,
} from "date-fns";
import { Benefit, BenefitData, benefitToText, PlanBenefit } from "./benefit";
import { PlanEdition, PlanEditionData } from "./plan_edition";
import { ptBR } from "date-fns/locale";
import Decimal from "decimal.js";

export type PlanData = {
  id: number;
  name: string;
  description: string;
  numSubscribers: number;
  duration: number;
  benefits: BenefitData[];
  editions: PlanEditionData[];
};

export class Plan {
  readonly id: number;
  readonly name: string;
  readonly description: string;
  readonly numSubscribers: number;
  readonly duration: Duration;
  readonly benefits: Benefit[];
  readonly editions: PlanEdition[];

  constructor(data: PlanData) {
    this.id = data.id;
    this.name = data.name;
    this.description = data.description;
    this.numSubscribers = data.numSubscribers;

    this.duration = intervalToDuration({
      start: new Date(),
      end: addDays(new Date(), data.duration),
    });

    this.benefits = [
      Benefit.CAN_READ_BASIC_BOOKS,
      ...data.benefits.map((benefit) => benefit.name as Benefit),
    ];

    this.editions = data.editions.map((data) => new PlanEdition(data));
  }

  public getCheapestEdition(): PlanEdition | undefined {
    return this.editions.length > 0 ? this.editions[0] : undefined;
  }

  public getMostExpensiveEdition(): PlanEdition | undefined {
    return this.editions.length > 0
      ? this.editions[this.editions.length - 1]
      : undefined;
  }

  public getCheapestPrice(): Decimal {
    const edition = this.getCheapestEdition();
    return edition ? edition.price : new Decimal(1);
  }

  public getMostExpensivePrice(): Decimal {
    const edition = this.getMostExpensiveEdition();
    return edition ? edition.price : this.getCheapestPrice();
  }

  public getCheapestEndDate(): Date | undefined {
    const promotial = this.getCheapestEdition();
    return promotial ? promotial.closedIn : undefined;
  }

  public getDurationText(): string {
    console.log(
      formatDuration(this.duration, {
        format: ["years", "months", "days"],
        zero: false,
        locale: ptBR,
      })
    );
    return formatDuration(this.duration, {
      format: ["years", "months", "days"],
      zero: false,
      locale: ptBR,
    });
  }

  public getScore(): Decimal {
    return this.getCheapestPrice()
      .div(this.duration.days ?? 1)
      .mul(this.benefits.length);
  }

  public getPlanBenefits(): PlanBenefit[] {
    return Object.values(Benefit).map((benefit) => ({
      value: benefit,
      description: benefitToText[benefit],
      actived: this.benefits.includes(benefit),
    }));
  }
}
