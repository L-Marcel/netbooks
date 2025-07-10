export type AuthorData = {
  id?: number;
  name: string;
  score?: number;
};

export class Author {
  readonly id: number;
  readonly name: string;
  readonly score: number = 0;

  constructor(data: AuthorData) {
    this.id = data.id ?? -1;
    this.name = data.name;
    this.score = data.score ?? 0;
  }
}
