import './App.css'
import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Principal from "./pages/principal.jsx";
import Usuarios from "./pages/usuarios.jsx";
import Compra from "./pages/Compra.jsx";

function App() {
    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/usuarios" element={<Usuarios />} />
            <Route path="/principal" element={<Principal />} />
            <Route path="/compra" element={<Compra/>} />
        </Routes>
    );
}

export default App;