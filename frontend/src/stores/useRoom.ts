import { User } from "@models/user";
import { UUID } from "crypto";
import { Client } from "@stomp/stompjs";
import { create } from "zustand";
import { disconnect } from "@services/socket";

export type Participant = {
  user: UUID;
  name: string;
  room: string;
};

export type Room = {
  code: string;
  owner: UUID;
  participants: Participant[];
};

type RoomStore = {
  client?: Client;
  room?: Room;
  participant?: Participant;
  setClient: (client?: Client) => void;
  setRoom: (room?: Room) => void;
  setParticipant: (participant?: Participant) => void;
};

const useRoom = create<RoomStore>((set, get) => ({
  setClient: (client?: Client) => set({ client }),
  setRoom: (room?: Room) => {
    set({ room });

    const { participant } = get();
    if (participant) {
      const updated = room?.participants.find(
        (p) => p.user === participant.user
      );
      if (updated) {
        set({ participant: updated });
      } else {
        disconnect();
      }
    }
  },
  setParticipant: (participant?: Participant) => set({ participant }),
}));

export default useRoom;
