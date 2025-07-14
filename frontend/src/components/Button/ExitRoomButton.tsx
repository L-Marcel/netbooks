import { exit } from "@services/participant";
import Button from ".";

export default function ExitRoomButton() {
  const onExit = () => exit();

  return (
    <Button className="btn btn-error absolute left-5 top-5" onClick={onExit}>
      Sair
    </Button>
  );
}
