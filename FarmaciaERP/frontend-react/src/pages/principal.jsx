import React from "react";
import { useNavigate } from "react-router-dom";

function Home() {
    const navigate = useNavigate();

    return (
        <div className="container d-flex flex-column justify-content-center align-items-center vh-100">

            <h1 className="mb-4">Bienvenido al sistema</h1>

            <div className="card p-4 shadow" style={{ width: "300px" }}>
                <p className="text-center mb-3">
                    Accede al módulo de usuarios
                </p>

                <button
                    className="btn btn-primary w-100"
                    onClick={() => navigate("/usuarios")}
                >
                    Ir a Usuarios
                </button>
            </div>

        </div>
    );
}


export default Home;