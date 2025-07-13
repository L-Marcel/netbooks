import { useLoading } from "@stores/useLoading";
import { DetailedHTMLProps, TextareaHTMLAttributes } from "react";

export default function TextArea({
  disabled,
  ...props
}: DetailedHTMLProps<
  TextareaHTMLAttributes<HTMLTextAreaElement>,
  HTMLTextAreaElement
>) {
  const loadingHasAny = useLoading((state) => state.hasAny);

  return <textarea disabled={loadingHasAny || disabled} {...props} />;
}
