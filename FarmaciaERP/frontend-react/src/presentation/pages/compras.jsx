import React, { useState } from "react";

// Datos de ejemplo (estáticos)
const SUPPLIERS_MOCK = [
    { id: "1", name: "Laboratorios XYZ S.A." },
    { id: "2", name: "Droguería ABC S.A.C." },
    { id: "3", name: "Farma Corp S.A." },
];

const ORDERS_MOCK = [
    { id: "1", orderNumber: "OC-001", supplierName: "Laboratorios XYZ S.A.", date: "2026-05-03", status: "Pendiente", totalItems: 15, totalAmount: 12500.0 },
    { id: "2", orderNumber: "OC-002", supplierName: "Droguería ABC S.A.C.", date: "2026-05-02", status: "En Tránsito", totalItems: 8, totalAmount: 8750.5 },
    { id: "3", orderNumber: "OC-003", supplierName: "Laboratorios XYZ S.A.", date: "2026-05-01", status: "Recibida", totalItems: 12, totalAmount: 15200.0 },
];

const LOW_STOCK_MOCK = [
    { name: "Paracetamol 500mg", current: 50, min: 200 },
    { name: "Ibuprofeno 400mg", current: 30, min: 150 },
    { name: "Amoxicilina 500mg", current: 20, min: 100 },
];

const getBadgeClass = (status) => {
    switch (status) {
        case "Recibida": return "bg-success";
        case "En Tránsito": return "bg-info";
        case "Aprobada": return "bg-primary";
        case "Cancelada": return "bg-danger";
        default: return "bg-secondary";
    }
};

const EMPTY_ITEM = { product: "", quantity: "", unit: "UNIDAD", unitPrice: "" };

function Compras() {
    const [showForm, setShowForm] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
    const [orders, setOrders] = useState(ORDERS_MOCK);
    const [suppliers] = useState(SUPPLIERS_MOCK);
    const [supplierId, setSupplierId] = useState("");
    const [items, setItems] = useState([]);
    const [currentItem, setCurrentItem] = useState(EMPTY_ITEM);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === "supplierId") setSupplierId(value);
        else if (name === "searchTerm") setSearchTerm(value);
        else setCurrentItem(prev => ({ ...prev, [name]: value }));
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

    const handleSubmit = () => {
        if (!canSubmit) return;
        setLoading(true);
        // Simular guardado
        const supplier = suppliers.find(s => s.id === supplierId);
        const newOrder = {
            id: String(orders.length + 1),
            orderNumber: `OC-${String(orders.length + 1).padStart(3, "0")}`,
            supplierName: supplier.name,
            date: new Date().toISOString().split("T")[0],
            status: "Pendiente",
            totalItems: items.length,
            totalAmount: total,
        };
        setTimeout(() => {
            setOrders([newOrder, ...orders]);
            setSupplierId("");
            setItems([]);
            setCurrentItem(EMPTY_ITEM);
            setShowForm(false);
            setLoading(false);
        }, 500);
    };

    return (
        <div className="bg-white min-vh-100 p-4">
            <div className="container-fluid">
                {/* Header */}
                <div className="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h2 className="mb-0">Órdenes de Compra</h2>
                        <p className="text-muted">Gestión de pedidos a proveedores</p>
                    </div>
                    <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
                        + Nueva Orden
                    </button>
                </div>

                {/* Formulario de nueva orden */}
                {showForm && (
                    <div className="card mb-4 shadow-sm">
                        <div className="card-body">
                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <h5 className="card-title mb-0">Crear Orden de Compra</h5>
                                <button className="btn-close" onClick={() => setShowForm(false)}></button>
                            </div>

                            {/* Alerta de stock bajo */}
                            <div className="alert alert-warning">
                                <strong>⚠ Productos bajo stock mínimo</strong>
                                {LOW_STOCK_MOCK.map(p => (
                                    <div key={p.name} className="d-flex justify-content-between small">
                                        <span>{p.name}</span>
                                        <span>Stock: {p.current} / Mín: {p.min}</span>
                                    </div>
                                ))}
                            </div>

                            {/* Proveedor */}
                            <div className="mb-3">
                                <label className="form-label fw-semibold">Proveedor</label>
                                <select className="form-select" name="supplierId" value={supplierId} onChange={handleChange}>
                                    <option value="">Seleccionar proveedor…</option>
                                    {suppliers.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                                </select>
                            </div>

                            <hr />
                            <p className="fw-semibold">Agregar Productos</p>

                            {/* Fila para agregar items */}
                            <div className="row g-2 mb-3">
                                <div className="col-md-3">
                                    <input type="text" name="product" className="form-control" placeholder="Producto" value={currentItem.product} onChange={handleChange} />
                                </div>
                                <div className="col-md-2">
                                    <input type="number" name="quantity" className="form-control" placeholder="Cant." min="1" value={currentItem.quantity} onChange={handleChange} />
                                </div>
                                <div className="col-md-2">
                                    <select name="unit" className="form-select" value={currentItem.unit} onChange={handleChange}>
                                        <option value="UNIDAD">Unidad</option>
                                        <option value="CAJA">Caja</option>
                                        <option value="FRASCO">Frasco</option>
                                    </select>
                                </div>
                                <div className="col-md-3">
                                    <div className="input-group">
                                        <span className="input-group-text">S/</span>
                                        <input type="number" name="unitPrice" className="form-control" placeholder="Precio unit." min="0" step="0.01" value={currentItem.unitPrice} onChange={handleChange} />
                                    </div>
                                </div>
                                <div className="col-md-2">
                                    <button className="btn btn-success w-100" onClick={addItem}>+ Agregar</button>
                                </div>
                            </div>

                            {/* Tabla de items agregados */}
                            {items.length > 0 && (
                                <div className="table-responsive mb-3">
                                    <table className="table table-sm table-bordered">
                                        <thead className="table-light">
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
                                                <td><button className="btn btn-sm btn-danger" onClick={() => removeItem(i)}>✕</button></td>
                                            </tr>
                                        ))}
                                        <tr className="table-active">
                                            <td colSpan="4" className="text-end fw-bold">Total a pagar:</td>
                                            <td colSpan="2" className="fw-bold text-primary">S/ {total.toFixed(2)}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            )}

                            <div className="d-flex justify-content-end gap-2">
                                <button className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancelar</button>
                                <button className="btn btn-primary" onClick={handleSubmit} disabled={!canSubmit || loading}>
                                    {loading ? "Guardando…" : "✓ Generar Orden"}
                                </button>
                            </div>
                        </div>
                    </div>
                )}

                {/* Tabla de órdenes existentes */}
                <div className="card shadow-sm">
                    <div className="card-body">
                        <div className="mb-3">
                            <div className="input-group">
                                <span className="input-group-text">🔍</span>
                                <input type="text" name="searchTerm" className="form-control" placeholder="Buscar por número de orden o proveedor..." value={searchTerm} onChange={handleChange} />
                            </div>
                        </div>

                        <div className="table-responsive">
                            <table className="table table-hover align-middle">
                                <thead className="table-light">
                                <tr>
                                    <th>N° Orden</th><th>Proveedor</th><th>Fecha</th><th>Items</th><th>Monto</th><th>Estado</th><th>Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                {filtered.length === 0 ? (
                                    <tr><td colSpan="7" className="text-center text-muted">No se encontraron órdenes.</td></tr>
                                ) : (
                                    filtered.map(order => (
                                        <tr key={order.id}>
                                            <td className="fw-bold">{order.orderNumber}</td>
                                            <td>{order.supplierName}</td>
                                            <td>{order.date}</td>
                                            <td>{order.totalItems}</td>
                                            <td>S/. {order.totalAmount.toFixed(2)}</td>
                                            <td><span className={`badge ${getBadgeClass(order.status)}`}>{order.status}</span></td>
                                            <td>
                                                <button className="btn btn-sm btn-outline-secondary me-1" title="Ver detalle">👁</button>
                                                <button className="btn btn-sm btn-outline-secondary" title="Ver documento">📄</button>
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
        </div>
    );
}

export default Compras;