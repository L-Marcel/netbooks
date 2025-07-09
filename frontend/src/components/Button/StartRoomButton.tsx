//import { useNavigate } from "react-router-dom";

import Button from ".";

export default function StartRoomButton() {
  //const navigate = useNavigate();

  const onStart = () => {
    // iniciar jogo aqui
  };

  return (
    <Button className="btn btn-primary w-full" onClick={onStart}>
      Iniciar
    </Button>
  );
}
