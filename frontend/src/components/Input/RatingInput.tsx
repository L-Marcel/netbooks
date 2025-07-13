import Input from ".";

interface Props {
  readonly?: boolean;
  onChange?: (rate: number) => void;
  rate: number;
}

const DEFAULT_ON_CHANGE = () => {};

export function RatingInput({
  readonly = false,
  rate,
  onChange = DEFAULT_ON_CHANGE,
}: Props) {
  const value = Math.round(rate) / 2;

  return (
    <div className="rating rating-lg rating-half">
      {readonly ? (
        <>
          <div
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning"
            aria-label="0.5 star"
            aria-current={value === 0.5 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning"
            aria-label="1 star"
            aria-current={value === 1 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning"
            aria-label="1.5 star"
            aria-current={value === 1.5 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning"
            aria-label="2 star"
            aria-current={value === 2 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning"
            aria-label="2.5 star"
            aria-current={value === 2.5 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning"
            aria-label="3 star"
            aria-current={value === 3 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning"
            aria-label="3.5 star"
            aria-current={value === 3.5 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning"
            aria-label="4 star"
            aria-current={value === 4 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning"
            aria-label="4.5 star"
            aria-current={value === 4.5 ? "true" : "false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning"
            aria-label="5 star"
            aria-current={value === 5 ? "true" : "false"}
          />
        </>
      ) : (
        <>
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="0.5 star"
            checked={value === 0.5}
            onChange={() => onChange(1)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="1 star"
            checked={value === 1}
            onChange={() => onChange(2)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="1.5 star"
            checked={value === 1.5}
            onChange={() => onChange(3)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="2 star"
            checked={value === 2}
            onChange={() => onChange(4)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="2.5 star"
            checked={value === 2.5}
            onChange={() => onChange(5)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="3 star"
            checked={value === 3}
            onChange={() => onChange(6)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="3.5 star"
            checked={value === 3.5}
            onChange={() => onChange(7)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="4 star"
            checked={value === 4}
            onChange={() => onChange(8)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="4.5 star"
            checked={value === 4.5}
            onChange={() => onChange(9)}
          />
          <Input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary group-[.is-premium]:bg-warning disabled:hover:cursor-wait"
            aria-label="5 star"
            checked={value === 5}
            onChange={() => onChange(10)}
          />
        </>
      )}
    </div>
  );
}
