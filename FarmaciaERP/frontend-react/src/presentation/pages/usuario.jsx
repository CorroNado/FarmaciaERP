import React, { useState } from "react";

const USUARIOS_INICIALES = [
    { id: 1, nombre: "Carlos Mendoza",  email: "c.mendoza@farma.pe", rol: "Admin",         estado: "Activo",   telefono: "987-654-321", creado: "02/01/2025", ultimo: "Hoy" },
    { id: 2, nombre: "Ana Torres",      email: "a.torres@farma.pe",  rol: "Farmacéutico",  estado: "Activo",   telefono: "912-345-678", creado: "15/01/2025", ultimo: "Ayer" },
    { id: 3, nombre: "Luis Quispe",     email: "l.quispe@farma.pe",  rol: "Cajero",        estado: "Activo",   telefono: "956-123-789", creado: "20/01/2025", ultimo: "04/05/2025" },
    { id: 4, nombre: "María Huanca",    email: "m.huanca@farma.pe",  rol: "Almacén",       estado: "Inactivo", telefono: "934-567-890", creado: "01/02/2025", ultimo: "01/03/2025" },
    { id: 5, nombre: "Jorge Paredes",   email: "j.paredes@farma.pe", rol: "Cajero",        estado: "Activo",   telefono: "921-987-654", creado: "10/02/2025", ultimo: "Hoy" },
    { id: 6, nombre: "Rosa Ccoa",       email: "r.ccoa@farma.pe",    rol: "Farmacéutico",  estado: "Activo",   telefono: "945-321-456", creado: "18/02/2025", ultimo: "Hoy" },
    { id: 7, nombre: "Pedro Mamani",    email: "p.mamani@farma.pe",  rol: "Almacén",       estado: "Activo",   telefono: "963-147-258", creado: "05/03/2025", ultimo: "05/05/2025" },
];

const AVATAR_COLORS = [
    ["#E6F1FB","#0C447C"], ["#EAF3DE","#27500A"], ["#FAEEDA","#633806"],
    ["#EEEDFE","#3C3489"], ["#FAECE7","#712B13"], ["#E1F5EE","#085041"], ["#FBEAF0","#72243E"],
];

const ROL_BADGE = {
    Admin: "primary", Farmacéutico: "success", Cajero: "warning", Almacén: "secondary"
};

const FORM_VACIO = { nombre: "", email: "", rol: "", estado: "Activo", telefono: "" };
const PER_PAGE = 5;

function iniciales(nombre) {
    return nombre.split(" ").slice(0, 2).map(w => w[0]).join("");
}

function Usuario() {
    const [usuarios, setUsuarios] = useState(USUARIOS_INICIALES);
    const [nextId, setNextId] = useState(8);
    const [busqueda, setBusqueda] = useState("");
    const [filtroRol, setFiltroRol] = useState("");
    const [filtroEstado, setFiltroEstado] = useState("");
    const [pagina, setPagina] = useState(1);
    const [modal, setModal] = useState(null); // null | { tipo: "nuevo"|"editar"|"ver"|"eliminar", usuario? }
    const [form, setForm] = useState(FORM_VACIO);

    // Filtrado
    const filtrados = usuarios.filter(u => {
        const q = busqueda.toLowerCase();
        return (
            (!q || u.nombre.toLowerCase().includes(q) || u.email.toLowerCase().includes(q)) &&
            (!filtroRol || u.rol === filtroRol) &&
            (!filtroEstado || u.estado === filtroEstado)
        );
    });

    const totalPaginas = Math.max(1, Math.ceil(filtrados.length / PER_PAGE));
    const paginaActual = Math.min(pagina, totalPaginas);
    const filas = filtrados.slice((paginaActual - 1) * PER_PAGE, paginaActual * PER_PAGE);

    const activos = usuarios.filter(u => u.estado === "Activo").length;

    // Abrir modales
    const abrirNuevo = () => { setForm(FORM_VACIO); setModal({ tipo: "nuevo" }); };
    const abrirEditar = (u) => { setForm({ ...u }); setModal({ tipo: "editar", usuario: u }); };
    const abrirVer = (u) => setModal({ tipo: "ver", usuario: u });
    const abrirEliminar = (u) => setModal({ tipo: "eliminar", usuario: u });
    const cerrar = () => setModal(null);

    // CRUD
    const guardar = () => {
        if (!form.nombre || !form.email || !form.rol) return alert("Nombre, correo y rol son obligatorios.");
        if (modal.tipo === "nuevo") {
            setUsuarios([...usuarios, { ...form, id: nextId, creado: "Hoy", ultimo: "—" }]);
            setNextId(nextId + 1);
        } else {
            setUsuarios(usuarios.map(u => u.id === modal.usuario.id ? { ...u, ...form } : u));
        }
        cerrar();
    };

    const eliminar = () => {
        setUsuarios(usuarios.filter(u => u.id !== modal.usuario.id));
        cerrar();
    };

    return (
        <div>
            {/* STATS */}
            <div className="row g-3 mb-3">
                {[
                    { label: "Total usuarios", valor: usuarios.length, color: "" },
                    { label: "Activos",        valor: activos,                    color: "text-success" },
                    { label: "Inactivos",      valor: usuarios.length - activos,  color: "text-danger" },
                    { label: "Roles",          valor: 4,                          color: "" },
                ].map((s, i) => (
                    <div className="col-md-3" key={i}>
                        <div className="card border-0 bg-light">
                            <div className="card-body py-2">
                                <div className="text-secondary" style={{ fontSize: 11 }}>{s.label}</div>
                                <div className={`fs-4 fw-semibold ${s.color}`}>{s.valor}</div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* TOOLBAR */}
            <div className="d-flex gap-2 mb-3 flex-wrap">
                <div className="input-group" style={{ maxWidth: 280 }}>
                    <span className="input-group-text bg-white"><i className="bi bi-search text-secondary"></i></span>
                    <input
                        className="form-control"
                        placeholder="Buscar nombre o email…"
                        value={busqueda}
                        onChange={e => { setBusqueda(e.target.value); setPagina(1); }}
                        style={{ fontSize: 13 }}
                    />
                </div>
                <select className="form-select" style={{ maxWidth: 160, fontSize: 13 }}
                        value={filtroRol} onChange={e => { setFiltroRol(e.target.value); setPagina(1); }}>
                    <option value="">Todos los roles</option>
                    <option>Admin</option>
                    <option>Farmacéutico</option>
                    <option>Cajero</option>
                    <option>Almacén</option>
                </select>
                <select className="form-select" style={{ maxWidth: 160, fontSize: 13 }}
                        value={filtroEstado} onChange={e => { setFiltroEstado(e.target.value); setPagina(1); }}>
                    <option value="">Todos los estados</option>
                    <option>Activo</option>
                    <option>Inactivo</option>
                </select>
                <button className="btn ms-auto d-flex align-items-center gap-1"
                        style={{ background: "#1a1a2e", color: "#fff", fontSize: 13 }}
                        onClick={abrirNuevo}>
                    <i className="bi bi-plus-lg"></i> Nuevo usuario
                </button>
            </div>

            {/* TABLA */}
            <div className="card border" style={{ borderColor: "#e8e8f0", borderRadius: 12 }}>
                <div className="table-responsive">
                    <table className="table mb-0 align-middle" style={{ fontSize: 13 }}>
                        <thead className="table-light">
                        <tr>
                            <th>Usuario</th>
                            <th>Rol</th>
                            <th>Estado</th>
                            <th>Último acceso</th>
                            <th>Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        {filas.length === 0 ? (
                            <tr><td colSpan={5} className="text-center text-secondary py-4">Sin resultados</td></tr>
                        ) : filas.map((u) => {
                            const [bg, fg] = AVATAR_COLORS[(u.id - 1) % AVATAR_COLORS.length];
                            return (
                                <tr key={u.id}>
                                    <td>
                                        <div className="d-flex align-items-center gap-2">
                                            <div style={{
                                                width: 32, height: 32, borderRadius: "50%",
                                                background: bg, color: fg,
                                                display: "flex", alignItems: "center", justifyContent: "center",
                                                fontSize: 11, fontWeight: 600, flexShrink: 0
                                            }}>
                                                {iniciales(u.nombre)}
                                            </div>
                                            <div>
                                                <div className="fw-medium">{u.nombre}</div>
                                                <div className="text-secondary" style={{ fontSize: 11 }}>{u.email}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span className={`badge rounded-pill bg-${ROL_BADGE[u.rol]}-subtle text-${ROL_BADGE[u.rol]}`} style={{ fontSize: 11 }}>{u.rol}</span></td>
                                    <td><span className={`badge rounded-pill ${u.estado === "Activo" ? "bg-success-subtle text-success" : "bg-danger-subtle text-danger"}`} style={{ fontSize: 11 }}>{u.estado}</span></td>
                                    <td className="text-secondary">{u.ultimo}</td>
                                    <td>
                                        <div className="d-flex gap-1">
                                            <button className="btn btn-sm btn-outline-primary" title="Ver detalle" onClick={() => abrirVer(u)}>
                                                <i className="bi bi-eye"></i>
                                            </button>
                                            <button className="btn btn-sm btn-outline-warning" title="Editar" onClick={() => abrirEditar(u)}>
                                                <i className="bi bi-pencil"></i>
                                            </button>
                                            <button className="btn btn-sm btn-outline-danger" title="Eliminar" onClick={() => abrirEliminar(u)}>
                                                <i className="bi bi-trash"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </table>
                </div>

                {/* PAGINACIÓN */}
                <div className="d-flex align-items-center justify-content-between px-3 py-2 border-top" style={{ fontSize: 12 }}>
          <span className="text-secondary">
            Mostrando {filas.length ? (paginaActual - 1) * PER_PAGE + 1 : 0}–{Math.min(paginaActual * PER_PAGE, filtrados.length)} de {filtrados.length}
          </span>
                    <nav>
                        <ul className="pagination pagination-sm mb-0">
                            {Array.from({ length: totalPaginas }, (_, i) => (
                                <li key={i} className={`page-item ${paginaActual === i + 1 ? "active" : ""}`}>
                                    <button className="page-link" onClick={() => setPagina(i + 1)}>{i + 1}</button>
                                </li>
                            ))}
                        </ul>
                    </nav>
                </div>
            </div>

            {/* ── MODAL NUEVO / EDITAR ── */}
            {(modal?.tipo === "nuevo" || modal?.tipo === "editar") && (
                <div className="modal show d-block" style={{ background: "rgba(0,0,0,.35)" }}>
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">{modal.tipo === "nuevo" ? "Nuevo usuario" : "Editar usuario"}</h5>
                                <button className="btn-close" onClick={cerrar}></button>
                            </div>
                            <div className="modal-body">
                                <div className="row g-3">
                                    <div className="col-md-7">
                                        <label className="form-label small">Nombre completo</label>
                                        <input className="form-control form-control-sm" value={form.nombre}
                                               onChange={e => setForm({ ...form, nombre: e.target.value })} placeholder="Ej: Juan Pérez" />
                                    </div>
                                    <div className="col-md-5">
                                        <label className="form-label small">Teléfono</label>
                                        <input className="form-control form-control-sm" value={form.telefono}
                                               onChange={e => setForm({ ...form, telefono: e.target.value })} placeholder="987-654-321" />
                                    </div>
                                    <div className="col-12">
                                        <label className="form-label small">Correo electrónico</label>
                                        <input className="form-control form-control-sm" value={form.email}
                                               onChange={e => setForm({ ...form, email: e.target.value })} placeholder="correo@farma.pe" />
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label small">Rol</label>
                                        <select className="form-select form-select-sm" value={form.rol}
                                                onChange={e => setForm({ ...form, rol: e.target.value })}>
                                            <option value="">Seleccionar…</option>
                                            <option>Admin</option>
                                            <option>Farmacéutico</option>
                                            <option>Cajero</option>
                                            <option>Almacén</option>
                                        </select>
                                    </div>
                                    <div className="col-md-6">
                                        <label className="form-label small">Estado</label>
                                        <select className="form-select form-select-sm" value={form.estado}
                                                onChange={e => setForm({ ...form, estado: e.target.value })}>
                                            <option>Activo</option>
                                            <option>Inactivo</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div className="modal-footer">
                                <button className="btn btn-sm btn-outline-secondary" onClick={cerrar}>Cancelar</button>
                                <button className="btn btn-sm" style={{ background: "#1a1a2e", color: "#fff" }} onClick={guardar}>
                                    {modal.tipo === "nuevo" ? "Crear usuario" : "Guardar cambios"}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* ── MODAL VER DETALLE ── */}
            {modal?.tipo === "ver" && (() => {
                const u = modal.usuario;
                const [bg, fg] = AVATAR_COLORS[(u.id - 1) % AVATAR_COLORS.length];
                return (
                    <div className="modal show d-block" style={{ background: "rgba(0,0,0,.35)" }}>
                        <div className="modal-dialog modal-dialog-centered">
                            <div className="modal-content">
                                <div className="modal-header">
                                    <h5 className="modal-title">Detalle del usuario</h5>
                                    <button className="btn-close" onClick={cerrar}></button>
                                </div>
                                <div className="modal-body">
                                    <div className="d-flex align-items-center gap-3 mb-3 pb-3 border-bottom">
                                        <div style={{ width: 48, height: 48, borderRadius: "50%", background: bg, color: fg, display: "flex", alignItems: "center", justifyContent: "center", fontSize: 16, fontWeight: 600 }}>
                                            {iniciales(u.nombre)}
                                        </div>
                                        <div>
                                            <div className="fw-semibold">{u.nombre}</div>
                                            <div className="text-secondary small">{u.email}</div>
                                        </div>
                                        <span className={`badge ms-auto bg-${ROL_BADGE[u.rol]}-subtle text-${ROL_BADGE[u.rol]}`}>{u.rol}</span>
                                    </div>
                                    <div className="row g-2" style={{ fontSize: 13 }}>
                                        {[
                                            ["Estado", <span className={`badge ${u.estado === "Activo" ? "bg-success-subtle text-success" : "bg-danger-subtle text-danger"}`}>{u.estado}</span>],
                                            ["Teléfono", u.telefono || "—"],
                                            ["Fecha de creación", u.creado || "—"],
                                            ["Último acceso", u.ultimo || "—"],
                                        ].map(([l, v], i) => (
                                            <div className="col-6" key={i}>
                                                <div className="text-secondary" style={{ fontSize: 11 }}>{l}</div>
                                                <div>{v}</div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                                <div className="modal-footer">
                                    <button className="btn btn-sm btn-outline-secondary" onClick={cerrar}>Cerrar</button>
                                    <button className="btn btn-sm btn-outline-warning" onClick={() => abrirEditar(u)}>
                                        <i className="bi bi-pencil me-1"></i>Editar
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                );
            })()}

            {/* ── MODAL ELIMINAR ── */}
            {modal?.tipo === "eliminar" && (
                <div className="modal show d-block" style={{ background: "rgba(0,0,0,.35)" }}>
                    <div className="modal-dialog modal-dialog-centered modal-sm">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Eliminar usuario</h5>
                                <button className="btn-close" onClick={cerrar}></button>
                            </div>
                            <div className="modal-body text-center">
                                <i className="bi bi-exclamation-triangle text-danger" style={{ fontSize: 32 }}></i>
                                <p className="mt-2 mb-1">¿Eliminar a <strong>{modal.usuario.nombre}</strong>?</p>
                                <small className="text-secondary">Esta acción no se puede deshacer.</small>
                            </div>
                            <div className="modal-footer justify-content-center">
                                <button className="btn btn-sm btn-outline-secondary" onClick={cerrar}>Cancelar</button>
                                <button className="btn btn-sm btn-danger" onClick={eliminar}>Sí, eliminar</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Usuario;