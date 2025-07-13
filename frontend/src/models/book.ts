import { ImageData } from "@services/image";
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
  private cover?: ImageData;
  private banner?: ImageData;
  private file?: File;

  constructor(data: BookData) {
    this.id = data.id;
    this.isbn = data.isbn;
    this.title = data.title;
    this.numPages = data.numPages;
    this.publishedIn = new Date(data.publishedIn);
    this.publisher = new Publisher(data.publisher);
    this.description = data.description;
    this.stars = data.stars;
    this.tags = data.tags.map((tag) => new Tag(tag));
    this.authors = data.authors.map((author) => new Author(author));
    this.requirements = data.requirements.map(
      (requirement) => requirement.name as Benefit
    );
  }

  public setFile(file: File) {
    this.file = file;
  }

  public getFile() {
    return this.file;
  }

  public setCover(cover: ImageData) {
    this.cover = cover;
  }

  public getCover(): ImageData | undefined {
    return this.cover;
  }

  public getCoverUrl(): string {
    return `${import.meta.env.VITE_BACKEND_URL}/books/covers/${this.id}.webp`;
  }

  public setBanner(banner: ImageData) {
    this.banner = banner;
  }

  public getBanner(): ImageData | undefined {
    return this.banner;
  }

  public getBannerUrl(): string {
    return `${import.meta.env.VITE_BACKEND_URL}/books/banners/${this.id}.webp`;
  }

  public isPremium(): boolean {
    return this.requirements.includes(Benefit.CAN_READ_ALL_BOOKS);
  }
}
