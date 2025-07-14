import useRoom, { Participant, Room } from "@stores/useRoom";
import api from "./axios";
import { connect, disconnect } from "./socket";
import useUser from "@stores/useUser";
import { Genres } from "@stores/useMatch";

export async function join(
  userName: string,
  code?: string,
  isOwner?: boolean
): Promise<void> {
  const { setParticipant } = useRoom.getState();

  return await api
    .post<Participant>(`rooms/${code}/join`, { userName })
    .then((response) => {
      if (isOwner) {
        connect(response.data.room, response.data.user, true);
      } else {
        connect(response.data.room, response.data.user);
      }
      setParticipant(response.data);
    });
}

export async function close(): Promise<void> {
  return await api.delete("rooms").then(() => disconnect());
}

export async function create(): Promise<string> {
  return await api.post<Room>("rooms").then((response) => {
    const { user } = useUser.getState();
    connect(response.data.code, user?.uuid, true);
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

export async function sendOptionsToParticipants(code: string): Promise<Genres>  {
  return await api.post(`rooms/${code}/sendOptions`);
}

export async function searchMatchResult(genres: string[], code: string) {
  return await api.post(`rooms/${code}/result`, genres);
}