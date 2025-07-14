import { close } from "@services/room";
import { useNavigate } from "react-router-dom";
import Button from ".";

export default function CloseRoomButton() {
  const navigate = useNavigate();

  const onClose = () => close().then(() => navigate("/match"));

  return (
    <Button className="btn btn-error absolute left-5 top-5" onClick={onClose}>
      Fechar sala
    </Button>
  );
}
