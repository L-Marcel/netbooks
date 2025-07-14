import "@styles/global.css";

import { Navigate, Route, Routes } from "react-router-dom";
import Home from "@pages/Home";
import Login from "@pages/Login";
import Register from "@pages/Register";
import MainLayout from "@components/Layout/MainLayout";
import Explore from "@pages/Explore";
import Books from "@pages/Books";
import Subscribe from "@pages/Subscribe";
import Match from "@pages/Match";
import MatchRoom from "@pages/MatchRoom";
import Billing from "@pages/Billing";
import NavigationListener from "@components/Listeners/NavigationListener";
import Readings from "@pages/Readings";
import EditProfile from "@pages/EditProfile";
import Admin from "@pages/Admin";
import EditBook from "@pages/Books/Edit";
import Bookcase from "@pages/Bookcase";

function App() {
  return (
    <>
      <NavigationListener />
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/home" element={<Home />} />
          <Route path="/bookcase" element={<Bookcase />} />
          <Route path="/explore" element={<Explore />} />
          <Route path="/subscribe" element={<Subscribe />} />
          <Route path="/billing" element={<Billing />} />
          <Route path="/books/:id" element={<Books />} />
          <Route path="/books/:id/edit" element={<EditBook />} />
          <Route path="/match" element={<Match />} />
          <Route path="/profile/edit" element={<EditProfile />} />
          <Route path="/admin" element={<Admin />} />
        </Route>
        <Route path="/readings/:id" element={<Readings />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/match/:code" element={<MatchRoom />} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
    </>
  );
}

export default App;
