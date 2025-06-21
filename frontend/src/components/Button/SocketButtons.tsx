import { exit } from "@services/participant";
import { close } from "@services/room";
import { useNavigate } from "react-router-dom";


export function CloseRoomButton() {
    const navigate = useNavigate();

    const onClose = () => {
        close().then(() => {
            navigate("/home");
        });
    };

    return(
        <button
            className="btn btn-error absolute left-5 top-5"
            onClick={onClose}
        >
            Fechar sala
        </button>
    );
}

export function ExitRoomButton() {
    const navigate = useNavigate();

    const onExit = () => {
        exit();
        navigate("/home");
    };

    return(
        <button
            className="btn btn-error absolute left-5 top-5"
            onClick={onExit}
        >
            Sair
        </button>
    );
}


export function StartRoomButton() {
    const navigate = useNavigate();

    const onStart = () => {
        // iniciar jogo aqui
    };

    return(
        <button
            className="btn btn-primary"
            onClick={onStart}
        >
            Iniciar
        </button>
    );
}

