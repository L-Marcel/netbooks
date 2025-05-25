import { Book } from "@stores/useBook";
import api from "./axios";

function putBookLinks(book: Book): Book {
    return {
        ...book,
        cover: `http://localhost:8080/books/covers/${book.id}.webp`,
        banner: `http://localhost:8080/books/banners/${book.id}.webp`
    };
}

export async function fetchBooks(): Promise<Book[]> {
    return api.get<Book[]>("books")
        .then((response) => response.data.map(putBookLinks));
};