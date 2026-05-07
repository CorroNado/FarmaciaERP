import React from "react";

const alertas = [
    { tipo: "danger", texto: "Stock crítico: Paracetamol 500mg", sub: "Quedan 12 unidades — mínimo 50" },
    { tipo: "danger", texto: "Vencimiento próximo: Amoxicilina", sub: "Lote A-224 vence el 15/05/2026" },
    { tipo: "warning", texto: "Factura por pagar: Distribuidora Lima", sub: "S/ 2,400 — vence hoy" },
    { tipo: "warning", texto: "Stock bajo: Ibuprofeno 400mg", sub: "Quedan 30 unidades — mínimo 50" },
    { tipo: "success", texto: "Recepción completada: Lote B-310", sub: "180 productos ingresados al almacén" },
];

const ventas = [
    { producto: "Paracetamol 500mg", cant: 20, total: "S/ 40" },
    { producto: "Amoxicilina 500mg", cant: 10, total: "S/ 85" },
    { producto: "Omeprazol 20mg", cant: 5, total: "S/ 32" },
    { producto: "Ibuprofeno 400mg", cant: 15, total: "S/ 60" },
    { producto: "Loratadina 10mg", cant: 8, total: "S/ 24" },
];

const stockCritico = [
    { nombre: "Paracetamol 500mg", actual: 12, minimo: 50, color: "danger" },
    { nombre: "Ibuprofeno 400mg", actual: 30, minimo: 50, color: "warning" },
    { nombre: "Metformina 850mg", actual: 18, minimo: 40, color: "warning" },
    { nombre: "Vitamina C 500mg", actual: 35, minimo: 60, color: "success" },
    { nombre: "Atorvastatina 20mg", actual: 8, minimo: 30, color: "danger" },
];

const colorDot = { danger: "bg-danger", warning: "bg-warning", success: "bg-success" };

function Dashboard() {
    return (
        <div>

            {/* MÉTRICAS */}
            <div className="row g-3 mb-3">
                {[
                    { icon: "bi-currency-dollar", label: "Ventas hoy", valor: "S/ 4,820", badge: "badge bg-success-subtle text-success", badgeText: "↑ +12% vs ayer" },
                    { icon: "bi-cart3", label: "Compras del mes", valor: "S/ 18,340", badge: "badge bg-danger-subtle text-danger", badgeText: "↓ -4% vs mes ant." },
                    { icon: "bi-box-seam", label: "Productos en stock", valor: "1,248", badge: "badge bg-warning-subtle text-warning", badgeText: "28 en mínimo" },
                    { icon: "bi-receipt", label: "Facturas pendientes", valor: "7", badge: "badge bg-info-subtle text-info", badgeText: "3 vencen hoy" },
                ].map((m, i) => (
                    <div className="col-md-3" key={i}>
                        <div className="card border-0 bg-light h-100">
                            <div className="card-body">
                                <div className="text-secondary small mb-1">
                                    <i className={`bi ${m.icon} me-1`}></i>{m.label}
                                </div>
                                <div className="fs-4 fw-semibold mb-1">{m.valor}</div>
                                <span className={m.badge} style={{ fontSize: 11 }}>{m.badgeText}</span>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* GRÁFICO VENTAS + ALERTAS */}
            <div className="row g-3 mb-3">
                <div className="col-md-7">
                    <div className="card border h-100" style={{ borderColor: "#e8e8f0" }}>
                        <div className="card-body">
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <span className="fw-medium small text-secondary">Ventas últimos 7 días</span>
                                <span className="text-muted" style={{ fontSize: 12 }}>Soles (S/)</span>
                            </div>
                            {/* Aquí puedes integrar Chart.js o Recharts */}
                            <div className="bg-light rounded d-flex align-items-center justify-content-center" style={{ height: 200 }}>
                                <span className="text-secondary small">Integrar gráfico de ventas aquí</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-md-5">
                    <div className="card border h-100" style={{ borderColor: "#e8e8f0" }}>
                        <div className="card-body">
                            <div className="fw-medium small text-secondary mb-3">Alertas del sistema</div>
                            {alertas.map((a, i) => (
                                <div key={i} className="d-flex align-items-start gap-2 py-2 border-bottom border-light">
                  <span className={`rounded-circle mt-1 flex-shrink-0 ${colorDot[a.tipo]}`}
                        style={{ width: 8, height: 8, display: "inline-block" }}></span>
                                    <div>
                                        <div style={{ fontSize: 13 }}>{a.texto}</div>
                                        <div className="text-secondary" style={{ fontSize: 11 }}>{a.sub}</div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>

            {/* ÚLTIMAS VENTAS + TOP PRODUCTOS + STOCK CRÍTICO */}
            <div className="row g-3">
                <div className="col-md-4">
                    <div className="card border h-100" style={{ borderColor: "#e8e8f0" }}>
                        <div className="card-body">
                            <div className="fw-medium small text-secondary mb-3">Últimas ventas</div>
                            <table className="table table-sm mb-0" style={{ fontSize: 13 }}>
                                <thead className="text-secondary">
                                <tr><th>Producto</th><th>Cant.</th><th className="text-end">Total</th></tr>
                                </thead>
                                <tbody>
                                {ventas.map((v, i) => (
                                    <tr key={i}>
                                        <td>{v.producto}</td>
                                        <td>{v.cant}</td>
                                        <td className="text-end">{v.total}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card border h-100" style={{ borderColor: "#e8e8f0" }}>
                        <div className="card-body">
                            <div className="fw-medium small text-secondary mb-3">Top productos — este mes</div>
                            <div className="bg-light rounded d-flex align-items-center justify-content-center" style={{ height: 200 }}>
                                <span className="text-secondary small">Integrar gráfico horizontal aquí</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card border h-100" style={{ borderColor: "#e8e8f0" }}>
                        <div className="card-body">
                            <div className="fw-medium small text-secondary mb-3">Stock crítico — 28 productos</div>
                            <div className="d-flex flex-column gap-3">
                                {stockCritico.map((s, i) => (
                                    <div key={i}>
                                        <div className="d-flex justify-content-between mb-1" style={{ fontSize: 13 }}>
                                            <span>{s.nombre}</span>
                                            <span className={`text-${s.color} fw-medium`}>{s.actual} / {s.minimo}</span>
                                        </div>
                                        <div className="progress" style={{ height: 6 }}>
                                            <div
                                                className={`progress-bar bg-${s.color}`}
                                                style={{ width: `${Math.round((s.actual / s.minimo) * 100)}%` }}
                                            ></div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    );
}

export default Dashboard;