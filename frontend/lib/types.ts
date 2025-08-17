export interface Event {
  id: number;
  name: string;
  description: string;
  date: string; // ISO date string
  location: string;
}

export type RsvpStatus = 'ATTENDING' | 'MAYBE' | 'NOT_ATTENDING' | 'PENDING';

export interface Rsvp {
  id: number;
  guestId: number;
  eventId: number;
  eventName: string;
  status: RsvpStatus;
  plusOnes: number;
  comments: string;
  notes?: string;
}

export interface SiteConfig {
  siteTitle: string;
  welcomeMessage: string;
  galleryEnabled: boolean;
  rsvpEnabled: boolean;
  registryInfo: string;
}

export interface AuthUser {
  email: string;
  role: 'ADMIN' | 'USER';
}
