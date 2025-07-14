import { ReactNode, useEffect, useState } from "react";
import useRoom from "@stores/useRoom";
import { getParticipant } from "../../services/participant";
import { useNavigate } from "react-router-dom";

interface Props {
  children: ReactNode;
}

export default function ParticipantGuard({ children }: Props) {
  const navigate = useNavigate();
  const participant = useRoom((state) => state.participant);
  const room = useRoom((state) => state.room);
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    getParticipant()
      .catch(() => {
        if (room) navigate("/match");
      })
      .finally(() => setChecked(true));
  }, [setChecked, navigate, room]);

  return participant && checked ? children : null;
}
