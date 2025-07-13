import { Author, AuthorData } from "./author";
import { Benefit, BenefitData } from "./benefit";
import { Publisher, PublisherData } from "./publisher";
import { Tag, TagData } from "./tag";

export interface BookData {
  id: number;
  isbn?: number;
  title: string;
  numPages: number;
  publishedIn: Date;
  publisher: PublisherData;
  description: string;
  stars: number;
  tags: TagData[];
  authors: AuthorData[];
  requirements: BenefitData[];
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
  readonly requirements: Benefit[];
  readonly cover: string;
  readonly banner: string;

  constructor(data: BookData) {
    this.id = data.id;
    this.isbn = data.isbn;
    this.title = data.title;
    this.numPages = data.numPages;
    this.publishedIn = data.publishedIn;
    this.publisher = new Publisher(data.publisher);
    this.description = data.description;
    this.stars = data.stars;
    this.tags = data.tags.map((tag) => new Tag(tag));
    this.authors = data.authors.map((author) => new Author(author));
    this.requirements = data.requirements.map(
      (requirement) => requirement.name as Benefit
    );
    this.cover = `${import.meta.env.VITE_BACKEND_URL}/books/covers/${this.id}.webp`;
    this.banner = `${import.meta.env.VITE_BACKEND_URL}/books/banners/${this.id}.webp`;
  }
}
