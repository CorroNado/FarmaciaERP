export class Sucursal {
  constructor({ id, codigo, nombre, activa }) {
    this.id = id;
    this.codigo = codigo ?? '';
    this.nombre = nombre ?? '';
    this.activa = Boolean(activa);
  }

  static fromApi(raw) {
    return new Sucursal(raw);
  }

  // CrearSucursalRequest — { codigo, nombre }
  static toApiCrear({ codigo, nombre }) {
    return {
      codigo: (codigo ?? '').trim(),
      nombre: (nombre ?? '').trim(),
    };
  }
}
