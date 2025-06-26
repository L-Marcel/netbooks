import { close } from "@services/room";
import { useNavigate } from "react-router-dom";

export default function CloseRoomButton() {
  const navigate = useNavigate();

  const onClose = () => {
    close().then(() => {
      navigate("/home");
    });
  };

  return (
    <button className="btn btn-error absolute left-5 top-5" onClick={onClose}>
      Fechar sala
    </button>
  );
}
