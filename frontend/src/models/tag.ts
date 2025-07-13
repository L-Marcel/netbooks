export type TagData = {
  name: string;
  score?: number;
};

export class Tag {
  readonly name: string;
  readonly score: number;

  constructor(data: TagData) {
    this.name = data.name;
    this.score = data.score ?? 0;
  }
}
