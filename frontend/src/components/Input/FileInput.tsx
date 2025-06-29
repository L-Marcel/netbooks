import {
  ButtonHTMLAttributes,
  ChangeEvent,
  DetailedHTMLProps,
  DragEvent,
  useRef,
  useState,
} from "react";
import Cropper, { Area } from "react-easy-crop";
import getCroppedImg from "../../services/crop";
import { FaCloudUploadAlt, FaTrash, FaUpload } from "react-icons/fa";
import { FaCheck, FaRotate, FaX } from "react-icons/fa6";

interface CropProps
  extends Exclude<
    DetailedHTMLProps<
      ButtonHTMLAttributes<HTMLButtonElement>,
      HTMLButtonElement
    >,
    "onClick"
  > {
  file: {
    url: string;
    name?: string;
  };
  imageSize: {
    height: number;
    width: number;
    aspect: number;
  };
  canClear?: boolean;
  onImageLoaded: (base64: string, blob: Blob, filename?: string) => void;
  onImageClear: () => void;
}

export default function ImageInput({
  file,
  imageSize,
  onImageClear,
  onImageLoaded,
  canClear,
  ...props
}: CropProps) {
  const areaRef = useRef<HTMLDialogElement>(null);
  const cropRef = useRef<HTMLDialogElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const [src, setSrc] = useState<string>(file.url);
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [area, setArea] = useState<Area>({
    x: 0,
    y: 0,
    width: 0,
    height: 0,
  });

  const onResetZoom = () => setZoom(1);

  const onChangeZoom = (e: ChangeEvent<HTMLInputElement>) => {
    setZoom(Number.parseFloat(e.currentTarget.value));
  };

  const onClickToOpen = () => {
    cropRef.current?.close();
    areaRef.current?.showModal();
  };

  const onClickToUpload = () => inputRef.current?.click();

  const onClickToClear = () => {
    onCancel();
    if (inputRef.current) inputRef.current.value = "";
    setSrc("");
    onImageClear();
  };

  const onLoad = (blob: Blob) => {
    const url = URL.createObjectURL(blob);
    setSrc(url);
    cropRef.current?.showModal();
    areaRef.current?.close();
  };

  const onCrop = (base64: string, blob: Blob) => {
    const file =
      inputRef.current !== null &&
      inputRef.current.files !== null &&
      inputRef.current.files?.length > 0
        ? inputRef.current.files[0]
        : undefined;

    const url = URL.createObjectURL(blob);
    setSrc(url);
    onImageLoaded(base64, blob, file?.name);
    cropRef.current?.close();
    areaRef.current?.close();
  };

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.currentTarget.files !== null) {
      const file = (e.currentTarget.files as FileList)[0];
      onLoad(file);
    } else {
      onLoad(new Blob());
    }
  };

  const onDropImage = (e: DragEvent<HTMLButtonElement>) => {
    e.preventDefault();

    const files = e.dataTransfer.files;
    if (files.length === 0) return;

    const file = files[0];
    if (!file.type.startsWith("image/")) return;

    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(file);
    if (inputRef.current) {
      inputRef.current.files = dataTransfer.files;
      onLoad(file);
    }
  };

  const onConfirm = async () => {
    try {
      const result = await getCroppedImg(src, area);
      if (result) onCrop(result.base64, result.blob);
    } catch (e) {
      console.log(e);
      onCancel();
    }
  };

  const onCancel = () => {
    setCrop({ x: 0, y: 0 });
    setZoom(1);
    setArea({ x: 0, y: 0, width: 0, height: 0 });
    setSrc(file.url);
    cropRef.current?.close();
    areaRef.current?.close();
  };

  return (
    <>
      <button type="button" {...props} onClick={onClickToOpen} />
      <dialog ref={areaRef} className="modal">
        <div className="modal-box flex flex-col gap-4">
          <h1 className="text-lg font-semibold">Importar imagem</h1>
          <button
            type="button"
            onClick={onClickToUpload}
            onDrop={onDropImage}
            onDragOver={(e) => e.preventDefault()}
            style={{
              aspectRatio: imageSize.aspect,
              maxHeight: imageSize.height,
            }}
            className={`btn btn-outline overflow-hidden p-0 h-auto ${src ? "" : "not-focus:border-dashed"} not-focus:border-base-300 not-focus:border-2 focus:btn-primary focus:bg-base-100`}
          >
            {src ? (
              <img src={src} className="size-fit !object-contain" />
            ) : (
              <div className="flex flex-col gap-4 justify-center items-center">
                <FaCloudUploadAlt className="size-9" />
                <div className="flex flex-col gap-1">
                  <strong>Clique ou arraste para carregar</strong>
                  <p className="hidden sm:block font-light">
                    Aceitamos PNG, JPG ou JPEG
                  </p>
                </div>
              </div>
            )}
          </button>
          <div className="flex gap-4 justify-between items-center">
            <input
              ref={inputRef}
              type="file"
              className="hidden"
              accept="image/jpeg, image/jpg, image/png"
              onChange={onChange}
            />
            <p className="overflow-ellipsis overflow-hidden w-full text-nowrap font-light">
              {file.name ?? file.url ?? "Nenhum arquivo carregado..."}
            </p>
            <button
              type="button"
              className="btn btn-soft btn-primary btn-square"
              onClick={onClickToUpload}
            >
              <FaUpload />
            </button>
            <button
              type="button"
              disabled={!canClear}
              className="btn btn-soft btn-secondary btn-square"
              onClick={onClickToClear}
            >
              <FaTrash />
            </button>
            <button
              type="button"
              onClick={onCancel}
              className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
            >
              <FaX />
            </button>
          </div>
        </div>
      </dialog>
      <dialog ref={cropRef} className="modal">
        <div className="modal-box flex flex-col gap-4 p-0">
          <h1 className="mt-6 mx-6 text-lg font-semibold">Recortar imagem</h1>
          <Cropper
            classes={{ containerClassName: "!relative bg-base-100 rounded-md" }}
            style={{
              containerStyle: {
                maxHeight: imageSize.height,
              },
            }}
            image={src}
            crop={crop}
            zoom={zoom}
            maxZoom={10}
            minZoom={0.1}
            zoomSpeed={0.25}
            aspect={imageSize.aspect}
            onCropChange={setCrop}
            onCropComplete={(_, area) => setArea(area)}
            onZoomChange={setZoom}
            keyboardStep={2}
            objectFit="cover"
            restrictPosition={false}
          />
          <div className="flex gap-4 mx-6 mb-6">
            <div className="flex flex-row gap-4 justify-between items-center w-full flex-wrap">
              <div className="flex flex-row gap-4 justify-start items-center not-sm:w-full">
                <button
                  type="button"
                  className="btn btn-circle btn-soft btn-primary"
                  onClick={onResetZoom}
                >
                  <FaRotate />
                </button>
                <input
                  type="range"
                  step={0.1}
                  min="0.1"
                  max="10"
                  value={zoom}
                  onChange={onChangeZoom}
                  className="range range-primary w-full"
                />
              </div>
              <div className="flex flex-row gap-4 justify-start items-center">
                <button
                  type="button"
                  className="btn btn-soft btn-primary"
                  onClick={onConfirm}
                >
                  <FaCheck />
                  Confirmar
                </button>
                <button
                  type="button"
                  className="btn btn-soft btn-square btn-secondary"
                  onClick={onCancel}
                >
                  <FaX />
                </button>
              </div>
            </div>
            <button
              type="button"
              onClick={onCancel}
              className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
            >
              <FaX />
            </button>
          </div>
        </div>
      </dialog>
    </>
  );
}
