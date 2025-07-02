import { UUID } from "crypto";

export interface ReadingData {
  id: number;
  startedIn: Date;
  stoppedIn: Date;
  finished: boolean;
  currentPage: number;
  user: UUID;
  book: number;
}

export class Reading {
  readonly id: number;
  readonly startedIn: Date;
  readonly stoppedIn: Date;
  readonly finished: boolean;
  readonly currentPage: number;
  readonly user: UUID;
  readonly book: number;

  constructor(data: ReadingData) {
    this.id = data.id;
    this.startedIn = data.startedIn;
    this.stoppedIn = data.stoppedIn;
    this.finished = data.finished;
    this.currentPage = data.currentPage;
    this.user = data.user;
    this.book = data.book;
  }
}
