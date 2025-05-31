export type Author = {
  id: number;
  name: string;
};

export type Tag = {
  name: string;
};

export type Publisher = {
  name: string;
};

export interface BookData {
  id: number;
  isbn?: number;
  title: string;
  numPages: number;
  publishedIn: Date;
  publisher: Publisher;
  description: string;
  stars: number;
  tags: Tag[];
  authors: Author[];
}

export class Book {
  readonly id: number;
  readonly isbn?: number;
  readonly title: string;
  readonly numPages: number;
  readonly publishedIn: Date;
  readonly publisher: Publisher;
  readonly description: string;
  readonly stars: number;
  readonly tags: Tag[];
  readonly authors: Author[];
  readonly cover: string;
  readonly banner: string;

  constructor(data: BookData) {
    this.id = data.id;
    this.isbn = data.isbn;
    this.title = data.title;
    this.numPages = data.numPages;
    this.publishedIn = data.publishedIn;
    this.publisher = data.publisher;
    this.description = data.description;
    this.stars = data.stars;
    this.tags = data.tags;
    this.authors = data.authors;
    this.cover = `http://localhost:8080/books/covers/${this.id}.webp`;
    this.banner = `http://localhost:8080/books/banners/${this.id}.webp`;
  }
}
