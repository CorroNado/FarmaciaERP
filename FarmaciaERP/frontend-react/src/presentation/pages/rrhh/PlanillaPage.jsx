import { useMemo, useState } from 'react';
import Button from '@/presentation/components/ui/Button';
import Input  from '@/presentation/components/ui/Input';
import Select from '@/presentation/components/ui/Select';
import Modal  from '@/presentation/components/ui/Modal';
import RrhhTabs from '@/presentation/components/rrhh/RrhhTabs';
import { usePlanilla } from '@/presentation/hooks/usePlanilla';
import { MESES_PLANILLA, MES_OPTIONS } from '@/domain/models/Planilla';

function StageHeader({ eyebrow, title, description, badge }) {
  return (
    <div className="px-6 py-5 border-b border-slate-200 flex items-start justify-between gap-4 flex-wrap">
      <div>
        <p className="font-mono text-[11px] tracking-widest uppercase text-teal-700">{eyebrow}</p>
        <h2 className="text-xl font-semibold text-slate-800 mt-1">{title}</h2>
        <p className="text-sm text-slate-500 mt-1 max-w-2xl">{description}</p>
      </div>
      {badge && (
        <span className="font-mono text-[11px] px-2.5 py-1 rounded-full bg-teal-50 text-teal-700 border border-teal-200 whitespace-nowrap">
          {badge}
        </span>
      )}
    </div>
  );
}

const money = (v) => `S/. ${Number(v ?? 0).toFixed(2)}`;

function exportPlanillaCSV(planilla) {
  if (!planilla || !planilla.detalles.length) return;
  const headers = [
    'Código', 'Colaborador', 'Sueldo Base', 'Días con Turno', 'Faltas', 'Tardanzas (min)',
    'Horas Extras', 'Descuento Faltas', 'Descuento Tardanzas', 'Bono Asistencia (5%)',
    'Bono Cumplimiento', 'Bono Metas', 'ESSALUD (9%)', 'AFP (10%)', 'Otros Descuentos', 'Sueldo Neto',
  ];
  const rows = planilla.detalles.map((d) => [
    d.codigoEmpleado, d.nombreCompleto, d.sueldoBase.toFixed(2), d.diasConTurno, d.faltas, d.minutosTardanza,
    d.horasExtras.toFixed(2), d.descuentoFaltas.toFixed(2), d.descuentoTardanzas.toFixed(2), d.bonoAsistencia.toFixed(2),
    d.bonoCumplimiento.toFixed(2), d.bonoMetas.toFixed(2), d.essalud.toFixed(2), d.afp.toFixed(2), d.otrosDescuentos.toFixed(2), d.sueldoNeto.toFixed(2),
  ]);
  const csv = [headers, ...rows]
    .map((r) => r.map((v) => `"${String(v ?? '').replace(/"/g, '""')}"`).join(','))
    .join('\n');
  const blob = new Blob(['\uFEFF' + csv], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `Planilla_${MESES_PLANILLA[planilla.mes]}_${planilla.anio}.csv`;
  a.click();
  URL.revokeObjectURL(url);
}

// ─────────────────────────────────────────────────────────────────────────
// Modal: Recibo de sueldo individual
// ─────────────────────────────────────────────────────────────────────────
function ReciboModal({ detalle, mes, anio, onClose }) {
  if (!detalle) return null;
  return (
    <Modal isOpen={!!detalle} title="📄 Recibo de Sueldo" onClose={onClose}>
      <div className="flex flex-col gap-4">
        <div className="grid grid-cols-2 gap-x-4 gap-y-1.5 text-sm">
          <p><span className="text-slate-500">Colaborador:</span> <span className="font-semibold text-slate-800">{detalle.nombreCompleto}</span></p>
          <p><span className="text-slate-500">Código:</span> <span className="font-semibold text-slate-800">{detalle.codigoEmpleado}</span></p>
          <p><span className="text-slate-500">Período:</span> <span className="font-semibold text-slate-800">{MESES_PLANILLA[mes]} {anio}</span></p>
          <p><span className="text-slate-500">Días con turno:</span> <span className="font-semibold text-slate-800">{detalle.diasConTurno}</span></p>
        </div>

        <div className="rounded-xl border border-slate-200 overflow-hidden">
          <table className="w-full text-sm">
            <tbody className="divide-y divide-slate-100">
              <tr><td className="px-3 py-2 font-medium text-slate-700">Sueldo Base</td><td className="px-3 py-2 text-right">{money(detalle.sueldoBase)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">Faltas ({detalle.faltas})</td><td className="px-3 py-2 text-right text-red-600">- {money(detalle.descuentoFaltas)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">Tardanzas ({detalle.minutosTardanza} min)</td><td className="px-3 py-2 text-right text-red-600">- {money(detalle.descuentoTardanzas)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">Horas Extras ({detalle.horasExtras.toFixed(2)}h)</td><td className="px-3 py-2 text-right text-emerald-600">+ {money(detalle.montoExtra)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">Bono Asistencia Perfecta (5%)</td><td className="px-3 py-2 text-right text-emerald-600">+ {money(detalle.bonoAsistencia)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">Bono Cumplimiento (5%)</td><td className="px-3 py-2 text-right text-emerald-600">+ {money(detalle.bonoCumplimiento)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">Bono Metas</td><td className="px-3 py-2 text-right text-emerald-600">+ {money(detalle.bonoMetas)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">ESSALUD (9%)</td><td className="px-3 py-2 text-right text-red-600">- {money(detalle.essalud)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">AFP (10%)</td><td className="px-3 py-2 text-right text-red-600">- {money(detalle.afp)}</td></tr>
              <tr><td className="px-3 py-2 font-medium text-slate-700">Otros Descuentos</td><td className="px-3 py-2 text-right text-red-600">- {money(detalle.otrosDescuentos)}</td></tr>
              <tr className="bg-slate-50">
                <td className="px-3 py-2.5 font-bold text-slate-800">Neto a Pagar</td>
                <td className="px-3 py-2.5 text-right font-bold text-teal-700">{money(detalle.sueldoNeto)}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <p className="text-xs text-slate-400 text-center">Recibo generado el {new Date().toLocaleString('es-PE')}</p>

        <div className="flex justify-center">
          <Button variant="secondary" onClick={() => window.print()}>🖨️ Imprimir</Button>
        </div>
      </div>
    </Modal>
  );
}

// ─────────────────────────────────────────────────────────────────────────
// PÁGINA — RRHH.03 Nómina / Planilla
// ─────────────────────────────────────────────────────────────────────────
export default function PlanillaPage() {
  const now = new Date();
  const {
    planilla, historial, loading, saving, error,
    calcular, guardar, cargarGuardada, eliminarGuardada,
    actualizarBonoMetas,
  } = usePlanilla();

  const [mes, setMes] = useState(String(now.getMonth() + 1));
  const [anio, setAnio] = useState(String(now.getFullYear()));
  const [reciboDetalle, setReciboDetalle] = useState(null);
  const [confirmarSobrescritura, setConfirmarSobrescritura] = useState(false);
  const [eliminando, setEliminando] = useState(null);

  const montoTotal = useMemo(() => {
    if (!planilla) return 0;
    return planilla.detalles.reduce((acc, d) => acc + Number(d.sueldoNeto ?? 0), 0);
  }, [planilla]);

  async function handleCalcular() {
    setConfirmarSobrescritura(false);
    await calcular(mes, anio);
  }

  async function handleGuardar() {
    if (!planilla) return;
    const bonosMetas = planilla.detalles.map((d) => ({ empleadoId: d.empleadoId, bonoMetas: d.bonoMetas }));
    const ok = await guardar({ mes: planilla.mes, anio: planilla.anio, bonosMetas, confirmarSobrescritura });
    if (ok) setConfirmarSobrescritura(false);
  }

  async function handleCargarGuardada(item) {
    const data = await cargarGuardada(item.id);
    if (data) {
      setMes(String(data.mes));
      setAnio(String(data.anio));
    }
  }

  async function handleEliminarGuardada(id) {
    setEliminando(id);
    await eliminarGuardada(id);
    setEliminando(null);
  }

  return (
    <div>
      <RrhhTabs />
      <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
        <StageHeader
          eyebrow="RRHH.03 · Nómina"
          title="Cálculo de Planilla Mensual"
          description="Sueldo neto calculado a partir de la asistencia del mes: horas extras, faltas, tardanzas, bonos automáticos y descuentos legales (ESSALUD, AFP)."
          badge="Nómina Mensual"
        />

        <div className="p-6">
          {/* Selector mes/año y acciones */}
          <div className="bg-slate-50 rounded-xl border border-slate-200 p-4 mb-5 flex items-end justify-between gap-3 flex-wrap">
            <div className="flex items-end gap-3 flex-wrap">
              <Select label="Mes" name="mes" value={mes} onChange={(e) => setMes(e.target.value)} options={MES_OPTIONS} placeholder="" />
              <Input label="Año" name="anio" type="number" value={anio} onChange={(e) => setAnio(e.target.value)} />
              <Button onClick={handleCalcular} loading={loading}>📊 Calcular Planilla</Button>
            </div>
            <div className="flex gap-2 flex-wrap">
              <Button variant="secondary" onClick={handleGuardar} disabled={!planilla} loading={saving}>💾 Guardar Planilla</Button>
              <Button variant="outline" onClick={() => exportPlanillaCSV(planilla)} disabled={!planilla}>📥 Exportar a Excel</Button>
            </div>
          </div>

          {error && (
            <div className="mb-4 px-4 py-3 bg-red-50 border border-red-200 rounded-xl text-sm text-red-600 flex items-center gap-2">
              <span>⚠</span> {typeof error === 'string' ? error : 'Ocurrió un error'}
            </div>
          )}

          {planilla?.guardada === false && historial.some((h) => h.mes === planilla.mes && h.anio === planilla.anio) && (
            <div className="mb-4 px-4 py-3 bg-amber-50 border border-amber-200 rounded-xl text-sm text-amber-700 flex items-center justify-between gap-3 flex-wrap">
              <span>⚠ Ya existe una planilla guardada para {MESES_PLANILLA[planilla.mes]} {planilla.anio}. Al guardar se sobrescribirá.</span>
              <label className="flex items-center gap-2 text-xs font-medium">
                <input type="checkbox" checked={confirmarSobrescritura} onChange={(e) => setConfirmarSobrescritura(e.target.checked)} />
                Confirmo sobrescritura
              </label>
            </div>
          )}

          {/* Tabla de planilla calculada */}
          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden mb-6">
            {loading ? (
              <div className="flex items-center justify-center py-14 text-slate-400 text-sm">Calculando planilla...</div>
            ) : !planilla ? (
              <div className="flex flex-col items-center justify-center py-16 text-slate-400">
                <p className="text-sm font-medium">Selecciona un mes y año, luego haz clic en «Calcular Planilla».</p>
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-slate-200">
                      {['Código', 'Colaborador', 'Sueldo Base', 'Días', 'Faltas', 'Tard. (min)', 'H. Extras',
                        'Desc. Faltas', 'Desc. Tard.', 'Bono Asist.', 'Bono Cumpl.', 'Bono Metas',
                        'ESSALUD', 'AFP', 'Otros Desc.', 'Sueldo Neto', ''].map((h) => (
                        <th key={h} className="px-3 py-3 text-left text-[11px] font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                      ))}
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {planilla.detalles.length === 0 && (
                      <tr><td colSpan={17} className="px-3 py-10 text-center text-slate-400">No hay datos para el mes seleccionado.</td></tr>
                    )}
                    {planilla.detalles.map((d) => (
                      <tr key={d.empleadoId} className="hover:bg-slate-50">
                        <td className="px-3 py-2.5 font-semibold text-slate-800 whitespace-nowrap">{d.codigoEmpleado}</td>
                        <td className="px-3 py-2.5 text-slate-700 whitespace-nowrap">{d.nombreCompleto}</td>
                        <td className="px-3 py-2.5 whitespace-nowrap">{money(d.sueldoBase)}</td>
                        <td className="px-3 py-2.5">{d.diasConTurno}</td>
                        <td className="px-3 py-2.5">{d.faltas}</td>
                        <td className="px-3 py-2.5">{d.minutosTardanza}</td>
                        <td className="px-3 py-2.5 whitespace-nowrap">{d.horasExtras.toFixed(2)}h</td>
                        <td className="px-3 py-2.5 text-red-600 whitespace-nowrap">- {money(d.descuentoFaltas)}</td>
                        <td className="px-3 py-2.5 text-red-600 whitespace-nowrap">- {money(d.descuentoTardanzas)}</td>
                        <td className="px-3 py-2.5 text-emerald-600 whitespace-nowrap">+ {money(d.bonoAsistencia)}</td>
                        <td className="px-3 py-2.5 text-emerald-600 whitespace-nowrap">+ {money(d.bonoCumplimiento)}</td>
                        <td className="px-3 py-2.5">
                          <input
                            type="number" min="0" step="0.01" value={d.bonoMetas}
                            onChange={(e) => actualizarBonoMetas(d.empleadoId, Number(e.target.value) || 0)}
                            className="w-20 border border-slate-200 rounded-lg px-2 py-1 text-sm focus:ring-2 focus:ring-blue-500/20 focus:border-blue-500 outline-none"
                          />
                        </td>
                        <td className="px-3 py-2.5 text-red-600 whitespace-nowrap">- {money(d.essalud)}</td>
                        <td className="px-3 py-2.5 text-red-600 whitespace-nowrap">- {money(d.afp)}</td>
                        <td className="px-3 py-2.5 text-red-600 whitespace-nowrap">- {money(d.otrosDescuentos)}</td>
                        <td className="px-3 py-2.5 font-bold text-teal-700 whitespace-nowrap">{money(d.sueldoNeto)}</td>
                        <td className="px-3 py-2.5">
                          <Button variant="secondary" className="!px-2.5 !py-1.5 !text-xs" onClick={() => setReciboDetalle(d)}>📄 Recibo</Button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                  {planilla.detalles.length > 0 && (
                    <tfoot>
                      <tr className="bg-slate-50 border-t border-slate-200">
                        <td colSpan={15} className="px-3 py-3 text-right font-semibold text-slate-600">Monto Total Neto</td>
                        <td className="px-3 py-3 font-bold text-teal-800 whitespace-nowrap">{money(montoTotal)}</td>
                        <td />
                      </tr>
                    </tfoot>
                  )}
                </table>
              </div>
            )}
          </div>

          {/* Historial de planillas guardadas */}
          <div>
            <h3 className="text-sm font-semibold text-slate-700 mb-3">📂 Planillas Guardadas</h3>
            {historial.length === 0 ? (
              <p className="text-sm text-slate-400">No hay planillas guardadas aún.</p>
            ) : (
              <div className="flex flex-wrap gap-2">
                {historial.map((h) => (
                  <div key={h.id} className="flex items-center gap-3 bg-slate-100 border-l-4 border-teal-600 rounded-lg px-4 py-2.5 flex-wrap">
                    <span className="text-sm font-semibold text-slate-800">{MESES_PLANILLA[h.mes]} {h.anio}</span>
                    <span className="text-xs text-slate-500">{h.cantidadColaboradores} colaboradores · {money(h.montoTotalNeto)}</span>
                    <span className="text-xs text-slate-400">{h.fechaGuardado ? new Date(h.fechaGuardado).toLocaleString('es-PE') : ''}</span>
                    <Button variant="secondary" className="!px-2.5 !py-1.5 !text-xs" onClick={() => handleCargarGuardada(h)}>📂 Cargar</Button>
                    <Button variant="danger" className="!px-2.5 !py-1.5 !text-xs" loading={eliminando === h.id} onClick={() => handleEliminarGuardada(h.id)}>🗑️</Button>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>

      <ReciboModal detalle={reciboDetalle} mes={planilla?.mes} anio={planilla?.anio} onClose={() => setReciboDetalle(null)} />
    </div>
  );
}
