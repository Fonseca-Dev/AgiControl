import React, { createContext, useContext } from 'react';
import type { ReactNode } from 'react';

interface TipoTransacaoContextType {
  renderIcon: (iconType: string) => JSX.Element;
}

const TipoTransacaoContext = createContext<TipoTransacaoContextType | undefined>(undefined);

export const useTipoTransacao = () => {
  const context = useContext(TipoTransacaoContext);
  if (context === undefined) {
    throw new Error('useTipoTransacao deve ser usado dentro de um TipoTransacaoProvider');
  }
  return context;
};

interface TipoTransacaoProviderProps {
  children: ReactNode;
}

export const TipoTransacaoProvider: React.FC<TipoTransacaoProviderProps> = ({ children }) => {
  
  // Função para renderizar o ícone baseado no tipo
  const renderIcon = (iconType: string): JSX.Element => {
    const iconStyle = {
      width: '24px',
      height: '24px',
      color: '#000000'
    };

    switch (iconType) {
      case 'cafe':
        return (
          <svg style={iconStyle} viewBox="0 0 24 24" fill="currentColor">
            <path d="M2 21v-1h20v1H2zM20 8h-2V5h2m0-2H4v10a4 4 0 0 0 4 4h6a4 4 0 0 0 4-4V8a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2z"/>
          </svg>
        );
      case 'mercado':
        return (
          <svg style={iconStyle} viewBox="0 0 24 24" fill="currentColor">
            <path d="M17 18a2 2 0 0 1 2 2a2 2 0 0 1-2 2a2 2 0 0 1-2-2c0-1.11.89-2 2-2M1 2h3.27l.94 2H20a1 1 0 0 1 1 1c0 .17-.05.34-.12.5l-3.58 6.49c-.34.61-1 1.01-1.75 1.01H8.1l-.9 1.63l-.03.12A.25.25 0 0 0 7.42 15H19v2H7a2 2 0 0 1-2-2c0-.35.09-.68.24-.96l1.36-2.45L3 4H1V2m6 16a2 2 0 0 1 2 2a2 2 0 0 1-2 2a2 2 0 0 1-2-2c0-1.11.89-2 2-2z"/>
          </svg>
        );
      case 'farmacia':
        return (
          <svg style={iconStyle} viewBox="0 0 24 24" fill="currentColor">
            <path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
          </svg>
        );
      case 'cifrao':
        return (
          <svg style={iconStyle} viewBox="0 0 24 24" fill="currentColor">
            <path d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z"/>
          </svg>
        );
      case 'pix':
        return (
          <svg style={iconStyle} viewBox="0 0 24 24" fill="currentColor">
            <path d="M11.8 10.9c-2.27-.59-3-1.2-3-2.15 0-1.09 1.01-1.85 2.7-1.85 1.78 0 2.44.85 2.5 2.1h2.21c-.07-1.72-1.12-3.3-3.21-3.81V3h-3v2.16c-1.94.42-3.5 1.68-3.5 3.61 0 2.31 1.91 3.46 4.7 4.13 2.5.6 3 1.48 3 2.41 0 .69-.49 1.79-2.7 1.79-2.06 0-2.87-.92-2.98-2.1h-2.2c.12 2.19 1.76 3.42 3.68 3.83V21h3v-2.15c1.95-.37 3.5-1.5 3.5-3.55 0-2.84-2.43-3.81-4.7-4.4z"/>
          </svg>
        );
      default:
        return (
          <svg style={iconStyle} viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2z"/>
          </svg>
        );
    }
  };

  return (
    <TipoTransacaoContext.Provider value={{ renderIcon }}>
      {children}
    </TipoTransacaoContext.Provider>
  );
};