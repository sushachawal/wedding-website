'use client';

import React, { useState } from 'react';
import { Rsvp, RsvpStatus } from '@/lib/types';
import Button from './ui/Button';

interface RsvpFormProps {
  eventId: number;
  currentRsvp: Rsvp | null;
  onSubmit: (rsvpData: { status: RsvpStatus; notes: string }) => void;
}

const RsvpForm: React.FC<RsvpFormProps> = ({ eventId, currentRsvp, onSubmit }) => {
  const [status, setStatus] = useState<RsvpStatus>(currentRsvp?.status || 'PENDING');
  const [notes, setNotes] = useState(currentRsvp?.notes || '');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit({ status, notes });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label htmlFor={`rsvp-status-${eventId}`} className="block text-sm font-medium text-gray-700">
          Your RSVP
        </label>
        <select
          id={`rsvp-status-${eventId}`}
          value={status}
          onChange={(e) => setStatus(e.target.value as RsvpStatus)}
          className="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
        >
          <option value="ATTENDING">Attending</option>
          <option value="DECLINED">Declined</option>
          <option value="PENDING">Pending</option>
        </select>
      </div>
      <div>
        <label htmlFor={`rsvp-notes-${eventId}`} className="block text-sm font-medium text-gray-700">
          Notes (optional)
        </label>
        <textarea
          id={`rsvp-notes-${eventId}`}
          value={notes}
          onChange={(e) => setNotes(e.target.value)}
          rows={3}
          className="mt-1 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
        />
      </div>
      <Button type="submit">Submit RSVP</Button>
    </form>
  );
};

export default RsvpForm;
