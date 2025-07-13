import Button from "@components/Button";
import AuthGuard from "@components/Guards/AuthGuard";
import SubscriberGuard from "@components/Guards/SubscriberGuard";
import Input from "@components/Input";
import { create, getOpenRoom, join } from "@services/room";
import useUser from "@stores/useUser";
import { useState } from "react";
import { FaArrowRight } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export default function Match() {
  return (
    <AuthGuard>
      <SubscriberGuard>
        <Page />
      </SubscriberGuard>
    </AuthGuard>
  );
}

function Page() {
  const navigate = useNavigate();
  const user = useUser((state) => state.user);
  const [roomCode, setRoomCode] = useState("");

  const createRoom = async () => {
    try {
      const room = await getOpenRoom();
      navigate("/match/" + room.code);
    } catch {
      create().then((code) => {
        navigate("/match/" + code);
      });
    }
  };

  const enterRoom = async () => {
    if (!roomCode) return;
    try {
      if (user) {
        await join(user.name, roomCode);
        navigate("/match/" + roomCode);
      }
    } catch (error) {
      console.error("Erro ao entrar na sala:", error);
    }
  };

  return (
    <main
      className="flex flex-col w-full h-full min-h-[calc(100vh-4rem)] items-center bg-base-100"
      style={{ height: "calc(100vh - 70px)" }}
    >
      <div className="flex flex-col items-center justify-center w-md h-screen bg-base-100 gap-10">
        <Button className="btn btn-primary w-md" onClick={createRoom}>
          Criar Sala
        </Button>

        <hr className="w-md border-gray-700" />

        <div className="flex">
          <Input
            type="text"
            name="code"
            id="code"
            placeholder="Código da Sala"
            autoComplete="false"
            value={roomCode}
            onChange={(e) => setRoomCode(e.target.value)}
            className="input text-lg w-[400px]"
          />
          <Button className="btn w-[50px]" onClick={enterRoom}>
            <FaArrowRight />
          </Button>
        </div>
      </div>
    </main>
  );
}
