import { UUID } from "crypto";
import { Client } from "@stomp/stompjs";
import { create } from "zustand";
import { disconnect } from "@services/socket";
import { searchMatchResult } from "@services/room";
import useMatch from "./useMatch";

export type Participant = {
  user: UUID;
  name: string;
  room: string;
};

export type Room = {
  code: string;
  owner: UUID;
  participants: Participant[];
  voted: number;
};

type RoomStore = {
  client?: Client;
  room?: Room;
  participant?: Participant;
  voted: number;
  setClient: (client?: Client) => void;
  setRoom: (room?: Room) => void;
  setParticipant: (participant?: Participant) => void;
  setVoted: () => void;
  resetVoted: () => void;
};

const useRoom = create<RoomStore>((set, get) => ({
  voted: 0,
  setClient: (client?: Client) => set({ client }),
  setRoom: (room?: Room) => {
    set({ room });

    if (!room) return;

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
  setVoted: () => {
    const current = get().voted + 1;
    set({ voted: current });

    console.log("Votos:", current);

    const room = get().room;
    if (room && current >= room.participants.length) {
      console.log(useMatch.getState().selectedOptions, room.code);
      searchMatchResult(useMatch.getState().getTopGenres(), room.code);
    }
  },

  resetVoted: () => set({ voted: 0 }),
}));

export default useRoom;
