import React from "react";
import { useNavigate } from "react-router-dom";

function Home() {
    const navigate = useNavigate();

    return (
        <div>
            <h1>Bienvenido al sistema</h1>

            <button onClick={() => navigate("/usuarios")}>
                Ir a Usuarios
            </button>
        </div>
    );
}

export default Home;