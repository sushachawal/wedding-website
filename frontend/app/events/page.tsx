'use client';

import { useEffect, useState } from 'react';
import api from '@/lib/api';
import { Event } from '@/lib/types';
import EventCard from '@/components/EventCard';
import { useAuth } from '@/context/AuthContext';

export default function EventsPage() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    const fetchEvents = async () => {
      if (!user) {
        setError('Please log in to view events.');
        setLoading(false);
        return;
      }
      try {
        const response = await api.get('/api/events');
        setEvents(response.data);
      } catch (err) {
        setError('Failed to fetch events.');
      }
      setLoading(false);
    };

    fetchEvents();
  }, [user]);

  if (loading) return <p>Loading events...</p>;
  if (error) return <p className="text-red-500">{error}</p>;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Our Events</h1>
      {events.length > 0 ? (
        events.map(event => <EventCard key={event.id} event={event} />)
      ) : (
        <p>No events found.</p>
      )}
    </div>
  );
}
