import { FaCheck, FaX } from "react-icons/fa6";
import { Validation } from "../../services/axios";
import TextArea from "./TextArea";

interface FieldProps
  extends React.DetailedHTMLProps<
    React.TextareaHTMLAttributes<HTMLTextAreaElement>,
    HTMLTextAreaElement
  > {
  label?: string;
  validations?: Validation[];
}

const DEFAULT_VALIDATIONS: Validation[] = [];

export default function FieldArea({
  id,
  validations = DEFAULT_VALIDATIONS,
  label,
  className = "",
  ...props
}: FieldProps) {
  const hasError =
    validations.length > 0 &&
    validations.some((validation) => validation.error);

  return (
    <fieldset className="flex flex-col w-full">
      <p className="mb-2 font-medium text-sm">{label}</p>
      <TextArea
        className={`textarea focus-within:textarea-primary ${hasError ? "!textarea-error" : ""} w-full text-base-content ${className}`}
        id={id}
        name={id}
        {...props}
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
