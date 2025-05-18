import { Area } from "react-easy-crop";

async function createImage(url: string) {
  return new Promise<HTMLImageElement>((resolve, reject) => {
    const image = new Image();
    image.addEventListener("load", () => resolve(image));
    image.addEventListener("error", (error) => reject(error));
    image.setAttribute("crossOrigin", "anonymous");
    image.src = url;
  });
}

export default async function getCroppedImg(src: string, crop: Area) {
  const image = await createImage(src);
  const canvas = document.createElement("canvas");
  const ctx = canvas.getContext("2d");

  if (!ctx) {
    return null;
  }

  canvas.width = image.width;
  canvas.height = image.height;

  ctx.drawImage(image, 0, 0);

  const croppedCanvas = document.createElement("canvas");
  const croppedCtx = croppedCanvas.getContext("2d");

  if (!croppedCtx) {
    return null;
  }

  croppedCanvas.width = crop.width;
  croppedCanvas.height = crop.height;
  croppedCtx.drawImage(
    canvas,
    crop.x,
    crop.y,
    crop.width,
    crop.height,
    0,
    0,
    crop.width,
    crop.height
  );

  return new Promise<{ base64: string; blob: Blob }>((resolve) => {
    croppedCanvas.toBlob((file) => {
      resolve({
        base64: croppedCanvas.toDataURL("image/jpeg"),
        blob: file ?? new Blob(),
      });
    }, "image/jpeg");
  });
}
