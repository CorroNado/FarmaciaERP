import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
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