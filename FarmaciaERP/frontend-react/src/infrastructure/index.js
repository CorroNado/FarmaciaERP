import { authService }      from '@/infrastructure/services/authService';
import { userRepository }   from '@/infrastructure/repositories/userRepository';
import { reportRepository } from '@/infrastructure/repositories/reportRepository';

import { loginUseCase }           from '@/domain/usecases/auth/loginUseCase';
import { getUsersUseCase }        from '@/domain/usecases/users/getUsersUseCase';
import { getUserByIdUseCase }     from '@/domain/usecases/users/getUserByIdUseCase';
import { createUserUseCase }      from '@/domain/usecases/users/createUserUseCase';
import { editUserUseCase }        from '@/domain/usecases/users/editUserUseCase';
import { deleteUserUseCase }      from '@/domain/usecases/users/deleteUserUseCase';
import { getAccessReportUseCase } from '@/domain/usecases/reports/getAccessReportUseCase';

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
  reports: {
    getAccess: getAccessReportUseCase(reportRepository),
  },
};