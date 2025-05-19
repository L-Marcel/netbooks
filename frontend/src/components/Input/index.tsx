import { FaCheck, FaX } from "react-icons/fa6";
import { Validation } from "../../services/axios";

interface InputProps
  extends React.DetailedHTMLProps<
    React.InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {
  label?: string;
  validations?: Validation[];
}

export default function Input({
  id,
  validations = [],
  label,
  className = "",
  ...props
}: InputProps) {
  const hasError =
    validations.length > 0 &&
    validations.some((validation) => validation.error);

  return (
    <fieldset className="flex flex-col w-full">
      <p className="mb-2 font-light text-sm">{label}</p>
      <label
        className={`input focus-within:input-primary ${hasError ? "!input-error" : ""} w-full text-base-content ${className}`}
      >
        <input id={id} name={id} {...props} />
      </label>
      <p
        style={{ display: hasError ? "flex" : "none" }}
        className="flex flex-col gap-1 pt-2 font-light text-xs sm:text-sm"
      >
        {validations.map((validation) => (
          <span
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
