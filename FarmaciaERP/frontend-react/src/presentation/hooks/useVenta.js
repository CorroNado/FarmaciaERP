import { useState, useEffect, useCallback, useMemo } from 'react';
import { useCases } from '@/infrastructure';
import { calcularTotales } from '@/domain/models/Venta';
import { useClienteSelector } from '@/presentation/hooks/useClienteSelector';

export function useVenta() {
  const [medicamentos, setMedicamentos] = useState([]);
  const [loadingCatalogo, setLoadingCatalogo] = useState(false);
  const [error, setError] = useState(null);

  const [cart, setCart] = useState([]); // [{id, nombre, precio, cantidad, stock}]
  const {
    cliente, buscarClientePorDni, registrarClienteRapido, limpiarCliente, errorCliente,
  } = useClienteSelector();
  const [metodoPago, setMetodoPago] = useState('EFECTIVO');
  const [tipoComprobante, setTipoComprobante] = useState('BOLETA');
  const [procesando, setProcesando] = useState(false);
  const [ultimaVenta, setUltimaVenta] = useState(null);

  const cargarCatalogo = useCallback(async (filtro = '') => {
    setLoadingCatalogo(true);
    setError(null);
    try {
      const data = await useCases.medicamentos.getAll(filtro ? { nombre: filtro } : {});
      setMedicamentos(data);
    } catch (err) {
      setError(err.message ?? 'No se pudo cargar el catálogo de medicamentos');
    } finally {
      setLoadingCatalogo(false);
    }
  }, []);

  useEffect(() => {
    cargarCatalogo();
  }, [cargarCatalogo]);

  function agregarAlCarrito(medicamento) {
    if (!medicamento.estaDisponible) return;
    if (medicamento.requiereReceta) {
      setError(`${medicamento.nombre} requiere receta médica y no puede venderse desde el POS`);
      return;
    }
    setCart((prev) => {
      const existente = prev.find((i) => i.id === medicamento.id);
      if (existente) {
        if (existente.cantidad >= medicamento.stock) return prev;
        return prev.map((i) => i.id === medicamento.id ? { ...i, cantidad: i.cantidad + 1 } : i);
      }
      return [...prev, {
        id: medicamento.id,
        nombre: medicamento.nombre,
        presentacion: medicamento.presentacion,
        precio: medicamento.precio,
        stock: medicamento.stock,
        cantidad: 1,
      }];
    });
  }

  function cambiarCantidad(id, delta) {
    setCart((prev) => prev
      .map((i) => {
        if (i.id !== id) return i;
        const nuevaCantidad = Math.min(i.cantidad + delta, i.stock);
        return { ...i, cantidad: nuevaCantidad };
      })
      .filter((i) => i.cantidad > 0));
  }

  function quitarDelCarrito(id) {
    setCart((prev) => prev.filter((i) => i.id !== id));
  }

  function vaciarCarrito() {
    setCart([]);
  }

  const totales = useMemo(() => calcularTotales(cart), [cart]);

  async function procesarPago() {
    setError(null);
    if (!cliente) { setError('Selecciona o registra un cliente antes de continuar'); return null; }
    if (cart.length === 0) { setError('Agrega al menos un producto al carrito'); return null; }

    setProcesando(true);
    try {
      const venta = await useCases.ventas.crear({
        clienteId: cliente.id,
        metodoPago,
        tipoComprobante,
        cart,
      });
      const pagada = await useCases.ventas.pagar(venta.id);
      setUltimaVenta(pagada);
      vaciarCarrito();
      await cargarCatalogo(); // refresca stock mostrado
      return pagada;
    } catch (err) {
      setError(err.response?.data?.message ?? err.response?.data ?? err.message ?? 'No se pudo procesar la venta');
      return null;
    } finally {
      setProcesando(false);
    }
  }

  function cerrarTicket() {
    setUltimaVenta(null);
  }

  return {
    medicamentos,
    loadingCatalogo,
    error: error ?? errorCliente,
    setError,
    cargarCatalogo,

    cart,
    agregarAlCarrito,
    cambiarCantidad,
    quitarDelCarrito,
    vaciarCarrito,
    totales,

    cliente,
    buscarClientePorDni,
    registrarClienteRapido,
    limpiarCliente,

    metodoPago,
    setMetodoPago,
    tipoComprobante,
    setTipoComprobante,

    procesando,
    procesarPago,
    ultimaVenta,
    cerrarTicket,
  };
}
