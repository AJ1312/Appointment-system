// ============================================================
// MediCare – Shared Frontend Utilities
// API helpers, session management, UI utilities
// ============================================================

const API_BASE = 'http://localhost:8080/api';

// ── Session ──────────────────────────────────────────────────
const Session = {
    set:     (key, val) => localStorage.setItem(key, JSON.stringify(val)),
    get:     (key) => { try { return JSON.parse(localStorage.getItem(key)); } catch { return null; } },
    remove:  (key) => localStorage.removeItem(key),
    getUser: ()    => Session.get('medicare_user'),
    setUser: (u)   => Session.set('medicare_user', u),
    clear:   ()    => localStorage.removeItem('medicare_user')
};

// ── API ───────────────────────────────────────────────────────
const Api = {
    get: async (path) => {
        const res = await fetch(API_BASE + path);
        return res.json();
    },
    post: async (path, body) => {
        const res = await fetch(API_BASE + path, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify(body)
        });
        return res.json();
    },
    put: async (path, params) => {
        const url = API_BASE + path + '?' + new URLSearchParams(params);
        const res = await fetch(url, { method: 'PUT' });
        return res.json();
    }
};

// ── Toast notification ────────────────────────────────────────
function showToast(message, type = 'info') {
    const colors = {
        success: 'rgba(16,185,129,0.92)',
        error:   'rgba(239,68,68,0.92)',
        info:    'rgba(59,130,246,0.92)'
    };
    const toast      = document.createElement('div');
    toast.textContent = message;
    toast.style.cssText = `
        position:fixed;bottom:1.75rem;right:1.75rem;
        padding:0.85rem 1.4rem;border-radius:10px;
        font-family:Inter,sans-serif;font-size:0.875rem;font-weight:500;
        background:${colors[type] || colors.info};color:#fff;
        z-index:9999;box-shadow:0 8px 24px rgba(0,0,0,0.4);
        animation:fadeIn 0.3s ease;
    `;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3200);
}

// ── Button loading state ──────────────────────────────────────
function setLoading(btnId, loading) {
    const btn = document.getElementById(btnId);
    if (!btn) return;
    if (loading) {
        btn.dataset.original = btn.innerHTML;
        btn.innerHTML        = '<span class="spinner"></span>';
        btn.disabled         = true;
    } else {
        btn.innerHTML = btn.dataset.original || btn.innerHTML;
        btn.disabled  = false;
    }
}

// ── Formatting ────────────────────────────────────────────────
function formatDate(dateStr) {
    if (!dateStr) return '—';
    return new Date(dateStr).toLocaleDateString('en-IN', {
        day: 'numeric', month: 'short', year: 'numeric'
    });
}

function statusBadge(status) {
    const map = {
        BOOKED:    'badge-booked',
        WAITING:   'badge-waiting',
        COMPLETED: 'badge-completed',
        CANCELLED: 'badge-cancelled'
    };
    const cls = map[status] || 'badge-booked';
    return `<span class="badge ${cls}">${status}</span>`;
}

// ── Navbar user state ─────────────────────────────────────────
function updateNav() {
    const user    = Session.getUser();
    const loginBtn = document.getElementById('loginBtn');
    if (!loginBtn) return;
    if (user) {
        loginBtn.textContent = user.name.split(' ')[0];
        if (user.role === 'ADMIN') {
            loginBtn.href = 'admin.html';
        } else if (user.role === 'DOCTOR') {
            loginBtn.href = 'doctor.html';
        } else {
            loginBtn.href = 'dashboard.html';
        }
    }
}

updateNav();
