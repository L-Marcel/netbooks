import Button from ".";
import useRoom from "@stores/useRoom";
import { sendOptionsToParticipants } from "@services/room";

export default function StartRoomButton() {
  const { room } = useRoom.getState();

  const onStart = () => {
    if(room) sendOptionsToParticipants(room?.code);
  };

  return (
    <Button className="btn btn-primary w-full" onClick={onStart}>
      Iniciar
    </Button>
  );
}
