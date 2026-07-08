import React        from 'react';
import ReactDOM     from 'react-dom/client';
import { AuthProvider } from './presentation/context/AuthProvider';
import AppRouter    from './core/routes/AppRouter';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AuthProvider>
      <AppRouter />
    </AuthProvider>
  </React.StrictMode>
);