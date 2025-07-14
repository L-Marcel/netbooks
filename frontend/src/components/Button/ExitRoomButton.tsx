import { exit } from "@services/participant";
import { useNavigate } from "react-router-dom";
import Button from ".";

export default function ExitRoomButton() {
  const navigate = useNavigate();

  const onExit = () => {
    exit();
    navigate("/match");
  };

  return (
    <Button className="btn btn-error absolute left-5 top-5" onClick={onExit}>
      Sair
    </Button>
  );
}
