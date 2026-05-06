import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'


function App() {
  const [count, setCount] = useState(0)
  const [seccion, setSeccion] = useState("Inicio");
  return (
    <>
      <div className="container">
        {/* HEADER */}
        <header className="header">
          <h1 className="logo">Farmacéutica FarmaCore</h1>
          <div className="login">
            <span className="icon"></span>
            <button>👤 Iniciar sesión</button>
          </div>
        </header>

        {/* MAIN */}
        <div className="main">

          {/* CONTENIDO CENTRAL */}
          <div className="content">
            <h2>{seccion}</h2>
            <p>
              Bienvenido a la sección de <strong>{seccion}</strong>. Aquí irá el contenido.
            </p>
          </div>

          {/* SIDEBAR DERECHA */}
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

    </>
  )
}

export default App
