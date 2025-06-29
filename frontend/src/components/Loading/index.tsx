interface Props {
  isLoading: boolean;
  defaultMessage: string;
  loadingMessage: string;
}

export default function Loading({
  isLoading,
  defaultMessage,
  loadingMessage,
}: Props) {
  return (
    <>
      {isLoading && <span className="loading loading-spinner"></span>}
      {isLoading ? loadingMessage : defaultMessage}
    </>
  );
}
