import { useState, useEffect } from 'react';
import { useCases } from '../../infrastructure';

export function useAccessReport(userId) {
  const [report,  setReport]  = useState(null);
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState(null);

  useEffect(() => {
    if (!userId) return;

    async function fetchReport() {
      try {
        setLoading(true);
        setError(null);
        const data = await useCases.reports.getAccess.execute(userId);
        setReport(data);
      } catch (err) {
        setError(err.message ?? 'Error al obtener el reporte');
      } finally {
        setLoading(false);
      }
    }

    fetchReport();
  }, [userId]);

  return { report, loading, error };
}