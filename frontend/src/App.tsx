import "@styles/global.css";

import { Route, Routes } from "react-router-dom";
import Home from "@pages/Home";
import Login from "@pages/Login";
import Subscribe from "@pages/Subscribe";

function App() {
  return (
    <>
      <Routes>
        <Route path="*" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Subscribe />} />
      </Routes>
    </>
  );
}

export default App;
