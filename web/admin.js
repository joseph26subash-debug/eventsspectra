/**
 * Event Spectra - Dedicated Administrative Portal Controller
 * Connects to Java backend REST endpoints and provides full administrative oversight.
 */

const AdminApp = {
  backendUrl: window.location.origin.includes('localhost') || window.location.origin.includes('127.0.0.1')
    ? window.location.origin
    : 'http://localhost:8080',
  
  users: [],
  events: [],
  registrations: [],
  certificates: [],
  notifications: []
};

// Quick Fill for Admin Login Form
window.quickFillAdmin = function() {
  const u = document.getElementById("gateAdminUser");
  const p = document.getElementById("gateAdminPass");
  if (u) u.value = "admin@fisat.ac.in";
  if (p) p.value = "admin123";
};

document.addEventListener("DOMContentLoaded", async () => {
  setupTabs();
  setupModals();
  setupBroadcastForm();
  setupAdminAuthGate();
  await loadAdminData();
  renderAll();
});

function setupAdminAuthGate() {
  const gate = document.getElementById("adminLoginGate");
  const content = document.getElementById("adminConsoleContent");
  const form = document.getElementById("adminGateLoginForm");
  const logoutBtn = document.getElementById("adminLogoutBtn");

  // Check if admin is currently authenticated in this browser session
  const isAuth = sessionStorage.getItem("event_spectra_admin_auth") === "true";
  if (isAuth) {
    if (gate) gate.style.display = "none";
    if (content) content.style.display = "block";
  } else {
    if (gate) gate.style.display = "block";
    if (content) content.style.display = "none";
  }

  form?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const user = document.getElementById("gateAdminUser").value.trim();
    const pass = document.getElementById("gateAdminPass").value;

    try {
      const res = await fetch(`${AdminApp.backendUrl}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: user, password: pass })
      });
      const data = await res.json();
      if (data.success && data.user && data.user.role === "ADMIN") {
        sessionStorage.setItem("event_spectra_admin_auth", "true");
        if (gate) gate.style.display = "none";
        if (content) content.style.display = "block";
        showToast("Administrator Authentication Verified! Welcome to Central Oversight.", "success");
        return;
      } else {
        showToast(data.error || "Access Denied: Administrative credentials required.", "error");
      }
    } catch (err) {
      // Local fallback verification
      if ((user.toLowerCase() === "admin@fisat.ac.in" || user.toLowerCase() === "admin") && pass === "admin123") {
        sessionStorage.setItem("event_spectra_admin_auth", "true");
        if (gate) gate.style.display = "none";
        if (content) content.style.display = "block";
        showToast("Administrator Authentication Verified!", "success");
      } else {
        showToast("Access Denied: Invalid administrator password.", "error");
      }
    }
  });

  logoutBtn?.addEventListener("click", () => {
    sessionStorage.removeItem("event_spectra_admin_auth");
    if (gate) gate.style.display = "block";
    if (content) content.style.display = "none";
    showToast("Administrator signed out.", "info");
  });
}

function setupTabs() {
  const tabBtns = document.querySelectorAll(".tab-btn");
  tabBtns.forEach(btn => {
    btn.addEventListener("click", () => {
      tabBtns.forEach(b => b.classList.remove("active"));
      btn.classList.add("active");

      const targetId = btn.getAttribute("data-target");
      document.querySelectorAll(".admin-tab-pane").forEach(pane => {
        pane.style.display = "none";
      });

      const activePane = document.getElementById(targetId);
      if (activePane) {
        activePane.style.display = "block";
      }
    });
  });

  // Also support nav-links in top navbar
  const navLinks = document.querySelectorAll(".navbar .nav-link");
  navLinks.forEach(link => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const tab = link.getAttribute("data-tab");
      const matchingBtn = document.querySelector(`.tab-btn[data-target="tab-${tab}"]`);
      if (matchingBtn) matchingBtn.click();
    });
  });
}

function saveAdminToLocalStorage() {
  try {
    const data = {
      users: AdminApp.users,
      events: AdminApp.events,
      registrations: AdminApp.registrations,
      certificates: AdminApp.certificates,
      notifications: AdminApp.notifications
    };
    localStorage.setItem("event_spectra_data", JSON.stringify(data));
  } catch (e) {
    console.warn("Storage save error:", e);
  }
}

function loadAdminFromLocalStorage() {
  try {
    const saved = localStorage.getItem("event_spectra_data");
    if (saved) {
      const data = JSON.parse(saved);
      if (Array.isArray(data.users) && data.users.length) AdminApp.users = data.users;
      if (Array.isArray(data.events) && data.events.length) AdminApp.events = data.events;
      if (Array.isArray(data.registrations)) AdminApp.registrations = data.registrations;
      if (Array.isArray(data.certificates)) AdminApp.certificates = data.certificates;
      if (Array.isArray(data.notifications)) AdminApp.notifications = data.notifications;
      return true;
    }
  } catch (e) {
    console.warn("Storage load error:", e);
  }
  return false;
}

async function loadAdminData() {
  const isCloudHost = window.location.hostname !== 'localhost' && window.location.hostname !== '127.0.0.1';
  if (isCloudHost) {
    if (!loadAdminFromLocalStorage()) {
      initDefaultAdminMock();
      saveAdminToLocalStorage();
    }
    return;
  }

  try {
    const [uRes, eRes, rRes, cRes, nRes] = await Promise.all([
      fetch(`${AdminApp.backendUrl}/api/users`).then(r => r.json()),
      fetch(`${AdminApp.backendUrl}/api/events`).then(r => r.json()),
      fetch(`${AdminApp.backendUrl}/api/registrations`).then(r => r.json()),
      fetch(`${AdminApp.backendUrl}/api/certificates`).then(r => r.json()),
      fetch(`${AdminApp.backendUrl}/api/notifications`).then(r => r.json())
    ]);
    AdminApp.users = uRes;
    AdminApp.events = eRes;
    AdminApp.registrations = rRes;
    AdminApp.certificates = cRes;
    AdminApp.notifications = nRes;
  } catch (err) {
    console.warn("Could not load from backend, using default mock:", err);
    if (!loadAdminFromLocalStorage()) {
      initDefaultAdminMock();
      saveAdminToLocalStorage();
    }
  }
}

function initDefaultAdminMock() {
  AdminApp.users = [
    { userId: 48, name: "Joseph P Subash", email: "joseph.subash@fisat.ac.in", role: "STUDENT", studentId: 48, course: "B.Tech CSE", semester: 4, phone: "+91 98471 23456" },
    { userId: 46, name: "Joanna Jomon", email: "joanna.jomon@fisat.ac.in", role: "STUDENT", studentId: 46, course: "B.Tech CSE", semester: 4, phone: "+91 94471 67890" },
    { userId: 47, name: "Joel Shantu", email: "joel.shantu@fisat.ac.in", role: "STUDENT", studentId: 47, course: "B.Tech CSE", semester: 4, phone: "+91 94951 12345" },
    { userId: 49, name: "Joshua Jomesh", email: "joshua.jomesh@fisat.ac.in", role: "STUDENT", studentId: 49, course: "B.Tech CSE", semester: 4, phone: "+91 97451 98765" },
    { userId: 50, name: "Jovial J Thattil", email: "jovial.thattil@fisat.ac.in", role: "STUDENT", studentId: 50, course: "B.Tech CSE", semester: 4, phone: "+91 96331 54321" },
    { userId: 101, name: "ACM FISAT Student Chapter", email: "acm@fisat.ac.in", role: "COORDINATOR", organizerId: 201, clubName: "FISAT ACM Chapter", clubDescription: "Computing society chapter fostering computer science innovation." },
    { userId: 102, name: "IEEE SB FISAT", email: "ieee@fisat.ac.in", role: "COORDINATOR", organizerId: 202, clubName: "IEEE SB FISAT", clubDescription: "Advancing technology for humanity through technical workshops and hackathons." },
    { userId: 103, name: "FOSS Cell FISAT", email: "foss@fisat.ac.in", role: "COORDINATOR", organizerId: 203, clubName: "FOSS Cell FISAT", clubDescription: "Promoting open-source culture and Linux." },
    { userId: 104, name: "CSI FISAT Chapter", email: "csi@fisat.ac.in", role: "COORDINATOR", organizerId: 204, clubName: "CSI FISAT Chapter", clubDescription: "National IT professionals body encouraging research." },
    { userId: 1, name: "FISAT Events Administrator", email: "admin@fisat.ac.in", role: "ADMIN", department: "Dean of Academics" }
  ];
  AdminApp.events = [
    { eventId: 101, title: "HackSpectra 2026: 24-Hour AI & Web3 Hackathon", organizerClub: "FISAT ACM Chapter", category: "Hackathon", date: "2026-11-14", time: "09:00 AM", venue: "CCF", capacity: 120, registeredCount: 45, status: "Upcoming" },
    { eventId: 102, title: "Workshop on Deep Learning & LLMs with PyTorch", organizerClub: "IEEE SB FISAT", category: "Workshop", date: "2026-11-05", time: "10:00 AM", venue: "Auditorium 2", capacity: 80, registeredCount: 68, status: "Upcoming" },
    { eventId: 103, title: "TechFest Inaugural Keynote & Campus Project Expo", organizerClub: "FISAT ACM Chapter", category: "Technical", date: "Today", time: "02:00 PM", venue: "Main Auditorium", capacity: 250, registeredCount: 190, status: "Today" }
  ];
  AdminApp.registrations = [
    { registrationId: 501, studentId: 48, eventId: 101, registrationDate: "2026-10-25 14:30", status: "Confirmed" },
    { registrationId: 502, studentId: 48, eventId: 102, registrationDate: "2026-10-26 10:15", status: "Confirmed" },
    { registrationId: 503, studentId: 46, eventId: 101, registrationDate: "2026-10-26 11:00", status: "Confirmed" }
  ];
  AdminApp.certificates = [
    { certificateId: 801, studentId: 48, eventId: 103, studentName: "Joseph P Subash", eventTitle: "TechFest Keynote", issueDate: "2026-10-30", certificateType: "Participation" }
  ];
  AdminApp.notifications = [
    { notificationId: 301, userId: 0, message: "Welcome to Event Spectra Centralized Platform", dateTime: "2026-10-28 09:00" }
  ];
}

function renderAll() {
  renderKPIs();
  renderOverviewTable();
  renderStudentsTable();
  renderClubsTable();
  renderEventsTable();
  renderRegistrationsTable();
  renderBroadcastHistory();
}

function renderKPIs() {
  const students = AdminApp.users.filter(u => u.role === "STUDENT");
  const clubs = AdminApp.users.filter(u => u.role === "COORDINATOR");

  document.getElementById("statStudents").textContent = students.length;
  document.getElementById("statClubs").textContent = clubs.length;
  document.getElementById("statEvents").textContent = AdminApp.events.length;
  document.getElementById("statBookings").textContent = AdminApp.registrations.filter(r => r.status !== "Cancelled").length;
  document.getElementById("statCerts").textContent = AdminApp.certificates.length;
}

function renderOverviewTable() {
  const tbody = document.querySelector("#overviewEventsTable tbody");
  if (!tbody) return;
  tbody.innerHTML = AdminApp.events.slice(0, 5).map(e => `
    <tr>
      <td><strong>${escapeHtml(e.title)}</strong></td>
      <td>${escapeHtml(e.organizerClub)}</td>
      <td><span class="event-category-badge">${escapeHtml(e.category)}</span></td>
      <td>${e.registeredCount || 0} / ${e.capacity}</td>
    </tr>
  `).join("");
}

function renderStudentsTable() {
  const tbody = document.querySelector("#studentsMasterTable tbody");
  if (!tbody) return;

  const students = AdminApp.users.filter(u => u.role === "STUDENT");
  tbody.innerHTML = students.map(s => {
    const studentBookings = AdminApp.registrations.filter(r => r.studentId === s.userId && r.status !== "Cancelled").length;
    return `
      <tr>
        <td><strong>${s.studentId || s.userId}</strong></td>
        <td><strong>${escapeHtml(s.name)}</strong></td>
        <td>${escapeHtml(s.email)}</td>
        <td>${escapeHtml(s.course || 'B.Tech CSE')}</td>
        <td>Semester ${s.semester || 4}</td>
        <td>${escapeHtml(s.phone || '+91 98471 00000')}</td>
        <td>
          <button class="btn btn-outline btn-sm" onclick="alert('Student: ${s.name}\\nRoll: ${s.studentId}\\nTotal Bookings: ${studentBookings}')">Inspect</button>
        </td>
      </tr>
    `;
  }).join("");
}

function renderClubsTable() {
  const tbody = document.querySelector("#clubsMasterTable tbody");
  if (!tbody) return;

  const clubs = AdminApp.users.filter(u => u.role === "COORDINATOR");
  tbody.innerHTML = clubs.map(c => `
    <tr>
      <td>#${c.organizerId || c.userId}</td>
      <td><strong>${escapeHtml(c.clubName || c.name)}</strong></td>
      <td>${escapeHtml(c.email)}</td>
      <td style="max-width:300px; font-size:0.8rem; color:var(--text-muted);">${escapeHtml(c.clubDescription || 'Academic Club')}</td>
      <td><span class="status-badge-confirmed">Active / Recognized</span></td>
      <td>
        <button class="btn btn-outline btn-sm" onclick="alert('Club coordinator for ${escapeHtml(c.clubName || c.name)} is active.')">Details</button>
      </td>
    </tr>
  `).join("");
}

function renderEventsTable() {
  const tbody = document.querySelector("#eventsMasterTable tbody");
  if (!tbody) return;

  tbody.innerHTML = AdminApp.events.map(e => `
    <tr>
      <td>#${e.eventId}</td>
      <td><strong>${escapeHtml(e.title)}</strong></td>
      <td>${escapeHtml(e.organizerClub)}</td>
      <td><span class="event-category-badge">${escapeHtml(e.category)}</span></td>
      <td>${escapeHtml(e.date)} &bull; ${escapeHtml(e.time)}</td>
      <td>${escapeHtml(e.venue)}</td>
      <td>${e.registeredCount || 0} / ${e.capacity}</td>
      <td><span class="status-badge-confirmed">${escapeHtml(e.status)}</span></td>
      <td>
        <button class="btn btn-danger btn-sm" onclick="adminCancelEvent(${e.eventId})">Cancel Event</button>
      </td>
    </tr>
  `).join("");
}

function renderRegistrationsTable() {
  const tbody = document.querySelector("#registrationsMasterTable tbody");
  if (!tbody) return;

  tbody.innerHTML = AdminApp.registrations.map(r => {
    const student = AdminApp.users.find(u => u.userId === r.studentId) || { name: `Student #${r.studentId}`, studentId: r.studentId };
    const event = AdminApp.events.find(e => e.eventId === r.eventId) || { title: `Event #${r.eventId}` };

    return `
      <tr>
        <td>#${r.registrationId}</td>
        <td><strong>${escapeHtml(student.name)}</strong></td>
        <td>Roll ${student.studentId || r.studentId}</td>
        <td>${escapeHtml(event.title)}</td>
        <td>${escapeHtml(r.registrationDate)}</td>
        <td><span class="status-badge-confirmed">${escapeHtml(r.status)}</span></td>
      </tr>
    `;
  }).join("");
}

function renderBroadcastHistory() {
  const tbody = document.querySelector("#broadcastHistoryTable tbody");
  if (!tbody) return;

  const broadcasts = AdminApp.notifications.filter(n => n.userId === 0);
  if (broadcasts.length === 0) {
    tbody.innerHTML = `<tr><td colspan="4" style="text-align:center;">No campus alerts dispatched yet.</td></tr>`;
    return;
  }

  tbody.innerHTML = broadcasts.map(b => `
    <tr>
      <td>#${b.notificationId}</td>
      <td>${escapeHtml(b.message)}</td>
      <td>${escapeHtml(b.dateTime)}</td>
      <td><span class="event-category-badge">All FISAT Students</span></td>
    </tr>
  `).join("");
}

async function adminCancelEvent(eventId) {
  if (!confirm("Are you sure you want to administratively cancel this event?")) return;

  try {
    const res = await fetch(`${AdminApp.backendUrl}/api/events?id=${eventId}`, {
      method: "DELETE"
    });
    if (res.ok) {
      showToast("Event cancelled by Administrator.", "info");
      await loadAdminData();
      renderAll();
      return;
    }
  } catch (e) {
    console.warn("Backend call failed, applying locally:", e);
  }

  // Local fallback
  const found = AdminApp.events.find(e => e.eventId === eventId);
  if (found) {
    found.status = "Cancelled";
    showToast("Event cancelled by Administrator.", "info");
    saveAdminToLocalStorage();
    renderAll();
  }
}

function setupBroadcastForm() {
  const form = document.getElementById("adminBroadcastForm");
  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const title = document.getElementById("broadcastTitle").value.trim();
    const content = document.getElementById("broadcastContent").value.trim();
    const fullMsg = `📢 [FISAT ADMIN ALERT] ${title}: ${content}`;

    try {
      const res = await fetch(`${AdminApp.backendUrl}/api/notifications`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          userId: 0, // Broadcast to all
          message: fullMsg
        })
      });
      if (res.ok) {
        showToast("Official campus announcement broadcasted to all students!", "success");
        form.reset();
        await loadAdminData();
        renderAll();
        return;
      }
    } catch (err) {
      console.warn("Backend call failed, applying locally:", err);
    }

    // Local fallback
    AdminApp.notifications.unshift({
      notificationId: Math.floor(300 + Math.random() * 500),
      userId: 0,
      message: fullMsg,
      dateTime: new Date().toISOString().replace('T', ' ').substring(0, 16),
      isRead: false
    });
    showToast("Official campus announcement broadcasted to all students!", "success");
    form.reset();
    saveAdminToLocalStorage();
    renderAll();
  });
}

function setupModals() {
  const addModal = document.getElementById("addStudentModal");
  document.getElementById("openAddStudentModal")?.addEventListener("click", () => {
    addModal.classList.remove("hidden");
  });
  document.getElementById("closeAddStudentModal")?.addEventListener("click", () => {
    addModal.classList.add("hidden");
  });

  document.getElementById("addStudentForm")?.addEventListener("submit", (e) => {
    e.preventDefault();
    const name = document.getElementById("newStudentName").value.trim();
    const roll = parseInt(document.getElementById("newStudentRoll").value);
    const sem = parseInt(document.getElementById("newStudentSem").value);
    const course = document.getElementById("newStudentCourse").value.trim();
    const email = document.getElementById("newStudentEmail").value.trim();

    AdminApp.users.push({
      userId: roll,
      studentId: roll,
      name: name,
      email: email,
      course: course,
      semester: sem,
      role: "STUDENT",
      phone: "+91 98471 00000"
    });

    addModal.classList.add("hidden");
    document.getElementById("addStudentForm").reset();
    showToast(`Student ${name} (Roll ${roll}) enrolled successfully!`, "success");
    saveAdminToLocalStorage();
    renderAll();
  });
}

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
