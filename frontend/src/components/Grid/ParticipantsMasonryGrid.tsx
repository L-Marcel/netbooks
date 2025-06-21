import { Participant } from "@stores/useRoom";
import React from "react";

interface Props {
  participants: Participant[];
}

export default function ParticipantsMasonryGrid(
    { participants}  : Props
) {
  return (
    <div className="p-4 columns-1 sm:columns-2 md:columns-3 gap-4 space-y-4">
      {participants.map((participant, index) => (
        <div
          key={index}
          className="break-inside-avoid bg-gray-500 rounded-2xl p-4 shadow-md text-center font-medium text-white"
        >
          {participant.name}
        </div>
      ))}
    </div>
  );
}
