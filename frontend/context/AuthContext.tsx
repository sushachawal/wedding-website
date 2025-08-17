'use client';

import React, { createContext, useState, useEffect, useContext } from 'react';
import api from '@/lib/api';
import { AuthUser } from '@/lib/types';
import { jwtDecode } from 'jwt-decode';

interface AuthContextType {
  user: AuthUser | null;
  login: (token: string) => void;
  logout: () => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const decoded: { sub: string; roles: string[] } = jwtDecode(token);
        setUser({ email: decoded.sub, role: decoded.roles.includes('ROLE_ADMIN') ? 'ADMIN' : 'USER' });
      } catch (error) {
        console.error('Invalid token');
        localStorage.removeItem('token');
      }
    }
    setLoading(false);
  }, []);

  const login = (token: string) => {
    localStorage.setItem('token', token);
    try {
      const decoded: { sub: string; roles: string[] } = jwtDecode(token);
      setUser({ email: decoded.sub, role: decoded.roles.includes('ROLE_ADMIN') ? 'ADMIN' : 'USER' });
    } catch (error) {
      console.error('Invalid token on login');
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
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
