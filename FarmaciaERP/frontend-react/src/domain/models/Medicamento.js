export class Medicamento {
  constructor({ id, nombre, presentacion, precio, stock, categoria, fechaVencimiento }) {
    this.id = id;
    this.nombre = nombre;
    this.presentacion = presentacion ?? '';
    this.precio = Number(precio ?? 0);
    this.stock = Number(stock ?? 0);
    this.categoria = categoria ?? 'VENTA_LIBRE';
    this.fechaVencimiento = fechaVencimiento ?? null;
  }

  get requiereReceta() {
    return this.categoria === 'RECETA_SIMPLE' || this.categoria === 'RECETA_RETENIDA' || this.categoria === 'CONTROLADO';
  }

  get estaVencido() {
    if (!this.fechaVencimiento) return false;
    return new Date(this.fechaVencimiento) < new Date();
  }

  get estaDisponible() {
    return this.stock > 0 && !this.estaVencido;
  }

  get stockBajo() {
    return this.stock > 0 && this.stock <= 10;
  }

  static fromApi(raw) {
    return new Medicamento({
      id: raw.id,
      nombre: raw.nombre,
      presentacion: raw.presentacion,
      precio: raw.precio,
      stock: raw.stock,
      categoria: raw.categoria,
      fechaVencimiento: raw.fechaVencimiento,
    });
  }
}
