import React from "react";
import { Link, Outlet, useLocation } from "react-router-dom";
import 'bootstrap-icons/font/bootstrap-icons.css';

const navItems = [
    { to: "/principal", label: "Dashboard", icon: "bi-grid-1x2" },
    { to: "/principal/usuario", label: "Usuario", icon: "bi-person" },
    { to: "/principal/compras", label: "Compras", icon: "bi-basket2-fill" },
    { to: "/principal/almacen", label: "Almacén", icon: "bi-box-seam" },
    { to: "/principal/reporte", label: "Reporte", icon: "bi-box-seam" },
];

function Principal() {
    const location = useLocation();

    const currentPage = navItems.find(item =>
        location.pathname === item.to ||
        (item.to !== "/principal" && location.pathname.startsWith(item.to))
    );
    const pageTitle = currentPage?.label ?? "Dashboard";

    return (
        <div className="d-flex vh-100" style={{ fontFamily: "'DM Sans', sans-serif" }}>

            {/* SIDEBAR */}
            <div
                className="d-flex flex-column bg-white py-4 px-3"
                style={{ width: "240px", minWidth: "240px", borderRight: "1.5px solid #e8e8f0" }}
            >
                {/* Brand */}
                <div
                    className="d-flex align-items-center gap-2 pb-3 mb-3 fw-semibold"
                    style={{ borderBottom: "1.5px solid #e8e8f0", color: "#1a1a2e" }}
                >
                    <div
                        className="d-flex align-items-center justify-content-center rounded-2 text-white"
                        style={{ width: 30, height: 30, background: "#1a1a2e", fontSize: 14 }}
                    ><i class="bi bi-capsule"></i></div>
                    Farmacia ERP
                </div>

                {/* Nav */}
                <div className="d-flex flex-column gap-1">
                    {navItems.map(({ to, label, icon }) => {
                        const isActive = location.pathname === to ||
                            (to !== "/principal" && location.pathname.startsWith(to));
                        return (
                            <Link
                                key={to}
                                to={to}
                                className={`d-flex align-items-center gap-2 px-3 py-2 rounded-2 text-decoration-none small ${
                                    isActive ? "fw-medium text-white" : "text-secondary"
                                }`}
                                style={{
                                    background: isActive ? "#1a1a2e" : "transparent",
                                    transition: "all 0.15s",
                                }}
                                onMouseEnter={e => { if (!isActive) e.currentTarget.style.background = "#f4f4f8"; e.currentTarget.style.color = "#1a1a2e"; }}
                                onMouseLeave={e => { if (!isActive) e.currentTarget.style.background = "transparent"; }}
                            >
                                <i className={`bi ${icon}`} style={{ width: 18, textAlign: "center" }}></i>
                                {label}
                            </Link>
                        );
                    })}
                </div>
            </div>

            {/* MAIN */}
            <div className="d-flex flex-column flex-grow-1 overflow-hidden">

                {/* TOPBAR */}
                <div
                    className="d-flex align-items-center justify-content-between bg-white px-4"
                    style={{ height: "60px", borderBottom: "1.5px solid #e8e8f0", flexShrink: 0 }}
                >
                    {/* Título de página */}
                    <div>
                        <div className="fw-semibold" style={{ fontSize: 15, color: "#1a1a2e" }}>
                            {pageTitle}
                        </div>
                        <div className="text-secondary" style={{ fontSize: 11 }}>
                            Farmacia ERP / {pageTitle}
                        </div>
                    </div>

                    {/* Usuario logueado */}
                    <div className="d-flex align-items-center gap-2 px-3 py-2 rounded-2"
                         style={{ cursor: "pointer" }}>
                        <div
                            className="d-flex align-items-center justify-content-center rounded-circle text-white fw-semibold"
                            style={{ width: 32, height: 32, background: "#1a1a2e", fontSize: 12 }}
                        >
                            JD
                        </div>
                        <div>
                            <div className="fw-medium" style={{ fontSize: 13, color: "#1a1a2e" }}>Juan Díaz</div>
                            <div className="text-secondary" style={{ fontSize: 11 }}>Administrador</div>
                        </div>
                    </div>
                </div>

                {/* CONTENIDO */}
                <div className="flex-grow-1 p-4 overflow-auto" style={{ background: "#fafafa" }}>
                    <Outlet />
                </div>

            </div>
        </div>
    );
}

export default Principal;