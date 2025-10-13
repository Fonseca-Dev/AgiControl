import React, { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import { buscarUltimaConta } from '../services/contaService';

interface SaldoContextType {
  saldo: number;
  setSaldo: (value: number) => void;
  atualizarSaldo: (userId: string) => Promise<void>; // opcional, para atualizar após operações
}

const SaldoContext = createContext<SaldoContextType | undefined>(undefined);

export const useSaldo = () => {
  const context = useContext(SaldoContext);
  if (!context) {
    throw new Error('useSaldo deve ser usado dentro de um SaldoProvider');
  }
  return context;
};

interface SaldoProviderProps {
  children: ReactNode;
}

export const SaldoProvider: React.FC<SaldoProviderProps> = ({ children }) => {
  const [saldo, setSaldo] = useState<number>(0); // Valor inicial do saldo
  // Função que busca o saldo mais recente da conta do usuário
  const atualizarSaldo = async (userId: string) => {
    try {
      const conta = await buscarUltimaConta(userId);
      if (conta?.saldo !== undefined) {
        setSaldo(conta.saldo);
      }
    } catch (error) {
      console.error("Erro ao atualizar saldo:", error);
    }
  };

  // Opcional: Buscar saldo automaticamente se o usuário estiver salvo no localStorage
  useEffect(() => {
    const userData = localStorage.getItem("usuario");
    if (userData) {
      const user = JSON.parse(userData);
      atualizarSaldo(user.id);
    }
  }, []);

  return (
    <SaldoContext.Provider value={{ saldo, setSaldo, atualizarSaldo }}>
      {children}
    </SaldoContext.Provider>
  );
};
