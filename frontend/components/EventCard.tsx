import React from 'react';
import { Event } from '@/lib/types';

interface EventCardProps {
  event: Event;
}

const EventCard: React.FC<EventCardProps> = ({ event }) => {
  return (
    <div className="bg-white shadow-lg rounded-lg overflow-hidden mb-6">
      <div className="p-6">
        <h2 className="text-2xl font-bold mb-2">{event.name}</h2>
        <p className="text-gray-700 mb-4">{event.description}</p>
        <div className="text-sm text-gray-600">
          <p><span className="font-semibold">Date:</span> {new Date(event.date).toLocaleString()}</p>
          <p><span className="font-semibold">Location:</span> {event.location}</p>
        </div>
      </div>
    </div>
  );
};

export default EventCard;
