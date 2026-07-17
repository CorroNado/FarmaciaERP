import React, { useState } from 'react';

const TreeNode = ({ node, onAddChild, level = 0 }) => {
  const [isExpanded, setIsExpanded] = useState(false);
  const hasChildren = node.children && node.children.length > 0;

  return (
    <div className="select-none">
      <div
        className={`flex items-center justify-between p-2 rounded-lg hover:bg-slate-50 transition-colors duration-150 border-b border-slate-100 ${
          level === 0 ? 'bg-slate-50/50 font-semibold' : ''
        }`}
        style={{ paddingLeft: `${Math.max(level * 1.5, 0.5)}rem` }}
      >
        <div className="flex items-center gap-2">
          {hasChildren ? (
            <button
              onClick={() => setIsExpanded(!isExpanded)}
              type="button"
              className="p-1 hover:bg-slate-200 rounded text-slate-500 transition-transform"
            >
              <svg
                className={`w-4 h-4 transform transition-transform ${isExpanded ? 'rotate-90' : ''}`}
                fill="none" viewBox="0 0 24 24" stroke="currentColor"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
              </svg>
            </button>
          ) : (
            <span className="w-6" />
          )}
          <span className="text-sm font-mono text-indigo-600 font-bold">{node.codigo}</span>
          <span className="text-sm text-slate-700">{node.descripcion}</span>
          {node.tipo && (
            <span className="text-xs px-2 py-0.5 bg-slate-200/60 rounded text-slate-600 font-medium">
              {node.tipo}
            </span>
          )}
        </div>

        <button
          onClick={() => onAddChild(node)}
          type="button"
          className="text-xs text-indigo-600 hover:text-indigo-800 hover:underline px-2 py-1"
        >
          + Agregar Subnivel
        </button>
      </div>

      {hasChildren && isExpanded && (
        <div className="mt-1">
          {node.children.map((child) => (
            <TreeNode key={child.codigo} node={child} onAddChild={onAddChild} level={level + 1} />
          ))}
        </div>
      )}
    </div>
  );
};

export const CatalogoJerarquicoTree = ({ data, onAddNode }) => {
  return (
    <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm">
      <div className="p-4 bg-slate-50 border-b border-slate-200 flex justify-between items-center">
        <h3 className="text-sm font-semibold text-slate-800">Estructura JerÃ¡rquica del CatÃ¡logo</h3>
        <button
          onClick={() => onAddNode(null)}
          type="button"
          className="bg-indigo-600 hover:bg-indigo-700 text-white text-xs font-semibold px-3 py-1.5 rounded-lg transition-colors"
        >
          + Nueva RaÃ­z
        </button>
      </div>
      <div className="p-4 space-y-1 max-h-[600px] overflow-y-auto">
        {data.length === 0 ? (
          <p className="text-sm text-slate-400 text-center py-8">No hay registros definidos en este catÃ¡logo.</p>
        ) : (
          data.map((rootNode) => (
            <TreeNode key={rootNode.codigo} node={rootNode} onAddChild={onAddNode} />
          ))
        )}
      </div>
    </div>
  );
};