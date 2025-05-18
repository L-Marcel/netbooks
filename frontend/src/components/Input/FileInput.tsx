import {
  DetailedHTMLProps,
  InputHTMLAttributes,
  useCallback,
  useRef,
  useState,
} from "react";
import Cropper, { Area } from "react-easy-crop";
import getCroppedImg from "../../services/crop";
import { FaUpload } from "react-icons/fa";

interface CropProps
  extends DetailedHTMLProps<
    InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {
  imageSize: {
    height: number;
    width: number;
    aspect: number;
  };
  canClear?: boolean;
  onFileLoaded: (base64: string, blob: Blob) => void;
  onFileClear: () => void;
  className?: string;
}

export default function ImageInput({
  imageSize,
  onFileClear,
  onFileLoaded,
  canClear,
  className,
}: CropProps) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const [src, setSrc] = useState<string>("");
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [area, setArea] = useState<Area>({
    x: 0,
    y: 0,
    width: 0,
    height: 0,
  });

  const onClickToUpload = () => {
    inputRef.current?.click();
  };

  const onClickToClear = () => {
    setCrop({ x: 0, y: 0 });
    setZoom(1);
    setArea({ x: 0, y: 0, width: 0, height: 0 });
    dialogRef.current?.close();
    setSrc("");
    onFileClear();
  };

  const onLoad = (base64: string, blob: Blob) => {
    onFileLoaded(base64, blob);
    const url = URL.createObjectURL(blob);
    setSrc(url);
    dialogRef.current?.showModal();
  };

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.currentTarget.files !== null) {
      const reader = new FileReader();
      const file = (e.currentTarget.files as FileList)[0];
      if (
        file.type.startsWith("image/png") ||
        file.type.startsWith("image/jpeg")
      ) {
        reader.onload = () => onLoad(reader.result?.toString() ?? "", file);
        reader.readAsDataURL(e.currentTarget.files[0]);
      } else {
        //callInvalidImageToast();
      }
    } else {
      onLoad("", new Blob());
    }
  };

  const onConfirm = useCallback(async () => {
    try {
      const result = await getCroppedImg(src, area);
      if (result) onFileLoaded(result.base64, result.blob);
    } catch (e) {
      console.log(e);
    }
  }, [src, area, onFileLoaded]);

  return (
    <>
      <div
        className={`join join-horizontal !overflow-visible ${className ?? ""}`}
      >
        <button
          onClick={onClickToUpload}
          className="btn btn-xs btn-circle btn-neutral focus:btn-primary join-item"
        >
          <FaUpload />
          <input
            ref={inputRef}
            tabIndex={-1}
            type="file"
            accept="image/jpeg, image/jpg, image/png"
            className="hidden focus-visible:outline-none"
            onChange={onChange}
          />
        </button>
        {canClear && (
          <button
            onClick={onClickToClear}
            className="btn btn-xs btn-circle btn-neutral focus:btn-primary join-item"
          >
            X
          </button>
        )}
      </div>
      <dialog ref={dialogRef} className="modal">
        <section
          style={{
            height: imageSize.height,
          }}
        >
          <Cropper
            classes={{ containerClassName: "" }}
            image={src}
            crop={crop}
            zoom={zoom}
            maxZoom={10}
            minZoom={1}
            zoomSpeed={0.25}
            aspect={imageSize.aspect}
            onCropChange={setCrop}
            onCropComplete={(_, area) => setArea(area)}
            onZoomChange={setZoom}
            keyboardStep={2}
          />
        </section>
        <div className="modal-box">
          <form method="dialog">
            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">
              ✕
            </button>
            <button onClick={onConfirm} className="btn btn-primary">
              Confirmar
            </button>
          </form>
          <h3 className="text-lg font-bold">Hello!</h3>
          <p className="py-4">Press ESC key or click on ✕ button to close</p>
        </div>
      </dialog>
    </>
  );
}
