import { useLoading } from "@stores/useLoading";
import { ReactNode } from "react";

interface Props {
  id: string | number;
  defaultMessage: ReactNode;
  loadingMessage?: ReactNode;
}

export default function Loading({ id, defaultMessage, loadingMessage }: Props) {
  const loading = useLoading((state) => state.loadingSet);

  return (
    <>
      {loading.has(id) && <span className="loading loading-spinner"></span>}
      {loading.has(id) ? loadingMessage : defaultMessage}
    </>
  );
}
