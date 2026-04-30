import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import Login from "./pages/Login";


function App() {
  const [count, setCount] = useState(0)
  const [seccion, setSeccion] = useState("Inicio");
  const [pantalla, setPantalla] = useState("login");
  return (
    <>
      {pantalla === "login" ? (
        <Login volver={() => setPantalla("app")} />
      ) : (
        <div className="container">

          {/* HEADER */}
          <header className="header">
            <h1 className="logo">Farmacéutica FarmaCore</h1>
          </header>

          {/* MAIN */}
          <div className="main">

            <div className="content">
              <h2>{seccion}</h2>
              <p>
                Bienvenido a la sección de <strong>{seccion}</strong>
              </p>
            </div>

            <aside className="sidebar">
              <h3>Opciones</h3>
              <ul>
                <li onClick={() => setSeccion("Inicio")}>Inicio</li>
                <li onClick={() => setSeccion("Medicamentos")}>Medicamentos</li>
                <li onClick={() => setSeccion("Pedidos")}>Pedidos</li>
                <li onClick={() => setSeccion("Clientes")}>Clientes</li>
              </ul>
            </aside>

          </div>
        </div>
      )}
    </>
  )

}

export default App
