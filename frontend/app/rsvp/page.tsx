'use client';

import { useEffect, useState } from 'react';
import api from '@/lib/api';
import { Event, Rsvp, RsvpStatus } from '@/lib/types';
import RsvpForm from '@/components/RsvpForm';
import { useAuth } from '@/context/AuthContext';

export default function RsvpPage() {
  const [events, setEvents] = useState<Event[]>([]);
  const [rsvps, setRsvps] = useState<Rsvp[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      if (!user) {
        setError('Please log in to RSVP.');
        setLoading(false);
        return;
      }
      try {
        const [eventsRes, rsvpsRes] = await Promise.all([
          api.get('/api/events'),
          api.get('/api/rsvps'),
        ]);
        setEvents(eventsRes.data);
        setRsvps(rsvpsRes.data);
      } catch (err) {
        setError('Failed to fetch data.');
      }
      setLoading(false);
    };

    fetchData();
  }, [user]);

  const handleRsvpSubmit = async (eventId: number, rsvpData: { status: RsvpStatus; notes: string }) => {
    try {
      const response = await api.post('/api/rsvps', { ...rsvpData, eventId });
      // Update the local state with the new RSVP
      setRsvps(prevRsvps => {
        const otherRsvps = prevRsvps.filter(r => r.eventId !== eventId);
        return [...otherRsvps, response.data];
      });
    } catch (err) {
      setError('Failed to submit RSVP.');
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-red-500">{error}</p>;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">RSVP</h1>
      <div className="space-y-8">
        {events.map(event => {
          const currentRsvp = rsvps.find(rsvp => rsvp.eventId === event.id) || null;
          return (
            <div key={event.id} className="bg-white p-6 rounded-lg shadow-md">
              <h2 className="text-2xl font-bold mb-2">{event.name}</h2>
              <p className="text-gray-600 mb-4">{event.description}</p>
              <RsvpForm
                eventId={event.id}
                currentRsvp={currentRsvp}
                onSubmit={(data) => handleRsvpSubmit(event.id, data)}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
}
