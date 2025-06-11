import { Duration } from "date-fns";
import { Benefit, BenefitData, benefitToText, PlanBenefit } from "./benefit";
import { PlanEdition, PlanEditionData } from "./plan_edition";
import Decimal from "decimal.js";

export type PlanData = {
  id: number;
  name: string;
  description: string;
  popularity: number;
  duration: number;
  benefits: BenefitData[];
  editions: PlanEditionData[];
};

export class Plan {
  readonly id: number;
  readonly name: string;
  readonly description: string;
  readonly popularity: number;
  readonly duration: Duration;
  readonly benefits: Benefit[];
  readonly editions: PlanEdition[];

  constructor(data: PlanData) {
    this.id = data.id;
    this.name = data.name;
    this.description = data.description;
    this.popularity = data.popularity;
    this.duration = {
      hours: data.duration,
    };
    this.benefits = [
      Benefit.CAN_READ_BASIC_BOOKS,
      ...data.benefits.map((benefit) => benefit.name as Benefit),
    ];
    this.editions = data.editions.map((data) => new PlanEdition(data));
  }

  public getCurrectEdition(): PlanEdition | undefined {
    return this.editions.find((edition) => !edition.closedIn);
  }

  public getCurrentPrice(): Decimal {
    const edition = this.getCurrectEdition();
    return edition ? edition.price : new Decimal(0);
  }

  public getPlanBenefits(): PlanBenefit[] {
    return Object.values(Benefit).map((benefit) => ({
      value: benefit,
      description: benefitToText[benefit],
      actived: this.benefits.includes(benefit),
    }));
  }
}
