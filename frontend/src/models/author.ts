export type AuthorData = {
  id: number;
  name: string;
  score: number;
};

export class Author {
  readonly id: number;
  readonly name: string;
  readonly score: number;

  constructor(data: AuthorData) {
    this.id = data.id;
    this.name = data.name;
    this.score = data.score;
  }
}
