import React, { useState, useEffect } from 'react';
import { contabilidadRepository } from '../../../infrastructure/repositories/contabilidadRepository';

export const ActivosFijosPage = () => {
  const [activos, setActivos] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [nuevoActivo, setNuevoActivo] = useState({
    descripcion: '',
    categoriaSunat: 'MAQUINARIA',
    costoAdquisicion: '',
    fechaAdquisicion: '',
    vidaUtilMeses: 120
  });

  const fetchActivos = async () => {
    try {
      const data = await contabilidadRepository.getActivosFijos();
      setActivos(data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    fetchActivos();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await contabilidadRepository.crearActivoFijo({
        ...nuevoActivo,
        costoAdquisicion: parseFloat(nuevoActivo.costoAdquisicion)
      });
      setOpenModal(false);
      fetchActivos();
    } catch (err) {
      alert("Error al registrar activo");
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Control de Activos Fijos</h1>
          <p className="text-sm text-slate-500">CatÃ¡logo e imputaciÃ³n de amortizaciÃ³n de activos con directiva SUNAT.</p>
        </div>
        <button
          onClick={() => setOpenModal(true)}
          type="button"
          className="bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-semibold px-4 py-2.5 rounded-xl transition-colors shadow-sm"
        >
          + Registrar Activo Fijo
        </button>
      </div>

      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-slate-50 text-xs font-semibold text-slate-500 border-b border-slate-200 uppercase">
              <th className="p-4">CÃ³digo / DescripciÃ³n</th>
              <th className="p-4">CategorÃ­a SUNAT</th>
              <th className="p-4">Costo AdquisiciÃ³n</th>
              <th className="p-4">DepreciaciÃ³n Acumulada</th>
              <th className="p-4 text-right">Valor Neto</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-100 text-sm text-slate-700">
            {activos.map((a, idx) => (
              <tr key={idx} className="hover:bg-slate-50">
                <td className="p-4 font-medium">{a.descripcion}</td>
                <td className="p-4 text-xs font-mono">{a.categoriaSunat}</td>
                <td className="p-4">S/ {a.costoAdquisicion?.toFixed(2)}</td>
                <td className="p-4 text-amber-600 font-semibold">S/ {a.depreciacionAcumulada?.toFixed(2)}</td>
                <td className="p-4 text-right font-bold">S/ {(a.costoAdquisicion - a.depreciacionAcumulada).toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {openModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex items-center justify-center z-50">
          <form onSubmit={handleSubmit} className="bg-white p-6 rounded-2xl border border-slate-200 shadow-xl w-full max-w-md space-y-4">
            <h3 className="font-bold text-slate-800 text-lg">Registrar Activo Fijo</h3>
            <div className="space-y-3">
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">DescripciÃ³n</label>
                <input
                  type="text" required
                  value={nuevoActivo.descripcion}
                  onChange={e => setNuevoActivo({...nuevoActivo, descripcion: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">CategorÃ­a SUNAT</label>
                <select
                  value={nuevoActivo.categoriaSunat}
                  onChange={e => setNuevoActivo({...nuevoActivo, categoriaSunat: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none bg-slate-50"
                >
                  <option value="MAQUINARIA">Maquinaria y Equipo (10%)</option>
                  <option value="EQUIPO_COMPUTO">Equipos de CÃ³mputo (25%)</option>
                  <option value="VEHICULO">VehÃ­culos (20%)</option>
                  <option value="EDIFICIO">Edificaciones (5%)</option>
                </select>
              </div>
              <div className="grid grid-cols-2 gap-2">
                <div>
                  <label className="block text-xs font-semibold text-slate-500 uppercase">Costo</label>
                  <input
                    type="number" step="0.01" required
                    value={nuevoActivo.costoAdquisicion}
                    onChange={e => setNuevoActivo({...nuevoActivo, costoAdquisicion: e.target.value})}
                    className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-semibold text-slate-500 uppercase">Fecha Adq.</label>
                  <input
                    type="date" required
                    value={nuevoActivo.fechaAdquisicion}
                    onChange={e => setNuevoActivo({...nuevoActivo, fechaAdquisicion: e.target.value})}
                    className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                  />
                </div>
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button" onClick={() => setOpenModal(false)}
                className="px-4 py-2 text-xs text-slate-500 font-semibold hover:bg-slate-100 rounded-lg"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 text-xs bg-indigo-600 text-white font-semibold hover:bg-indigo-700 rounded-lg"
              >
                Registrar Activo
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};