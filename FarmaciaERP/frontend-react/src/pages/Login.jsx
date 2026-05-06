import React, { useState } from "react";
import "./login.css";

import { login } from "../services/authService";
function Login() {
    const [form, setForm] = useState({
        email: "",
        password: ""
    });

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const data = await login(form); // 🔥 aquí usas el service
            console.log("DATA:", data);

            // guardar token
            localStorage.setItem("token", data.token);

            // redirigir
            window.location.href = "/usuarios";

        } catch (error) {
            console.error("Error:", error.message);
            alert(error.message);
        }
    };
    return (
        <div className="login-container">
            <div className="login-card">
                <h2 className="title">Farmacia ERP</h2>
                <p className="subtitle">Iniciar sesión</p>

                <form onSubmit={handleSubmit}>
                    <div className="input-group">
                        <label>Email</label>
                        <input
                            type="email"
                            name="email"
                            placeholder="ejemplo@gmail.com"
                            value={form.email}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="input-group">
                        <label>Contraseña</label>
                        <input
                            type="password"
                            name="password"
                            placeholder="********"
                            value={form.password}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <button type="submit" className="btn-login">
                        Ingresar
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Login;