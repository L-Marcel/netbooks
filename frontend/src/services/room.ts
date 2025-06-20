import useRoom, { Participant, Room } from "@stores/useRoom";
import api from "./axios";
import { connect, disconnect } from "./socket";
import { User } from "@models/user";

export async function join(userName: string, code?: string): Promise<void> {
    const { setParticipant } = useRoom.getState();

    return await api
    .post<Participant>(`rooms/${code}/join`, 
      {userName},
    )
    .then((response) => {
      connect(response.data.room, response.data.user.uuid);
      setParticipant(response.data);
    });;
}

export async function close(): Promise<void> {
    return await api.delete("rooms").then(() => {
        disconnect();
    });
}

export async function create(): Promise<string> {
    return await api.post<Room>("rooms",).then((response) => {
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
