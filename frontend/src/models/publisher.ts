export type PublisherData = {
  name: string;
  score?: number;
};

export class Publisher {
  readonly name: string;
  readonly score: number;

  constructor(data: PublisherData) {
    this.name = data.name;
    this.score = data.score ?? 0;
  }
}
