import './App.css'
import Login from "./pages/Login";
import Principal from "./pages/Principal";

function App() {
  return (
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/usuarios" element={<Usuarios />} />
        <Route path="/principal" element={<Principal />} />
      </Routes>
  );
}

export default App;