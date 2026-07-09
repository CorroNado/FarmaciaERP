import { useState } from 'react';
import { Search, RotateCcw, CheckSquare, Square } from 'lucide-react';

const CHECKLIST = [
  { id: 1, label: 'Empaque original completo',   sub: 'Caja, manuales y accesorios incluidos' },
  { id: 2, label: 'Producto sin uso aparente',    sub: 'Sin marcas, ralladuras o desgaste'     },
  { id: 3, label: 'Etiquetas y sellos intactos',  sub: 'Código de barras y sellos de garantía'  },
  { id: 4, label: 'Prueba funcional exitosa',     sub: 'Encendido y verificación de operatividad'},
];

const DOCUMENTO_DEMO = {
  id: 'F001-00012458',
  fecha: '26/05/2026 10:35',
  cliente: 'Distribuidora Farmacéutica Los Andes S.A.C.',
  ruc: '20123456789',
  productos: [
    { nombre: 'Paracetamol 500mg x 100 tabletas', sku: 'SAP-001234', cantidad: 50, monto: 2275.00 },
    { nombre: 'Amoxicilina 500mg x 12 cápsulas',  sku: 'SAP-005678', cantidad: 30, monto: 867.00  },
  ],
  total: 3142.00,
};

export default function ReverseOpsPage() {
  const [busqueda,   setBusqueda]   = useState('');
  const [documento,  setDocumento]  = useState(null);
  const [checklist,  setChecklist]  = useState({});
  const [observacion, setObservacion] = useState('');

  function buscarDocumento() {
    if (busqueda) setDocumento(DOCUMENTO_DEMO);
  }

  function toggleCheck(id) {
    setChecklist((prev) => ({ ...prev, [id]: !prev[id] }));
  }

  return (
    <div>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-gray-900">Reverse Ops & Returns - Módulo de Postventa y Garantías</h2>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">

        {/* Vinculador */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center gap-2 font-semibold text-gray-700 mb-4">
            <Search size={16} className="text-blue-600" />
            Vinculador de Documentos
          </div>
          <p className="text-xs text-gray-500 mb-2">Buscar Transacción Original</p>
          <div className="flex gap-2 mb-4">
            <input
              value={busqueda}
              onChange={(e) => setBusqueda(e.target.value)}
              placeholder="Número de Factura/Boleta o Ticket"
              className="flex-1 border border-gray-200 rounded-xl px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition"
            />
            <button onClick={buscarDocumento} className="bg-blue-600 text-white px-4 py-2.5 rounded-xl hover:bg-blue-700 transition">
              <Search size={16} />
            </button>
          </div>

          {documento && (
            <div className="border-2 border-blue-500 rounded-xl p-4">
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs font-bold bg-green-600 text-white px-2 py-0.5 rounded font-mono">{documento.id}</span>
                <span className="text-xs text-gray-400">{documento.fecha}</span>
              </div>
              <p className="text-sm font-semibold text-gray-700">{documento.cliente}</p>
              <p className="text-xs text-gray-400 mb-3">RUC: {documento.ruc}</p>
              <p className="text-xs font-semibold text-gray-600 mb-2">Productos Originales</p>
              <div className="flex flex-col gap-2 mb-3">
                {documento.productos.map((p) => (
                  <div key={p.sku} className="flex justify-between text-xs">
                    <div>
                      <p className="font-medium text-gray-700">{p.nombre}</p>
                      <p className="text-gray-400">SKU: {p.sku}</p>
                    </div>
                    <div className="text-right">
                      <p className="font-semibold text-gray-700">{p.cantidad} und</p>
                      <p className="text-gray-400">S/ {p.monto.toLocaleString('es-PE', { minimumFractionDigits: 2 })}</p>
                    </div>
                  </div>
                ))}
              </div>
              <div className="border-t border-gray-100 pt-2 flex justify-between text-sm font-bold">
                <span>Total Original:</span>
                <span className="text-blue-600">S/ {documento.total.toLocaleString('es-PE', { minimumFractionDigits: 2 })}</span>
              </div>
            </div>
          )}
        </div>

        {/* Control de calidad */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center gap-2 font-semibold text-gray-700 mb-4">
            <RotateCcw size={16} className="text-violet-600" />
            Control de Calidad - Productos Retornados
          </div>

          <div className="mb-4">
            <label className="text-xs font-semibold text-gray-600 uppercase tracking-wide block mb-1">Producto a Inspeccionar</label>
            <select className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition">
              <option>Paracetamol 500mg x 100 tabletas - Cant: 10</option>
              <option>Amoxicilina 500mg x 12 cápsulas - Cant: 5</option>
            </select>
          </div>

          <div className="bg-gray-50 rounded-xl p-4 mb-4">
            <p className="text-sm font-semibold text-gray-700 mb-3">Checklist de Inspección</p>
            <div className="flex flex-col gap-3">
              {CHECKLIST.map((item) => (
                <button key={item.id} onClick={() => toggleCheck(item.id)} className="flex items-start gap-3 text-left">
                  {checklist[item.id]
                    ? <CheckSquare size={16} className="text-blue-600 shrink-0 mt-0.5" />
                    : <Square size={16} className="text-gray-400 shrink-0 mt-0.5" />
                  }
                  <div>
                    <p className="text-sm font-medium text-gray-700">{item.label}</p>
                    <p className="text-xs text-gray-400">{item.sub}</p>
                  </div>
                </button>
              ))}
            </div>
          </div>

          <div className="mb-4">
            <p className="text-sm font-semibold text-gray-700 mb-2">Decisión de Calidad</p>
            <div className="grid grid-cols-2 gap-3">
              <button className="bg-green-500 hover:bg-green-600 text-white py-2.5 rounded-xl text-sm font-semibold transition flex items-center justify-center gap-2">
                <CheckSquare size={14} /> Apto Reventa
              </button>
              <button className="bg-red-500 hover:bg-red-600 text-white py-2.5 rounded-xl text-sm font-semibold transition flex items-center justify-center gap-2">
                ✕ Merma/Daño Total
              </button>
            </div>
          </div>

          <div>
            <label className="text-xs font-semibold text-gray-600 uppercase tracking-wide block mb-1">Observaciones del Inspector</label>
            <textarea
              value={observacion}
              onChange={(e) => setObservacion(e.target.value)}
              placeholder="Detalles adicionales sobre el estado del producto..."
              rows={3}
              className="w-full border border-gray-200 rounded-xl px-4 py-3 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition resize-none"
            />
          </div>
        </div>
      </div>
    </div>
  );
}