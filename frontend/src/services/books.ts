import { Book, BookData } from "@models/book";
import api from "./axios";
import { ImageData, imageUrlToData } from "./image";
import { Benefit } from "@models/benefit";
import { Author } from "@models/author";
import { Publisher } from "@models/publisher";
import { Tag } from "@models/tag";

export type BookRegisterData = {
  isbn?: number;
  title: string;
  publishedIn: Date;
  publisher?: Publisher;
  description: string;
  tags: Tag[];
  authors: Author[];
  requirements: Benefit[];
  file?: File;
  cover?: ImageData;
  banner?: ImageData;
};

export async function registerBook(data: FormData) {
  return api.post<void>("/books", data, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

export async function updateBook(data: FormData, id: number) {
  return api.put<void>("/books/" + id, data, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

export async function fetchBooks(): Promise<Book[]> {
  return api.get<BookData[]>("books").then(async (response) => {
    const books = response.data
      .map((data) => new Book(data))
      .sort((a, b) => b.stars - a.stars);

    await Promise.all(
      books.map(async (book) => {
        const banner = await imageUrlToData(book.getBannerUrl()).catch(
          () => undefined
        );
        const cover = await imageUrlToData(book.getCoverUrl()).catch(
          () => undefined
        );
        if (banner) book.setBanner(banner);
        if (cover) book.setCover(cover);

        return book;
      })
    );

    return books;
  });
}

export async function searchBooks(query: string): Promise<Book[]> {
  return api
    .get<BookData[]>("books/search?query=" + query)
    .then(async (response) => {
      const books = response.data.map((data) => new Book(data));

      await Promise.all(
        books.map(async (book) => {
          const banner = await imageUrlToData(book.getBannerUrl()).catch(
            () => undefined
          );
          const cover = await imageUrlToData(book.getCoverUrl()).catch(
            () => undefined
          );
          if (banner) book.setBanner(banner);
          if (cover) book.setCover(cover);

          return book;
        })
      );

      return books;
    });
}

export async function searchBooksFromBookcase(query: string): Promise<Book[]> {
  return api
    .get<BookData[]>("books/bookcase/search?query=" + query)
    .then(async (response) => {
      const books = response.data.map((data) => new Book(data));

      await Promise.all(
        books.map(async (book) => {
          const banner = await imageUrlToData(book.getBannerUrl()).catch(
            () => undefined
          );
          const cover = await imageUrlToData(book.getCoverUrl()).catch(
            () => undefined
          );
          if (banner) book.setBanner(banner);
          if (cover) book.setCover(cover);

          return book;
        })
      );

      return books;
    });
}

export async function fetchBook(id: number, withFile?: boolean): Promise<Book> {
  return api.get<BookData>("books/" + id).then(async (response) => {
    const book = new Book(response.data);
    const banner = await imageUrlToData(book.getBannerUrl()).catch(
      () => undefined
    );
    const cover = await imageUrlToData(book.getCoverUrl()).catch(
      () => undefined
    );
    if (banner) book.setBanner(banner);
    if (cover) book.setCover(cover);

    if (withFile) {
      const file = await downloadBook(book.id).catch(() => undefined);
      if (file) book.setFile(file);
    }

    return book;
  });
}

export async function deleteBook(id: number): Promise<void> {
  return api.delete("books/" + id);
}

export async function downloadBook(id: number): Promise<File> {
  return api
    .get<File>("books/" + id + "/download", {
      responseType: "blob",
    })
    .then(
      (response) =>
        new File([response.data], response.headers["filename"], {
          type: "application/pdf",
        })
    );
}
