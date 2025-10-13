import React, { useState } from "react";
import { ArrowLeft } from "lucide-react";
import { useNavigate } from "react-router-dom";
import backgroundImage from "../../assets/images/background.png";
import { loginPorEmailESenha } from "../../services/usuarioService";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [avatarImage] = useState<string | null>(
    () => localStorage.getItem("userAvatar") || null
  );
  const [userName] = useState<string>(
    () => localStorage.getItem("userName") || "Usuário"
  );

  const [showLoginPopup, setShowLoginPopup] = useState(false);
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [loading, setLoading] = useState(false);

  // 🔹 Função de login com API
  const handleLogin = async (e: React.FormEvent) => {
  e.preventDefault();

  if (!email || !senha) {
    alert("⚠️ Preencha e-mail e senha!");
    return;
  }

  setLoading(true);

  try {
    const resposta = await loginPorEmailESenha({ email, senha });

    // Checando o status retornado pela API
    if (resposta.status === "OK") {
      alert("✅ Login realizado com sucesso!");
      localStorage.setItem("userEmail", email);
      localStorage.setItem("userName", resposta.objeto.nome);
      localStorage.setItem("userID", resposta.objeto.id);
      setShowLoginPopup(false);
      navigate("/home");
    } else {
      alert("❌ E-mail ou senha incorretos!");
    }
  } catch (erro) {
    console.error("Erro ao tentar logar:", erro);
    alert("⚠️ Erro ao conectar com o servidor. Tente novamente.");
  } finally {
    setLoading(false);
  }
};


  const handleQuickLogin = () => setShowLoginPopup(true);
  const handleSignup = () => navigate("/signup");
  const handleBackClick = () => navigate("/");

  return (
    <>
      <style>{`
        .login-input::placeholder {
          color: #9CA3AF !important;
          opacity: 1;
        }
      `}</style>

      <div
        style={{
          position: "absolute",
          top: "0px",
          left: "0px",
          width: "393px",
          height: "852px",
          display: "flex",
          flexDirection: "column",
          overflowY: "auto",
          boxSizing: "border-box",
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          backgroundRepeat: "no-repeat",
          scrollbarWidth: "none",
          msOverflowStyle: "none",
        }}
      >
        {/* Logo */}
        <div style={{ position: "absolute", top: "40px", left: "16px", zIndex: 2 }}>
          <h1
            style={{
              fontFamily: "system-ui, -apple-system, sans-serif",
              fontWeight: 600,
              fontSize: "24px",
              color: "white",
              margin: 0,
            }}
          >
            <span style={{ fontWeight: 600 }}>agi</span>
            <span style={{ fontWeight: 260 }}>Control</span>
          </h1>
        </div>

        {/* Botão voltar */}
        <button
          onClick={handleBackClick}
          style={{
            background: "none",
            border: "none",
            color: "white",
            cursor: "pointer",
            display: "flex",
            alignItems: "center",
            gap: "8px",
            position: "absolute",
            top: "70px",
            right: "99px",
            fontSize: "16px",
            zIndex: 2,
          }}
        >
          <ArrowLeft size={24} />
          Voltar
        </button>

        {/* Avatar */}
        <div
          style={{
            position: "absolute",
            left: "45px",
            top: "50%",
            transform: "translateY(-50%)",
            marginTop: "-40px",
            zIndex: 2,
          }}
        >
          {avatarImage ? (
            <img
              src={avatarImage}
              alt="avatar"
              style={{
                width: "80px",
                height: "80px",
                borderRadius: "50%",
                border: "2px solid white",
                objectFit: "cover",
                boxShadow: "0 4px 12px rgba(0, 0, 0, 0.3)",
              }}
            />
          ) : (
            <div
              style={{
                width: "80px",
                height: "80px",
                borderRadius: "50%",
                border: "2px solid white",
                backgroundColor: "#f0f0f0",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontSize: "40px",
                color: "#999999",
              }}
            >
              👤
            </div>
          )}
        </div>

        {/* Saudação */}
        <div
          style={{
            position: "absolute",
            left: "45px",
            top: "50%",
            transform: "translateY(-50%)",
            marginTop: "30px",
            width: "calc(100% - 32px)",
            zIndex: 2,
          }}
        >
          <h2
            style={{
              fontSize: "24px",
              fontWeight: "bold",
              margin: "0 0 -30px 0",
              color: "white",
            }}
          >
            Que bom te ver de novo,<br />
            {userName}!
          </h2>
        </div>

        <div
          style={{
            position: "absolute",
            left: "45px",
            top: "50%",
            transform: "translateY(-50%)",
            marginTop: "90px",
            width: "calc(100% - 32px)",
            zIndex: 2,
          }}
        >
          <h2
            style={{
              fontSize: "24px",
              fontWeight: "bold",
              margin: "0",
              color: "#CAFC92",
              textAlign: "left",
            }}
          >
            Bora agilizar?
          </h2>
        </div>

        {/* Botões */}
        <div
          style={{
            position: "absolute",
            left: "45px",
            bottom: "30px",
            display: "flex",
            flexDirection: "column",
            gap: "20px",
            width: "calc(100% - 90px)",
            zIndex: 2,
          }}
        >
          <button
            type="button"
            onClick={handleQuickLogin}
            style={{
              width: "100%",
              padding: "16px",
              backgroundColor: "white",
              color: "#2563eb",
              border: "2px solid #2563eb",
              borderRadius: "12px",
              fontSize: "16px",
              fontWeight: "600",
              cursor: "pointer",
            }}
          >
            Entrar
          </button>

          <button
            type="button"
            onClick={handleSignup}
            style={{
              width: "100%",
              padding: "16px",
              backgroundColor: "transparent",
              color: "white",
              border: "2px solid white",
              borderRadius: "12px",
              fontSize: "16px",
              fontWeight: "600",
              cursor: "pointer",
            }}
          >
            Cadastrar-se
          </button>
        </div>

        {/* Pop-up de Login */}
        {showLoginPopup && (
          <div
            style={{
              position: "fixed",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
              backgroundColor: "rgba(0, 0, 0, 0.5)",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              zIndex: 1000,
            }}
          >
            <div
              style={{
                backgroundColor: "white",
                borderRadius: "16px",
                padding: "24px",
                width: "90%",
                maxWidth: "300px",
                boxShadow: "0 10px 25px #0000004d",
              }}
            >
              <h3
                style={{
                  margin: "0 0 20px 0",
                  textAlign: "center",
                  color: "#2563eb",
                  fontSize: "18px",
                  fontWeight: "600",
                }}
              >
                Entrar na conta
              </h3>

              <form
                onSubmit={handleLogin}
                style={{ display: "flex", flexDirection: "column", gap: "16px" }}
              >
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="Digite seu email"
                  required
                  className="login-input"
                  style={{
                    width: "100%",
                    padding: "12px",
                    border: "1px solid #D2D2D2",
                    borderRadius: "120px",
                  }}
                />

                <input
                  type="password"
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  placeholder="Digite sua senha"
                  required
                  className="login-input"
                  style={{
                    width: "100%",
                    padding: "12px",
                    border: "1px solid #D2D2D2",
                    borderRadius: "120px",
                  }}
                />

                <div
                  style={{
                    display: "flex",
                    gap: "12px",
                    marginTop: "8px",
                  }}
                >
                  <button
                    type="button"
                    onClick={() => setShowLoginPopup(false)}
                    style={{
                      flex: 1,
                      padding: "12px",
                      backgroundColor: "transparent",
                      color: "#6b7280",
                      border: "1px solid #D2D2D2",
                      borderRadius: "120px",
                      cursor: "pointer",
                    }}
                  >
                    Cancelar
                  </button>

                  <button
                    type="submit"
                    disabled={loading}
                    style={{
                      flex: 1,
                      padding: "12px",
                      backgroundColor: loading ? "#93c5fd" : "#2563eb",
                      color: "white",
                      border: "none",
                      borderRadius: "120px",
                      cursor: "pointer",
                      fontWeight: 600,
                    }}
                  >
                    {loading ? "Entrando..." : "Entrar"}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </>
  );
};

export default Login;


