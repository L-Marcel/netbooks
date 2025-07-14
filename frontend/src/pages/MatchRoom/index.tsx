import Button from "@components/Button";
import CloseRoomButton from "@components/Button/CloseRoomButton";
import CopyLinkButton from "@components/Button/CopyLinkButton";
import ExitRoomButton from "@components/Button/ExitRoomButton";
import Cards from "@components/GenresCard/Cards";
import ParticipantsMasonryGrid from "@components/Grid/ParticipantsMasonryGrid";
import AuthGuard from "@components/Guards/AuthGuard";
import ParticipantGuard from "@components/Guards/ParticipantGuard";
import RoomGuard from "@components/Guards/RoomGuard";
import SubscriberGuard from "@components/Guards/SubscriberGuard";
import { sendOptionsToParticipants } from "@services/room";
import useMatch from "@stores/useMatch";
import useRoom from "@stores/useRoom";

export default function MatchRoom() {
  return (
    <AuthGuard>
      <SubscriberGuard>
        <RoomGuard>
          <ParticipantGuard>
            <Page />
          </ParticipantGuard>
        </RoomGuard>
      </SubscriberGuard>
    </AuthGuard>
  );
}

function Page() {
  const room = useRoom((state) => state.room);
  const participant = useRoom((state) => state.participant);
  const genres = useMatch((state) => state.genres);

  const roomCode = room?.code;
  const isOwner = room?.owner === participant?.user;

  const onStart = () => {
    if (room) sendOptionsToParticipants(room?.code);
  };

  return (
    <main className="flex flex-col w-full h-full min-h-[100dvh] items-center bg-base-100">
      <div className="flex flex-col items-center justify-center max-w-full mx-4 min-h-[100dvh] bg-base-100">
        <div className="flex flex-col items-center max-w-full px-5 py-5 justify-center gap-6">
          {room && (
            <>
              {isOwner ? <CloseRoomButton /> : <ExitRoomButton />}
              {genres ? (
                <Cards genresOptions={genres} />
              ) : (
                <>
                  <h1 className="text-xl font-semibold">
                    Montando uma sala...
                  </h1>
                  {roomCode && <CopyLinkButton code={roomCode} />}
                  {isOwner ? (
                    <Button
                      className="btn btn-primary btn-wide"
                      onClick={onStart}
                    >
                      Iniciar
                    </Button>
                  ) : (
                    <h1 className="text-xl font-semibold">Aguardando dono.</h1>
                  )}
                  <p className="text-lg">Participantes</p>
                  <ParticipantsMasonryGrid
                    participants={room?.participants ?? []}
                  />
                </>
              )}
            </>
          )}
        </div>
      </div>
    </main>
  );
}
