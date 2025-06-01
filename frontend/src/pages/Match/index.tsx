import CopyLinkButton from "@components/Button/CopyLinkButton";
import AuthGuard from "@components/Guards/AuthGuard";
import SubscriberGuard from "@components/Guards/SubscriberGuard";

export default function Match() {
    return(
        /*
        <AuthGuard>
            <SubscriberGuard>
                <Page />
            </SubscriberGuard>
        </AuthGuard>
        */
       <Page />
    );
}

function Page(){
    const roomCode = "12345";

    return(
        <main className="flex flex-col w-full h-ful items-center bg-base-100">
            <div className="flex flex-col items-center justify-center w-full h-screen bg-base-100">
                <CopyLinkButton code={roomCode}/>
                <button
                    className="btn btn-primary"
                    //onClick={}
                >
                    Iniciar
                </button>
            </div>
        </main>
    );
}