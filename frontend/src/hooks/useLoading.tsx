import { useCallback, useMemo, useState } from "react";

export type LoadingContext = {
  hasAny: boolean;
  has: (id: string | number) => boolean;
  start: (id: string | number) => void;
  stop: (id: string | number) => void;
  clear: () => void;
};

export default function useLoading(): LoadingContext {
  const [loadingSet, setLoadingSet] = useState(new Set<string | number>());

  const start = useCallback((id: string | number) => {
    setLoadingSet((prev) => {
      const set = new Set(prev);
      set.add(id);
      return set;
    });
  }, []);

  const stop = useCallback((id: string | number) => {
    setLoadingSet((prev) => {
      const set = new Set(prev);
      set.delete(id);
      return set;
    });
  }, []);

  const clear = useCallback(
    () => setLoadingSet(new Set<string | number>()), 
    []
  );

  const hasAny = useMemo(
    () => loadingSet.size > 0, 
    [loadingSet]
  );

  const has = useCallback(
    (id: string | number) => loadingSet.has(id),
    [loadingSet]
  );

  return {
    hasAny,
    has,
    start,
    stop,
    clear,
  };
}
