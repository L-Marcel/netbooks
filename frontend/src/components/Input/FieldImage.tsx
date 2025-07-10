import { FaCheck, FaX } from "react-icons/fa6";
import { Validation } from "../../services/axios";
import { IconType } from "react-icons";
import ImageInput, { CropProps } from "./ImageInput";

interface FieldProps extends CropProps {
  label?: string;
  validations?: Validation[];
  icon?: IconType;
}

const DEFAULT_VALIDATIONS: Validation[] = [];

export default function FieldImage({
  validations = DEFAULT_VALIDATIONS,
  label,
  className = "",
  icon: Icon,
  file,
  ...props
}: FieldProps) {
  const hasError =
    validations.length > 0 &&
    validations.some((validation) => validation.error);

  return (
    <fieldset className="flex flex-col w-full">
      <p className="mb-2 font-medium text-sm">{label}</p>
      <ImageInput
        className={`input hover:cursor-pointer focus-within:input-primary ${hasError ? "!input-error" : ""} w-full text-base-content ${className}`}
        file={file}
        {...props}
      >
        {Icon && <Icon className="size-4" />}
        {file?.name ??
          (file.url != "" ? file.url : "Nenhum arquivo selecionado...")}
      </ImageInput>
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
