import CloseRoomButton from "@components/Button/CloseRoomButton";
import CopyLinkButton from "@components/Button/CopyLinkButton";
import ExitRoomButton from "@components/Button/ExitRoomButton";
import StartRoomButton from "@components/Button/StartRoomButton";
import ParticipantsMasonryGrid from "@components/Grid/ParticipantsMasonryGrid";
import AuthGuard from "@components/Guards/AuthGuard";
import ParticipantGuard from "@components/Guards/ParticipantGuard";
import RoomGuard from "@components/Guards/RoomGuard";
import SubscriberGuard from "@components/Guards/SubscriberGuard";
import useRoom from "@stores/useRoom";

export default function MatchRoom() {
    return(
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

function Page(){
    const room = useRoom((state) => state.room);

    const { participant } = useRoom.getState();
    const roomCode = room?.code;

    const isOwner = (room?.owner === participant?.user);
   
    return(
        <main className="flex flex-col w-full h-ful items-center bg-base-100">
            <div className="flex flex-col items-center justify-center w-full h-screen bg-base-100 gap-10">
                {room && (<>
                    {isOwner ? <CloseRoomButton/> : <ExitRoomButton/>}
                    
                    {roomCode && <CopyLinkButton code={roomCode} />}

                    {isOwner ? 
                        <StartRoomButton/>
                    : 
                        <h1 className="text-4xl text-gray-500 mt-4">Aguardando...</h1>
                    }

                    <h1 className="text-xl">Participantes</h1>
                    <ParticipantsMasonryGrid participants={room?.participants ?? []}/>

                </>)}
            </div>
            
        </main>
    );
}