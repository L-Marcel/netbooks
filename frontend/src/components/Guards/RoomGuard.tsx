import { ReactNode, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useRoom from "@stores/useRoom";
import { getRoom } from "../../services/room";

interface Props {
  children: ReactNode;
}

export default function RoomGuard({ children }: Props) {
  const { code } = useParams();

  const room = useRoom((state) => state.room);
  const navigate = useNavigate();

  useEffect(() => {
    if (!room) {
      getRoom(code).catch(() => navigate("/match"));
    }
  }, [room, code, navigate]);

  return room ? children : null;
}
