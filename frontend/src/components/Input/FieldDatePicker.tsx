import Button from "@components/Button";
import { Validation } from "@services/axios";
import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";
import { DayPicker, Matcher } from "react-day-picker";
import { IconType } from "react-icons";

interface Props
  extends DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {
  date: Date;
  onPick: (date: Date) => void;
  label?: string;
  validations?: Validation[];
  icon?: IconType;
  restrict?: Matcher | Matcher[];
}

const DEFAULT_VALIDATIONS: Validation[] = [];

export default function FieldDatePicker({
  date,
  restrict,
  icon: Icon,
  id,
  className,
  label,
  onPick,
  validations = DEFAULT_VALIDATIONS,
  ...props
}: Props) {
  const hasError =
    validations.length > 0 &&
    validations.some((validation) => validation.error);

  return (
    <fieldset className="flex flex-col w-full">
      <p className="mb-2 font-medium text-sm">{label}</p>
      <Button
        popoverTarget="rdp-popover"
        id={id}
        name={id}
        className={`input hover:cursor-pointer focus-within:input-primary ${hasError ? "!input-error" : ""} w-full text-base-content ${className}`}
        style={{ anchorName: "--rdp" } as React.CSSProperties}
        {...props}
      >
        {Icon && <Icon className="size-4" />}
        {date ? date.toLocaleDateString() : "Selecionar uma data"}
      </Button>
      <div
        popover="auto"
        id="rdp-popover"
        className="dropdown"
        style={{ positionAnchor: "--rdp" } as React.CSSProperties}
      >
        <DayPicker
          required
          className="react-day-picker"
          mode="single"
          selected={date}
          onSelect={onPick}
          disabled={restrict}
        />
      </div>
    </fieldset>
  );
}
