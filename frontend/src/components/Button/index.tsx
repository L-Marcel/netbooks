import { useLoading } from "@stores/useLoading";
import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";

interface Props
  extends DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {
  notDisableOnLoading?: boolean;
}

export default function Button({
  disabled,
  notDisableOnLoading = false,
  ...props
}: Props) {
  const hasAnyLoading = useLoading((state) => state.hasAny);

  return (
    <button
      disabled={(hasAnyLoading && !notDisableOnLoading) || disabled}
      type="button"
      {...props}
    />
  );
}
