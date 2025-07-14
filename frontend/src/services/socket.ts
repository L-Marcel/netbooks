import useRoom, { Room } from "@stores/useRoom";
// import { getRoom } from "./room";
import { UUID } from "crypto";
import { Client } from "@stomp/stompjs";
import useMatch, { Genres } from "@stores/useMatch";

export function disconnect(): void {
  const { setRoom, setParticipant, setClient } = useRoom.getState();

  setClient(undefined);
  setRoom(undefined);
  setParticipant(undefined);
}

export function connect(
  code?: string,
  participant?: UUID,
  isOwner?: boolean
): void {
  const { room, setRoom, setClient, client: oldClient } = useRoom.getState();

  if (oldClient && oldClient.connected) return;
  console.log("Participante conectado ao websocket!");

  const client: Client = new Client({
    brokerURL: `${import.meta.env.VITE_WEBSOCKET_URL}/websocket`,
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe("/channel/events/rooms/" + code + "/closed", () =>
        disconnect()
      );

      client.subscribe(
        "/channel/events/rooms/" + code + "/updated",
        (message) => {
          const room: Room = JSON.parse(message.body);
          setRoom(room);
        }
      );

      client.subscribe(
        "/channel/events/rooms/" + code + "/participants",
        (message) => {
          const tags: { name: string }[] = JSON.parse(message.body);
          const genres: Genres = tags.map(tag => tag.name);

          console.log("GENRES:", genres);

          useMatch.getState().setGenres(genres);
        }
      );

      client.subscribe(
        "/channel/events/rooms/" + code + "/participants/result",
        (message) => {
          console.log(message);
          // recebimento do resultado
        }
      );

      if (isOwner) {
        client.subscribe(
          "channel/events/rooms/" + code + "/" + room?.owner + "/start",
          () => {
            // função de inicio de sala
          }
        );
      }
    },
  });

  client.activate();
  setClient(client);
}
