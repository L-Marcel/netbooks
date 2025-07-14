import Button from "@components/Button";
import { RatingInput } from "@components/Input/RatingInput";
import Loading from "@components/Loading";
import { Book } from "@models/book";
import {
  fetchClassification,
  updateClassification,
} from "@services/classifications";
import { useLoading } from "@stores/useLoading";
import {
  DetailedHTMLProps,
  DialogHTMLAttributes,
  FormEvent,
  useCallback,
  useRef,
  useState,
} from "react";
import { FaPencil } from "react-icons/fa6";

interface Props
  extends Omit<
    DetailedHTMLProps<
      DialogHTMLAttributes<HTMLDialogElement>,
      HTMLDialogElement
    >,
    "ref"
  > {
  book: Book;
  onUpdate: () => void;
}

export default function BookClassificationDialog({
  book,
  onUpdate,
  ...props
}: Props) {
  const ref = useRef<HTMLDialogElement>(null);
  const [stars, setStars] = useState<number>(0);
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const update = useCallback(() => {
    startLoading("classification");
    fetchClassification(book?.id)
      .then((classification) => {
        setStars(classification.value);
      })
      .catch(() => setStars(-1))
      .finally(() => stopLoading("classification"));
  }, [book, startLoading, stopLoading, setStars]);

  const onSubmit = useCallback(
    (e: FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      startLoading("updating-classification");
      updateClassification(book?.id, stars)
        .then((classification) => {
          onUpdate();
          setStars(classification.value);
        })
        .catch(() => setStars(-1))
        .finally(() => {
          stopLoading("updating-classification");
          ref.current?.close();
        });
    },
    [ref, book, stars, onUpdate, startLoading, stopLoading]
  );

  const onOpen = useCallback(() => {
    ref.current?.showModal();
    update();
  }, [ref, update]);

  return (
    <>
      <Button
        onClick={onOpen}
        className="btn btn-square btn-ghost btn-primary btn-sm"
      >
        <FaPencil />
      </Button>
      <dialog ref={ref} {...props} className="modal">
        <div className="modal-box">
          <h3 className="font-bold text-lg">Avalie {book.title}!</h3>
          <div className="py-4">
            <RatingInput onChange={setStars} rate={stars} />
          </div>
          <div className="flex justify-end mt-2">
            <form onSubmit={onSubmit}>
              <Button type="submit" className="btn btn-primary">
                <Loading
                  id="updating-classification"
                  loadingMessage="Atualizando..."
                  defaultMessage="Confirmar"
                />
              </Button>
            </form>
          </div>
        </div>
      </dialog>
    </>
  );
}
