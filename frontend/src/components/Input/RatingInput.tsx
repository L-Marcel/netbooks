interface Props {
  readonly?: boolean;
  rate: number;
}

export function RatingInput({ readonly = false, rate }: Props) {
  const value = Math.round(rate) / 2;

  return (
    <div className="rating rating-lg rating-half">
      {readonly ? (
        <>
          <div
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="0.5 star"
            aria-current={value === 0.5? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="1 star"
            aria-current={value === 1? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="1.5 star"
            aria-current={value === 1.5? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="2 star"
            aria-current={value === 2? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="2.5 star"
            aria-current={value === 2.5? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="3 star"
            aria-current={value === 3? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="3.5 star"
            aria-current={value === 3.5? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="4 star"
            aria-current={value === 4? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="4.5 star"
            aria-current={value === 4.5? "true":"false"}
          />
          <div
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="5 star"
            aria-current={value === 5? "true":"false"}
          />
        </>
      ) : (
        <>
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="0.5 star"
            defaultChecked={value === 0.5}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="1 star"
            defaultChecked={value === 1}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="1.5 star"
            defaultChecked={value === 1.5}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="2 star"
            defaultChecked={value === 2}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="2.5 star"
            defaultChecked={value === 2.5}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="3 star"
            defaultChecked={value === 3}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="3.5 star"
            defaultChecked={value === 3.5}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="4 star"
            defaultChecked={value === 4}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-1 bg-primary"
            aria-label="4.5 star"
            defaultChecked={value === 4.5}
          />
          <input
            type="radio"
            name="rating-11"
            className="mask mask-star-2 mask-half-2 bg-primary"
            aria-label="5 star"
            defaultChecked={value === 5}
          />
        </>
      )}
    </div>
  );
}
