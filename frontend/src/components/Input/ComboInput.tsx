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
import Button from "@components/Button";

export interface ComboboxProps<T>
  extends Omit<
    DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>,
    "onSelect"
  > {
  multiple?: boolean;
  selected?: T | T[];
  onSearch: (query: string, signal: AbortSignal) => Promise<T[]>;
  onSelect: (selected: T | T[]) => void;
  onEnter?: (query: string) => string;
  filterData?: (options: T[]) => T[];
  onDraw: (value?: T) => string | undefined;
  extractData: (value: T) => {
    id: string;
    name: string;
    value: T;
  };
}

const DEFAULT_FILTER_DATA = (options: unknown[]) => options;
const DEFAULT_ON_ENTER = (query: string) => query;

export default function ComboboxInput<T>({
  onSelect,
  onDraw,
  extractData,
  onSearch,
  filterData = DEFAULT_FILTER_DATA as (options: T[]) => T[],
  onEnter = DEFAULT_ON_ENTER,
  multiple = false,
  selected,
  ...props
}: ComboboxProps<T>) {
  const abortControllerRef = useRef<AbortController>(null);
  const comboboxOptionsRef = useRef<HTMLDivElement>(null);
  const comboboxInputRef = useRef<HTMLInputElement>(null);
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
      }, 750),
    [onSearch, setFiltered]
  );

  useEffect(() => {
    debouncedFetch(query);
  }, [debouncedFetch, query]);

  const options = useMemo(
    () =>
      multiple
        ? selected
          ? [...(selected as T[]), ...filtered]
          : filtered
        : selected
          ? [selected as T, ...filtered]
          : filtered,
    [selected, filtered, multiple]
  );

  const filteredOptions = useMemo(
    () =>
      filterData(
        Array.from(
          options
            .reduce((map, currentOption) => {
              const content = extractData(currentOption);
              if (!map.has(content.id)) map.set(content.id, currentOption);
              return map;
            }, new Map<string, T>())
            .values()
        )
      ),
    [options, filterData, extractData]
  );

  const selecteds = multiple ? ((selected as T[]) ?? []) : [];

  const onRemoveSelected = (option: T) => {
    if (multiple) {
      const content = extractData(option);
      onSelect(
        selecteds.filter((selected) => {
          const selectedContent = extractData(selected);
          return content.id !== selectedContent.id;
        })
      );
    }
  };

  const onKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key !== "Enter") return;

    const hasActiveOption = comboboxOptionsRef.current?.querySelector(
      '[data-headlessui-state="active"]'
    );

    if (!hasActiveOption) {
      event.preventDefault();
      event.stopPropagation();
      const newQuery = onEnter(query);
      setQuery(newQuery);
      if (comboboxInputRef.current) comboboxInputRef.current.value = newQuery;
    }
  };

  return (
    <>
      <Combobox
        multiple={multiple}
        virtual={{ options: filteredOptions }}
        immediate
        value={selected}
        onChange={onSelect}
      >
        <_ComboboxInput
          as={Input}
          ref={comboboxInputRef}
          onKeyDown={onKeyDown}
          displayValue={(option) => onDraw(option as T) ?? query}
          onChange={(event) => setQuery(event.target.value)}
          {...props}
        />
        {(!multiple || selecteds.length !== filteredOptions.length) && (
          <ComboboxOptions
            ref={comboboxOptionsRef}
            anchor="bottom"
            transition
            className="w-(--input-width) z-20 border border-base-200 rounded-box bg-base-100 p-1 [--anchor-gap:--spacing(4)]"
          >
            {({ option }) => {
              const content = extractData(option);
              return (
                <ComboboxOption
                  key={content.id}
                  value={content.value}
                  className={`group focus-visible:bg-primary hover:bg-primary flex w-full hover:cursor-pointer items-center gap-2 rounded-lg px-3 py-1.5 select-none ${multiple ? "data-selected:hidden" : ""}`}
                >
                  <FaCheck className="invisible size-4 text-primary-content group-data-selected:visible" />
                  <div className="text-sm text-primary-content">
                    {content.name}
                  </div>
                </ComboboxOption>
              );
            }}
          </ComboboxOptions>
        )}
      </Combobox>
      {selecteds.length > 0 && (
        <div
          onClick={(e) => e.preventDefault()}
          className="flex cursor-default -mt-2 flex-row flex-wrap justify-start items-center pb-4 gap-1"
        >
          {selecteds.map((option) => {
            const content = extractData(option);
            return (
              <Button
                onClick={(e) => {
                  e.preventDefault();
                  onRemoveSelected(option);
                }}
                className="text-sm badge btn btn-primary btn-ghost overflow-hidden"
                key={content.id}
              >
                {content.name}
              </Button>
            );
          })}
        </div>
      )}
    </>
  );
}
