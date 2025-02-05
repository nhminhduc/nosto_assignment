import { create } from 'zustand';

interface StoreState {
  supportedCurrencies: string[];
  setSupportedCurrencies: (currencies: string[]) => void;
}

export const useStore = create<StoreState>((set) => ({
  // Define your supported currencies – adjust as needed
  supportedCurrencies: ['USD', 'EUR', 'GBP', 'JPY', 'CAD'],
  setSupportedCurrencies: (currencies) =>
    set({ supportedCurrencies: currencies }),
}));
