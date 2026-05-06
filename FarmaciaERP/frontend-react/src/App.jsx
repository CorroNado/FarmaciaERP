import './App.css'
import Login from "./pages/Login";
import Usuarios from "./pages/usuarios";



function App() {
  const path = window.location.pathname;

  if (path === "/usuarios") {
    return <Usuarios />;
  }

  return <Login />;

}

export default App;