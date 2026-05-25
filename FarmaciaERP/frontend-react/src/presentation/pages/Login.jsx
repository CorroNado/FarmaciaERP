import React, { useState } from "react";
import { login } from "../../infrastructure/services/authService";

function Login() {
    const [form, setForm] = useState({ emailContact: "", password: "" });
    const [showPass, setShowPass] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const data = await login(form);
            console.log("DATA:", data);
            localStorage.setItem("token", data.token);
            window.location.href = "/principal";
        } catch (error) {
            console.error("Error:", error.message);
            alert(error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div
            className="d-flex align-items-center justify-content-center vh-100"
            style={{ background: "#f0f4f8" }}
        >
            <div style={{ width: "100%", maxWidth: 420 }}>

                {/* Logo + título */}
                <div className="text-center mb-4">
                    <div
                        className="d-inline-flex align-items-center justify-content-center rounded-3 mb-3"
                        style={{ width: 56, height: 56, background: "#1a1a2e" }}
                    >
                        <i className="bi bi-capsule text-white" style={{ fontSize: 24 }}></i>
                    </div>
                    <h4 className="fw-semibold mb-1">FarmaERP Pro</h4>
                    <p className="text-secondary mb-0" style={{ fontSize: 14 }}>
                        Sistema Integral de Gestión Farmacéutica
                    </p>
                </div>

                {/* Card */}
                <div
                    className="card border shadow-sm"
                    style={{ borderColor: "#e8e8f0", borderRadius: 16 }}
                >
                    <div className="card-body p-4">
                        <h5 className="fw-semibold mb-1">Iniciar Sesión</h5>
                        <p className="text-secondary mb-4" style={{ fontSize: 13 }}>
                            Ingresa tus credenciales para continuar
                        </p>

                        <form onSubmit={handleSubmit}>

                            {/* Email */}
                            <div className="mb-3">
                                <label className="form-label small fw-medium">
                                    Correo Electrónico
                                </label>
                                <div className="input-group">
                  <span className="input-group-text bg-white border-end-0">
                    <i className="bi bi-person text-secondary"></i>
                  </span>
                                    <input
                                        type="emailContact"
                                        name="emailContact"
                                        className="form-control border-start-0 ps-0"
                                        placeholder="user@farmacia.com"
                                        value={form.emailContact}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>
                            </div>

                            {/* Contraseña */}
                            <div className="mb-3">
                                <label className="form-label small fw-medium">
                                    Contraseña
                                </label>
                                <div className="input-group">
                  <span className="input-group-text bg-white border-end-0">
                    <i className="bi bi-lock text-secondary"></i>
                  </span>
                                    <input
                                        type={showPass ? "text" : "password"}
                                        name="password"
                                        className="form-control border-start-0 border-end-0 ps-0"
                                        placeholder="••••••••"
                                        value={form.password}
                                        onChange={handleChange}
                                        required
                                    />
                                    <button
                                        type="button"
                                        className="input-group-text bg-white border-start-0"
                                        onClick={() => setShowPass(!showPass)}
                                        style={{ cursor: "pointer" }}
                                    >
                                        <i className={`bi ${showPass ? "bi-eye-slash" : "bi-eye"} text-secondary`}></i>
                                    </button>
                                </div>
                            </div>

                            {/* Recordarme + olvidé contraseña */}
                            <div className="d-flex align-items-center justify-content-between mb-4">
                                <div className="form-check mb-0">
                                    <input className="form-check-input" type="checkbox" id="recordarme" />
                                    <label className="form-check-label small" htmlFor="recordarme">
                                        Recordarme
                                    </label>
                                </div>
                                <a href="#" className="small text-decoration-none" style={{ color: "#378ADD" }}>
                                    ¿Olvidaste tu contraseña?
                                </a>
                            </div>

                            {/* Botón */}
                            <button
                                type="submit"
                                className="btn w-100 fw-medium"
                                style={{ background: "#1a1a2e", color: "#fff", borderRadius: 8, padding: "10px" }}
                                disabled={loading}
                            >
                                {loading
                                    ? <><span className="spinner-border spinner-border-sm me-2"></span>Iniciando…</>
                                    : "Iniciar Sesión"
                                }
                            </button>

                        </form>
                    </div>
                </div>

            </div>
        </div>
    );
}

export default Login;