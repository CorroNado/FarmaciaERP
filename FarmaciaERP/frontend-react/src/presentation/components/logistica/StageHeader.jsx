export default function StageHeader({ eyebrow, title, description, badge }) {
  return (
    <div className="px-6 py-5 border-b border-slate-200 flex items-start justify-between gap-4 flex-wrap">
      <div>
        <p className="font-mono text-[11px] tracking-widest uppercase text-teal-700">{eyebrow}</p>
        <h2 className="text-xl font-semibold text-slate-800 mt-1">{title}</h2>
        <p className="text-sm text-slate-500 mt-1 max-w-2xl">{description}</p>
      </div>
      {badge && (
        <span className="font-mono text-[11px] px-2.5 py-1 rounded-full bg-teal-50 text-teal-700 border border-teal-200 whitespace-nowrap">
          {badge}
        </span>
      )}
    </div>
  );
}
