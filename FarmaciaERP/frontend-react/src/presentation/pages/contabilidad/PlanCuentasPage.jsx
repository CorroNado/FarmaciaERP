import React, { useEffect, useState } from 'react';
import { useContabilidad } from '../../hooks/Contabilidad/useContabilidad';
import { CatalogoJerarquicoTree } from '../../components/ui/CatalogoJerarquicoTree';

export const PlanCuentasPage = () => {
  const { planCuentas, loading, fetchPlanCuentas, addCuentaContable } = useContabilidad();
  const [selectedParent, setSelectedParent] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [newCuenta, setNewCuenta] = useState({ codigo: '', descripcion: '', tipo: 'ACTIVO' });

  useEffect(() => {
    fetchPlanCuentas();
  }, [fetchPlanCuentas]);

  const handleAddNode = (parentNode) => {
    setSelectedParent(parentNode);
    if (parentNode) {
      setNewCuenta(prev => ({ ...prev, codigo: `${parentNode.codigo}.` }));
    } else {
      setNewCuenta({ codigo: '', descripcion: '', tipo: 'ACTIVO' });
    }
    setModalOpen(true);
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      await addCuentaContable({
        ...newCuenta,
        padreId: selectedParent ? selectedParent.id : null
      });
      setModalOpen(false);
    } catch (err) {
      alert("Error al agregar cuenta: " + err.message);
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Plan de Cuentas Contables</h1>
          <p className="text-sm text-slate-500">DefiniciÃ³n de cuentas contables estructuradas para el mapeo con el Plan Contable General Empresarial (PCGE).</p>
        </div>
      </div>

      {loading && <p className="text-indigo-600 animate-pulse text-sm font-semibold">Procesando catÃ¡logo...</p>}

      <CatalogoJerarquicoTree data={planCuentas} onAddNode={handleAddNode} />

      {modalOpen && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm flex items-center justify-center z-50">
          <form onSubmit={handleSave} className="bg-white p-6 rounded-2xl border border-slate-200 shadow-xl w-full max-w-md space-y-4">
            <h3 className="font-bold text-slate-800 text-lg">
              {selectedParent ? `Crear subcuenta de [${selectedParent.codigo}]` : 'Crear Cuenta RaÃ­z'}
            </h3>

            <div className="space-y-3">
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">CÃ³digo Cuenta</label>
                <input
                  type="text"
                  required
                  value={newCuenta.codigo}
                  onChange={e => setNewCuenta({...newCuenta, codigo: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                />
              </div>
              <div>
                <label className="block text-xs font-semibold text-slate-500 uppercase">DescripciÃ³n</label>
                <input
                  type="text"
                  required
                  value={newCuenta.descripcion}
                  onChange={e => setNewCuenta({...newCuenta, descripcion: e.target.value})}
                  className="w-full text-sm border border-slate-300 rounded-lg p-2.5 mt-1 focus:ring-2 focus:ring-indigo-500 outline-none"
                />
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-2">
              <button
                type="button"
                onClick={() => setModalOpen(false)}
                className="px-4 py-2 text-xs text-slate-500 font-semibold hover:bg-slate-100 rounded-lg"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 text-xs bg-indigo-600 text-white font-semibold hover:bg-indigo-700 rounded-lg"
              >
                Guardar Cuenta
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};