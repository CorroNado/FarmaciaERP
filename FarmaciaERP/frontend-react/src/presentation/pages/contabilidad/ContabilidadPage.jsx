import React, { useState } from 'react';
import { FiGlRail } from '../../components/fiGl/FiGlRail';
import { PlanCuentasPage } from './PlanCuentasPage';
import { BalanceGeneralPage } from './BalanceGeneralPage';
import { EstadoResultadosPage } from './EstadoResultadosPage';
import { ActivosFijosPage } from './ActivosFijosPage';

export const ContabilidadPage = () => {
  const [activeTab, setActiveTab] = useState('catalogo');

  const renderContent = () => {
    switch (activeTab) {
      case 'catalogo':
        return <PlanCuentasPage />;
      case 'balance':
        return <BalanceGeneralPage />;
      case 'resultados':
        return <EstadoResultadosPage />;
      case 'activos':
        return <ActivosFijosPage />;
      case 'costos':
      case 'diario':
        return (
          <div className="p-8 text-center text-slate-500">
            <h2 className="text-xl font-semibold mb-2">SecciÃ³n en integraciÃ³n...</h2>
            <p className="text-sm">Esta secciÃ³n del General Ledger (FI-GL) estÃ¡ lista para enlazar con la lÃ³gica de asientos de Diario.</p>
          </div>
        );
      default:
        return <PlanCuentasPage />;
    }
  };

  return (
    <div className="flex bg-slate-50 min-h-screen">
      <FiGlRail activeTab={activeTab} onTabChange={setActiveTab} />
      <div className="flex-1 overflow-x-hidden">
        {renderContent()}
      </div>
    </div>
  );
};