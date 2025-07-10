import { FaCheck, FaX } from "react-icons/fa6";
import { Validation } from "../../services/axios";
import { IconType } from "react-icons";
import Input from ".";
import { ButtonHTMLAttributes, DetailedHTMLProps, useRef } from "react";
import Button from "@components/Button";

interface Props
  extends Omit<
    DetailedHTMLProps<
      ButtonHTMLAttributes<HTMLButtonElement>,
      HTMLButtonElement
    >,
    "onClick"
  > {
  files?: File[];
  label?: string;
  validations?: Validation[];
  onFilesChanged?: (files: File[]) => void;
  icon?: IconType;
  accept?: string[];
  multiple?: boolean;
}

const DEFAULT_FILES: File[] = [];
const DEFAULT_VALIDATIONS: Validation[] = [];
const DEFAULT_ACCEPT: string[] = [];
const DEFAULT_ON_FILES_CHANGED: (files: File[]) => void = () => {};

export default function FieldFile({
  id,
  files = DEFAULT_FILES,
  validations = DEFAULT_VALIDATIONS,
  label,
  className = "",
  onFilesChanged = DEFAULT_ON_FILES_CHANGED,
  icon: Icon,
  accept = DEFAULT_ACCEPT,
  multiple = false,
  ...props
}: Props) {
  const ref = useRef<HTMLInputElement>(null);

  const hasError =
    validations.length > 0 &&
    validations.some((validation) => validation.error);

  const onClick = () => {
    ref.current?.click();
  };

  const onFilesChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.currentTarget.files) {
      const files = Array.from(event.currentTarget.files);
      onFilesChanged(files);
    }

    event.target.value = "";
  };

  return (
    <fieldset className="flex flex-col w-full overflow-hidden">
      <p className="mb-2 font-medium text-sm">{label}</p>
      <Button
        className={`input hover:cursor-pointer focus-within:input-primary ${hasError ? "!input-error" : ""} w-full text-base-content ${className}`}
        onClick={onClick}
        {...props}
      >
        {Icon && <Icon className="size-4" />}
        {files.length > 0 ? files[0].name : <span>Selecione arquivo</span>}
      </Button>
      <Input
        onChange={onFilesChange}
        accept={accept.join(",")}
        multiple={multiple}
        ref={ref}
        type="file"
        id={id}
        name={id}
        className="hidden"
      />
      <p
        style={{ display: hasError ? "flex" : "none" }}
        className="flex flex-col gap-1 pt-2 font-light text-xs sm:text-sm"
      >
        {validations.map((validation) => (
          <span
            key={validation.content + "-" + validation.error}
            className={`flex flex-row justify-start items-baseline gap-1 ${validation.error ? "text-error" : "text-success"}`}
          >
            {validation.error ? (
              <FaX className="not-sm:hidden pt-1" />
            ) : (
              <FaCheck className="not-sm:hidden pt-1" />
            )}{" "}
            {validation.content}
          </span>
        ))}
      </p>
    </fieldset>
  );
}
