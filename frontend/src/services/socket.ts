import useRoom, { Room } from "@stores/useRoom";
import { getRoom } from "./room";
import { UUID } from "crypto";
import { Client } from "@stomp/stompjs";

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
):void {
    const { room, setRoom, setClient, client: oldClient } = useRoom.getState();

    if(oldClient && oldClient.connected) return;
    
    const client: Client = new Client({
        brokerURL: `${import.meta.env.VITE_WEBSOCKET_URL}/websocket`,
        reconnectDelay: 5000,
        onConnect: () => {
            client.subscribe("channel/events/rooms/" + code + "/closed", () =>
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
                "/channel/events/rooms/" + code + "/participants/result",
                (message) => {
                    // recebimento do resultado
                }
            );

            if(isOwner){
                client.subscribe("channel/events/rooms/" + code + "/" + room?.owner + "/start", () => {
                    // função de inicio de sala
                });
            }
        
        }
    });

    client.activate();
    setClient(client);
}