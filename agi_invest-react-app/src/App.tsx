import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './App/Home/Home';
import Login from './App/Login/Login';
import Signup from './App/Signup/Signup';
import Extract from './App/Extrato/Extract';
import { SaldoProvider } from './contexts/SaldoContext';
import { TransacaoProvider } from './contexts/TransacaoContext';
import { TipoTransacaoProvider } from './contexts/TipoTransacaoContext';
import './App.css';

function App() {
  return (
    <div className="app-container">
      <SaldoProvider>
        <TransacaoProvider>
          <TipoTransacaoProvider>
            <Router>
              <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/login" element={<Login />} />
                <Route path="/signup" element={<Signup />} />
                <Route path="/home" element={<Home />} />
                <Route path="/extrato" element={<Extract />} />
              </Routes>
            </Router>
          </TipoTransacaoProvider>
        </TransacaoProvider>
      </SaldoProvider>
    </div>
  );
}

export default App;

