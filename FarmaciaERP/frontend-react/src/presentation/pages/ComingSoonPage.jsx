
export default function ComingSoonPage({ title }) {
  return (
    <>
      <div className="flex flex-col items-center justify-center h-full py-24 text-center">
        <div className="w-16 h-16 bg-slate-100 rounded-2xl flex items-center justify-center text-3xl mb-6">
          🚧
        </div>
        <h2 className="text-2xl font-bold text-slate-800 mb-2">{title}</h2>
        <p className="text-slate-500 text-sm max-w-sm">
          Este módulo está en desarrollo. Pronto estará disponible.
        </p>
      </div>
    </>
  );
}