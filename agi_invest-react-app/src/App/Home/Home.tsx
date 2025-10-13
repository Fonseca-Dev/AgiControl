import React, { useEffect, useState } from "react";
import {
  Eye,
  EyeOff,
  RefreshCcw,
  Bell,
  MessageCircle,
  Settings,
} from "lucide-react";
import { useNavigate } from "react-router-dom";
import Menubar from "../Menubar/Menubar";

const Home: React.FC = () => {
  const navigate = useNavigate();
  const [showBalance, setShowBalance] = React.useState(false);
  const [saldo, setSaldo] = useState<number | null>(null);

  const [userAvatar] = React.useState<string | null>(() => { 
    return localStorage.getItem("userAvatar") || null; 
  }); 
  
  const [userName] = React.useState<string>(() => { 
    return localStorage.getItem("userName") || "Usuário"; 
  });

  useEffect(() => {
    const usuarioId = localStorage.getItem("userID");

    if (usuarioId) {
      fetch(`https://sistema-gastos-694972193726.southamerica-east1.run.app/usuarios/${usuarioId}/contas`)
        .then(res => {
          if (!res.ok) {
            throw new Error("Erro ao buscar contas");
          }
          return res.json();
        })
        .then(data => {
          if (data && data.objeto && data.objeto.length > 0) {
            // pega a ÚLTIMA conta do array
            const ultimaConta = data.objeto[data.objeto.length - 1];
            setSaldo(ultimaConta.saldo);
          }
        })
        .catch(err => {
          console.error("Erro ao buscar saldo:", err);
        });
    }
  }, []);

  const handleProfileClick = () => {
    navigate("/login");
  };

  return (
    <>
      <div
        style={
          {
             display: "flex",
            flexDirection: "column",
            minHeight: "100vh",
            overflowY: "auto",
            boxSizing: "border-box",
          } as React.CSSProperties
        }
      >
        {/* Header/card azul */}
        <div
          style={{
            position: "fixed",
            top: "0px",
            bottom: "0px",
            width: "393px",
            backgroundColor: "#2563eb",
            color: "white",
            padding: "16px",
            boxShadow: "0 4px 6px -1px rgba(0, 0, 0, 0.1)",
            zIndex: 1000,
          }}
        >
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              marginBottom: "32px",
              marginTop: "20px",
            }}
          >
            <Settings size={26} />
            <div style={{ display: "flex", gap: "22px" }}>
              <MessageCircle size={26} />
              <Bell size={26} />
            </div>
          </div>

          {/* Perfil */}
          <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
            <button
              onClick={handleProfileClick}
              style={{
                width: "48px",
                height: "48px",
                borderRadius: "50%",
                border: "2px solid white",
                backgroundColor: userAvatar ? "transparent" : "#1e40af",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontSize: "20px",
                fontWeight: "bold",
                cursor: "pointer",
                color: "white",
                overflow: "hidden",
                padding: 0,
              }}
            >
              {userAvatar ? (
                <img
                  src={userAvatar}
                  alt="Avatar do usuário"
                  style={{
                    width: "100%",
                    height: "100%",
                    objectFit: "cover",
                    objectPosition: "center",
                  }}
                />
              ) : (
                userName.charAt(0).toUpperCase()
              )}
            </button>
            <p style={{ fontSize: "18px", fontWeight: "500", margin: 0 }}>
              Olá, {userName}!
            </p>
          </div>

          {/* Saldo */}
          <div style={{ marginTop: "0px", position: "relative" }}>
            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: "12px",
                flexWrap: "nowrap",
              }}
            >
              <div
                style={{
                  fontSize: "32px",
                  fontWeight: "bold",
                  margin: 0,
                  lineHeight: "1.2",
                  color: "white",
                  fontFamily: "system-ui, -apple-system, sans-serif",
                  whiteSpace: "nowrap",
                  display: "inline-block",
                }}
              >
                {showBalance
                  ? "•••••"
                  : saldo !== null && saldo !== undefined
                  ? saldo.toLocaleString("pt-BR", {
                      style: "currency",
                      currency: "BRL",
                    })
                  : "Carregando..."}
              </div>
              <button
                onClick={() => setShowBalance(!showBalance)}
                style={{
                  background: "none",
                  border: "none",
                  color: "white",
                  cursor: "pointer",
                  display: "flex",
                  alignItems: "center",
                }}
              >
                {showBalance ? <EyeOff size={22} /> : <Eye size={22} />}
              </button>
            </div>
            <button
              onClick={() => navigate("/extrato")}
              style={{
                fontSize: "14px",
                textDecoration: "underline",
                background: "none",
                border: "none",
                color: "white",
                cursor: "pointer",
                position: "absolute",
                left: "170px",
                top: "50px",
              }}
            >
              Ver extrato
            </button>
          </div>
        </div>

        {/* Ações principais */}
        <div
          style={{
            position: "fixed",
            top: "260px",
            left: "16px",
            right: "16px",
            zIndex: 1002,
            display: "flex",
            justifyContent: "space-between",
            gap: "8px",
          }}
        >
          {/* Pix */}
          <button
            style={{
              backgroundColor: "white",
              color: "#2563eb",
              fontWeight: "500",
              padding: "12px",
              borderRadius: "12px",
              border: "none",
              cursor: "pointer",
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: "8px",
              minHeight: "80px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
            }}
          >
            <svg
              style={{ width: "24px", height: "24px" }}
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z" />
            </svg>
            Pix
          </button>

          {/* Pagar conta */}
          <button
            style={{
              backgroundColor: "white",
              color: "#2563eb",
              fontWeight: "500",
              padding: "12px",
              borderRadius: "12px",
              border: "none",
              cursor: "pointer",
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: "8px",
              minHeight: "80px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
            }}
          >
            <svg
              style={{ width: "24px", height: "24px" }}
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M14 2H6c-1.1 0-1.99.9-1.99 2L4 20c0 1.1.89 2 2 2h16c1.1 0 2-.9 2-2V8l-6-6zm2 16H8v-2h8v2zm0-4H8v-2h8v2zm-3-5V3.5L18.5 9H13z" />
            </svg>
            Pagar conta
          </button>

          {/* Empréstimos */}
          <button
            style={{
              backgroundColor: "white",
              color: "#2563eb",
              fontWeight: "500",
              padding: "12px",
              borderRadius: "12px",
              border: "none",
              cursor: "pointer",
              flex: 1,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: "8px",
              minHeight: "80px",
              boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
            }}
          >
            <svg
              style={{ width: "24px", height: "24px" }}
              fill="currentColor"
              viewBox="0 0 24 24"
            >
              <path d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z" />
            </svg>
            Empréstimos
          </button>
        </div>

        {/* Espaçador */}
        <div style={{ height: "200px" }}></div>

        {/* Card branco fixo no bottom */}
        <div
          style={{
            position: "fixed",
            left: "0px",
            right: "0px",
            top: "310px",
            bottom: "0px",
            height: "500px",
            borderTopRightRadius: "16px",
            borderTopLeftRadius: "16px",
            width: "100%",
            backgroundColor: "white",
            zIndex: 1001,
            display: "flex",
            flexDirection: "column",
          }}
        >
          {/* Container com scroll */}
          <div
            style={{
              padding: "24px",
              paddingBottom: "100px",
              overflowY: "auto",
              flex: 1,
            }}
          >
            {/* Seção de erro */}
            <div
              style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                textAlign: "center",
                color: "#6b7280",
                paddingTop: "30px",
                padding: "6px 0",
                borderBottom: "1px solid #e5e7eb",
                marginTop: "20px",
                marginBottom: "24px",
                boxShadow: "0 4px 5px rgba(0, 0, 0, 0.06)",
              }}
            >
              <svg
                style={{ height: "8px", color: "#9ca3af", marginBottom: "12px" }}
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              ></svg>
              <p style={{ margin: "0 0 8px 0" }}>
                Algum problema impediu o carregamento desse conteúdo.
              </p>
              <button
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  gap: "8px",
                  color: "#2563eb",
                  background: "none",
                  border: "none",
                  cursor: "pointer",
                  fontWeight: "500",
                  margin: "0 auto",
                }}
              >
                <RefreshCcw size={16} />
                Tentar novamente
              </button>
            </div>

            {/* Atalhos de serviços */}
            <div
              style={{
                display: "grid",
                gridTemplateColumns: "repeat(3, 1fr)",
                gap: "16px",
                textAlign: "center",
                fontSize: "14px",
                fontWeight: "500",
                color: "#2563eb",
              }}
            >
              <div>
                <div
                  style={{
                    backgroundColor: "white",
                    padding: "12px",
                    borderRadius: "50%",
                    width: "48px",
                    height: "48px",
                    margin: "0 auto 8px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <svg
                    style={{ width: "20px", height: "20px", color: "#2563eb" }}
                    fill="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z" />
                  </svg>
                </div>
                Crédito do Trabalhador
              </div>

              <div>
                <div
                  style={{
                    backgroundColor: "white",
                    padding: "12px",
                    borderRadius: "50%",
                    width: "48px",
                    height: "48px",
                    margin: "0 auto 8px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <span
                    style={{
                      color: "#2563eb",
                      fontWeight: "bold",
                      fontSize: "18px",
                    }}
                  >
                    $
                  </span>
                </div>
                Empréstimo FGTS
              </div>

              <div>
                <div
                  style={{
                    backgroundColor: "white",
                    padding: "12px",
                    borderRadius: "50%",
                    width: "48px",
                    height: "48px",
                    margin: "0 auto 8px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <svg
                    style={{ width: "20px", height: "20px", color: "#2563eb" }}
                    fill="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path d="M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11zM7 10h5v5H7z" />
                  </svg>
                </div>
                Antecipação 13º INSS
              </div>

              <div>
                <div
                  style={{
                    backgroundColor: "white",
                    padding: "12px",
                    borderRadius: "50%",
                    width: "48px",
                    height: "48px",
                    margin: "0 auto 8px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <span
                    style={{
                      color: "#2563eb",
                      fontWeight: "bold",
                      fontSize: "18px",
                    }}
                  >
                    $
                  </span>
                </div>
                Consignado INSS
              </div>

              <div>
                <div
                  style={{
                    backgroundColor: "white",
                    padding: "12px",
                    borderRadius: "50%",
                    width: "48px",
                    height: "48px",
                    margin: "0 auto 8px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <svg
                    style={{ width: "20px", height: "20px", color: "#2563eb" }}
                    fill="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
                  </svg>
                </div>
                Portabilidade Consig. INSS
              </div>

              <div>
                <div
                  style={{
                    backgroundColor: "white",
                    padding: "12px",
                    borderRadius: "50%",
                    width: "48px",
                    height: "48px",
                    margin: "0 auto 8px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    boxShadow: "0 2px 8px rgba(0, 0, 0, 0.1)",
                  }}
                >
                  <span
                    style={{
                      color: "#2563eb",
                      fontWeight: "bold",
                      fontSize: "18px",
                    }}
                  >
                    $
                  </span>
                </div>
                Empréstimo Pessoal INSS
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Menubar fixo */}
      <div
        style={{
          position: "fixed",
          bottom: "0px",
          left: "0px",
          right: "0px",
          zIndex: 9999,
        }}
      >
        <Menubar />
      </div>
    </>
  );
};

export default Home;



















