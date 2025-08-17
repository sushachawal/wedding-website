'use client';

import Link from 'next/link';
import React from 'react';
import { useAuth } from '@/context/AuthContext';
import Button from '../ui/Button';

const Header: React.FC = () => {
  const { user, logout, loading } = useAuth();
  return (
    <header className="bg-white shadow-md">
      <nav className="container mx-auto px-6 py-4 flex justify-between items-center">
        <Link href="/" className="text-2xl font-bold text-gray-800">
          Our Wedding
        </Link>
        <div className="flex items-center space-x-4">
          <Link href="/events" className="text-gray-600 hover:text-gray-800">
            Events
          </Link>
          <Link href="/rsvp" className="text-gray-600 hover:text-gray-800">
            RSVP
          </Link>
          <Link href="/gallery" className="text-gray-600 hover:text-gray-800">
            Gallery
          </Link>
          {user && user.role === 'ADMIN' && (
            <Link href="/admin" className="text-gray-600 hover:text-gray-800">
              Admin
            </Link>
          )}
          <div className="w-48 text-right">
            {!loading && (
              <>
                {user ? (
                  <div className="flex items-center justify-end space-x-4">
                    <span className="text-sm text-gray-600">{user.email}</span>
                    <Button onClick={logout} variant="secondary" size="sm">
                      Logout
                    </Button>
                  </div>
                ) : (
                  <Link href="/login">
                    <Button variant="primary" size="sm">Login</Button>
                  </Link>
                )}
              </>
            )}
          </div>
        </div>
      </nav>
    </header>
  );
};

export default Header;
