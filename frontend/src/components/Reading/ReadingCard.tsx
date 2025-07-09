import { Reading } from "@models/reading";
import { format } from "date-fns";
import { FaRegCheckCircle } from "react-icons/fa";
import Loading from "@components/Loading";
import Button from "@components/Button";
import { Link } from "react-router-dom";
import { useLoading } from "@stores/useLoading";
import { finishReading } from "@services/readings";
import { CSSProperties } from "react";

interface Props {
  reading: Reading;
  onFinish: () => void;
}

export default function ReadingCard({ reading, onFinish }: Props) {
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);
  const loadingId = "continue-reading-" + (reading?.id ?? -1);
  const completed = reading.getPercentage() === 100;
  const completeAndFinished = reading.finished && completed;

  const onFinishReading = () => {
    startLoading(loadingId);
    finishReading(reading?.id ?? -1)
      .then(onFinish)
      .finally(() => stopLoading(loadingId));
  };

  const icon = completeAndFinished ? (
    <FaRegCheckCircle className="size-6 text-success" />
  ) : reading.finished ? (
    <div
      className="radial-progress text-error"
      style={
        {
          "--value": reading.getPercentage(),
          "--size": "1.5rem",
          "--thickness": "3px",
        } as CSSProperties
      }
      aria-valuenow={reading.getPercentage()}
      role="progressbar"
    />
  ) : (
    <div
      className="radial-progress text-info"
      style={
        {
          "--value": reading.getPercentage(),
          "--size": "1.5rem",
          "--thickness": "3px",
        } as CSSProperties
      }
      aria-valuenow={reading.getPercentage()}
      role="progressbar"
    />
  );

  const border = completeAndFinished ? (
    <span className="w-2 h-auto bg-success" />
  ) : reading.finished ? (
    <span className="w-2 h-auto bg-error" />
  ) : (
    <span className="w-2 h-auto bg-info" />
  );

  const badge = completeAndFinished ? (
    <span className="badge text-md font-semibold badge-success">
      {reading.getPercentage()}% / finalizada
    </span>
  ) : reading.finished ? (
    <span className="badge text-md font-semibold badge-error">
      {reading.getPercentage()}% / encerrada
    </span>
  ) : (
    <span className="badge text-md font-semibold badge-info">
      {reading.getPercentage()}% / andamento
    </span>
  );

  return (
    <li className="flex flex-row w-full bg-base-200 overflow-hidden rounded-box shadow-sm">
      {border}
      <div className="flex flex-col w-full">
        <div className="flex flex-col p-4 bg-base-300">
          <div className="flex flex-row flex-nowrap items-center gap-2">
            {icon} {badge}
          </div>
        </div>
        <div className="flex flex-col p-4">
          <h3 className="text-md font-semibold mb-1">Informações extras</h3>
          <p className="text-sm font-extralight">
            Início: {format(reading.startedIn, "dd/MM/yyyy")}
          </p>
          <p className="text-sm font-extralight">
            {reading.finished ? "Fim" : "Parada"}:{" "}
            {format(reading.stoppedIn, "dd/MM/yyyy")}
          </p>
          <p className="text-sm font-extralight">
            Página: {reading.currentPage}/{reading.numPages}
          </p>
          {!reading.finished && (
            <div className="flex flex-row gap-3">
              <Link
                to={"/readings/" + reading.id}
                className="mt-4 btn btn-info btn-md"
              >
                Continuar
              </Link>
              <Button
                className={`mt-4 btn ${completed ? "btn-success" : "btn-error"} btn-md`}
                onClick={onFinishReading}
              >
                <Loading
                  id={loadingId}
                  loadingMessage={
                    completed ? "Finalizando..." : "Encerrando..."
                  }
                  defaultMessage={completed ? "Finalizar" : "Encerrar"}
                />
              </Button>
            </div>
          )}
        </div>
      </div>
    </li>
  );
}
