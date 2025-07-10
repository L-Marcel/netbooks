import { useLoading } from "@stores/useLoading";
import { ReactNode } from "react";

interface Props {
  id: string | number;
  withoutSpinner?: boolean;
  defaultMessage: ReactNode;
  loadingMessage?: ReactNode;
}

export default function Loading({
  id,
  withoutSpinner = false,
  defaultMessage,
  loadingMessage,
}: Props) {
  const loading = useLoading((state) => state.loadingSet);

  return (
    <>
      {loading.has(id) && !withoutSpinner && (
        <span className="loading loading-spinner"></span>
      )}
      {loading.has(id) ? loadingMessage : defaultMessage}
    </>
  );
}
