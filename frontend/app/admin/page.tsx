'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import api from '@/lib/api';
import { SiteConfig } from '@/lib/types';
import { useAuth } from '@/context/AuthContext';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';

export default function AdminPage() {
  const { user, loading: authLoading } = useAuth();
  const router = useRouter();
  const [config, setConfig] = useState<SiteConfig | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (authLoading) return;
    if (!user || user.role !== 'ADMIN') {
      router.push('/login');
      return;
    }

    const fetchConfig = async () => {
      try {
        const response = await api.get('/api/admin/config');
        setConfig(response.data);
      } catch (err) {
        setError('Failed to fetch site configuration.');
      }
      setLoading(false);
    };

    fetchConfig();
  }, [user, authLoading, router]);

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!config) return;

    try {
      await api.put('/api/admin/config', config);
      alert('Configuration updated successfully!');
    } catch (err) {
      setError('Failed to update configuration.');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    if (!config) return;
    const { name, value, type } = e.target;
    const checked = (e.target as HTMLInputElement).checked;
    setConfig({
      ...config,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  if (authLoading || loading) return <p>Loading...</p>;
  if (error) return <p className="text-red-500">{error}</p>;
  if (!config) return <p>No configuration found.</p>;

  return (
    <div className="max-w-2xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Admin - Site Configuration</h1>
      <form onSubmit={handleUpdate} className="space-y-6">
        <div>
          <label htmlFor="siteTitle" className="block text-sm font-medium text-gray-700">Site Title</label>
          <Input id="siteTitle" name="siteTitle" value={config.siteTitle} onChange={handleChange} />
        </div>
        <div>
          <label htmlFor="welcomeMessage" className="block text-sm font-medium text-gray-700">Welcome Message</label>
          <textarea id="welcomeMessage" name="welcomeMessage" value={config.welcomeMessage} onChange={handleChange} rows={4} className="mt-1 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md" />
        </div>
        <div className="flex items-center">
          <input id="galleryEnabled" name="galleryEnabled" type="checkbox" checked={config.galleryEnabled} onChange={handleChange} className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded" />
          <label htmlFor="galleryEnabled" className="ml-2 block text-sm text-gray-900">Enable Gallery</label>
        </div>
        <div className="flex items-center">
          <input id="rsvpEnabled" name="rsvpEnabled" type="checkbox" checked={config.rsvpEnabled} onChange={handleChange} className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded" />
          <label htmlFor="rsvpEnabled" className="ml-2 block text-sm text-gray-900">Enable RSVP</label>
        </div>
        <div>
          <label htmlFor="registryInfo" className="block text-sm font-medium text-gray-700">Registry Info</label>
          <Input id="registryInfo" name="registryInfo" value={config.registryInfo} onChange={handleChange} />
        </div>
        <Button type="submit">Save Configuration</Button>
      </form>
    </div>
  );
}
