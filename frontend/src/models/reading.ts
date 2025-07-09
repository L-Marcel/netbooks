import { UUID } from "crypto";
import Decimal from "decimal.js";

export interface ReadingData {
  id: number;
  startedIn: Date;
  stoppedIn: Date;
  finished: boolean;
  currentPage: number;
  numPages: number;
  percentage: number;
  user: UUID;
  book: number;
}

export class Reading {
  readonly id: number;
  readonly startedIn: Date;
  readonly stoppedIn: Date;
  readonly finished: boolean;
  currentPage: number;
  readonly numPages: number;
  private percentage: Decimal;
  readonly user: UUID;
  readonly book: number;

  public clone() {
    return new Reading({
      id: this.id,
      book: this.book,
      startedIn: this.startedIn,
      stoppedIn: this.stoppedIn,
      finished: this.finished,
      currentPage: this.currentPage,
      numPages: this.numPages,
      percentage: this.getPercentage(),
      user: this.user,
    });
  }

  constructor(data: ReadingData) {
    this.id = data.id;
    this.startedIn = data.startedIn;
    this.stoppedIn = data.stoppedIn;
    this.finished = data.finished;
    this.currentPage = data.currentPage;
    this.numPages = data.numPages;
    this.percentage = new Decimal(data.percentage);
    this.user = data.user;
    this.book = data.book;
  }

  public getPercentage(): number {
    return this.percentage.toDecimalPlaces(2).toNumber();
  }

  public setPage(page: number) {
    this.currentPage = Math.min(Math.max(page, 1), this.numPages);
    this.percentage = new Decimal((page / this.numPages) * 100);
  }

  public nextPage() {
    this.currentPage = Math.min(this.currentPage + 1, this.numPages);
    this.percentage = new Decimal((this.currentPage / this.numPages) * 100);
  }

  public previousPage() {
    this.currentPage = Math.max(this.currentPage - 1, 0);
    this.percentage = new Decimal((this.currentPage / this.numPages) * 100);
  }
}
