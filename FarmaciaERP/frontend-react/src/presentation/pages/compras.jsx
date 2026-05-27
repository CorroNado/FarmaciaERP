import React, { useState } from "react";

// ========== DATOS DE EJEMPLO ==========
// Proveedores
const proveedoresIniciales = [
    { id: 1, ruc: "20123456789", nombre: "Laboratorios XYZ S.A.", vigencia: "2026-12-31" },
    { id: 2, ruc: "20555666777", nombre: "Droguería ABC S.A.C.", vigencia: "2025-08-15" },
    { id: 3, ruc: "20333444888", nombre: "Farma Corp S.A.", vigencia: "2027-03-20" },
];

// Inventario (para control de stock mínimo)
const inventarioInicial = [
    { id: 1, nombre: "Paracetamol 500mg", stock: 50, stockMinimo: 200 },
    { id: 2, nombre: "Ibuprofeno 400mg", stock: 30, stockMinimo: 150 },
    { id: 3, nombre: "Amoxicilina 500mg", stock: 20, stockMinimo: 100 },
    { id: 4, nombre: "Omeprazol 20mg", stock: 180, stockMinimo: 150 },
];

// Órdenes de compra
const ordenesIniciales = [
    {
        id: 1,
        numero: "OC-001",
        proveedorId: 1,
        proveedorNombre: "Laboratorios XYZ S.A.",
        fecha: "2026-05-08",
        estado: "En tránsito",
        productos: [
            { nombre: "Paracetamol 500mg", cantidad: 200, precioUnitario: 8.5, subtotal: 1700 },
        ],
        total: 1700,
    },
    {
        id: 2,
        numero: "OC-002",
        proveedorId: 2,
        proveedorNombre: "Droguería ABC S.A.C.",
        fecha: "2026-05-07",
        estado: "Recibida",
        productos: [
            { nombre: "Ibuprofeno 400mg", cantidad: 150, precioUnitario: 10, subtotal: 1500 },
        ],
        total: 1500,
    },
    {
        id: 3,
        numero: "OC-003",
        proveedorId: 4,
        proveedorNombre: "Farma Corp S.A.",
        fecha: "2026-05-10",
        estado: "Pendiente",
        productos: [
            { nombre: "Omeprazol 20mg", cantidad: 50, precioUnitario: 3.8, subtotal: 190 },
        ],
        total: 190,
    },
];

// Recepciones
const recepcionesIniciales = [];

// ========== COMPONENTE PRINCIPAL ==========
function Compras() {
    // ---- Estados generales ----
    const [pestania, setPestania] = useState("ordenes"); // ordenes, proveedores, recepcion
    const [proveedores, setProveedores] = useState(proveedoresIniciales);
    const [inventario, setInventario] = useState(inventarioInicial);
    const [ordenes, setOrdenes] = useState(ordenesIniciales);
    const [recepciones, setRecepciones] = useState(recepcionesIniciales);

    // ---- Estados para formularios ----
    // Nuevo proveedor
    const [nuevoProveedor, setNuevoProveedor] = useState({ ruc: "", nombre: "", vigencia: "" });
    // Nueva orden
    const [mostrarFormOrden, setMostrarFormOrden] = useState(false);
    const [ordenSeleccionadaId, setOrdenSeleccionadaId] = useState("");
    const [productosOrden, setProductosOrden] = useState([]);
    const [productoSugerido, setProductoSugerido] = useState(null); // producto bajo stock
    // Recepción
    const [ordenParaRecibir, setOrdenParaRecibir] = useState(null);
    const [guiaRemision, setGuiaRemision] = useState("");
    const [itemsRecepcion, setItemsRecepcion] = useState([]);

    // ========== FUNCIONES ==========

    // ---- Proveedores ----
    const agregarProveedor = () => {
        if (!nuevoProveedor.ruc || !nuevoProveedor.nombre || !nuevoProveedor.vigencia) {
            alert("Completa todos los campos del proveedor");
            return;
        }
        const nuevoId = proveedores.length + 1;
        setProveedores([...proveedores, { id: nuevoId, ...nuevoProveedor }]);
        setNuevoProveedor({ ruc: "", nombre: "", vigencia: "" });
    };

    const actualizarVigencia = (id, nuevaVigencia) => {
        setProveedores(proveedores.map(p => (p.id === id ? { ...p, vigencia: nuevaVigencia } : p)));
    };

    // ---- Órdenes de compra ----
    const productosBajoStock = () => {
        return inventario.filter(p => p.stock < p.stockMinimo);
    };

    const aplicarSugerencia = () => {
        const bajos = productosBajoStock();
        if (bajos.length === 0) {
            alert("No hay productos con stock bajo.");
            return;
        }
        // Tomamos el primer producto bajo stock como sugerencia
        const sugerido = bajos[0];
        const cantidadSugerida = sugerido.stockMinimo - sugerido.stock;
        setProductoSugerido({
            nombre: sugerido.nombre,
            cantidad: cantidadSugerida,
            precio: 5.0, // precio simulado
        });
    };

    const agregarProducto = () => {
        if (!productoSugerido) {
            // Si no hay sugerencia, permitir agregar manual simple
            const nombre = prompt("Nombre del producto");
            if (!nombre) return;
            const cantidad = parseFloat(prompt("Cantidad"));
            if (isNaN(cantidad) || cantidad <= 0) return;
            const precio = parseFloat(prompt("Precio unitario"));
            if (isNaN(precio)) return;
            setProductosOrden([...productosOrden, { nombre, cantidad, precioUnitario: precio, subtotal: cantidad * precio }]);
        } else {
            // Agregar el producto sugerido
            const nuevo = {
                nombre: productoSugerido.nombre,
                cantidad: productoSugerido.cantidad,
                precioUnitario: productoSugerido.precio,
                subtotal: productoSugerido.cantidad * productoSugerido.precio,
            };
            setProductosOrden([...productosOrden, nuevo]);
            setProductoSugerido(null);
        }
    };

    const eliminarProductoOrden = (index) => {
        const nuevaLista = [...productosOrden];
        nuevaLista.splice(index, 1);
        setProductosOrden(nuevaLista);
    };

    const calcularTotalOrden = () => {
        return productosOrden.reduce((sum, p) => sum + p.subtotal, 0);
    };

    const crearOrden = () => {
        if (!ordenSeleccionadaId) {
            alert("Selecciona un proveedor");
            return;
        }
        if (productosOrden.length === 0) {
            alert("Agrega al menos un producto");
            return;
        }
        const proveedor = proveedores.find(p => p.id === parseInt(ordenSeleccionadaId));
        const nuevaOrden = {
            id: ordenes.length + 1,
            numero: `OC-${String(ordenes.length + 1).padStart(3, "0")}`,
            proveedorId: proveedor.id,
            proveedorNombre: proveedor.nombre,
            fecha: new Date().toISOString().split("T")[0],
            estado: "Pendiente",
            productos: productosOrden,
            total: calcularTotalOrden(),
        };
        setOrdenes([nuevaOrden, ...ordenes]);
        // Limpiar formulario
        setOrdenSeleccionadaId("");
        setProductosOrden([]);
        setMostrarFormOrden(false);
    };

    // ---- Recepción de mercadería ----
    const ordenesPendientes = ordenes.filter(o => o.estado !== "Recibida" && o.estado !== "Cancelada");

    const iniciarRecepcion = (orden) => {
        setOrdenParaRecibir(orden);
        setGuiaRemision("");
        // Preparar items de recepción con lotes vacíos
        const items = orden.productos.map(p => ({
            nombre: p.nombre,
            cantidadEsperada: p.cantidad,
            cantidadRecibida: p.cantidad,
            lote: "",
            vencimiento: "",
        }));
        setItemsRecepcion(items);
    };

    const actualizarItemRecepcion = (index, campo, valor) => {
        const nuevos = [...itemsRecepcion];
        nuevos[index][campo] = valor;
        setItemsRecepcion(nuevos);
    };

    const confirmarRecepcion = () => {
        if (!guiaRemision.trim()) {
            alert("Ingresa el número de Guía de Remisión");
            return;
        }
        for (let item of itemsRecepcion) {
            if (!item.lote || !item.vencimiento) {
                alert(`Falta lote o fecha de vencimiento para ${item.nombre}`);
                return;
            }
        }
        // Crear registro de recepción
        const nuevaRecepcion = {
            id: recepciones.length + 1,
            ordenId: ordenParaRecibir.id,
            ordenNumero: ordenParaRecibir.numero,
            guia: guiaRemision,
            fecha: new Date().toISOString().split("T")[0],
            items: itemsRecepcion.map(i => ({
                producto: i.nombre,
                cantidadRecibida: i.cantidadRecibida,
                lote: i.lote,
                vencimiento: i.vencimiento,
            })),
        };
        setRecepciones([nuevaRecepcion, ...recepciones]);

        // Cambiar estado de la orden a "Recibida"
        setOrdenes(ordenes.map(o => (o.id === ordenParaRecibir.id ? { ...o, estado: "Recibida" } : o)));

        // Actualizar inventario (sumar stock)
        const nuevoInventario = [...inventario];
        itemsRecepcion.forEach(item => {
            const idx = nuevoInventario.findIndex(i => i.nombre === item.nombre);
            if (idx !== -1) {
                nuevoInventario[idx].stock += item.cantidadRecibida;
            } else {
                nuevoInventario.push({
                    id: nuevoInventario.length + 1,
                    nombre: item.nombre,
                    stock: item.cantidadRecibida,
                    stockMinimo: 10,
                });
            }
        });
        setInventario(nuevoInventario);

        // Limpiar y cerrar
        setOrdenParaRecibir(null);
        setGuiaRemision("");
        setItemsRecepcion([]);
        alert("Recepción registrada correctamente");
    };

    // ========== RENDER ==========
    return (
        <div className="bg-white min-vh-100 p-4">
            <div className="container">
                <h2 className="mb-4 text-primary">📦 Módulo de Compras y Abastecimiento</h2>

                {/* Pestañas */}
                <ul className="nav nav-tabs mb-4">
                    <li className="nav-item">
                        <button className={`nav-link ${pestania === "ordenes" ? "active" : ""}`} onClick={() => setPestania("ordenes")}>
                            🛒 Órdenes de Compra
                        </button>
                    </li>
                    <li className="nav-item">
                        <button className={`nav-link ${pestania === "proveedores" ? "active" : ""}`} onClick={() => setPestania("proveedores")}>
                            🏢 Proveedores
                        </button>
                    </li>
                    <li className="nav-item">
                        <button className={`nav-link ${pestania === "recepcion" ? "active" : ""}`} onClick={() => setPestania("recepcion")}>
                            📥 Recepción
                        </button>
                    </li>
                </ul>

                {/* ================= PANEL ÓRDENES ================= */}
                {pestania === "ordenes" && (
                    <div>
                        <div className="d-flex justify-content-between mb-3">
                            <h4>Listado de Órdenes</h4>
                            <button className="btn btn-primary" onClick={() => setMostrarFormOrden(!mostrarFormOrden)}>
                                + Nueva Orden
                            </button>
                        </div>

                        {mostrarFormOrden && (
                            <div className="card mb-4 shadow-sm">
                                <div className="card-body">
                                    <h5 className="card-title">Crear Orden de Compra</h5>
                                    <hr />

                                    {/* Sugerencia de stock mínimo */}
                                    <div className="alert alert-info">
                                        <strong>⚠️ Productos con stock bajo:</strong>
                                        {productosBajoStock().map(p => (
                                            <div key={p.id}>
                                                {p.nombre}: stock {p.stock} (mínimo {p.stockMinimo})
                                            </div>
                                        ))}
                                        <button className="btn btn-sm btn-outline-primary mt-2" onClick={aplicarSugerencia}>
                                            Sugerir producto
                                        </button>
                                    </div>

                                    {/* Proveedor */}
                                    <div className="mb-3">
                                        <label className="form-label">Proveedor</label>
                                        <select className="form-select" value={ordenSeleccionadaId} onChange={e => setOrdenSeleccionadaId(e.target.value)}>
                                            <option value="">Seleccionar...</option>
                                            {proveedores.map(p => (
                                                <option key={p.id} value={p.id}>
                                                    {p.nombre} (RUC: {p.ruc})
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    {/* Productos de la orden */}
                                    <p className="fw-bold">Productos</p>
                                    <button className="btn btn-sm btn-secondary mb-2" onClick={agregarProducto}>
                                        + Agregar producto
                                    </button>
                                    {productosOrden.length > 0 && (
                                        <div className="table-responsive">
                                            <table className="table table-sm table-bordered">
                                                <thead className="table-light">
                                                <tr><th>Producto</th><th>Cantidad</th><th>Precio</th><th>Subtotal</th><th></th></tr>
                                                </thead>
                                                <tbody>
                                                {productosOrden.map((p, idx) => (
                                                    <tr key={idx}>
                                                        <td>{p.nombre}</td>
                                                        <td>{p.cantidad}</td>
                                                        <td>S/ {p.precioUnitario.toFixed(2)}</td>
                                                        <td>S/ {p.subtotal.toFixed(2)}</td>
                                                        <td><button className="btn btn-sm btn-danger" onClick={() => eliminarProductoOrden(idx)}>✕</button></td>
                                                    </tr>
                                                ))}
                                                <tr className="table-active">
                                                    <td colSpan="3" className="text-end fw-bold">Total:</td>
                                                    <td colSpan="2" className="fw-bold text-primary">S/ {calcularTotalOrden().toFixed(2)}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    )}

                                    <div className="d-flex justify-content-end gap-2 mt-3">
                                        <button className="btn btn-secondary" onClick={() => setMostrarFormOrden(false)}>Cancelar</button>
                                        <button className="btn btn-primary" onClick={crearOrden}>Generar Orden</button>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Tabla de órdenes existentes */}
                        <div className="card shadow-sm">
                            <div className="card-body">
                                <div className="table-responsive">
                                    <table className="table table-hover">
                                        <thead className="table-light">
                                        <tr><th>N° Orden</th><th>Proveedor</th><th>Fecha</th><th>Total</th><th>Estado</th></tr>
                                        </thead>
                                        <tbody>
                                        {ordenes.map(orden => (
                                            <tr key={orden.id}>
                                                <td className="fw-bold">{orden.numero}</td>
                                                <td>{orden.proveedorNombre}</td>
                                                <td>{orden.fecha}</td>
                                                <td>S/ {orden.total.toFixed(2)}</td>
                                                <td>
                            <span className={`badge ${orden.estado === "Recibida" ? "bg-success" : "bg-warning"}`}>
                              {orden.estado}
                            </span>
                                                </td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {/* ================= PANEL PROVEEDORES ================= */}
                {pestania === "proveedores" && (
                    <div>
                        <div className="card shadow-sm mb-4">
                            <div className="card-body">
                                <h5>Registrar nuevo proveedor</h5>
                                <div className="row g-2 align-items-end">
                                    <div className="col-md-3">
                                        <label className="form-label">RUC</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            value={nuevoProveedor.ruc}
                                            onChange={e => setNuevoProveedor({ ...nuevoProveedor, ruc: e.target.value })}
                                        />
                                    </div>
                                    <div className="col-md-4">
                                        <label className="form-label">Razón social</label>
                                        <input
                                            type="text"
                                            className="form-control"
                                            value={nuevoProveedor.nombre}
                                            onChange={e => setNuevoProveedor({ ...nuevoProveedor, nombre: e.target.value })}
                                        />
                                    </div>
                                    <div className="col-md-3">
                                        <label className="form-label">Vigencia autorización</label>
                                        <input
                                            type="date"
                                            className="form-control"
                                            value={nuevoProveedor.vigencia}
                                            onChange={e => setNuevoProveedor({ ...nuevoProveedor, vigencia: e.target.value })}
                                        />
                                    </div>
                                    <div className="col-md-2">
                                        <button className="btn btn-primary w-100" onClick={agregarProveedor}>
                                            Registrar
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="card shadow-sm">
                            <div className="card-body">
                                <h5>Lista de proveedores</h5>
                                <div className="table-responsive">
                                    <table className="table table-bordered">
                                        <thead className="table-light">
                                        <tr><th>RUC</th><th>Razón social</th><th>Vigencia</th><th>Estado</th><th></th></tr>
                                        </thead>
                                        <tbody>
                                        {proveedores.map(p => {
                                            const vigente = new Date(p.vigencia) > new Date();
                                            return (
                                                <tr key={p.id}>
                                                    <td>{p.ruc}</td>
                                                    <td>{p.nombre}</td>
                                                    <td>
                                                        <input
                                                            type="date"
                                                            className="form-control form-control-sm"
                                                            value={p.vigencia}
                                                            onChange={e => actualizarVigencia(p.id, e.target.value)}
                                                        />
                                                    </td>
                                                    <td>
                                                        {vigente ? (
                                                            <span className="badge bg-success">Vigente</span>
                                                        ) : (
                                                            <span className="badge bg-danger">Vencida</span>
                                                        )}
                                                    </td>
                                                    <td>
                                                        <button
                                                            className="btn btn-sm btn-outline-secondary"
                                                            onClick={() => {
                                                                const nueva = prompt("Nueva fecha (YYYY-MM-DD)", p.vigencia);
                                                                if (nueva) actualizarVigencia(p.id, nueva);
                                                            }}
                                                        >
                                                            ✏️
                                                        </button>
                                                    </td>
                                                </tr>
                                            );
                                        })}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {/* ================= PANEL RECEPCIÓN ================= */}
                {pestania === "recepcion" && (
                    <div>
                        <div className="row">
                            <div className="col-md-5">
                                <div className="card shadow-sm">
                                    <div className="card-body">
                                        <h5>Órdenes pendientes</h5>
                                        {ordenesPendientes.length === 0 ? (
                                            <p className="text-muted">No hay órdenes pendientes.</p>
                                        ) : (
                                            <div className="list-group">
                                                {ordenesPendientes.map(orden => (
                                                    <div key={orden.id} className="list-group-item d-flex justify-content-between align-items-center">
                                                        <div>
                                                            <strong>{orden.numero}</strong> - {orden.proveedorNombre}
                                                        </div>
                                                        <button className="btn btn-sm btn-primary" onClick={() => iniciarRecepcion(orden)}>
                                                            Recibir
                                                        </button>
                                                    </div>
                                                ))}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>

                            <div className="col-md-7">
                                {ordenParaRecibir && (
                                    <div className="card shadow-sm">
                                        <div className="card-body">
                                            <h5>Recepcionar: {ordenParaRecibir.numero}</h5>
                                            <div className="mb-3">
                                                <label className="form-label">Guía de Remisión</label>
                                                <input type="text" className="form-control" value={guiaRemision} onChange={e => setGuiaRemision(e.target.value)} />
                                            </div>
                                            <div className="table-responsive">
                                                <table className="table table-sm table-bordered">
                                                    <thead className="table-light">
                                                    <tr><th>Producto</th><th>Cant. pedida</th><th>Cant. recibida</th><th>Lote</th><th>Vencimiento</th></tr>
                                                    </thead>
                                                    <tbody>
                                                    {itemsRecepcion.map((item, idx) => (
                                                        <tr key={idx}>
                                                            <td>{item.nombre}</td>
                                                            <td>{item.cantidadEsperada}</td>
                                                            <td>
                                                                <input
                                                                    type="number"
                                                                    className="form-control form-control-sm"
                                                                    value={item.cantidadRecibida}
                                                                    onChange={e =>
                                                                        actualizarItemRecepcion(idx, "cantidadRecibida", parseInt(e.target.value) || 0)
                                                                    }
                                                                />
                                                            </td>
                                                            <td>
                                                                <input
                                                                    type="text"
                                                                    className="form-control form-control-sm"
                                                                    placeholder="Lote"
                                                                    value={item.lote}
                                                                    onChange={e => actualizarItemRecepcion(idx, "lote", e.target.value)}
                                                                />
                                                            </td>
                                                            <td>
                                                                <input
                                                                    type="date"
                                                                    className="form-control form-control-sm"
                                                                    value={item.vencimiento}
                                                                    onChange={e => actualizarItemRecepcion(idx, "vencimiento", e.target.value)}
                                                                />
                                                            </td>
                                                        </tr>
                                                    ))}
                                                    </tbody>
                                                </table>
                                            </div>
                                            <div className="d-flex justify-content-end gap-2">
                                                <button className="btn btn-secondary" onClick={() => setOrdenParaRecibir(null)}>Cancelar</button>
                                                <button className="btn btn-success" onClick={confirmarRecepcion}>Confirmar Recepción</button>
                                            </div>
                                        </div>
                                    </div>
                                )}
                                {!ordenParaRecibir && (
                                    <div className="card shadow-sm bg-light">
                                        <div className="card-body text-muted text-center">
                                            Selecciona una orden pendiente para iniciar la recepción.
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* Historial de recepciones */}
                        {recepciones.length > 0 && (
                            <div className="card shadow-sm mt-4">
                                <div className="card-body">
                                    <h5>Historial de recepciones</h5>
                                    <div className="table-responsive">
                                        <table className="table table-sm">
                                            <thead>
                                            <tr><th>Guía</th><th>Orden</th><th>Fecha</th><th>Productos (Lote / Vencimiento)</th></tr>
                                            </thead>
                                            <tbody>
                                            {recepciones.map(rec => (
                                                <tr key={rec.id}>
                                                    <td>{rec.guia}</td>
                                                    <td>{rec.ordenNumero}</td>
                                                    <td>{rec.fecha}</td>
                                                    <td>
                                                        {rec.items.map((it, i) => (
                                                            <div key={i}>
                                                                {it.producto}: Lote {it.lote}, Vence {it.vencimiento}
                                                            </div>
                                                        ))}
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}

export default Compras;