import { ChangeEvent, useState } from "react";
import Field from "@components/Input/Field";
import { useNavigate } from "react-router-dom";
import {
  fetchUpdatedUser,
  registerUser,
  updateUser,
} from "../../services/user";
import {
  FaBookReader,
  FaCalendarAlt,
  FaPenAlt,
  FaTag,
  FaUsers,
} from "react-icons/fa";
import { ApiError, ValidationErrors } from "../../services/axios";
import Loading from "@components/Loading";
import { useLoading } from "@stores/useLoading";
import Button from "@components/Button";
import { Book } from "@models/book";
import { BookRegisterData } from "@services/books";
import { fromZonedTime } from "date-fns-tz";
import BookHero from "./BookHero";
import FieldArea from "@components/Input/FieldArea";
import FieldDatePicker from "@components/Input/FieldDatePicker";
import FieldFile from "@components/Input/FieldFile";
import FieldImage from "@components/Input/FieldImage";
import { FaFileImage, FaFilePdf } from "react-icons/fa6";
import Input from "@components/Input";
import { Benefit } from "@models/benefit";
import { searchAuthors } from "@services/authors";
import { Author } from "@models/author";
import { sum } from "es-toolkit";
import FieldCombobox from "@components/Input/FieldCombobox";
import { Tag } from "@models/tag";
import { searchTags } from "@services/tags";
import { searchPublishers } from "@services/publishers";
import { Publisher } from "@models/publisher";

interface Props {
  book?: Book;
}

export default function BookForm({ book }: Props) {
  const navigate = useNavigate();
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const currentDate = fromZonedTime(new Date().setHours(0, 0, 0, 0), "-03:00");

  const [validations, setValidations] = useState<ValidationErrors>({});
  const [data, setData] = useState<BookRegisterData>({
    title: book?.title ?? "",
    description: book?.description ?? "",
    publisher: book?.publisher.name ?? "",
    publishedIn: book?.publishedIn ?? currentDate,
    authors: book?.authors ?? [],
    tags: book?.tags.map((tag) => tag.name) ?? [],
    requirements: book?.requirements ?? [],
  });

  const onChangePremium = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      requirements: e.target.checked ? [Benefit.CAN_READ_ALL_BOOKS] : [],
    }));

  const onChangePublishedIn = (date: Date) =>
    setData((data) => ({
      ...data,
      publishedIn: date,
    }));

  const onChangeData = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

  const onClearBanner = () =>
    setData((data) => ({
      ...data,
      banner: undefined,
    }));

  const onLoadBanner = (base64: string, blob: Blob, filename?: string) => {
    const url = URL.createObjectURL(blob);
    setData((data) => ({
      ...data,
      banner: {
        url,
        blob,
        base64,
        filename,
      },
    }));
  };

  const onClearCover = () =>
    setData((data) => ({
      ...data,
      cover: undefined,
    }));

  const onLoadCover = (base64: string, blob: Blob, filename?: string) => {
    const url = URL.createObjectURL(blob);
    setData((data) => ({
      ...data,
      cover: {
        url,
        blob,
        base64,
        filename,
      },
    }));
  };

  const onLoadFile = (files: File[]) =>
    setData((data) => ({
      ...data,
      file: files.length > 0 ? files[0] : undefined,
    }));

  const onCatchErrors = (error: ApiError) => {
    const { type, status, ...rest } = error;

    if (type === "validation" && status === 400) {
      setValidations({
        ...rest,
      } as ValidationErrors);
    }
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (book) startLoading("book-edit");
    else startLoading("book-register");

    const formData = new FormData();

    const body = {
      title: data.title,
      description: data.description,
      publisher: data.publisher,
      publishedIn: data.publishedIn,
      authors: data.authors,
      tags: data.tags,
      requirements: data.requirements,
    } as BookRegisterData;

    formData.append(
      "body",
      new Blob([JSON.stringify(body)], { type: "application/json" })
    );

    // if (data.avatar?.blob) {
    //   formData.append("avatar", data.avatar.blob, data.avatar.filename);
    // }

    if (book) {
      updateUser(formData)
        .then(async () => {
          await fetchUpdatedUser();
          navigate("/home");
        })
        .catch(onCatchErrors)
        .finally(() => {
          stopLoading("book-edit");
        });
    } else {
      registerUser(formData)
        .then(() => {
          navigate("/login");
        })
        .catch(onCatchErrors)
        .finally(() => {
          stopLoading("book-register");
        });
    }
  };

  console.log(data);

  return (
    <main className="flex flex-row gap-4">
      <section className="flex rounded-box p-6 bg-base-200 flex-col gap-0 w-full max-w-11/12 sm:max-w-sm lg:max-w-lg">
        <header className="text-start text-base-content">
          <h1 className="text-3xl font-bold text-base-content">
            Cadastrar livro
          </h1>
          <p>Cadastrar novo livro na plataforma</p>
        </header>
        <div className="divider divider-vertical my-2"></div>
        <form className="flex flex-col gap-5 w-full" onSubmit={onSubmit}>
          <Field
            icon={FaPenAlt}
            validations={validations["title"]}
            label="Título"
            id="title"
            type="text"
            value={data.title}
            onChange={onChangeData}
            placeholder="Harry Potter e a Pedra Filosofal"
          />
          <label className="label">
            <Input
              type="checkbox"
              checked={data.requirements.includes(Benefit.CAN_READ_ALL_BOOKS)}
              onChange={onChangePremium}
              className="checkbox checkbox-primary"
            />
            Catálogo premium
          </label>
          <FieldArea
            validations={validations["description"]}
            label="Descrição"
            id="description"
            value={data.description}
            onChange={onChangeData}
            placeholder="Primeiro livro da série Harry Potter."
          />
          <FieldFile
            icon={FaFilePdf}
            validations={validations["file"]}
            files={data.file ? [data.file] : undefined}
            accept={["application/pdf"]}
            onFilesChanged={onLoadFile}
            label="Arquivo"
            id="file"
          />
          <FieldImage
            imageSize={{
              aspect: 2 / 3,
              height: 300,
              width: 200,
            }}
            file={{
              url: data.cover?.url ?? "",
              name: data.cover?.filename,
            }}
            canClear={!!data.cover}
            onImageClear={onClearCover}
            onImageLoaded={onLoadCover}
            icon={FaFileImage}
            validations={validations["cover"]}
            label="Capa"
            id="cover"
          />
          <FieldImage
            imageSize={{
              aspect: 3 / 2,
              height: 400,
              width: 600,
            }}
            file={{
              url: data.banner?.url ?? "",
              name: data.banner?.filename,
            }}
            canClear={!!data.banner}
            onImageClear={onClearBanner}
            onImageLoaded={onLoadBanner}
            icon={FaFileImage}
            validations={validations["banner"]}
            label="Plano de fundo"
            id="banner"
          />
          <FieldDatePicker
            icon={FaCalendarAlt}
            validations={validations["publishedIn"]}
            label="Data de publicação"
            id="publishedIn"
            date={data.publishedIn}
            onPick={onChangePublishedIn}
            restrict={{
              after: currentDate,
            }}
          />
          <FieldCombobox<Publisher>
            icon={FaBookReader}
            validations={validations["publisher"]}
            label="Editora"
            placeholder="Editora"
            id="book-publisher-candidate"
            type="text"
            onSearch={searchPublishers}
            onFilter={(publishers) => {
              const totalScore = sum(
                publishers.map((publisher) => publisher.score)
              );
              if (totalScore > 1) return publishers.slice(0, 3);
              else if (totalScore > 0) return publishers.slice(0, 6);
              return publishers;
            }}
            onSelect={(publisher) =>
              setData((data) => ({
                ...data,
                publisher: (publisher as Publisher).name,
              }))
            }
            onDraw={(publisher) => publisher.name}
            onRender={(publisher) => ({
              id: publisher.name,
              name: publisher.name,
              value: publisher,
            })}
          />
          <FieldCombobox<Author>
            icon={FaUsers}
            validations={validations["authors"]}
            label="Autores"
            placeholder="Autor(a)"
            id="book-authors-candidates"
            onSearch={searchAuthors}
            multiple
            onFilter={(authors) => {
              const totalScore = sum(authors.map((author) => author.score));
              if (totalScore > 1) return authors.slice(0, 3);
              else if (totalScore > 0) return authors.slice(0, 6);
              return authors;
            }}
            onSelect={(authors) =>
              setData((data) => ({
                ...data,
                authors: authors as Author[],
              }))
            }
            onDraw={(author) => author.name}
            onRender={(author) => ({
              id: `${author.id}`,
              name: author.name,
              value: author,
            })}
          />
          <FieldCombobox<Tag>
            icon={FaTag}
            validations={validations["tags"]}
            label="Etiquetas"
            placeholder="Etiqueta"
            id="book-tags-candidates"
            onSearch={searchTags}
            multiple
            onFilter={(tags) => {
              const totalScore = sum(tags.map((score) => score.score));
              if (totalScore > 1) return tags.slice(0, 3);
              else if (totalScore > 0) return tags.slice(0, 6);
              return tags;
            }}
            onSelect={(tags) => {
              console.log(tags);
              setData((data) => ({
                ...data,
                tags: (tags as Tag[]).map((tag) => tag.name),
              }))
            }}
            onDraw={(tag) => tag.name}
            onRender={(tag) => ({
              id: tag.name,
              name: tag.name,
              value: tag,
            })}
          />
          <Button className="btn btn-primary" type="submit">
            {book ? (
              <Loading
                id="book-edit"
                loadingMessage="Salvando..."
                defaultMessage="Salvar"
              />
            ) : (
              <Loading
                id="book-register"
                loadingMessage="Registrando..."
                defaultMessage="Registrar"
              />
            )}
          </Button>
        </form>
      </section>
      <div className="flex overflow-hidden rounded-box h-min flex-col w-full grow">
        <BookHero preview previewData={data} />
      </div>
    </main>
  );
}
