import React from "react";
import "./usuarios.css";
import "../services/authService.js"
function Usuarios() {
    return (
        <div className="container">
            <header className="header">
                <h1>Farmacia ERP</h1>
                <p>Panel principal</p>
            </header>

            <div className="cards">
                <div className="card">
                    <h2>Usuarios</h2>
                    <p>Gestiona los usuarios del sistema</p>
                    <button>Entrar</button>
                </div>

                <div className="card">
                    <h2>Productos</h2>
                    <p>Administra medicamentos y stock</p>
                    <button>Entrar</button>
                </div>

                <div className="card">
                    <h2>Ventas</h2>
                    <p>Control de ventas y facturación</p>
                    <button>Entrar</button>
                </div>

                <div className="card">
                    <h2>Reportes</h2>
                    <p>Estadísticas del negocio</p>
                    <button>Entrar</button>
                </div>
            </div>
        </div>
    );
}

export default Usuarios;