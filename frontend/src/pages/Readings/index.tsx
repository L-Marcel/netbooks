import AuthGuard from "@components/Guards/AuthGuard";
// import { fetchBook } from "@services/books";
// import { fetchReadingsOfBook } from "@services/readings";
// import { useLoading } from "@stores/useLoading";
// import { useCallback, useEffect } from "react";
// import { useParams } from "react-router-dom";

export default function Readings() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  //   const { id } = useParams();
  //   const readingId = Number.parseFloat(id ?? "-1");
  //   const startLoading = useLoading((state) => state.start);
  //   const stopLoading = useLoading((state) => state.stop);

  //   const update = useCallback(() => {
  //     startLoading("book");
  //   }, [startLoading, stopLoading, bookId]);

  //   useEffect(update, [update]);

  return (
    <main className="flex flex-col w-full h-ful items-center bg-base-100">
      <section className="flex flex-col gap-8 w-full"></section>
    </main>
  );
}
