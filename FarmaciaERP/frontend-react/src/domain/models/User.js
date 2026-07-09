export class User {
  constructor({ id, email, estado, nombres, password, registro, role }) {
    this.id       = id;
    this.email    = email;
    this.estado   = estado;
    this.nombre   = nombres?.Nombres   ?? '';
    this.apellido = nombres?.Apellidos ?? '';
    this.password = password ?? '';
    this.fecha    = registro ?? null;
    this.rol      = role     ?? 'USUARIO';
  }

  get fullName() {
    return `${this.nombre} ${this.apellido}`.trim();
  }

  get isActive() {
    return this.estado === 'ACTIVO';
  }

  static fromApi(raw) {
    return new User({
      id:       raw.id,
      email:    raw.email?.email ?? raw.email,
      estado:   raw.estado,
      nombres:  raw.nombres,
      password: raw.password,
      registro: raw.registro,
      role:     raw.role,
    });
  }

  // ✅ ahora envía strings simples que espera CrearUsuarioResquest
static toApi(formData) {
  return {
    nombre:   formData.nombre,
    apellido: formData.apellido,
    email:    formData.email.toLowerCase(),
    password: formData.password,
    role:     formData.role ?? 'ADMINISTRADOR', // ← asegúrate que esté
  };
}

// Nuevo método específico para actualizar
static toApiUpdate(formData) {
  return {
    nombre:   formData.nombre,
    apellido: formData.apellido,
    email: formData.email,   // ← objeto para PUT (Usuario espera Email VO)
    password: formData.password,
    role:     formData.role,
    estado:   formData.estado,
  };
}
}