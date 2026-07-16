import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useDevolucion() {
  // Ventas disponibles como origen de una devolución (venta seleccionada + detalles)
  const [ventas, setVentas] = useState([]);
  const [loadingVentas, setLoadingVentas] = useState(false);

  const [historial, setHistorial] = useState([]);
  const [loadingHistorial, setLoadingHistorial] = useState(false);

  const [ventaSeleccionada, setVentaSeleccionada] = useState(null); // Venta completa
  const [cantidades, setCantidades] = useState({}); // { medicamentoId: cantidad }
  const [motivo, setMotivo] = useState('');
  const [accion, setAccion] = useState('');

  const [enviando, setEnviando] = useState(false);
  const [ultimaDevolucion, setUltimaDevolucion] = useState(null);
  const [error, setError] = useState(null);

  // Solo ventas ya cobradas pueden tener devolución
  const cargarVentas = useCallback(async () => {
    setLoadingVentas(true);
    setError(null);
    try {
      const data = await useCases.ventas.getAll.execute();
      setVentas(data.filter((v) => v.estado === 'PAGADA'));
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudieron cargar las ventas');
    } finally {
      setLoadingVentas(false);
    }
  }, []);

  const cargarHistorial = useCallback(async () => {
    setLoadingHistorial(true);
    setError(null);
    try {
      const data = await useCases.devoluciones.getAll.execute();
      setHistorial(data.sort((a, b) => new Date(b.fecha) - new Date(a.fecha)));
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudo cargar el historial de devoluciones');
    } finally {
      setLoadingHistorial(false);
    }
  }, []);

  function seleccionarVenta(ventaId) {
    const venta = ventas.find((v) => String(v.id) === String(ventaId)) ?? null;
    setVentaSeleccionada(venta);
    setCantidades({});
  }

  function cambiarCantidad(medicamentoId, cantidadVendida, delta) {
    setCantidades((prev) => {
      const actual = prev[medicamentoId] ?? 0;
      let val = actual + delta;
      if (val < 0) val = 0;
      if (val > cantidadVendida) val = cantidadVendida;
      return { ...prev, [medicamentoId]: val };
    });
  }

  const items = ventaSeleccionada
    ? (ventaSeleccionada.detalles ?? [])
        .map((d) => ({
          medicamentoId: d.medicamentoId,
          nombreMedicamento: d.nombreMedicamento,
          precioUnitario: d.precioUnitario,
          cantidad: cantidades[d.medicamentoId] ?? 0,
        }))
        .filter((i) => i.cantidad > 0)
    : [];

  const montoEstimado = items.reduce((acc, i) => acc + i.precioUnitario * i.cantidad, 0);

  function limpiarFormulario() {
    setVentaSeleccionada(null);
    setCantidades({});
    setMotivo('');
    setAccion('');
  }

  async function registrarDevolucion() {
    setError(null);
    setEnviando(true);
    try {
      const nueva = await useCases.devoluciones.crear.execute({
        ventaId: ventaSeleccionada?.id,
        motivo,
        accion,
        items,
      });
      setHistorial((prev) => [nueva, ...prev]);
      setUltimaDevolucion(nueva);
      limpiarFormulario();
      return nueva;
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudo registrar la devolución');
      return null;
    } finally {
      setEnviando(false);
    }
  }

  function cerrarTicket() {
    setUltimaDevolucion(null);
  }

  return {
    ventas, loadingVentas, cargarVentas,
    historial, loadingHistorial, cargarHistorial,
    ventaSeleccionada, seleccionarVenta,
    cantidades, cambiarCantidad,
    items, montoEstimado,
    motivo, setMotivo,
    accion, setAccion,
    enviando, registrarDevolucion,
    ultimaDevolucion, cerrarTicket,
    error, setError,
  };
}
