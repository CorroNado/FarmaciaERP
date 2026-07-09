import { authService }      from './services/authService';
import { userRepository }   from './repositories/userRepository';
import { reportRepository } from './repositories/reportRepository';
import { ventaRepository }  from './repositories/ventaRepository';

import { loginUseCase }           from '@/domain/usecases/auth/loginUseCase';
import { getUsersUseCase }        from '@/domain/usecases/users/getUsersUseCase';
import { getUserByIdUseCase }     from '@/domain/usecases/users/getUserByIdUseCase';
import { createUserUseCase }      from '@/domain/usecases/users/createUserUseCase';
import { editUserUseCase }        from '@/domain/usecases/users/editUserUseCase';
import { deleteUserUseCase }      from '@/domain/usecases/users/deleteUserUseCase';
import { getAccessReportUseCase } from '@/domain/usecases/reports/getAccessReportUseCase';
import { getVentasUseCase }       from '@/domain/usecases/ventas/getVentasUseCase';
import { getVentaByIdUseCase }    from '@/domain/usecases/ventas/getVentaByIdUseCase';
import { crearVentaUseCase }      from '@/domain/usecases/ventas/crearVentaUseCase';
import { pagarVentaUseCase }      from '@/domain/usecases/ventas/pagarVentaUseCase';
import { anularVentaUseCase }     from '@/domain/usecases/ventas/anularVentaUseCase';

export const useCases = {
  auth: {
    login: loginUseCase(authService),
  },
  users: {
    getAll:  getUsersUseCase(userRepository),
    getById: getUserByIdUseCase(userRepository),
    create:  createUserUseCase(userRepository),
    edit:    editUserUseCase(userRepository),
    delete:  deleteUserUseCase(userRepository),
  },
  ventas: {
    getAll:  getVentasUseCase(ventaRepository),
    getById: getVentaByIdUseCase(ventaRepository),
    crear:   crearVentaUseCase(ventaRepository),
    pagar:   pagarVentaUseCase(ventaRepository),
    anular:  anularVentaUseCase(ventaRepository),
  },
  reports: {
    getAccess: getAccessReportUseCase(reportRepository),
  },
};