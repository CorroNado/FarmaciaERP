export const MODULES = {
  VENTAS:          { key: 'osd',        label: 'OSD - Omnichannel Sales & Distribution',   to: '/osd'     },
  COMPRAS:         { key: 'mm',         label: 'MM – Compras e Inventarios',              to: '/mm'       },
  CALIDAD:         { key: 'qm',         label: 'QM – Calidad y Regulación Sanitaria',     to: '/qm'       },
  MARKETING:       { key: 'crm',        label: 'CRM – Marketing y Fidelización',          to: '/crm'      },
  FINANZAS:        { key: 'fico',       label: 'FI/CO – Finanzas y Control',              to: '/fico'     },
  RRHH:            { key: 'rrhh',       label: 'SuccessFactors – Talento Humano',         to: '/rrhh'     },
  INFRAESTRUCTURA: { key: 'pmps',       label: 'PM/PS – Infraestructura y Expansión',     to: '/pmps'     },
  ANALYTICS:       { key: 'bi',         label: 'Analytics Cloud – BI',                    to: '/bi'       },
  SEGURIDAD:       { key: 'seguridad',  label: 'Seguridad',                               to: '/usuarios' },
};

export const ROLE_ACCESS = {
  ADMINISTRADOR: {
    osd:  true, mm:   true,   qm: true,
    crm:  true, fico: true,   rrhh: true,
    pmps: true, bi:   true,   seguridad: true
  },
   JEFE_GENERAL: {
    osd:  true, mm:   true,   qm: true,
    crm:  true, fico: true,   rrhh: true,
    pmps: true, bi:   true,   seguridad: false
  },
  JEFE_LOGISTICA: {
    osd:  false, mm:   true,   qm: false,
    crm:  false, fico: false,   rrhh: false,
    pmps: true, bi:   false,   seguridad: false
  },

   QUIMICO_FARMACEUTICO: {
    osd:  false, mm:   true,   qm: true,
    crm:  false, fico: false,   rrhh: false,
    pmps: false, bi:   true,   seguridad: false
  },
  TECNICO_FARMACIA: {
    osd:  true, mm:   false,   qm: false,
    crm:  false, fico: false,   rrhh: false,
    pmps: false, bi:   false,   seguridad: false
  },
  CONTADOR: {
    osd:  false, mm:   true,   qm: false,
    crm:  false, fico: true,   rrhh: true,
    pmps: false, bi:   true,   seguridad: false
  },
};

export function getModulesForRole(role) {
  const access = ROLE_ACCESS[role] ?? {};
  return Object.values(MODULES).filter((mod) => !!access[mod.key]);
}

export function hasAccess(role, moduleKey) {
  return !!(ROLE_ACCESS[role]?.[moduleKey]);
}