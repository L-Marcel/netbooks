import api from "./axios";

export type ImageData = {
  url: string;
  blob: Blob;
  base64: string;
  filename?: string;
};

export async function imageBlobToBase64(blob: Blob): Promise<string> {
  return await new Promise<string>((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
    reader.readAsDataURL(blob);
  });
}

export async function imageUrlToData(url: string): Promise<ImageData> {
  return api
    .get<Blob>(url, {
      responseType: "blob",
    })
    .then(async (response) => {
      const blob = response.data;
      const base64 = await imageBlobToBase64(blob);
      return {
        url,
        blob,
        base64,
      };
    });
}
