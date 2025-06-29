//import { useNavigate } from "react-router-dom";

export default function StartRoomButton() {
  //const navigate = useNavigate();

  const onStart = () => {
    // iniciar jogo aqui
  };

  return (
    <button type="button" className="btn btn-primary w-full" onClick={onStart}>
      Iniciar
    </button>
  );
}
