import React from "react";
import logo  from "./logo.png";
import "./principal.css";

function MiComponente() {
    return (

        <div className="layout">

            {/* SIDEBAR */}
            <aside className="sidebar">
                <div className="logo-container">
                    <img src={logo} alt="Logo" className="logo" />
                </div>

                <nav className="menu">
                    <button className="menu-item">Usuarios</button>
                    <button className="menu-item">Medicamentos</button>
                    <button className="menu-item">Recetas</button>
                    <button className="menu-item">Reportes</button>
                </nav>
            </aside>

            {/* CONTENIDO PRINCIPAL */}
            <main className="main">

                <header className="topbar">
                    <h2>Bienvenido al sistema 🏥</h2>

                    <button className="login-btn">
                        👤 Iniciar sesión
                    </button>
                </header>

                <section className="content">
                    <div className="card">Gestión de Usuarios</div>
                    <div className="card">Control de Medicamentos</div>
                    <div className="card">Recetas Médicas</div>
                </section>

            </main>
        </div>

    );
}

export default MiComponente;