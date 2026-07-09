import Navbar  from './Navbar';
import Topbar  from './Topbar';

export default function MainLayout({ children }) {
  return (
    <div className="flex flex-col h-screen bg-slate-50 font-sans overflow-hidden">
      <Navbar />
      <Topbar />
      <main className="flex-1 overflow-y-auto p-8">
        {children}
      </main>
    </div>
  );
}