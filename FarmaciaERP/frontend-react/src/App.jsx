import './App.css'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./presentation/pages/Login.jsx";
import Principal from "./presentation/pages/principal.jsx";

import Dashboard from "./presentation/pages/dashboard.jsx";
import Compras from "./presentation/pages/compras.jsx";
import Almacen from "./presentation/pages/almacen.jsx";
import Usuario from "./presentation/pages/usuario.jsx";
import Reporte from "./presentation/pages/reporte.jsx";


function App() {
    return (

            <Routes>

                <Route path="/" element={<Login />} />

                <Route path="/principal" element={<Principal />}>

                    <Route index element={<Dashboard />} />
                    <Route path="compras" element={<Compras />} />
                    <Route path="almacen" element={<Almacen />} />
                    <Route path="usuario" element={<Usuario />} />
                    <Route path="reporte" element={<Reporte />} />


                </Route>

            </Routes>


    );
}

export default App;