import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { Home, FileText, CreditCard, User, Wallet } from "lucide-react";

const Menubar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation(); // para saber a rota atual

  // função para determinar a cor do botão
  const getColor = (path: string) => (location.pathname === path ? '#2563eb' : '#6b7280');

  
  return (
    <div style={{
      position: 'fixed',
      bottom: 0,
      left: 0,
      right: 0,
      borderTopRightRadius: '16px',
      borderTopLeftRadius: '16px',
      backgroundColor: 'white',
      boxShadow: '0 -2px 10px rgba(0, 0, 0, 0.1)',
      borderTop: '1px solid #e5e7eb',
      display: 'flex',
      justifyContent: 'space-around',
      alignItems: 'center',
      padding: '28px 0',
      zIndex: 1000
    }}>
      <button 
        onClick={() => navigate("/home")}
        style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        color: getColor("/home"),
        background: 'none',
        border: 'none',
        cursor: 'pointer',
        padding: '4px'
      }}>
        <Home size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Início</span>
      </button>

      <button 
        onClick={() => navigate("/extrato")}
        style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        color: getColor("/extrato"),
        background: 'none',
        border: 'none',
        cursor: 'pointer',
        padding: '4px'
      }}>
        <FileText size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Extrato</span>
      </button>

      <button style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        color: '#6b7280',
        background: 'none',
        border: 'none',
        cursor: 'pointer',
        padding: '4px'
      }}>
        <Wallet size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Conta</span>
      </button>

      <button style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        color: '#6b7280',
        background: 'none',
        border: 'none',
        cursor: 'pointer',
        padding: '4px'
      }}>
        <CreditCard size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Cartões</span>
      </button>

      <button style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        color: '#6b7280',
        background: 'none',
        border: 'none',
        cursor: 'pointer',
        padding: '4px'
      }}>
        <User size={22} />
        <span style={{ fontSize: '12px', marginTop: '4px' }}>Perfil</span>
      </button>
    </div>
  );
};


export default Menubar;

