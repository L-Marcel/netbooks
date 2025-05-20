import { FaCheck, FaX } from "react-icons/fa6";
import { Validation } from "../../services/axios";
import { IconType } from "react-icons";

interface InputProps
  extends React.DetailedHTMLProps<
    React.InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {
  label?: string;
  validations?: Validation[];
  icon?: IconType;
}

export default function Input({
  id,
  validations = [],
  label,
  className = "",
  icon: Icon,
  ...props
}: InputProps) {
  const hasError =
    validations.length > 0 &&
    validations.some((validation) => validation.error);

  return (
    <fieldset className="flex flex-col w-full">
      <p className="mb-2 font-medium text-sm">{label}</p>
      <label
        className={`input focus-within:input-primary ${hasError ? "!input-error" : ""} w-full text-base-content ${className}`}
      >
        {Icon && <Icon className="size-4"/>}
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
