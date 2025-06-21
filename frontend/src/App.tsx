import "@styles/global.css";

import { Navigate, Route, Routes } from "react-router-dom";
import Home from "@pages/Home";
import Login from "@pages/Login";
import Register from "@pages/Register";
import { QueryClient, QueryClientProvider } from "react-query";
import Bookcase from "@pages/Bookcase";
import MainLayout from "@components/Layout/MainLayout";
import Explore from "@pages/Explore";
import Books from "@pages/Books";
import Subscribe from "@pages/Subscribe";
import Match from "@pages/Match";
import MatchRoom from "@pages/MatchRoom";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/home" element={<Home />} />
          <Route path="/bookcase" element={<Bookcase />} />
          <Route path="/explore" element={<Explore />} />
          <Route path="/subscribe" element={<Subscribe />} />
          <Route path="/books/:id" element={<Books />} />
          <Route path="/match" element={<Match />} />
        </Route>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/match/:code" element={<MatchRoom />} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
    </QueryClientProvider>
  );
}

export default App;
