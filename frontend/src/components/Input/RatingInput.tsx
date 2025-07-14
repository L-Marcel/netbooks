import { useMemo } from "react";
import Input from ".";

interface Props {
  readonly?: boolean;
  onChange?: (rate: number) => void;
  rate: number;
}

const RATING_STEPS = Array.from({ length: 10 }, (_, i) => i + 1);
const DEFAULT_ON_CHANGE = () => {};

export function RatingInput({
  readonly = false,
  rate,
  onChange = DEFAULT_ON_CHANGE,
}: Props) {
  const value = useMemo(() => Math.round(rate) / 2, [rate]);

  if (readonly) {
    return (
      <div className="rating rating-lg rating-half">
        {RATING_STEPS.map((step) => {
          const star = step / 2;
          const isHalf = step % 2 !== 0;

          return (
            <div
              key={star}
              className={`mask mask-star-2 ${isHalf ? "mask-half-1" : "mask-half-2"} bg-primary group-[.is-premium]:bg-warning`}
              aria-label={`${star} star`}
              aria-current={value === star ? "true" : "false"}
            />
          );
        })}
      </div>
    );
  }

  return (
    <div className="rating rating-lg rating-half" key={value}>
      {RATING_STEPS.map((step) => {
        const star = step / 2;
        const isHalf = step % 2 !== 0;

        return (
          <Input
            key={star}
            type="radio"
            name="rating-input"
            className={`mask mask-star-2 ${isHalf ? "mask-half-1" : "mask-half-2"} bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait`}
            aria-label={`${star} star`}
            defaultChecked={value === star}
            onChange={() => onChange(step)}
          />
        );
      })}
    </div>
  );
}
