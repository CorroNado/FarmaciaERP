import { useState, useCallback } from 'react';
import { useCases } from '@/infrastructure';

export function useCotizacion() {
  const [items, setItems] = useState([]); // [{id, nombre, presentacion, precio, cantidad}]
  const [vigenciaDias, setVigenciaDias] = useState(7);

  const [historial, setHistorial] = useState([]);
  const [loadingHistorial, setLoadingHistorial] = useState(false);

  const [enviando, setEnviando] = useState(false);
  const [procesandoId, setProcesandoId] = useState(null);
  const [error, setError] = useState(null);

  const cargarHistorial = useCallback(async () => {
    setLoadingHistorial(true);
    setError(null);
    try {
      const data = await useCases.cotizaciones.getAll.execute();
      // Más recientes primero
      setHistorial(data.sort((a, b) => new Date(b.fecha) - new Date(a.fecha)));
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudo cargar el historial de cotizaciones');
    } finally {
      setLoadingHistorial(false);
    }
  }, []);

  // El catálogo se agrega sin límite de stock: una cotización es una oferta,
  // el stock real recién se valida al aceptarla y convertirla en venta.
  function agregarItem(medicamento) {
    setItems((prev) => {
      const existente = prev.find((i) => i.id === medicamento.id);
      if (existente) {
        return prev.map((i) => i.id === medicamento.id ? { ...i, cantidad: i.cantidad + 1 } : i);
      }
      return [...prev, {
        id: medicamento.id,
        nombre: medicamento.nombre,
        presentacion: medicamento.presentacion,
        precio: medicamento.precio,
        cantidad: 1,
      }];
    });
  }

  function cambiarCantidad(id, delta) {
    setItems((prev) => prev
      .map((i) => (i.id === id ? { ...i, cantidad: i.cantidad + delta } : i))
      .filter((i) => i.cantidad > 0));
  }

  function quitarItem(id) {
    setItems((prev) => prev.filter((i) => i.id !== id));
  }

  function vaciarItems() {
    setItems([]);
  }

  const totalCotizado = items.reduce((acc, i) => acc + i.precio * i.cantidad, 0);

  async function enviarCotizacion(clienteId) {
    setError(null);
    setEnviando(true);
    try {
      const nueva = await useCases.cotizaciones.crear.execute({ clienteId, vigenciaDias, items });
      vaciarItems();
      setHistorial((prev) => [nueva, ...prev]);
      return nueva;
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudo enviar la cotización');
      return null;
    } finally {
      setEnviando(false);
    }
  }

  async function aceptarCotizacion(id) {
    setError(null);
    setProcesandoId(id);
    try {
      const { cotizacion, venta } = await useCases.cotizaciones.aceptar.execute(id);
      setHistorial((prev) => prev.map((c) => (c.id === id ? cotizacion : c)));
      return venta;
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudo aceptar la cotización');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  async function rechazarCotizacion(id, motivo) {
    setError(null);
    setProcesandoId(id);
    try {
      const actualizada = await useCases.cotizaciones.rechazar.execute(id, motivo);
      setHistorial((prev) => prev.map((c) => (c.id === id ? actualizada : c)));
      return actualizada;
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudo rechazar la cotización');
      return null;
    } finally {
      setProcesandoId(null);
    }
  }

  return {
    items,
    agregarItem,
    cambiarCantidad,
    quitarItem,
    totalCotizado,

    vigenciaDias,
    setVigenciaDias,

    historial,
    loadingHistorial,
    cargarHistorial,

    enviando,
    enviarCotizacion,

    procesandoId,
    aceptarCotizacion,
    rechazarCotizacion,

    error,
    setError,
  };
}
