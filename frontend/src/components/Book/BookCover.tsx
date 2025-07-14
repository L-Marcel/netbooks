import Button from "@components/Button";
import { deleteBook } from "@services/books";
import { useLoading } from "@stores/useLoading";
import useUser from "@stores/useUser";
import { useQueryClient } from "@tanstack/react-query";
import { motion } from "motion/react";
import { MouseEvent } from "react";
import { FaTrash } from "react-icons/fa";
import { FaFire, FaPencil } from "react-icons/fa6";
import { useNavigate } from "react-router-dom";

interface Props {
  id?: number;
  index?: number;
  alwaysVisible?: boolean;
  withoutAdminButtons?: boolean;
  cover?: string;
  premium?: boolean;
}

export default function BookCover({
  id,
  cover,
  index,
  alwaysVisible = true,
  withoutAdminButtons = false,
  premium = false,
}: Props) {
  const user = useUser((state) => state.user);
  const navigate = useNavigate();
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);
  const queryClient = useQueryClient();

  const onDelete = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (id) {
      startLoading("deleting-book-" + id);
      deleteBook(id)
        .then(() => {
          navigate("/home");
          queryClient.invalidateQueries();
        })
        .finally(() => {
          stopLoading("deleting-book-" + id);
        });
    }
  };

  const onEdit = (e: MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    if (id) {
      navigate("/books/" + id + "/edit");
    }
  };

  return (
    <motion.div
      key={"cover-" + index}
      initial={{ opacity: 0.25, height: 300 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.75 }}
      className={`group overflow-hidden relative rounded-lg min-w-[100px] max-w-[100px] max-h-[150px] min-h-[150px] lg:min-w-[200px] lg:max-w-[200px] lg:max-h-[300px] lg:min-h-[300px] shadow-2xl ${alwaysVisible ? "" : "not-lg:hidden"} ${premium ? "is-premium" : ""}`}
    >
      <div className="rounded-lg overflow-hidden flex flex-row absolute inset-0 border-2 group-[.is-premium]:border-warning">
        {user?.isAdmin() && !withoutAdminButtons && (
          <div className="absolute border-2 group-[.is-premium]:border-warning rounded-tr-xl rounded-bl-xl join flex flex-row join-horizontal bottom-[-2px] left-[-2px]">
            <Button
              onClick={onDelete}
              className="z-50 join-item rounded-tl-none rounded-bl-lg btn btn-square btn-secondary btn-xs lg:btn-sm"
            >
              <FaTrash />
            </Button>
            <Button
              onClick={onEdit}
              className="z-50 join-item rounded-br-none rounded-tr-lg btn btn-square btn-primary btn-xs lg:btn-sm"
            >
              <FaPencil />
            </Button>
          </div>
        )}
      </div>
      {premium && (
        <div className="absolute bottom-2 right-1.5">
          <span className="absolute rounded-br-lg rounded-tl-box min-w-9 min-h-5 bg-warning-content opacity-80 -bottom-2 -right-1.5" />
          <FaFire className="absolute -bottom-1 -right-1 size-8 text-warning-content" />
          <FaFire className="absolute bottom-0 right-0 size-6 text-warning" />
        </div>
      )}
      {cover ? (
        <img
          src={cover}
          className="rounded-lg -z-20 min-w-[100px] max-w-[100px] max-h-[150px] min-h-[150px] lg:min-w-[200px] lg:max-w-[200px] lg:max-h-[300px] lg:min-h-[300px]"
          height={300}
          width={200}
        />
      ) : (
        <div className="skeleton min-w-[100px] max-w-[100px] max-h-[150px] min-h-[150px] lg:min-w-[200px] lg:max-w-[200px] lg:max-h-[300px] lg:min-h-[300px]" />
      )}
    </motion.div>
  );
}
