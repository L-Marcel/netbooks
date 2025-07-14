import { Participant } from "@stores/useRoom";

interface Props {
  participants: Participant[];
}

export default function ParticipantsMasonryGrid({ participants }: Props) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 space-y-4">
      {participants.map((participant) => (
        <div
          key={participant.name}
          className="break-inside-avoid h-min-content bg-base-300 rounded-xl p-4 shadow text-center font-medium text-base-content"
        >
          {participant.name}
        </div>
      ))}
    </div>
  );
}
