import useRoom, { Participant } from "@stores/useRoom";
import api from "./axios";
import { connect } from "./socket";

export async function exit(): Promise<void> {
  return await api.delete("participants");
}

export async function getParticipant(isOwner?: boolean): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api.get<Participant>("participants/me").then((response) => {
    if (isOwner) {
      connect(response.data.room, true);
    } else {
      connect(response.data.room);
    }
    setParticipant(response.data);
  });
}
