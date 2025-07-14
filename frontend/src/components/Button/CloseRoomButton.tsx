import { close } from "@services/room";
import Button from ".";

export default function CloseRoomButton() {
  const onClose = () => close();

  return (
    <Button
      className="btn btn-secondary absolute left-5 top-5"
      onClick={onClose}
    >
      Fechar sala
    </Button>
  );
}
