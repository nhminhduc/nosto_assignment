'use client';
import React, { useState } from 'react';
import {
  QueryClientProvider,
  useQuery,
  useMutation,
} from '@tanstack/react-query';
import axios from 'axios';
import { queryClient } from '../lib/queryClient';

interface ConversionResult {
  sourceCurrency: string;
  targetCurrency: string;
  originalAmount: number;
  convertedAmount: number;
  formattedResult: string;
}

const getRates = async (): Promise<string[]> => {
  const response = await axios.get('http://localhost:8080/api/currencies');
  return response.data;
};

const CurrencyConverter: React.FC = () => {
  const [sourceCurrency, setSourceCurrency] = useState<string>('');
  const [targetCurrency, setTargetCurrency] = useState<string>('');
  const [amount, setAmount] = useState<string>('');

  const {
    data: currencyOptions,
    error: currencyError,
    isLoading,
  } = useQuery<string[]>({
    queryKey: ['currencies'],
    queryFn: getRates,
  });

  const convertMutation = useMutation<
    ConversionResult,
    Error,
    { sourceCurrency: string; targetCurrency: string; amount: number }
  >({
    mutationFn: (data) =>
      axios
        .post('http://localhost:8080/api/convert', data)
        .then((res) => res.data),
  });

  const handleConvert = (e: React.FormEvent) => {
    e.preventDefault();
    convertMutation.mutate({
      sourceCurrency,
      targetCurrency,
      amount: parseFloat(amount),
    });
  };

  const handleSwitch = () => {
    setSourceCurrency(targetCurrency);
    setTargetCurrency(sourceCurrency);
  };

  if (isLoading) return <div>Loading currency options...</div>;
  if (currencyError)
    return <div>Error loading currency options. Please try again later.</div>;
  const uniqueCurrencies = Array.from(
    new Set(currencyOptions?.map((currency) => currency))
  );

  return (
    <div className="min-h-screen bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md mx-auto bg-white rounded-lg shadow-md overflow-hidden">
        <div className="px-4 py-5 sm:p-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-6">
            Currency Converter
          </h1>
          <form onSubmit={handleConvert} className="space-y-4">
            <div className="flex items-center space-x-4">
              <div className="flex-1">
                <label
                  htmlFor="sourceCurrency"
                  className="block text-sm font-medium text-gray-700"
                >
                  Source Currency
                </label>
                <select
                  id="sourceCurrency"
                  value={sourceCurrency}
                  onChange={(e) => setSourceCurrency(e.target.value)}
                  required
                  className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                >
                  <option value="">Select</option>
                  {uniqueCurrencies.map((currency) => (
                    <option key={currency} value={currency}>
                      {currency}
                    </option>
                  ))}
                </select>
              </div>
              <button
                type="button"
                onClick={handleSwitch}
                className="mt-6 p-2 bg-gray-100 rounded-full hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  className="h-6 w-6 text-gray-600"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4"
                  />
                </svg>
              </button>
              <div className="flex-1">
                <label
                  htmlFor="targetCurrency"
                  className="block text-sm font-medium text-gray-700"
                >
                  Target Currency
                </label>
                <select
                  id="targetCurrency"
                  value={targetCurrency}
                  onChange={(e) => setTargetCurrency(e.target.value)}
                  required
                  className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
                >
                  <option value="">Select</option>
                  {uniqueCurrencies.map((currency) => (
                    <option key={currency} value={currency}>
                      {currency}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div>
              <label
                htmlFor="amount"
                className="block text-sm font-medium text-gray-700"
              >
                Amount
              </label>
              <input
                type="number"
                id="amount"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                placeholder="Enter amount"
                required
                className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
              />
            </div>
            <div>
              <button
                type="submit"
                disabled={convertMutation.isPending}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
              >
                {convertMutation.isPending ? 'Converting...' : 'Convert'}
              </button>
            </div>
          </form>
          {convertMutation.isError && (
            <p className="mt-4 text-sm text-red-600">
              An error occurred during conversion. Please try again.
            </p>
          )}
          {convertMutation.isSuccess && (
            <div className="mt-6 bg-green-50 border-l-4 border-green-400 p-4">
              <h2 className="text-lg font-medium text-green-800">
                Conversion Result:
              </h2>
              <p className="mt-2 text-green-700">
                {convertMutation.data.formattedResult}
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

const App: React.FC = () => (
  <QueryClientProvider client={queryClient}>
    <CurrencyConverter />
  </QueryClientProvider>
);

export default App;
