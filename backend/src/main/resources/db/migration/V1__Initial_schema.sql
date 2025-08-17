CREATE TABLE guests (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  email TEXT UNIQUE NOT NULL,
  first_name TEXT,
  last_name TEXT,
  nickname TEXT,
  relation TEXT,
  role TEXT NOT NULL DEFAULT 'GUEST',
  profile_photo_url TEXT,
  password_hash TEXT,
  is_admin BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE invite_codes (
  id INTEGER PRIMARY KEY,
  guest_id INTEGER NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
  code_hash TEXT NOT NULL,
  expires_at TIMESTAMP,
  status TEXT NOT NULL DEFAULT 'active', -- active/revoked/consumed/expired
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE events (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  description TEXT,
  date TIMESTAMP NOT NULL,
  location TEXT,
  dress_code TEXT,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE event_invitees (
  event_id INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  guest_id INTEGER NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
  PRIMARY KEY (event_id, guest_id)
);

CREATE TABLE rsvps (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  guest_id INTEGER NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
  event_id INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE,
  status TEXT NOT NULL, -- attending/declined/pending
  plus_ones INTEGER DEFAULT 0,
  comments TEXT,
  notes TEXT,
  updated_at TIMESTAMP NOT NULL,
  UNIQUE (guest_id, event_id)
);

CREATE TABLE site_config (
  id INTEGER PRIMARY KEY CHECK (id = 1),
  site_title TEXT,
  welcome_message TEXT,
  gallery_enabled BOOLEAN,
  rsvp_enabled BOOLEAN,
  registry_info TEXT,
  -- max_concurrent_sessions INTEGER NOT NULL DEFAULT 2,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE TABLE sessions (
  id TEXT PRIMARY KEY,
  guest_id INTEGER NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
  created_at TIMESTAMP NOT NULL,
  last_seen_at TIMESTAMP NOT NULL,
  user_agent TEXT,
  ip_hash TEXT
);

CREATE TABLE reset_codes (
  id TEXT PRIMARY KEY,
  guest_id INTEGER NOT NULL REFERENCES guests(id) ON DELETE CASCADE,
  code_hash TEXT NOT NULL,
  expires_at TIMESTAMP,
  status TEXT NOT NULL DEFAULT 'active', -- active/revoked/consumed/expired
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
