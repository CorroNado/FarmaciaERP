import React, { useState, useEffect } from "react";
import "./compra.css";

// Servicios (pueden fallar, usar fallbacks)
import { getOrdenes, crearOrden, getProveedores } from "../services/compraService";

// Datos de respaldo (fallback)
const FALLBACK_SUPPLIERS = [
    { id: "1", name: "Laboratorios XYZ S.A." },
    { id: "2", name: "Droguería ABC S.A.C." },
    { id: "3", name: "Farma Corp S.A." },
];

const FALLBACK_ORDERS = [
    { id: "1", orderNumber: "OC-001", supplierName: "Laboratorios XYZ S.A.", date: "2026-05-03", status: "Pendiente",   totalItems: 15, totalAmount: 12500.0 },
    { id: "2", orderNumber: "OC-002", supplierName: "Droguería ABC S.A.C.",  date: "2026-05-02", status: "En Tránsito", totalItems: 8,  totalAmount: 8750.5  },
    { id: "3", orderNumber: "OC-003", supplierName: "Laboratorios XYZ S.A.", date: "2026-05-01", status: "Recibida",    totalItems: 12, totalAmount: 15200.0 },
];

const LOW_STOCK = [
    { name: "Paracetamol 500mg", current: 50,  min: 200 },
    { name: "Ibuprofeno 400mg",  current: 30,  min: 150 },
    { name: "Amoxicilina 500mg", current: 20,  min: 100 },
];

function getBadgeClass(status) {
    switch (status) {
        case "Recibida":    return "badge-estado badge-recibida";
        case "En Tránsito": return "badge-estado badge-transito";
        case "Aprobada":    return "badge-estado badge-aprobada";
        case "Cancelada":   return "badge-estado badge-cancelada";
        default:            return "badge-estado badge-pendiente";
    }
}

const EMPTY_ITEM = { product: "", quantity: "", unit: "UNIDAD", unitPrice: "" };

function Compra() {
    const [showForm, setShowForm] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
    const [orders, setOrders] = useState(FALLBACK_ORDERS);
    const [suppliers, setSuppliers] = useState(FALLBACK_SUPPLIERS);
    const [supplierId, setSupplierId] = useState("");
    const [items, setItems] = useState([]);
    const [currentItem, setCurrentItem] = useState(EMPTY_ITEM);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        async function cargarDatos() {
            try {
                const [ordenesData, proveedoresData] = await Promise.all([
                    getOrdenes(),
                    getProveedores(),
                ]);
                setOrders(ordenesData);
                setSuppliers(proveedoresData);
            } catch (err) {
                console.warn("Backend no disponible, usando datos locales:", err.message);
            }
        }
        cargarDatos();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === "supplierId") {
            setSupplierId(value);
        } else if (name === "searchTerm") {
            setSearchTerm(value);
        } else {
            setCurrentItem(prev => ({ ...prev, [name]: value }));
        }
    };

    const addItem = () => {
        const qty = parseFloat(currentItem.quantity);
        const price = parseFloat(currentItem.unitPrice);
        if (!currentItem.product.trim() || !qty || qty <= 0 || isNaN(price) || price < 0) return;
        setItems(prev => [...prev, { ...currentItem, quantity: qty, unitPrice: price }]);
        setCurrentItem(EMPTY_ITEM);
    };

    const removeItem = (index) => setItems(prev => prev.filter((_, i) => i !== index));

    const total = items.reduce((acc, i) => acc + i.quantity * i.unitPrice, 0);
    const filtered = orders.filter(o =>
        o.orderNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
        o.supplierName.toLowerCase().includes(searchTerm.toLowerCase())
    );
    const canSubmit = items.length > 0 && supplierId !== "";

    const handleSubmit = async (e) => {
        e.preventDefault();
        const supplier = suppliers.find(s => s.id === supplierId);
        if (!supplier || items.length === 0) return;

        setLoading(true);
        setError("");

        const payload = {
            supplierId,
            supplierName: supplier.name,
            items,
            totalAmount: total,
        };

        try {
            const nuevaOrden = await crearOrden(payload);
            setOrders(prev => [nuevaOrden, ...prev]);
        } catch (err) {
            console.error("Error al crear orden:", err.message);
            // Fallback local
            const ordenLocal = {
                id: String(orders.length + 1),
                orderNumber: `OC-${String(orders.length + 1).padStart(3, "0")}`,
                supplierName: supplier.name,
                date: new Date().toISOString().split("T")[0],
                status: "Pendiente",
                totalItems: items.length,
                totalAmount: total,
            };
            setOrders(prev => [ordenLocal, ...prev]);
        } finally {
            setSupplierId("");
            setItems([]);
            setCurrentItem(EMPTY_ITEM);
            setShowForm(false);
            setLoading(false);
        }
    };

    return (
        <div className="compra-container">
            <div className="compra-header">
                <div>
                    <h2 className="compra-title">Órdenes de Compra</h2>
                    <p className="compra-subtitle">Gestión de pedidos a proveedores</p>
                </div>
                <button className="btn-nueva-orden" onClick={() => setShowForm(!showForm)}>
                    + Nueva Orden
                </button>
            </div>

            {showForm && (
                <div className="compra-card">
                    <div className="compra-card-body">
                        <div className="form-header">
                            <h6>Crear Orden de Compra</h6>
                            <button className="btn-cerrar" onClick={() => setShowForm(false)}>✕</button>
                        </div>

                        <div className="alerta-stock">
                            <div className="alerta-stock-titulo">⚠ Productos bajo stock mínimo</div>
                            {LOW_STOCK.map(p => (
                                <div key={p.name} className="alerta-stock-fila">
                                    <p>{p.name}</p>
                                    <span>Stock: {p.current} / Mín: {p.min}</span>
                                </div>
                            ))}
                        </div>

                        <div style={{ marginBottom: "1rem" }}>
                            <label className="form-label-custom">Proveedor</label>
                            <select
                                className="form-control-custom"
                                name="supplierId"
                                value={supplierId}
                                onChange={handleChange}
                            >
                                <option value="">Seleccionar proveedor…</option>
                                {suppliers.map(s => (
                                    <option key={s.id} value={s.id}>{s.name}</option>
                                ))}
                            </select>
                        </div>

                        <hr className="separador" />
                        <p style={{ fontSize: "0.85rem", fontWeight: 600, marginBottom: "10px" }}>
                            Agregar Productos
                        </p>

                        <div className="item-row">
                            <div className="item-col">
                                <label>Producto</label>
                                <input
                                    type="text"
                                    name="product"
                                    className="form-control-custom"
                                    placeholder="Nombre del producto"
                                    value={currentItem.product}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="item-col">
                                <label>Cantidad</label>
                                <input
                                    type="number"
                                    name="quantity"
                                    className="form-control-custom"
                                    placeholder="0"
                                    min="1"
                                    value={currentItem.quantity}
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="item-col">
                                <label>Unidad</label>
                                <select
                                    name="unit"
                                    className="form-control-custom"
                                    value={currentItem.unit}
                                    onChange={handleChange}
                                >
                                    <option value="UNIDAD">Unidad</option>
                                    <option value="CAJA">Caja</option>
                                    <option value="FRASCO">Frasco</option>
                                </select>
                            </div>
                            <div className="item-col">
                                <label>Precio unit.</label>
                                <div className="input-prefijo">
                                    <span>S/</span>
                                    <input
                                        type="number"
                                        name="unitPrice"
                                        className="form-control-custom"
                                        placeholder="0.00"
                                        min="0"
                                        step="0.01"
                                        value={currentItem.unitPrice}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                            <div>
                                <button className="btn-agregar" onClick={addItem}>+ Agregar</button>
                            </div>
                        </div>

                        {items.length > 0 && (
                            <table className="tabla-items">
                                <thead>
                                <tr><th>Producto</th><th>Cant.</th><th>Unidad</th><th>Precio unit.</th><th>Subtotal</th><th></th></tr>
                                </thead>
                                <tbody>
                                {items.map((item, i) => (
                                    <tr key={i}>
                                        <td>{item.product}</td>
                                        <td>{item.quantity}</td>
                                        <td>{item.unit}</td>
                                        <td>S/ {item.unitPrice.toFixed(2)}</td>
                                        <td>S/ {(item.quantity * item.unitPrice).toFixed(2)}</td>
                                        <td><button className="btn-eliminar" onClick={() => removeItem(i)}>✕</button></td>
                                    </tr>
                                ))}
                                <tr>
                                    <td colSpan={4} className="total-label">Total a pagar:</td>
                                    <td colSpan={2} className="total-valor">S/ {total.toFixed(2)}</td>
                                </tr>
                                </tbody>
                            </table>
                        )}

                        <div className="form-acciones">
                            <button className="btn-cancelar" onClick={() => setShowForm(false)}>Cancelar</button>
                            <button
                                className="btn-generar"
                                onClick={handleSubmit}
                                disabled={!canSubmit || loading}
                            >
                                {loading ? "Guardando…" : "✓ Generar Orden"}
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <div className="compra-card">
                <div className="compra-card-body">
                    <div className="buscador-wrap">
                        <span className="buscador-icono">🔍</span>
                        <input
                            type="text"
                            name="searchTerm"
                            className="buscador-input"
                            placeholder="Buscar por número de orden o proveedor..."
                            value={searchTerm}
                            onChange={handleChange}
                        />
                    </div>

                    <div style={{ overflowX: "auto" }}>
                        <table className="tabla-ordenes">
                            <thead>
                            <tr><th>N° Orden</th><th>Proveedor</th><th>Fecha</th><th>Items</th><th>Monto</th><th>Estado</th><th>Acciones</th></tr>
                            </thead>
                            <tbody>
                            {filtered.length === 0 ? (
                                <tr><td colSpan={7} className="sin-resultados">No se encontraron órdenes.</td></tr>
                            ) : (
                                filtered.map(order => (
                                    <tr key={order.id}>
                                        <td className="orden-numero">{order.orderNumber}</td>
                                        <td>{order.supplierName}</td>
                                        <td className="orden-muted">{order.date}</td>
                                        <td className="orden-muted">{order.totalItems}</td>
                                        <td>S/. {order.totalAmount.toFixed(2)}</td>
                                        <td><span className={getBadgeClass(order.status)}>{order.status}</span></td>
                                        <td>
                                            <div className="acciones-grupo">
                                                <button className="btn-accion ver" title="Ver detalle">👁</button>
                                                <button className="btn-accion doc" title="Ver documento">📄</button>
                                            </div>
                                        </td>
                                    </tr>
                                ))
                            )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Compra;