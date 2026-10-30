/**
 * Event Spectra - Frontend Application
 * Interacts with Event Spectra Java Backend Server via REST API
 * Fallback to in-memory mock data if running without backend server.
 */

// Application State
const AppState = {
  backendUrl: window.location.origin.includes('localhost') || window.location.origin.includes('127.0.0.1')
    ? window.location.origin
    : 'http://localhost:8080',
  isBackendConnected: false,
  
  // Default logged in user: Joseph P Subash (Roll No. 48)
  currentUser: {
    userId: 48,
    name: "Joseph P Subash",
    email: "joseph.subash@fisat.ac.in",
    role: "STUDENT",
    roleDescription: "Student (B.Tech CSE, Semester 4)",
    studentId: 48,
    course: "B.Tech Computer Science & Engineering",
    semester: 4,
    phone: "+91 98471 23456"
  },

  // Fallback initial dataset (Matches Java DataStore)
  users: [
    { userId: 48, name: "Joseph P Subash", email: "joseph.subash@fisat.ac.in", role: "STUDENT", roleDescription: "Student (B.Tech CSE, Semester 4)", studentId: 48, course: "B.Tech CSE", semester: 4 },
    { userId: 46, name: "Joanna Jomon", email: "joanna.jomon@fisat.ac.in", role: "STUDENT", roleDescription: "Student (B.Tech CSE, Semester 4)", studentId: 46, course: "B.Tech CSE", semester: 4 },
    { userId: 47, name: "Joel Shantu", email: "joel.shantu@fisat.ac.in", role: "STUDENT", roleDescription: "Student (B.Tech CSE, Semester 4)", studentId: 47, course: "B.Tech CSE", semester: 4 },
    { userId: 49, name: "Joshua Jomesh", email: "joshua.jomesh@fisat.ac.in", role: "STUDENT", roleDescription: "Student (B.Tech CSE, Semester 4)", studentId: 49, course: "B.Tech CSE", semester: 4 },
    { userId: 50, name: "Jovial J Thattil", email: "jovial.thattil@fisat.ac.in", role: "STUDENT", roleDescription: "Student (B.Tech CSE, Semester 4)", studentId: 50, course: "B.Tech CSE", semester: 4 },
    { userId: 101, name: "ACM FISAT Student Chapter", email: "acm@fisat.ac.in", role: "COORDINATOR", roleDescription: "Club Coordinator (FISAT ACM)", organizerId: 201, clubName: "FISAT ACM Chapter" },
    { userId: 102, name: "IEEE SB FISAT", email: "ieee@fisat.ac.in", role: "COORDINATOR", roleDescription: "Club Coordinator (IEEE SB FISAT)", organizerId: 202, clubName: "IEEE SB FISAT" },
    { userId: 103, name: "FOSS Cell FISAT", email: "foss@fisat.ac.in", role: "COORDINATOR", roleDescription: "Club Coordinator (FOSS Cell)", organizerId: 203, clubName: "FOSS Cell FISAT" },
    { userId: 1, name: "FISAT Events Administrator", email: "admin@fisat.ac.in", role: "ADMIN", roleDescription: "Dean & Admin", department: "Dean of Academics" }
  ],

  events: [
    {
      eventId: 101,
      title: "HackSpectra 2026: 24-Hour AI & Web3 Hackathon",
      description: "Flagship hackathon of FISAT. Build groundbreaking AI agents, decentralized applications, and smart campus tools within 24 hours.",
      date: "2026-11-14",
      time: "09:00 AM",
      venue: "Central Computing Facility (CCF)",
      category: "Hackathon",
      capacity: 120,
      registeredCount: 45,
      organizerClub: "FISAT ACM Chapter",
      status: "Upcoming",
      isHackathon: true,
      theme: "Autonomous AI & Web3 Innovation",
      duration: "24 Hours",
      teamSize: 4
    },
    {
      eventId: 102,
      title: "Workshop on Deep Learning & LLMs with PyTorch",
      description: "Hands-on workshop exploring prompt engineering, fine-tuning local models, and deploying intelligent assistants on GPU clusters.",
      date: "2026-11-05",
      time: "10:00 AM",
      venue: "Auditorium 2, APJ Block",
      category: "Workshop",
      capacity: 80,
      registeredCount: 68,
      organizerClub: "IEEE SB FISAT",
      status: "Upcoming",
      isHackathon: false
    },
    {
      eventId: 103,
      title: "TechFest Inaugural Keynote & Campus Project Expo",
      description: "Kick-off ceremony for the Annual Engineering Expo featuring innovative student prototypes and industry keynote speech.",
      date: "Today",
      time: "02:00 PM",
      venue: "Main Auditorium",
      category: "Technical",
      capacity: 250,
      registeredCount: 190,
      organizerClub: "FISAT ACM Chapter",
      status: "Today",
      isHackathon: false
    },
    {
      eventId: 104,
      title: "Algorhythm: State-Level Competitive Coding Contest",
      description: "Speed coding duel testing algorithmic prowess, dynamic programming, and data structure speed. Exciting cash prizes!",
      date: "2026-11-20",
      time: "01:30 PM",
      venue: "Software Lab 3",
      category: "Competition",
      capacity: 60,
      registeredCount: 34,
      organizerClub: "CSI FISAT Chapter",
      status: "Upcoming",
      isHackathon: false
    },
    {
      eventId: 105,
      title: "Linux Kernel & Git Mastery Bootcamp",
      description: "Comprehensive hands-on session on Linux command line, kernel compilation basics, and collaborative Git workflows.",
      date: "2026-11-28",
      time: "11:00 AM",
      venue: "Seminar Hall 1",
      category: "Workshop",
      capacity: 50,
      registeredCount: 28,
      organizerClub: "FOSS Cell FISAT",
      status: "Upcoming",
      isHackathon: false
    }
  ],

  registrations: [
    { registrationId: 501, studentId: 48, eventId: 101, registrationDate: "2026-10-25 14:30", status: "Confirmed" },
    { registrationId: 502, studentId: 48, eventId: 102, registrationDate: "2026-10-26 10:15", status: "Confirmed" },
    { registrationId: 503, studentId: 46, eventId: 101, registrationDate: "2026-10-26 11:00", status: "Confirmed" },
    { registrationId: 504, studentId: 47, eventId: 103, registrationDate: "2026-10-27 09:30", status: "Confirmed" }
  ],

  certificates: [
    {
      certificateId: 801,
      studentId: 48,
      eventId: 103,
      studentName: "Joseph P Subash",
      eventTitle: "TechFest Inaugural Keynote & Campus Project Expo",
      issueDate: "2026-10-30",
      certificateUrl: "/certificates/cert-801.pdf",
      certificateType: "Certificate of Participation"
    },
    {
      certificateId: 802,
      studentId: 48,
      eventId: 100,
      studentName: "Joseph P Subash",
      eventTitle: "National Web Development Hackathon 2025",
      issueDate: "2026-09-15",
      certificateUrl: "/certificates/cert-802.pdf",
      certificateType: "Certificate of Merit (First Runner-Up)"
    }
  ],

  notifications: [
    { notificationId: 301, userId: 0, message: "Welcome to Event Spectra! Centralized event portal for FISAT students.", dateTime: "2026-10-28 09:00", isRead: true },
    { notificationId: 302, userId: 48, message: "Reminder: TechFest Inaugural Keynote is scheduled for Today at 02:00 PM in Main Auditorium.", dateTime: "2026-10-30 08:00", isRead: false },
    { notificationId: 303, userId: 48, message: "Your registration for 'HackSpectra 2026' has been confirmed by FISAT ACM Chapter.", dateTime: "2026-10-29 17:45", isRead: true }
  ],

  reminders: [
    { reminderId: 401, userId: 48, eventId: 101, eventTitle: "HackSpectra 2026: 24-Hour AI & Web3 Hackathon", reminderTime: "2026-11-14 08:30 AM", message: "Report to CCF with laptop and college ID card.", active: true }
  ],

  activeFilter: "ALL",
  searchQuery: "",
  selectedEventForModal: null,
  activeCertificate: null
};

// LocalStorage Helper for Cloud / Vercel deployment
function saveToLocalStorage() {
  if (AppState.isBackendConnected) return;
  try {
    const data = {
      users: AppState.users,
      events: AppState.events,
      registrations: AppState.registrations,
      certificates: AppState.certificates,
      notifications: AppState.notifications,
      reminders: AppState.reminders
    };
    localStorage.setItem("event_spectra_data", JSON.stringify(data));
  } catch (e) {
    console.warn("Storage save error:", e);
  }
}

function loadFromLocalStorage() {
  try {
    const saved = localStorage.getItem("event_spectra_data");
    if (saved) {
      const data = JSON.parse(saved);
      if (Array.isArray(data.users) && data.users.length) AppState.users = data.users;
      if (Array.isArray(data.events) && data.events.length) AppState.events = data.events;
      if (Array.isArray(data.registrations)) AppState.registrations = data.registrations;
      if (Array.isArray(data.certificates)) AppState.certificates = data.certificates;
      if (Array.isArray(data.notifications)) AppState.notifications = data.notifications;
      if (Array.isArray(data.reminders)) AppState.reminders = data.reminders;
      return true;
    }
  } catch (e) {
    console.warn("Storage load error:", e);
  }
  return false;
}

// DOM Initialization
document.addEventListener("DOMContentLoaded", () => {
  initApp();
});

async function initApp() {
  setupNavigation();
  setupUserSwitcher();
  setupModals();
  setupFilters();
  setupFormHandlers();
  
  // Try connecting to backend, if not available, seamlessly run with local state
  await checkBackendConnection();
  renderApp();
}

/**
 * Checks if the Java HTTP Server is running and loads live data.
 */
async function checkBackendConnection() {
  const statusEl = document.getElementById("backendStatus");
  const isCloudHost = window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1';

  if (isCloudHost) {
    AppState.isBackendConnected = false;
    loadFromLocalStorage();
    saveToLocalStorage();
    if (statusEl) {
      statusEl.innerHTML = `<strong>Event Spectra Cloud:</strong> Java OOP Models Active`;
      statusEl.parentElement.querySelector(".indicator-dot").style.backgroundColor = "var(--accent-green)";
    }
    return;
  }

  try {
    const res = await fetch(`${AppState.backendUrl}/api/events`, { signal: AbortSignal.timeout(1500) });
    if (res.ok) {
      AppState.isBackendConnected = true;
      const eventsData = await res.json();
      if (Array.isArray(eventsData) && eventsData.length > 0) {
        AppState.events = eventsData;
      }

      // Fetch other data
      await syncAllFromBackend();

      if (statusEl) {
        statusEl.innerHTML = `<strong>Java Backend Connected:</strong> Pure OOP Engine Active`;
        statusEl.parentElement.querySelector(".indicator-dot").style.backgroundColor = "var(--accent-green)";
      }
      return;
    }
  } catch (err) {
    // Backend offline; use embedded state
    AppState.isBackendConnected = false;
    loadFromLocalStorage();
    saveToLocalStorage();
    if (statusEl) {
      statusEl.innerHTML = `Running Local OOP Simulation (Java Server ready to run)`;
      statusEl.parentElement.querySelector(".indicator-dot").style.backgroundColor = "var(--accent-gold)";
    }
  }
}

async function syncAllFromBackend() {
  try {
    const [uRes, rRes, cRes, nRes, remRes] = await Promise.all([
      fetch(`${AppState.backendUrl}/api/users`).then(r => r.json()),
      fetch(`${AppState.backendUrl}/api/registrations`).then(r => r.json()),
      fetch(`${AppState.backendUrl}/api/certificates`).then(r => r.json()),
      fetch(`${AppState.backendUrl}/api/notifications`).then(r => r.json()),
      fetch(`${AppState.backendUrl}/api/reminders`).then(r => r.json())
    ]);
    if (Array.isArray(uRes)) AppState.users = uRes;
    if (Array.isArray(rRes)) AppState.registrations = rRes;
    if (Array.isArray(cRes)) AppState.certificates = cRes;
    if (Array.isArray(nRes)) AppState.notifications = nRes;
    if (Array.isArray(remRes)) AppState.reminders = remRes;
  } catch (e) {
    console.warn("Could not sync all resources from backend:", e);
  }
}

/* ==========================================================================
   NAVIGATION & TABS
   ========================================================================== */

function setupNavigation() {
  const links = document.querySelectorAll(".nav-link");
  links.forEach(link => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const tabId = link.getAttribute("data-tab");
      switchTab(tabId);
    });
  });
}

function switchTab(tabId) {
  // Update links
  document.querySelectorAll(".nav-link").forEach(l => {
    l.classList.toggle("active", l.getAttribute("data-tab") === tabId);
  });

  // Update tab panes
  document.querySelectorAll(".tab-pane").forEach(pane => {
    pane.classList.remove("active");
  });

  const activePane = document.getElementById(`tab-${tabId}`);
  if (activePane) {
    activePane.classList.add("active");
  }

  // Scroll smoothly to top of main content
  const main = document.querySelector(".main-content");
  if (main) {
    main.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  renderApp();
}

/* ==========================================================================
   USER & ROLE SWITCHER
   ========================================================================== */

function setupUserSwitcher() {
  const toggleBtn = document.getElementById("userMenuToggle");
  const dropdown = document.getElementById("userDropdown");

  toggleBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    dropdown.classList.toggle("hidden");
  });

  document.addEventListener("click", () => {
    dropdown.classList.add("hidden");
  });

  const switchButtons = document.querySelectorAll(".user-switch-item");
  switchButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      const uId = parseInt(btn.getAttribute("data-userid"));
      switchUser(uId);
      dropdown.classList.add("hidden");
    });
  });
}

function switchUser(userId) {
  const found = AppState.users.find(u => u.userId === userId);
  if (!found) return;

  AppState.currentUser = found;
  showToast(`Switched user to: ${found.name} (${found.role})`, "success");
  
  // Update UI Elements
  updateUserHeader();
  renderApp();
}

function updateUserHeader() {
  const u = AppState.currentUser;
  const nameEl = document.getElementById("currentUserName");
  const badgeEl = document.getElementById("currentUserRoleBadge");
  const avatarEl = document.getElementById("currentAvatar");

  if (nameEl) nameEl.textContent = u.name;
  
  if (badgeEl) {
    if (u.role === "STUDENT") {
      badgeEl.textContent = `Student (Roll ${u.studentId || u.userId})`;
    } else if (u.role === "COORDINATOR") {
      badgeEl.textContent = `Club Coordinator (${u.clubName || 'Club'})`;
    } else {
      badgeEl.textContent = `System Administrator`;
    }
  }

  if (avatarEl) {
    const initials = u.name.split(" ").map(w => w[0]).join("").substring(0, 2).toUpperCase();
    avatarEl.textContent = initials;
  }

  // Show/Hide Role-Specific Nav Links
  const coordLink = document.querySelector(".coord-only");
  const adminLink = document.querySelector(".admin-only");

  if (coordLink) {
    coordLink.classList.toggle("visible", u.role === "COORDINATOR" || u.role === "ADMIN");
  }
  if (adminLink) {
    adminLink.classList.toggle("visible", u.role === "ADMIN");
  }
}

/* ==========================================================================
   RENDER PIPELINE
   ========================================================================== */

function renderApp() {
  updateUserHeader();
  updateBadges();
  renderEvents();
  renderRegistrations();
  renderCertificates();
  renderNotificationsAndReminders();
  renderCoordinatorPortal();
  renderAdminPortal();
}

function updateBadges() {
  const uId = AppState.currentUser.userId;
  const myRegs = AppState.registrations.filter(r => r.studentId === uId && r.status !== "Cancelled");
  const myCerts = AppState.certificates.filter(c => c.studentId === uId);
  const myNotifs = AppState.notifications.filter(n => (n.userId === uId || n.userId === 0) && !n.isRead);

  const regB = document.getElementById("regBadge");
  const certB = document.getElementById("certBadge");
  const notifB = document.getElementById("notifBadge");

  if (regB) regB.textContent = myRegs.length;
  if (certB) certB.textContent = myCerts.length;
  if (notifB) notifB.textContent = myNotifs.length;
}

/* ==========================================================================
   EVENT DISCOVERY & RENDERING
   ========================================================================== */

function renderEvents() {
  const container = document.getElementById("eventsGrid");
  if (!container) return;

  const q = AppState.searchQuery.toLowerCase().trim();
  const filter = AppState.activeFilter;

  let filtered = AppState.events.filter(ev => {
    // Search query match
    const matchSearch = !q || 
      ev.title.toLowerCase().includes(q) ||
      ev.description.toLowerCase().includes(q) ||
      ev.venue.toLowerCase().includes(q) ||
      ev.organizerClub.toLowerCase().includes(q) ||
      (ev.theme && ev.theme.toLowerCase().includes(q));

    // Category / Status match
    let matchFilter = true;
    if (filter === "TODAY") {
      matchFilter = ev.status === "Today" || ev.date.toLowerCase() === "today";
    } else if (filter !== "ALL") {
      matchFilter = ev.category.toLowerCase() === filter.toLowerCase();
    }

    return matchSearch && matchFilter;
  });

  if (filtered.length === 0) {
    container.innerHTML = `
      <div style="grid-column: 1/-1; text-align: center; padding: 4rem 1rem;">
        <span style="font-size: 3rem;">🔍</span>
        <h3 style="margin-top: 1rem;">No events match your criteria</h3>
        <p style="color: var(--text-muted);">Try clearing your search term or switching the category filter.</p>
      </div>
    `;
    return;
  }

  const uId = AppState.currentUser.userId;
  const myRegIds = AppState.registrations
    .filter(r => r.studentId === uId && r.status !== "Cancelled")
    .map(r => r.eventId);

  container.innerHTML = filtered.map(ev => {
    const isRegistered = myRegIds.includes(ev.eventId);
    const capacityPct = Math.min(100, Math.round((ev.registeredCount / ev.capacity) * 100));
    const isHack = ev.isHackathon || ev.category === "Hackathon";
    const isToday = ev.status === "Today" || ev.date.toLowerCase() === "today";

    let catBadgeClass = "event-category-badge";
    if (isHack) catBadgeClass += " badge-hackathon";
    if (isToday) catBadgeClass += " badge-today";

    return `
      <div class="event-card" data-eventid="${ev.eventId}">
        <div>
          <div class="card-top">
            <span class="${catBadgeClass}">${isToday ? '🔥 Today\'s Event' : ev.category}</span>
            <span class="event-club-tag">${escapeHtml(ev.organizerClub)}</span>
          </div>
          <h3 class="event-title">${escapeHtml(ev.title)}</h3>
          <p class="event-desc">${escapeHtml(ev.description)}</p>

          ${isHack ? `
            <div class="card-hackathon-meta">
              <div class="hack-pill-row">
                <span>⚡ <strong>Theme:</strong> ${escapeHtml(ev.theme || 'Tech Innovation')}</span>
                <span>⏱️ <strong>Duration:</strong> ${escapeHtml(ev.duration || '24h')}</span>
              </div>
            </div>
          ` : ''}

          <div class="card-meta-list">
            <div class="meta-item">📅 <strong>Date:</strong> ${escapeHtml(ev.date)} &bull; ${escapeHtml(ev.time)}</div>
            <div class="meta-item">📍 <strong>Venue:</strong> ${escapeHtml(ev.venue)}</div>
          </div>
        </div>

        <div>
          <div class="capacity-tracker">
            <div class="capacity-labels">
              <span>Bookings: <strong>${ev.registeredCount} / ${ev.capacity}</strong></span>
              <span>${capacityPct}% Filled</span>
            </div>
            <div class="progress-bar-bg">
              <div class="progress-bar-fill" style="width: ${capacityPct}%"></div>
            </div>
          </div>

          <div class="card-actions">
            <button class="btn btn-outline btn-block view-details-btn" data-eventid="${ev.eventId}">Details</button>
            ${isRegistered ? `
              <button class="btn btn-success btn-block cancel-booking-btn" data-eventid="${ev.eventId}">✓ Registered</button>
            ` : `
              <button class="btn btn-primary btn-block register-btn" data-eventid="${ev.eventId}">Book Now</button>
            `}
          </div>
        </div>
      </div>
    `;
  }).join("");

  // Attach card event listeners
  container.querySelectorAll(".view-details-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const eId = parseInt(btn.getAttribute("data-eventid"));
      openEventModal(eId);
    });
  });

  container.querySelectorAll(".register-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const eId = parseInt(btn.getAttribute("data-eventid"));
      handleRegisterEvent(eId);
    });
  });

  container.querySelectorAll(".cancel-booking-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const eId = parseInt(btn.getAttribute("data-eventid"));
      handleCancelRegistration(eId);
    });
  });
}

function setupFilters() {
  const searchInput = document.getElementById("searchInput");
  if (searchInput) {
    searchInput.addEventListener("input", (e) => {
      AppState.searchQuery = e.target.value;
      renderEvents();
    });
  }

  const chips = document.querySelectorAll(".filter-chip");
  chips.forEach(chip => {
    chip.addEventListener("click", () => {
      chips.forEach(c => c.classList.remove("active"));
      chip.classList.add("active");
      AppState.activeFilter = chip.getAttribute("data-filter");
      renderEvents();
    });
  });
}

/* ==========================================================================
   REGISTRATIONS & BOOKING
   ========================================================================== */

function renderRegistrations() {
  const container = document.getElementById("registrationsContainer");
  if (!container) return;

  const uId = AppState.currentUser.userId;
  const myRegs = AppState.registrations.filter(r => r.studentId === uId && r.status !== "Cancelled");

  if (myRegs.length === 0) {
    container.innerHTML = `
      <div class="column-card" style="text-align: center; padding: 3.5rem 1rem;">
        <span style="font-size: 3rem;">🎟️</span>
        <h3 style="margin-top: 1rem;">No Registered Events Yet</h3>
        <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Explore upcoming workshops, hackathons, and competitions on the Discover tab.</p>
        <button class="btn btn-primary" onclick="switchTab('discover')">Explore Events</button>
      </div>
    `;
    return;
  }

  container.innerHTML = myRegs.map(reg => {
    const ev = AppState.events.find(e => e.eventId === reg.eventId) || {
      title: "FISAT Event #" + reg.eventId,
      date: "Upcoming",
      time: "TBA",
      venue: "Campus",
      organizerClub: "FISAT Club"
    };

    return `
      <div class="booking-card">
        <div class="booking-main">
          <div style="display:flex; align-items:center; gap: 0.5rem; margin-bottom: 0.25rem;">
            <span class="status-badge-confirmed">✓ ${reg.status}</span>
            <span style="font-size: 0.75rem; color: var(--text-dim);">Booking Ref: #${reg.registrationId}</span>
          </div>
          <h3 style="font-size: 1.15rem; font-weight: 700;">${escapeHtml(ev.title)}</h3>
          <div class="booking-meta-row">
            <span>📅 ${escapeHtml(ev.date)} &bull; ${escapeHtml(ev.time)}</span>
            <span>📍 ${escapeHtml(ev.venue)}</span>
            <span>🏛️ ${escapeHtml(ev.organizerClub)}</span>
          </div>
        </div>

        <div style="display: flex; gap: 0.5rem;">
          <button class="btn btn-outline btn-sm" onclick="openReminderForEvent(${ev.eventId})">⏰ Reminder</button>
          <button class="btn btn-outline btn-sm" onclick="openEventModal(${ev.eventId})">View Info</button>
          <button class="btn btn-danger btn-sm" onclick="handleCancelRegistration(${ev.eventId})">Cancel</button>
        </div>
      </div>
    `;
  }).join("");
}

async function handleRegisterEvent(eventId) {
  const uId = AppState.currentUser.userId;
  
  if (AppState.isBackendConnected) {
    try {
      const res = await fetch(`${AppState.backendUrl}/api/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ studentId: uId, eventId: eventId })
      });
      if (res.ok) {
        await syncAllFromBackend();
        showToast("Registration Confirmed! Ticket added to your bookings.", "success");
        renderApp();
        return;
      }
    } catch (e) {
      console.warn("Backend registration failed, using fallback:", e);
    }
  }

  // Local fallback simulation
  const ev = AppState.events.find(e => e.eventId === eventId);
  if (ev) {
    ev.registeredCount = (ev.registeredCount || 0) + 1;
    const newReg = {
      registrationId: Math.floor(500 + Math.random() * 500),
      studentId: uId,
      eventId: eventId,
      registrationDate: new Date().toISOString().replace('T', ' ').substring(0, 16),
      status: "Confirmed"
    };
    AppState.registrations.unshift(newReg);
    
    // Add notification
    AppState.notifications.unshift({
      notificationId: Math.floor(300 + Math.random() * 500),
      userId: uId,
      message: `Confirmed registration for "${ev.title}". We look forward to seeing you!`,
      dateTime: new Date().toISOString().replace('T', ' ').substring(0, 16),
      isRead: false
    });

    showToast(`Registered for ${ev.title}!`, "success");
    saveToLocalStorage();
    renderApp();
  }
}

async function handleCancelRegistration(eventId) {
  const uId = AppState.currentUser.userId;
  if (!confirm("Are you sure you want to cancel this event registration?")) return;

  if (AppState.isBackendConnected) {
    try {
      const res = await fetch(`${AppState.backendUrl}/api/cancel-registration`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ studentId: uId, eventId: eventId })
      });
      if (res.ok) {
        await syncAllFromBackend();
        showToast("Booking cancelled successfully.", "info");
        renderApp();
        return;
      }
    } catch (e) {
      console.warn("Backend cancel failed, using fallback:", e);
    }
  }

  // Fallback
  const idx = AppState.registrations.findIndex(r => r.studentId === uId && r.eventId === eventId && r.status !== "Cancelled");
  if (idx !== -1) {
    AppState.registrations[idx].status = "Cancelled";
    const ev = AppState.events.find(e => e.eventId === eventId);
    if (ev && ev.registeredCount > 0) ev.registeredCount--;
    showToast("Registration cancelled.", "info");
    saveToLocalStorage();
    renderApp();
  }
}

/* ==========================================================================
   CERTIFICATES HUB & CANVAS GENERATOR
   ========================================================================== */

function renderCertificates() {
  const container = document.getElementById("certificatesContainer");
  if (!container) return;

  const uId = AppState.currentUser.userId;
  const myCerts = AppState.certificates.filter(c => c.studentId === uId);

  if (myCerts.length === 0) {
    container.innerHTML = `
      <div class="column-card" style="grid-column: 1/-1; text-align: center; padding: 3.5rem 1rem;">
        <span style="font-size: 3rem;">📜</span>
        <h3 style="margin-top: 1rem;">No Certificates Issued Yet</h3>
        <p style="color: var(--text-muted); margin-bottom: 1.5rem;">Participate in FISAT workshops, coding hackathons, and competitions to earn accredited certificates.</p>
        <button class="btn btn-primary" onclick="switchTab('discover')">Explore Events</button>
      </div>
    `;
    return;
  }

  container.innerHTML = myCerts.map(cert => `
    <div class="cert-card">
      <div>
        <div class="cert-crest">🏆</div>
        <div class="cert-type-tag">${escapeHtml(cert.certificateType)}</div>
        <h3 class="cert-event-title">${escapeHtml(cert.eventTitle)}</h3>
        <div class="cert-meta">
          <div>Issued to: <strong>${escapeHtml(cert.studentName)}</strong></div>
          <div>Date of Issue: ${escapeHtml(cert.issueDate)}</div>
          <div>Certificate ID: #FISAT-ES-${cert.certificateId}</div>
        </div>
      </div>

      <div style="display: flex; gap: 0.5rem;">
        <button class="btn btn-primary btn-block" onclick="viewCertificateModal(${cert.certificateId})">View & Download</button>
      </div>
    </div>
  `).join("");
}

function viewCertificateModal(certId) {
  const cert = AppState.certificates.find(c => c.certificateId === certId);
  if (!cert) return;

  AppState.activeCertificate = cert;

  document.getElementById("certRenderType").textContent = cert.certificateType.toUpperCase();
  document.getElementById("certRenderRecipient").textContent = cert.studentName;
  document.getElementById("certRenderEvent").textContent = cert.eventTitle;
  document.getElementById("certRenderDate").textContent = cert.issueDate;

  // Student course if available
  const student = AppState.users.find(u => u.userId === cert.studentId);
  document.getElementById("certRenderDept").textContent = student ? student.course : "B.Tech Computer Science & Engineering";

  const modal = document.getElementById("certModal");
  modal.classList.remove("hidden");
}

function downloadCertificateCanvas() {
  const cert = AppState.activeCertificate;
  if (!cert) return;

  // We can render a high resolution canvas and prompt download
  const canvas = document.createElement("canvas");
  canvas.width = 1200;
  canvas.height = 800;
  const ctx = canvas.getContext("2d");

  // Background Parchment
  ctx.fillStyle = "#FFFDF9";
  ctx.fillRect(0, 0, 1200, 800);

  // Border Outer
  ctx.lineWidth = 14;
  ctx.strokeStyle = "#1E3A8A";
  ctx.strokeRect(30, 30, 1140, 740);

  // Border Inner Gold
  ctx.lineWidth = 4;
  ctx.strokeStyle = "#D97706";
  ctx.strokeRect(50, 50, 1100, 700);

  // Institution Title
  ctx.fillStyle = "#0F172A";
  ctx.font = "bold 34px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.textAlign = "center";
  ctx.fillText("FEDERAL INSTITUTE OF SCIENCE AND TECHNOLOGY (FISAT)®", 600, 130);

  ctx.fillStyle = "#475569";
  ctx.font = "18px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.fillText("Mookkannoor P.O., Angamaly, Ernakulam, Kerala - 683577", 600, 165);

  ctx.fillStyle = "#D97706";
  ctx.font = "bold 38px Georgia, serif";
  ctx.fillText(cert.certificateType.toUpperCase(), 600, 260);

  ctx.fillStyle = "#64748B";
  ctx.font = "italic 22px Georgia, serif";
  ctx.fillText("This certificate is proudly awarded to", 600, 310);

  // Student Name
  ctx.fillStyle = "#0F172A";
  ctx.font = "bold 44px Georgia, serif";
  ctx.fillText(cert.studentName, 600, 375);

  // Body Text
  ctx.fillStyle = "#334155";
  ctx.font = "20px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.fillText("for successful participation in the college event", 600, 440);

  ctx.fillStyle = "#1E3A8A";
  ctx.font = "bold 24px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.fillText(`"${cert.eventTitle}"`, 600, 480);

  ctx.fillStyle = "#475569";
  ctx.font = "18px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.fillText(`Organized by Event Spectra • FISAT Campus`, 600, 520);

  // Signatures
  ctx.textAlign = "left";
  ctx.fillStyle = "#1E293B";
  ctx.font = "bold 20px 'Brush Script MT', cursive, serif";
  ctx.fillText("Dr. Mini P R", 180, 680);
  ctx.font = "14px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.fillText("Principal / Dean Academics", 180, 705);

  // Seal in center
  ctx.beginPath();
  ctx.arc(600, 680, 45, 0, 2 * Math.PI);
  ctx.lineWidth = 3;
  ctx.strokeStyle = "#D97706";
  ctx.stroke();
  ctx.textAlign = "center";
  ctx.fillStyle = "#B45309";
  ctx.font = "bold 13px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.fillText("FISAT SEAL", 600, 678);
  ctx.fillText("VERIFIED", 600, 696);

  // Date on right
  ctx.textAlign = "right";
  ctx.fillStyle = "#1E293B";
  ctx.font = "16px 'Plus Jakarta Sans', Arial, sans-serif";
  ctx.fillText(`Issued: ${cert.issueDate}`, 1020, 680);
  ctx.fillText(`ID: FISAT-ES-${cert.certificateId}`, 1020, 705);

  // Trigger download
  const link = document.createElement("a");
  link.download = `Certificate_${cert.studentName.replace(/\s+/g, '_')}_${cert.certificateId}.png`;
  link.href = canvas.toDataURL("image/png");
  link.click();
  showToast("Certificate downloaded successfully!", "success");
}

/* ==========================================================================
   NOTIFICATIONS & REMINDERS
   ========================================================================== */

function renderNotificationsAndReminders() {
  const notifList = document.getElementById("notificationsList");
  const remList = document.getElementById("remindersList");
  if (!notifList || !remList) return;

  const uId = AppState.currentUser.userId;
  const myNotifs = AppState.notifications.filter(n => n.userId === uId || n.userId === 0);
  const myRems = AppState.reminders.filter(r => r.userId === uId);

  // Render Notifications
  if (myNotifs.length === 0) {
    notifList.innerHTML = `<p style="color:var(--text-dim); text-align:center; padding:1.5rem;">No alerts at this time.</p>`;
  } else {
    notifList.innerHTML = myNotifs.map(n => `
      <div class="notif-item ${!n.isRead ? 'unread' : ''}">
        <div>
          <div class="notif-message">${escapeHtml(n.message)}</div>
          <div class="notif-time">${escapeHtml(n.dateTime)}</div>
        </div>
        ${!n.isRead ? `
          <button class="btn btn-outline btn-sm" onclick="markNotificationRead(${n.notificationId})">Read</button>
        ` : ''}
      </div>
    `).join("");
  }

  // Render Reminders
  if (myRems.length === 0) {
    remList.innerHTML = `<p style="color:var(--text-dim); text-align:center; padding:1.5rem;">No reminders scheduled.</p>`;
  } else {
    remList.innerHTML = myRems.map(r => `
      <div class="reminder-item">
        <div>
          <div class="rem-title">${escapeHtml(r.eventTitle)}</div>
          <div class="rem-time">⏰ ${escapeHtml(r.reminderTime)}</div>
          ${r.message ? `<div style="font-size:0.75rem; color:var(--text-muted); margin-top:0.2rem;">Note: ${escapeHtml(r.message)}</div>` : ''}
        </div>
        <button class="btn btn-danger btn-sm" onclick="deleteReminder(${r.reminderId})">Dismiss</button>
      </div>
    `).join("");
  }
}

async function markNotificationRead(notifId) {
  if (AppState.isBackendConnected) {
    try {
      await fetch(`${AppState.backendUrl}/api/notifications/read`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ notificationId: notifId })
      });
    } catch (e) {
      console.warn("Backend mark read failed:", e);
    }
  }

  const found = AppState.notifications.find(n => n.notificationId === notifId);
  if (found) found.isRead = true;
  saveToLocalStorage();
  renderApp();
}

function markAllNotificationsRead() {
  const uId = AppState.currentUser.userId;
  AppState.notifications.forEach(n => {
    if (n.userId === uId || n.userId === 0) n.isRead = true;
  });
  showToast("All notifications marked as read.", "info");
  saveToLocalStorage();
  renderApp();
}

function deleteReminder(remId) {
  AppState.reminders = AppState.reminders.filter(r => r.reminderId !== remId);
  showToast("Reminder dismissed.", "info");
  saveToLocalStorage();
  renderApp();
}

/* ==========================================================================
   CLUB COORDINATOR PORTAL
   ========================================================================== */

function renderCoordinatorPortal() {
  const titleEl = document.getElementById("coordClubName");
  const eventsTable = document.getElementById("coordEventsTable");
  const partTable = document.getElementById("coordParticipantsTable");
  if (!eventsTable || !partTable) return;

  const user = AppState.currentUser;
  const clubName = user.clubName || "FISAT ACM Student Chapter";
  if (titleEl) titleEl.textContent = clubName;

  // Filter events published by this club
  const clubEvents = AppState.events.filter(e => 
    e.organizerClub.toLowerCase().includes(clubName.toLowerCase()) || 
    clubName.toLowerCase().includes(e.organizerClub.toLowerCase()) ||
    user.role === "ADMIN" // Admin sees all
  );

  // Stats
  document.getElementById("coordTotalEvents").textContent = clubEvents.length;
  
  const clubEventIds = clubEvents.map(e => e.eventId);
  const clubRegs = AppState.registrations.filter(r => clubEventIds.includes(r.eventId) && r.status !== "Cancelled");
  document.getElementById("coordTotalRegistrations").textContent = clubRegs.length;

  const clubCerts = AppState.certificates.filter(c => clubEventIds.includes(c.eventId));
  document.getElementById("coordTotalCertificates").textContent = clubCerts.length;

  // Render Events Table
  const tbody = eventsTable.querySelector("tbody");
  if (clubEvents.length === 0) {
    tbody.innerHTML = `<tr><td colspan="8" style="text-align:center;">No events created yet. Click "+ Create & Publish Event" above.</td></tr>`;
  } else {
    tbody.innerHTML = clubEvents.map(e => `
      <tr>
        <td>#${e.eventId}</td>
        <td><strong>${escapeHtml(e.title)}</strong></td>
        <td><span class="event-category-badge">${e.category}</span></td>
        <td>${escapeHtml(e.date)} &bull; ${escapeHtml(e.time)}</td>
        <td>${escapeHtml(e.venue)}</td>
        <td>${e.registeredCount} / ${e.capacity}</td>
        <td><span class="status-badge-confirmed">${escapeHtml(e.status)}</span></td>
        <td>
          <button class="btn btn-outline btn-sm" onclick="openEventModal(${e.eventId})">View</button>
          <button class="btn btn-danger btn-sm" onclick="deleteEventById(${e.eventId})">Delete</button>
        </td>
      </tr>
    `).join("");
  }

  // Render Participants & Certificate Issuance Table
  const partBody = partTable.querySelector("tbody");
  if (clubRegs.length === 0) {
    partBody.innerHTML = `<tr><td colspan="6" style="text-align:center;">No students registered for your club events yet.</td></tr>`;
  } else {
    partBody.innerHTML = clubRegs.map(r => {
      const student = AppState.users.find(u => u.userId === r.studentId) || { name: `Student #${r.studentId}`, course: "B.Tech CSE", semester: 4 };
      const event = AppState.events.find(e => e.eventId === r.eventId) || { title: `Event #${r.eventId}` };
      const hasCert = AppState.certificates.some(c => c.studentId === r.studentId && c.eventId === r.eventId);

      return `
        <tr>
          <td>#${r.registrationId}</td>
          <td><strong>${escapeHtml(student.name)}</strong></td>
          <td>${escapeHtml(student.course || 'B.Tech CSE')} (S${student.semester || 4})</td>
          <td>${escapeHtml(event.title)}</td>
          <td><span class="status-badge-confirmed">${r.status}</span></td>
          <td>
            ${hasCert ? `
              <span style="color:var(--accent-green); font-weight:600; font-size:0.8rem;">✓ Certificate Issued</span>
            ` : `
              <button class="btn btn-primary btn-sm" onclick="issueCertificateForStudent(${r.studentId}, ${r.eventId})">Issue Certificate</button>
            `}
          </td>
        </tr>
      `;
    }).join("");
  }
}

async function issueCertificateForStudent(studentId, eventId) {
  if (AppState.isBackendConnected) {
    try {
      const res = await fetch(`${AppState.backendUrl}/api/certificates/issue`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          studentId: studentId,
          eventId: eventId,
          certificateType: "Certificate of Participation"
        })
      });
      if (res.ok) {
        await syncAllFromBackend();
        showToast("Official certificate generated and emailed to student!", "success");
        renderApp();
        return;
      }
    } catch (e) {
      console.warn("Backend certificate issue failed:", e);
    }
  }

  // Fallback
  const student = AppState.users.find(u => u.userId === studentId);
  const ev = AppState.events.find(e => e.eventId === eventId);
  const newCert = {
    certificateId: Math.floor(800 + Math.random() * 500),
    studentId: studentId,
    eventId: eventId,
    studentName: student ? student.name : `Student #${studentId}`,
    eventTitle: ev ? ev.title : `Event #${eventId}`,
    issueDate: new Date().toISOString().substring(0, 10),
    certificateUrl: `/certificates/cert.pdf`,
    certificateType: "Certificate of Participation"
  };
  AppState.certificates.unshift(newCert);

  AppState.notifications.unshift({
    notificationId: Math.floor(300 + Math.random() * 500),
    userId: studentId,
    message: `Your Certificate for "${ev ? ev.title : 'Event'}" is now available for download!`,
    dateTime: new Date().toISOString().replace('T', ' ').substring(0, 16),
    isRead: false
  });

  showToast("Certificate generated and sent!", "success");
  saveToLocalStorage();
  renderApp();
}

async function deleteEventById(eventId) {
  if (!confirm("Are you sure you want to remove/cancel this event?")) return;

  if (AppState.isBackendConnected) {
    try {
      const res = await fetch(`${AppState.backendUrl}/api/events?id=${eventId}`, {
        method: "DELETE"
      });
      if (res.ok) {
        await syncAllFromBackend();
        showToast("Event cancelled successfully.", "info");
        renderApp();
        return;
      }
    } catch (e) {
      console.warn("Backend delete event failed:", e);
    }
  }

  // Fallback
  const ev = AppState.events.find(e => e.eventId === eventId);
  if (ev) {
    ev.status = "Cancelled";
    showToast("Event cancelled.", "info");
    saveToLocalStorage();
    renderApp();
  }
}

/* ==========================================================================
   ADMINISTRATOR PANEL
   ========================================================================== */

function renderAdminPortal() {
  const studentsTable = document.getElementById("adminStudentsTable");
  const clubsTable = document.getElementById("adminClubsTable");
  if (!studentsTable || !clubsTable) return;

  const students = AppState.users.filter(u => u.role === "STUDENT");
  const clubs = AppState.users.filter(u => u.role === "COORDINATOR");

  document.getElementById("adminStatStudents").textContent = students.length;
  document.getElementById("adminStatClubs").textContent = clubs.length;
  document.getElementById("adminStatEvents").textContent = AppState.events.length;
  document.getElementById("adminStatRegistrations").textContent = AppState.registrations.filter(r => r.status !== "Cancelled").length;

  // Render Students
  studentsTable.querySelector("tbody").innerHTML = students.map(s => `
    <tr>
      <td><strong>${s.studentId || s.userId}</strong></td>
      <td>${escapeHtml(s.name)}</td>
      <td>${escapeHtml(s.email)}</td>
      <td>${escapeHtml(s.course || 'B.Tech CSE')} (S${s.semester || 4})</td>
    </tr>
  `).join("");

  // Render Clubs
  clubsTable.querySelector("tbody").innerHTML = clubs.map(c => `
    <tr>
      <td>#${c.organizerId || c.userId}</td>
      <td><strong>${escapeHtml(c.clubName || c.name)}</strong></td>
      <td>${escapeHtml(c.email)}</td>
      <td><span class="status-badge-confirmed">Active Club</span></td>
    </tr>
  `).join("");
}

/* ==========================================================================
   MODALS SETUP & BEHAVIOR
   ========================================================================== */

function setupModals() {
  // Event Details Modal
  document.getElementById("closeEventModal").addEventListener("click", () => {
    document.getElementById("eventModal").classList.add("hidden");
  });

  // Create Event Modal
  const createModal = document.getElementById("createEventModal");
  document.getElementById("openCreateEventModalBtn")?.addEventListener("click", () => {
    createModal.classList.remove("hidden");
  });
  document.getElementById("closeCreateEventModal")?.addEventListener("click", () => {
    createModal.classList.add("hidden");
  });
  document.getElementById("cancelCreateEventBtn")?.addEventListener("click", () => {
    createModal.classList.add("hidden");
  });

  // Certificate Modal
  document.getElementById("closeCertModal")?.addEventListener("click", () => {
    document.getElementById("certModal").classList.add("hidden");
  });
  document.getElementById("downloadCertBtn")?.addEventListener("click", downloadCertificateCanvas);
  document.getElementById("simulateEmailCertBtn")?.addEventListener("click", () => {
    showToast(`Official Certificate PDF sent to ${AppState.currentUser.email}!`, "success");
  });

  // Reminder Modal
  document.getElementById("closeReminderModal")?.addEventListener("click", () => {
    document.getElementById("reminderModal").classList.add("hidden");
  });
  document.getElementById("openNewReminderBtn")?.addEventListener("click", () => {
    if (AppState.events.length > 0) {
      openReminderForEvent(AppState.events[0].eventId);
    }
  });

  // Project Team Modal (Slide 2)
  const teamModal = document.getElementById("teamModal");
  document.getElementById("teamBtn")?.addEventListener("click", () => {
    teamModal.classList.remove("hidden");
  });
  document.getElementById("closeTeamModal")?.addEventListener("click", () => {
    teamModal.classList.add("hidden");
  });

  // Java OOP Architecture Modal (Slide 7)
  const oopModal = document.getElementById("oopDocModal");
  document.getElementById("oopDocBtn")?.addEventListener("click", () => {
    oopModal.classList.remove("hidden");
  });
  document.getElementById("closeOopDocModal")?.addEventListener("click", () => {
    oopModal.classList.add("hidden");
  });

  // Mark all read
  document.getElementById("markAllReadBtn")?.addEventListener("click", markAllNotificationsRead);

  // Admin Broadcast Alert
  document.getElementById("broadcastAlertBtn")?.addEventListener("click", () => {
    const msg = prompt("Enter campus broadcast announcement message:");
    if (msg && msg.trim()) {
      AppState.notifications.unshift({
        notificationId: Math.floor(300 + Math.random() * 500),
        userId: 0, // all
        message: `📢 FISAT CAMPUS ANNOUNCEMENT: ${msg.trim()}`,
        dateTime: new Date().toISOString().replace('T', ' ').substring(0, 16),
        isRead: false
      });
      showToast("Campus broadcast alert sent to all students!", "success");
      saveToLocalStorage();
      renderApp();
    }
  });
}

function openEventModal(eventId) {
  const ev = AppState.events.find(e => e.eventId === eventId);
  if (!ev) return;

  AppState.selectedEventForModal = ev;

  document.getElementById("modalTitle").textContent = ev.title;
  document.getElementById("modalDesc").textContent = ev.description;
  document.getElementById("modalCategoryBadge").textContent = ev.category;
  document.getElementById("modalClubBadge").textContent = ev.organizerClub;
  document.getElementById("modalDate").textContent = ev.date;
  document.getElementById("modalTime").textContent = ev.time;
  document.getElementById("modalVenue").textContent = ev.venue;
  document.getElementById("modalCapacity").textContent = `${ev.registeredCount} / ${ev.capacity} booked`;

  // Hackathon specific fields
  const hackBox = document.getElementById("modalHackathonBox");
  if (ev.isHackathon || ev.category === "Hackathon") {
    hackBox.classList.remove("hidden");
    document.getElementById("modalHackTheme").textContent = ev.theme || "General Innovation";
    document.getElementById("modalHackDuration").textContent = ev.duration || "24 Hours";
    document.getElementById("modalHackTeamSize").textContent = `${ev.teamSize || 4} members per team`;
  } else {
    hackBox.classList.add("hidden");
  }

  // Check if registered
  const uId = AppState.currentUser.userId;
  const isRegistered = AppState.registrations.some(r => r.studentId === uId && r.eventId === ev.eventId && r.status !== "Cancelled");
  
  const regBtn = document.getElementById("modalRegisterBtn");
  if (isRegistered) {
    regBtn.textContent = "Cancel Registration";
    regBtn.className = "btn btn-danger btn-large";
    regBtn.onclick = () => {
      handleCancelRegistration(ev.eventId);
      document.getElementById("eventModal").classList.add("hidden");
    };
  } else {
    regBtn.textContent = "Register / Book Participation";
    regBtn.className = "btn btn-primary btn-large";
    regBtn.onclick = () => {
      handleRegisterEvent(ev.eventId);
      document.getElementById("eventModal").classList.add("hidden");
    };
  }

  document.getElementById("modalReminderBtn").onclick = () => {
    document.getElementById("eventModal").classList.add("hidden");
    openReminderForEvent(ev.eventId);
  };

  document.getElementById("eventModal").classList.remove("hidden");
}

function openReminderForEvent(eventId) {
  const ev = AppState.events.find(e => e.eventId === eventId);
  if (!ev) return;

  document.getElementById("reminderEventId").value = ev.eventId;
  document.getElementById("reminderEventDesc").textContent = `Schedule an alert for "${ev.title}" (${ev.date} at ${ev.time})`;
  document.getElementById("reminderTimeInput").value = `1 hour before (${ev.time})`;
  document.getElementById("reminderNoteInput").value = `Bring laptop and college ID to ${ev.venue}`;

  document.getElementById("reminderModal").classList.remove("hidden");
}

/* ==========================================================================
   FORMS HANDLING (Create Event, Reminder)
   ========================================================================== */

function setupFormHandlers() {
  // Category change toggles Hackathon attributes
  const catSelect = document.getElementById("eventInputCategory");
  const hackBox = document.getElementById("hackathonFormFields");
  if (catSelect && hackBox) {
    catSelect.addEventListener("change", () => {
      hackBox.classList.toggle("hidden", catSelect.value !== "Hackathon");
    });
  }

  // Create Event Submit
  const createForm = document.getElementById("createEventForm");
  if (createForm) {
    createForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      
      const category = document.getElementById("eventInputCategory").value;
      const isHack = category === "Hackathon";
      const club = AppState.currentUser.clubName || "FISAT ACM Student Chapter";

      const eventData = {
        title: document.getElementById("eventInputTitle").value.trim(),
        description: document.getElementById("eventInputDesc").value.trim(),
        date: document.getElementById("eventInputDate").value,
        time: document.getElementById("eventInputTime").value.trim(),
        venue: document.getElementById("eventInputVenue").value.trim(),
        capacity: parseInt(document.getElementById("eventInputCapacity").value) || 50,
        category: category,
        organizerClub: club,
        isHackathon: isHack,
        theme: isHack ? document.getElementById("eventInputTheme").value.trim() : "",
        duration: isHack ? document.getElementById("eventInputDuration").value.trim() : "",
        teamSize: isHack ? parseInt(document.getElementById("eventInputTeamSize").value) || 4 : 0
      };

      if (AppState.isBackendConnected) {
        try {
          const res = await fetch(`${AppState.backendUrl}/api/events`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(eventData)
          });
          if (res.ok) {
            await syncAllFromBackend();
            document.getElementById("createEventModal").classList.add("hidden");
            createForm.reset();
            showToast("Event published to Event Spectra successfully!", "success");
            renderApp();
            return;
          }
        } catch (err) {
          console.warn("Backend create event failed:", err);
        }
      }

      // Fallback
      eventData.eventId = Math.floor(100 + Math.random() * 900);
      eventData.registeredCount = 0;
      eventData.status = "Upcoming";
      AppState.events.unshift(eventData);

      // Notification
      AppState.notifications.unshift({
        notificationId: Math.floor(300 + Math.random() * 500),
        userId: 0,
        message: `New Event Announced: "${eventData.title}" organized by ${eventData.organizerClub}`,
        dateTime: new Date().toISOString().replace('T', ' ').substring(0, 16),
        isRead: false
      });

      document.getElementById("createEventModal").classList.add("hidden");
      createForm.reset();
      showToast("Event published successfully!", "success");
      saveToLocalStorage();
      renderApp();
    });
  }

  // Reminder Form Submit
  const remForm = document.getElementById("reminderForm");
  if (remForm) {
    remForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      const eventId = parseInt(document.getElementById("reminderEventId").value);
      const time = document.getElementById("reminderTimeInput").value.trim();
      const note = document.getElementById("reminderNoteInput").value.trim();
      const uId = AppState.currentUser.userId;

      if (AppState.isBackendConnected) {
        try {
          await fetch(`${AppState.backendUrl}/api/reminders`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              userId: uId,
              eventId: eventId,
              reminderTime: time,
              message: note
            })
          });
          await syncAllFromBackend();
          document.getElementById("reminderModal").classList.add("hidden");
          showToast("Reminder scheduled successfully!", "success");
          renderApp();
          return;
        } catch (err) {
          console.warn("Backend reminder submit failed:", err);
        }
      }

      // Fallback
      const ev = AppState.events.find(e => e.eventId === eventId);
      AppState.reminders.unshift({
        reminderId: Math.floor(400 + Math.random() * 500),
        userId: uId,
        eventId: eventId,
        eventTitle: ev ? ev.title : `Event #${eventId}`,
        reminderTime: time,
        message: note,
        active: true
      });

      document.getElementById("reminderModal").classList.add("hidden");
      showToast("Reminder scheduled!", "success");
      saveToLocalStorage();
      renderApp();
    });
  }
}

/* ==========================================================================
   UTILITIES
   ========================================================================== */

function showToast(message, type = "info") {
  const container = document.getElementById("toastContainer");
  if (!container) return;

  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;
  toast.textContent = message;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = "0";
    toast.style.transform = "translateX(100%)";
    toast.style.transition = "all 0.3s ease";
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

function escapeHtml(str) {
  if (!str) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}
