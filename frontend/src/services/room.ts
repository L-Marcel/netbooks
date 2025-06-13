import useRoom, { Participant, Room } from "@stores/useRoom";
import { randomUUID, UUID } from "crypto";
import api from "./axios";
import { connect, disconnect } from "./socket";

// arrumar
export async function join(uuid: UUID, code?: string): Promise<void> {
    const { setParticipant } = useRoom.getState();

    return await api
    .post<Participant>(`rooms/${code}/join`)
    .then((response) => {
      connect(response.data.room, response.data.uuid);
      setParticipant(response.data);
    });;
}

export async function close(): Promise<void> {
    return await api.delete("rooms").then(() => {
        disconnect();
    });
}

export async function create(): Promise<string> {
    const roomCode = randomUUID();
    return await api.post<Room>("rooms", {roomCode} ).then((response) => {
        return response.data.code;
    });
}

export async function getRoom(code?: string): Promise<void> {
  const { setRoom } = useRoom.getState();

  return await api.get<Room>(`rooms/${code}`).then((response) => {
    setRoom(response.data);
  });
}

export async function getOpenRoom(): Promise<Room> {
  return await api.get<Room>("rooms").then((response) => {
    return response.data;
  });
}
