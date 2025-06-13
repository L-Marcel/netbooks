import CopyLinkButton from "@components/Button/CopyLinkButton";
import AuthGuard from "@components/Guards/AuthGuard";
import SubscriberGuard from "@components/Guards/SubscriberGuard";
import { close, getRoom } from "@services/room";
import useRoom from "@stores/useRoom";
import { useNavigate } from "react-router-dom";

export default function Match() {
    return(
        <AuthGuard>
            <SubscriberGuard>
                <Page />
            </SubscriberGuard>
        </AuthGuard>
    );
}

function Page(){
    const { room, participant } = useRoom.getState();
    const roomCode = "12345";

    const isOwner = (room?.owner === participant?.uuid);

    /*
    const navigate = useNavigate();
    const onClose = () => {
        close().then(() => {
        navigate("/home");
        });
    };
    */
   
    return(
        <main className="flex flex-col w-full h-ful items-center bg-base-100">
            <button
                className="btn btn-error absolute left-5 top-5"
                //onClick={{isOwner ? onClose : onExit}}
            >
                {isOwner ? "Fechar sala" : "Sair"}
            </button>
            <div className="flex flex-col items-center justify-center w-full h-screen bg-base-100 gap-10">
                <CopyLinkButton code={roomCode}/>

                {isOwner ? (
                    <button
                        className="btn btn-primary"
                        //onClick={}
                    >
                        Iniciar
                    </button>
                 ) : (
                    <p className="text-lg text-gray-500 mt-4">Aguardando...</p>
                 )}
            </div>
        </main>
    );
}