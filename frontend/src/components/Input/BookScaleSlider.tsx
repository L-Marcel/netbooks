import Button from "@components/Button";
import { MotionValue, useMotionValueEvent } from "motion/react";
import { useState } from "react";
import { FaRedo, FaSearchMinus, FaSearchPlus } from "react-icons/fa";
import Input from ".";

interface Props {
  scale: MotionValue;
  min: MotionValue;
  max: MotionValue;
  onReset: () => void;
  step?: number;
}

export function ScaleSlider({ scale, min, max, onReset, step = 0.1 }: Props) {
  const [value, setValue] = useState(scale.get());
  const [minValue, setMinValue] = useState(min.get());
  const [maxValue, setMaxValue] = useState(max.get());

  useMotionValueEvent(scale, "change", (latest) => {
    setValue(latest);
  });

  useMotionValueEvent(max, "change", (latest) => {
    setMaxValue(latest);
  });

  useMotionValueEvent(min, "change", (latest) => {
    setMinValue(latest);
  });

  return (
    <div className="z-20 bg-base-200 items-center justify-center p-1 lg:p-2 gap-1 rounded-box w-min max-w-full flex flex-row min-w-1/2 md:min-w-1/3 lg:min-w-1/5 sticky">
      <Button
        onClick={onReset}
        className="btn btn-square btn-ghost btn-primary btn-xs lg:btn-sm"
      >
        <FaRedo />
      </Button>
      <Button
        onClick={() =>
          scale.set(Math.max(Math.min(value - step * 10, maxValue), minValue))
        }
        className="btn btn-square btn-ghost btn-primary not-md:hidden md:btn-xs lg:btn-sm"
      >
        <FaSearchMinus />
      </Button>
      <Input
        onChange={(e) => scale.set(Number.parseFloat(e.target.value) ?? 1)}
        type="range"
        min={minValue}
        max={maxValue}
        step={step}
        value={value}
        className="range range-primary not-md:hidden md:range-xs lg:range-sm"
      />
      <Button
        onClick={() =>
          scale.set(Math.max(Math.min(value + step * 10, maxValue), minValue))
        }
        className="btn btn-square btn-ghost btn-primary not-md:hidden md:btn-xs lg:btn-sm"
      >
        <FaSearchPlus />
      </Button>
    </div>
  );
}
