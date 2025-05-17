import { DetailedHTMLProps, InputHTMLAttributes } from "react";
import { IconType } from "react-icons";
import { FaEraser } from "react-icons/fa";
import { FaPencil } from "react-icons/fa6";

export interface FileInputProps
  extends DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {
  icon?: IconType;
  canClear?: boolean;
  onFileLoaded: (base64: string, blob: Blob) => void;
  onFileClear: () => void;
  inputOnly?: boolean;
}

export default function FileInput({
  icon: Icon = FaPencil,
  canClear,
  inputOnly = false,
  onFileClear = () => {},
  onFileLoaded = () => {},
  ...props
}: FileInputProps) {
  async function onChange(e: React.ChangeEvent<HTMLInputElement>) {
    if (e.currentTarget.files !== null) {
      const reader = new FileReader();
      const file = (e.currentTarget.files as FileList)[0];
      if (
        file.type.startsWith("image/png") ||
        file.type.startsWith("image/jpeg")
      ) {
        reader.onload = () =>
          onFileLoaded(reader.result?.toString() ?? "", file);
        reader.readAsDataURL(e.currentTarget.files[0]);
      } else {
        //callInvalidImageToast();
      }
    } else {
      onFileLoaded("", new Blob());
    }
  }

  if (inputOnly)
    return (
      <input
        type="file"
        tabIndex={-1}
        accept="image/jpeg, image/png"
        onChange={onChange}
        {...props}
      />
    );

  return (
    <div>
      {canClear && (
        <button type="button" onClick={onFileClear}>
          <FaEraser />
        </button>
      )}
      <label>
        <div>
          <Icon />
          <input
            type="file"
            tabIndex={0}
            accept="image/jpeg, image/png"
            onChange={onChange}
            {...props}
          />
          <p>Alterar avatar</p>
        </div>
      </label>
    </div>
  );
}
