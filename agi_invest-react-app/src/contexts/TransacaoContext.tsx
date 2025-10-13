import React, { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';

export interface Transacao {
  id: number;
  tipo: string;
  icon: string;
  horario: string;
  valor: number;
  metodo: string;
  tipoTransacao: 'entrada' | 'saida';
}

interface TransacaoContextType {
  transacoes: Transacao[];
  setTransacoes: (value: Transacao[]) => void;
  adicionarTransacao: (transacao: Omit<Transacao, 'id'>) => void;
  removerTransacao: (id: number) => void;
}

const TransacaoContext = createContext<TransacaoContextType | undefined>(undefined);

export const useTransacao = () => {
  const context = useContext(TransacaoContext);
  if (context === undefined) {
    throw new Error('useTransacao deve ser usado dentro de um TransacaoProvider');
  }
  return context;
};

interface TransacaoProviderProps {
  children: ReactNode;
}

export const TransacaoProvider: React.FC<TransacaoProviderProps> = ({ children }) => {
  // Dados de exemplo das transações
  const [transacoes, setTransacoes] = useState<Transacao[]>([
    {
      id: 1,
      tipo: 'Pix Recebido',
      icon: 'pix',
      horario: '07:45',
      valor: 250.00,
      metodo: 'Pix',
      tipoTransacao: 'entrada'
    },
    {
      id: 2,
      tipo: 'Café da Manhã',
      icon: 'cafe',
      horario: '08:30',
      valor: 12.50,
      metodo: 'Débito',
      tipoTransacao: 'saida'
    },
    {
      id: 3,
      tipo: 'Supermercado',
      icon: 'mercado',
      horario: '14:20',
      valor: 87.90,
      metodo: 'Em 3x',
      tipoTransacao: 'saida'
    },
    {
      id: 4,
      tipo: 'Farmácia',
      icon: 'farmacia',
      horario: '16:45',
      valor: 25.30,
      metodo: 'Pix',
      tipoTransacao: 'saida'
    },
    {
      id: 5,
      tipo: 'Transferência',
      icon: 'cifrao',
      horario: '09:15',
      valor: 150.00,
      metodo: 'Pix',
      tipoTransacao: 'saida'
    },
    {
      id: 6,
      tipo: 'Lanchonete',
      icon: 'cafe',
      horario: '12:30',
      valor: 18.75,
      metodo: 'Débito',
      tipoTransacao: 'saida'
    },
    {
      id: 7,
      tipo: 'Supermercado',
      icon: 'mercado',
      horario: '19:10',
      valor: 145.60,
      metodo: 'Em 2x',
      tipoTransacao: 'saida'
    },
    {
      id: 8,
      tipo: 'Medicamentos',
      icon: 'farmacia',
      horario: '10:00',
      valor: 34.80,
      metodo: 'Pix',
      tipoTransacao: 'saida'
    },
    {
      id: 9,
      tipo: 'Investimento',
      icon: 'cifrao',
      horario: '15:30',
      valor: 200.00,
      metodo: 'Débito',
      tipoTransacao: 'saida'
    }
  ]);

  const adicionarTransacao = (novaTransacao: Omit<Transacao, 'id'>) => {
    const id = Math.max(...transacoes.map(t => t.id), 0) + 1;
    setTransacoes(prev => [...prev, { ...novaTransacao, id }]);
  };

  const removerTransacao = (id: number) => {
    setTransacoes(prev => prev.filter(t => t.id !== id));
  };

  return (
    <TransacaoContext.Provider value={{ 
      transacoes, 
      setTransacoes, 
      adicionarTransacao, 
      removerTransacao 
    }}>
      {children}
    </TransacaoContext.Provider>
  );
};