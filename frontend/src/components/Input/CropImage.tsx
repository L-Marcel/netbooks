import { useCallback, useRef, useState } from "react";
import Cropper, { Area } from "react-easy-crop";
import FileInput, { FileInputProps } from "./FileInput";
import getCroppedImg from "../../services/crop";

interface CropProps extends FileInputProps {
  imageSize: {
    height: number;
    width: number;
    aspect: number;
  };
}

export default function CropImageDialogue({
  imageSize,
  onFileClear,
  onFileLoaded,
  ...props
}: CropProps) {
  const ref = useRef<HTMLDialogElement>(null);
  const [src, setSrc] = useState<string>("");
  const [crop, setCrop] = useState({ x: 0, y: 0 });
  const [zoom, setZoom] = useState(1);
  const [area, setArea] = useState<Area>({
    x: 0,
    y: 0,
    width: 0,
    height: 0,
  });

  const confirm = useCallback(async () => {
    try {
      const result = await getCroppedImg(src, area);
      if (result) onFileLoaded(result.base64, result.blob);
    } catch (e) {
      console.log(e);
    }
  }, [src, area, onFileLoaded]);

  return (
    <>
      <FileInput
        onFileClear={() => {
          setCrop({ x: 0, y: 0 });
          setZoom(1);
          setArea({ x: 0, y: 0, width: 0, height: 0 });
          ref.current?.close();
          setSrc("");
          onFileClear();
        }}
        onFileLoaded={(_, blob) => {
          const url = URL.createObjectURL(blob);
          setSrc(url);
          ref.current?.showModal();
        }}
        {...props}
      />
      <dialog ref={ref} className="modal">
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
            <button onClick={confirm} className="btn btn-primary">
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
