'use client';

import React, { createContext, useState, useEffect, useContext } from 'react';
import { AuthUser } from '@/lib/types';

interface AuthContextType {
  user: AuthUser | null;
  login: (email: string, password: string) => void;
  logout: () => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const email = localStorage.getItem('email');
    const auth = localStorage.getItem('auth');
    if (email && auth) {
      setUser({ email, role: 'USER' });
    }
    setLoading(false);
  }, []);

  const login = (email: string, password: string) => {
    const auth = btoa(`${email}:${password}`);
    localStorage.setItem('auth', auth);
    localStorage.setItem('email', email);
    setUser({ email, role: 'USER' });
  };

  const logout = () => {
    localStorage.removeItem('auth');
    localStorage.removeItem('email');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
