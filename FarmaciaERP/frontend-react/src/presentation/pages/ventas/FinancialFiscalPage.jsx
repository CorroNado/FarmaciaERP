import { useState } from 'react';
import { DollarSign, FileText, CheckCircle, Clock, XCircle, AlertTriangle } from 'lucide-react';

const DOCUMENTOS = [
  { id: 'F001-00012458', tipo: 'F', cliente: 'Distribuidora Farmacéutica Los Andes S.A.C.', ruc: '20123456789', monto: 8450.00, fecha: '26/05/2026 14:35', cdr: '20261234567-01-F001-12458', estado: 'ok' },
  { id: 'B001-00078945', tipo: 'B', cliente: 'Carlos Rodríguez Silva', dni: '87654321', monto: 1348.80, fecha: '26/05/2026 14:23', cdr: '20261234567-03-B001-78945', estado: 'ok' },
];

const BILLETES = [200, 100, 50, 20, 10];

export default function FinancialFiscalPage() {
  const [billetes, setBilletes]     = useState({});
  const [monedas,  setMonedas]      = useState('');

  const totalDeclarado = BILLETES.reduce((acc, b) => acc + (b * (parseInt(billetes[b]) || 0)), 0) + (parseFloat(monedas) || 0);

  return (
    <div>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-gray-900">Financial & Fiscal Gateway - Módulo de Cierres y Auditoría</h2>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">

        {/* Cierre de caja */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center gap-2 font-semibold text-gray-700 mb-4">
            <DollarSign size={16} className="text-green-600" />
            Arqueo a Ciegas - Cierre de Caja
          </div>

          <div className="bg-amber-50 border border-amber-200 rounded-xl px-4 py-3 mb-4 flex items-start gap-2">
            <AlertTriangle size={14} className="text-amber-600 mt-0.5 shrink-0" />
            <div>
              <p className="text-xs font-semibold text-amber-700">Modo Ciego Activado</p>
              <p className="text-xs text-amber-600">Ingrese el efectivo físico sin consultar el sistema</p>
            </div>
          </div>

          <div className="flex flex-col gap-3 mb-4">
            <div>
              <label className="text-xs font-semibold text-gray-600 uppercase tracking-wide block mb-1">Cajero</label>
              <input defaultValue="Ana Torres - Caja 03" className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm outline-none bg-gray-50" readOnly />
            </div>
            <div>
              <label className="text-xs font-semibold text-gray-600 uppercase tracking-wide block mb-1">Turno</label>
              <input defaultValue="Tarde (14:00 - 22:00)" className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm outline-none bg-gray-50" readOnly />
            </div>
          </div>

          <p className="text-sm font-semibold text-gray-700 mb-3">Conteo Físico de Efectivo</p>
          <div className="grid grid-cols-3 gap-2 mb-3">
            {BILLETES.map((b) => (
              <div key={b}>
                <label className="text-xs text-gray-500 block mb-1">Billetes S/ {b}</label>
                <input
                  type="number" min={0} placeholder="Cant."
                  value={billetes[b] ?? ''}
                  onChange={(e) => setBilletes((prev) => ({ ...prev, [b]: e.target.value }))}
                  className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition"
                />
              </div>
            ))}
            <div>
              <label className="text-xs text-gray-500 block mb-1">Monedas</label>
              <input
                type="number" min={0} placeholder="S/"
                value={monedas}
                onChange={(e) => setMonedas(e.target.value)}
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 transition"
              />
            </div>
          </div>

          <div className="border-2 border-blue-500 rounded-xl px-4 py-4 text-center mb-4">
            <p className="text-xs text-gray-500 mb-1">Total Declarado por Cajero</p>
            <p className="text-2xl font-bold text-blue-600">S/ {totalDeclarado.toFixed(2)}</p>
          </div>

          <button className="w-full bg-green-500 hover:bg-green-600 text-white py-3 rounded-xl font-semibold transition">
            Confirmar Cierre de Caja
          </button>
        </div>

        {/* Monitor SUNAT */}
        <div className="bg-white rounded-2xl border border-gray-200 p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 font-semibold text-gray-700">
              <FileText size={16} className="text-blue-600" />
              Monitor Fiscal - SUNAT
            </div>
            <button className="bg-blue-600 text-white text-xs px-3 py-1.5 rounded-lg hover:bg-blue-700 transition">Actualizar</button>
          </div>

          <div className="grid grid-cols-2 gap-3 mb-4">
            {[
              { label: 'Facturas Aceptadas',  value: 152, icon: CheckCircle, color: 'text-green-600', bg: 'bg-green-50'  },
              { label: 'Boletas Aceptadas',   value: 68,  icon: CheckCircle, color: 'text-blue-600',  bg: 'bg-blue-50'   },
              { label: 'En Proceso',          value: 8,   icon: Clock,       color: 'text-amber-600', bg: 'bg-amber-50'  },
              { label: 'Rechazadas',          value: 2,   icon: XCircle,     color: 'text-red-600',   bg: 'bg-red-50'    },
            ].map((s) => (
              <div key={s.label} className={`${s.bg} rounded-xl p-4 flex flex-col items-center`}>
                <s.icon size={20} className={`${s.color} mb-2`} />
                <p className={`text-2xl font-bold ${s.color}`}>{s.value}</p>
                <p className="text-xs text-gray-500 text-center mt-1">{s.label}</p>
              </div>
            ))}
          </div>

          <p className="text-sm font-semibold text-gray-700 mb-3">Documentos Recientes</p>
          <div className="flex flex-col gap-3">
            {DOCUMENTOS.map((doc) => (
              <div key={doc.id} className={`border rounded-xl p-4 ${doc.tipo === 'F' ? 'border-green-200 bg-green-50/30' : 'border-blue-200 bg-blue-50/30'}`}>
                <div className="flex items-center justify-between mb-1">
                  <span className={`text-xs font-bold px-2 py-0.5 rounded font-mono ${doc.tipo === 'F' ? 'bg-green-600 text-white' : 'bg-blue-600 text-white'}`}>{doc.id}</span>
                  <span className="text-sm font-bold text-gray-800">S/ {doc.monto.toLocaleString('es-PE', { minimumFractionDigits: 2 })}</span>
                </div>
                <p className="text-xs font-semibold text-gray-700">{doc.cliente}</p>
                <p className="text-xs text-gray-400 mb-2">{doc.ruc ?? doc.dni}</p>
                <div className="flex items-center gap-1 text-xs text-gray-400">
                  <div className="w-4 h-4 border border-gray-300 rounded grid grid-cols-2 gap-0.5 p-0.5">
                    <div className="bg-gray-400 rounded-sm" /><div className="bg-white rounded-sm" />
                    <div className="bg-white rounded-sm" /><div className="bg-gray-400 rounded-sm" />
                  </div>
                  CDR SUNAT: {doc.cdr}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}