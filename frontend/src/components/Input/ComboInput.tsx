import {
  Combobox,
  ComboboxInput as _ComboboxInput,
  ComboboxOption,
  ComboboxOptions,
} from "@headlessui/react";
import {
  DetailedHTMLProps,
  InputHTMLAttributes,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import { FaCheck } from "react-icons/fa";
import Input from ".";
import { debounce } from "es-toolkit";

export interface ComboboxProps<T>
  extends Omit<
    DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>,
    "onSelect"
  > {
  multiple?: boolean;
  selected?: T | T[];
  onSearch: (query: string, signal: AbortSignal) => Promise<T[]>;
  onSelect: (selected: T | T[]) => void;
  onFilter?: (options: T[]) => T[];
  onDraw: (value: T) => string;
  onRender: (value: T) => {
    id: string;
    name: string;
    value: T;
  };
}

const DEFAULT_ON_FILTER = (options: unknown[]) => options;

export default function ComboboxInput<T>({
  onSelect,
  onDraw,
  onRender,
  onSearch,
  onFilter = DEFAULT_ON_FILTER as (options: T[]) => T[],
  multiple = false,
  selected,
  ...props
}: ComboboxProps<T>) {
  const abortControllerRef = useRef<AbortController | null>(null);

  const [filtered, setFiltered] = useState<T[]>([]);
  const [query, setQuery] = useState("");

  const debouncedFetch = useMemo(
    () =>
      debounce((query: string) => {
        if (abortControllerRef.current) abortControllerRef.current.abort();

        const newController = new AbortController();
        abortControllerRef.current = newController;

        onSearch(query, newController.signal)
          .then(setFiltered)
          .catch((err: Error) => {
            if (err.name !== "AbortError") setFiltered([]);
          });
      }, 500),
    [onSearch, setFiltered]
  );

  useEffect(() => {
    debouncedFetch(query);
  }, [debouncedFetch, query]);

  const options = multiple
    ? selected
      ? [...(selected as T[]), ...filtered]
      : filtered
    : selected
      ? [selected as T, ...filtered]
      : filtered;

  const filteredOptions = onFilter(
    Array.from(
      options
        .reduce((map, currentOption) => {
          const content = onRender(currentOption);
          if (!map.has(content.id)) map.set(content.id, currentOption);
          return map;
        }, new Map<string, T>())
        .values()
    )
  );

  return (
    <Combobox
      multiple={multiple}
      immediate
      virtual={{ options: filteredOptions }}
      value={selected}
      onChange={onSelect}
    >
      <_ComboboxInput
        as={Input}
        displayValue={(option) => onDraw(option as T)}
        onChange={(event) => setQuery(event.target.value)}
        {...props}
      />
      <ComboboxOptions
        anchor="bottom"
        transition
        className="w-(--input-width) border border-base-200 rounded-box bg-base-100 p-1 [--anchor-gap:--spacing(4)]"
      >
        {({ option }) => {
          const content = onRender(option);
          return (
            <ComboboxOption
              key={content.id}
              value={content.value}
              className="group focus-visible:bg-primary hover:bg-primary flex w-full hover:cursor-pointer items-center gap-2 rounded-lg px-3 py-1.5 select-none"
            >
              <FaCheck className="invisible size-4 text-primary-content group-data-selected:visible" />
              <div className="text-sm text-primary-content">{content.name}</div>
            </ComboboxOption>
          );
        }}
      </ComboboxOptions>
    </Combobox>
  );
}
