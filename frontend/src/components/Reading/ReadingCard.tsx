import { Reading } from "@models/reading";
import { format } from "date-fns";
import { FaRegCheckCircle, FaRegCircle, FaTrashAlt } from "react-icons/fa";
import Loading from "@components/Loading";
import Button from "@components/Button";
import { Link } from "react-router-dom";
import { useLoading } from "@stores/useLoading";
import { finishReading } from "@services/readings";

interface Props {
  reading: Reading;
  onFinish: () => void;
}

export default function ReadingCard({ reading, onFinish }: Props) {
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);
  const loadingId = "continue-reading-" + (reading?.id ?? -1);

  const onFinishReading = () => {
    startLoading(loadingId);
    finishReading(reading?.id ?? -1)
      .then(onFinish)
      .finally(() => stopLoading(loadingId));
  };

  const icon = reading.finished ? (
    <FaRegCheckCircle className="size-6 text-success" />
  ) : (
    <FaRegCircle className="size-6 text-info" />
  );

  const border = reading.finished ? (
    <span className="w-2 h-auto bg-success" />
  ) : (
    <span className="w-2 h-auto bg-info" />
  );

  const badge = reading.finished ? (
    <span className="badge text-md font-semibold badge-success">
      Leitura finalizada
    </span>
  ) : (
    <span className="badge text-md font-semibold badge-info">
      Leitura em andamento
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
            Página: {reading.currentPage}
          </p>
          {!reading.finished && (
            <div className="flex flex-row gap-3">
              <Button
                className="mt-4 btn btn-error btn-md btn-square"
                onClick={onFinishReading}
              >
                <Loading id={loadingId} defaultMessage={<FaTrashAlt />} />
              </Button>
              <Link
                to={"/readings/" + reading.id}
                className="mt-4 btn btn-info btn-md w-min text-nowrap"
              >
                Continuar leitura
              </Link>
            </div>
          )}
        </div>
      </div>
    </li>
  );
}
