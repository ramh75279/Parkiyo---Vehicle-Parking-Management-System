<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Account Settings</title>
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700;800;900&display=swap"
          rel="stylesheet" />
    <link
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"
            rel="stylesheet" />
    <script>tailwind.config = { darkMode: "class", theme: { extend: { colors: { primary: "#1f68f9", "background-dark": "#020617" }, fontFamily: { display: ["Public Sans", "sans-serif"] } } } }</script>
    <style>
        body {
            font-family: 'Public Sans', sans-serif;
            background-color: #020617;
        }

        .premium-blur {
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
        }

        .bg-subtle-radial {
            background: radial-gradient(circle at 0% 0%, #1e293b 0%, #020617 100%);
        }

        .glass-card {
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }

        .sidebar-container {
            width: 80px;
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            background: rgba(2, 6, 23, 0.6);
            overflow: hidden;
            white-space: nowrap;
        }

        .sidebar-container:hover {
            width: 280px;
            background: rgba(2, 6, 23, 0.95);
        }

        .nav-label {
            opacity: 0;
            transition: opacity 0.3s ease;
            margin-left: 1rem;
        }

        .sidebar-container:hover .nav-label {
            opacity: 1;
        }

        .input-glass {
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            color: white;
            border-radius: 12px;
            padding: 11px 16px;
            font-size: 0.85rem;
            font-weight: 600;
            transition: all 0.2s;
            outline: none;
            width: 100%;
        }

        .input-glass:focus {
            border-color: rgba(31, 104, 249, 0.5);
            box-shadow: 0 0 0 3px rgba(31, 104, 249, 0.1);
        }

        .input-glass::placeholder {
            color: rgba(255, 255, 255, 0.18);
        }

        select.input-glass option {
            background: #0f172a;
        }

        input:-webkit-autofill {
            -webkit-text-fill-color: white !important;
            -webkit-box-shadow: 0 0 0px 1000px #0f172a inset !important;
        }

        label.field-label {
            font-size: 0.7rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.1em;
            color: #64748b;
            margin-bottom: 6px;
            display: block;
        }

        .notify-success {
            background: rgba(16, 185, 129, 0.08);
            border: 1px solid rgba(16, 185, 129, 0.2);
            color: #34d399;
            border-radius: 12px;
            padding: 12px 16px;
            font-size: 0.8rem;
            font-weight: 700;
        }

        .notify-error {
            background: rgba(239, 68, 68, 0.08);
            border: 1px solid rgba(239, 68, 68, 0.2);
            color: #f87171;
            border-radius: 12px;
            padding: 12px 16px;
            font-size: 0.8rem;
            font-weight: 700;
        }

        .tab-btn {
            padding: 10px 20px;
            border-radius: 12px;
            font-size: 0.75rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.08em;
            cursor: pointer;
            transition: all 0.2s;
            color: #64748b;
        }

        .tab-btn.active {
            background: rgba(31, 104, 249, 0.1);
            color: #60a5fa;
            border: 1px solid rgba(31, 104, 249, 0.2);
        }

        .tab-btn:not(.active):hover {
            color: white;
            background: rgba(255, 255, 255, 0.04);
        }

        ::-webkit-scrollbar {
            width: 5px;
            display: none;
        }

        ::-webkit-scrollbar-thumb {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
        }
    </style>
</head>

<body class="text-slate-100 font-display antialiased h-screen flex flex-col overflow-hidden">
<div class="h-1.5 w-full bg-gradient-to-r from-primary via-blue-400 to-primary shrink-0"></div>
<div class="flex flex-1 overflow-hidden">

    <aside class="sidebar-container border-r border-white/5 premium-blur flex flex-col shrink-0 z-50">
        <div class="p-6 mb-4 flex items-center">
            <div
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-[14px] bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-1 overflow-y-auto" id="sidebarNav"></nav>
        <div class="p-4 border-t border-white/5">
            <button onclick="window.location.href='logout.html'"
                    class="flex items-center w-full px-4 py-4 text-rose-500 hover:bg-rose-500/10 rounded-xl text-sm font-black transition-all">
                <span class="material-symbols-outlined shrink-0"><a href="logout.html">power_settings_new</a></span><span
                    class="nav-label"><a href="logout.html">Logout</a></span>
            </button>
        </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden bg-subtle-radial">
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div>
                <h2 class="text-xl font-black text-white">Account Settings</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Manage your
                    profile</p>
            </div>
            <div class="flex items-center gap-4">
                <div class="text-right">
                    <p class="text-xs font-black text-white uppercase tracking-widest" id="headerName">Kamal Perera
                    </p>
                    <p class="text-[10px] text-primary font-bold uppercase tracking-tighter" id="roleBadge">User</p>
                </div>
                <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-3xl mx-auto space-y-6">

                <!-- Tab bar -->
                <div class="flex gap-2 flex-wrap">
                    <button class="tab-btn active" onclick="showTab('profile',this)">Profile</button>
                    <button class="tab-btn" onclick="showTab('password',this)">Password</button>
                    <button class="tab-btn" onclick="showTab('notifications',this)">Notifications</button>
                    <button class="tab-btn" onclick="showTab('security',this)">Security</button>
                </div>

                <!-- ── PROFILE TAB ── -->
                <div id="tab-profile">
                    <div class="glass-card rounded-[2.5rem] p-8 mb-6">
                        <h3 class="text-base font-black text-white mb-6">Profile Photo</h3>
                        <div class="flex items-center gap-8">
                            <div class="relative">
                                <div
                                        class="h-24 w-24 rounded-[20px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                                    <div class="h-full w-full rounded-[18px] bg-slate-900 flex items-center justify-center overflow-hidden"
                                         id="avatarPreview">
                                        <span class="material-symbols-outlined text-white/30 text-4xl">person</span>
                                    </div>
                                </div>
                                <button onclick="document.getElementById('photoInput').click()"
                                        class="absolute -bottom-2 -right-2 h-8 w-8 rounded-full bg-primary flex items-center justify-center shadow-lg hover:scale-110 transition-all">
                                    <span class="material-symbols-outlined text-white text-sm">edit</span>
                                </button>
                                <input type="file" id="photoInput" accept="image/*" class="hidden"
                                       onchange="previewPhoto(this)" />
                            </div>
                            <div>
                                <p class="text-sm font-black text-white mb-1" id="avatarName">Kamal Perera</p>
                                <p class="text-xs text-slate-500 font-bold mb-4" id="avatarRole">User · PKW-00441-KP
                                </p>
                                <button onclick="document.getElementById('photoInput').click()"
                                        class="bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">Upload
                                    Photo</button>
                            </div>
                        </div>
                    </div>

                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-6">Personal Information</h3>
                        <div id="profileMsg" class="hidden mb-5"></div>
                        <div class="grid grid-cols-2 gap-5 mb-5">
                            <div>
                                <label class="field-label">First Name</label>
                                <input type="text" placeholder="John" class="input-glass bg-white/10 rounded-2xl"
                                       style="background:rgba(255,255,255,0.02);color:#64748b;" />
                            </div>
                            <div>
                                <label class="field-label">Last Name</label>
                                <input type="text" placeholder="Doe" class="input-glass bg-white/5 rounded-2xl"
                                       style="background:rgba(255,255,255,0.02);color:#64748b;" />
                            </div>
                            <div>
                                <label class="field-label">Email Address</label>
                                <input type="email" placeholder="you@parkiyo.com"
                                       class="input-glass bg-white/5 rounded-2xl"
                                       style="background:rgba(255,255,255,0.02);color:#64748b;" />
                            </div>
                            <div>
                                <label class="field-label">Phone Number</label>
                                <input type="tel" placeholder="+94 77 123 4567"
                                       class="input-glass bg-white/5 rounded-2xl"
                                       style="background:rgba(255,255,255,0.02);color:#64748b;" />
                            </div>
                            <div>
                                <label class="field-label">Role</label>
                                <input type="text" id="roleField" value="User"
                                       class="input-glass bg-white/5 rounded-2xl"
                                       style="background:rgba(255,255,255,0.02);color:#64748b;" readonly />
                            </div>
                            <div>
                                <label class="field-label">Language</label>
                                <select class="input-glass">
                                    <option selected>English</option>
                                    <option>Sinhala</option>
                                    <option>Tamil</option>
                                </select>
                            </div>
                        </div>
                        <button onclick="showMsg('profileMsg','Profile updated successfully!','success')"
                                class="bg-primary text-white font-black px-8 py-3 rounded-2xl hover:bg-primary/80 transition-all text-sm shadow-[0_0_16px_rgba(31,104,249,0.3)]">Save
                            Changes</button>
                    </div>
                </div>

                <!-- ── PASSWORD TAB ── -->
                <div id="tab-password" class="hidden">
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-6">Change Password</h3>
                        <div id="pwMsg" class="hidden mb-5"></div>
                        <div class="space-y-5 max-w-md">
                            <div>
                                <label class="field-label">Current Password</label>
                                <input type="password" placeholder="••••••••" class="input-glass bg-white/5 rounded-2xl"/>
                            </div>
                            <div>
                                <label class="field-label">New Password</label>
                                <input type="password" placeholder="Min. 8 characters" class="input-glass bg-white/5 rounded-2xl"
                                       id="newPw" />
                            </div>
                            <div>
                                <label class="field-label">Confirm New Password</label>
                                <input type="password" placeholder="Repeat new password" class="input-glass bg-white/5 rounded-2xl"
                                       id="confirmPw" />
                            </div>
                            <div
                                    class="p-4 rounded-2xl bg-white/[0.02] border border-white/5 text-xs text-slate-500 font-bold space-y-1">
                                <p>· At least 8 characters</p>
                                <p>· One uppercase letter</p>
                                <p>· One number or symbol</p>
                            </div>
                            <button onclick="checkPw()"
                                    class="bg-primary text-white font-black px-8 py-3 rounded-2xl hover:bg-primary/80 transition-all text-sm shadow-[0_0_16px_rgba(31,104,249,0.3)]">Update
                                Password</button>
                        </div>
                    </div>
                </div>

                <!-- ── NOTIFICATIONS TAB ── -->
                <div id="tab-notifications" class="hidden">
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-6">Notification Preferences</h3>
                        <div class="space-y-0 divide-y divide-white/5">
                            <div class="flex items-center justify-between py-5">
                                <div>
                                    <p class="text-sm font-black text-white">Payment receipts</p>
                                    <p class="text-xs text-slate-500 font-bold mt-0.5">Email receipt on every
                                        payment</p>
                                </div>
                                <label class="relative inline-flex items-center cursor-pointer"><input
                                        type="checkbox" class="sr-only peer" checked />
                                    <div
                                            class="w-11 h-6 bg-white/10 peer-checked:bg-primary rounded-full transition-all after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-5">
                                    </div>
                                </label>
                            </div>
                            <div class="flex items-center justify-between py-5">
                                <div>
                                    <p class="text-sm font-black text-white">Reservation reminders</p>
                                    <p class="text-xs text-slate-500 font-bold mt-0.5">1 hour before your booking
                                    </p>
                                </div>
                                <label class="relative inline-flex items-center cursor-pointer"><input
                                        type="checkbox" class="sr-only peer" checked />
                                    <div
                                            class="w-11 h-6 bg-white/10 peer-checked:bg-primary rounded-full transition-all after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-5">
                                    </div>
                                </label>
                            </div>
                            <div class="flex items-center justify-between py-5">
                                <div>
                                    <p class="text-sm font-black text-white">System alerts</p>
                                    <p class="text-xs text-slate-500 font-bold mt-0.5">Downtime, maintenance notices
                                    </p>
                                </div>
                                <label class="relative inline-flex items-center cursor-pointer"><input
                                        type="checkbox" class="sr-only peer" />
                                    <div
                                            class="w-11 h-6 bg-white/10 peer-checked:bg-primary rounded-full transition-all after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-5">
                                    </div>
                                </label>
                            </div>
                            <div class="flex items-center justify-between py-5">
                                <div>
                                    <p class="text-sm font-black text-white">Wallet low balance</p>
                                    <p class="text-xs text-slate-500 font-bold mt-0.5">Alert when balance drops
                                        below $10</p>
                                </div>
                                <label class="relative inline-flex items-center cursor-pointer"><input
                                        type="checkbox" class="sr-only peer" checked />
                                    <div
                                            class="w-11 h-6 bg-white/10 peer-checked:bg-primary rounded-full transition-all after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-5">
                                    </div>
                                </label>
                            </div>
                            <div class="flex items-center justify-between py-5">
                                <div>
                                    <p class="text-sm font-black text-white">Marketing & promotions</p>
                                    <p class="text-xs text-slate-500 font-bold mt-0.5">Deals, offers and news</p>
                                </div>
                                <label class="relative inline-flex items-center cursor-pointer"><input
                                        type="checkbox" class="sr-only peer" />
                                    <div
                                            class="w-11 h-6 bg-white/10 peer-checked:bg-primary rounded-full transition-all after:content-[''] after:absolute after:top-0.5 after:left-0.5 after:bg-white after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:after:translate-x-5">
                                    </div>
                                </label>
                            </div>
                        </div>
                        <button onclick="showMsg('profileMsg','Notification preferences saved!','success')"
                                class="mt-6 bg-primary text-white font-black px-8 py-3 rounded-2xl hover:bg-primary/80 transition-all text-sm">Save
                            Preferences</button>
                    </div>
                </div>

                <!-- ── SECURITY TAB ── -->
                <div id="tab-security" class="hidden">
                    <div class="glass-card rounded-[2.5rem] p-8 mb-6">
                        <h3 class="text-base font-black text-white mb-6">Two-Factor Authentication</h3>
                        <div
                                class="flex items-center justify-between p-5 rounded-2xl bg-emerald-500/5 border border-emerald-500/15 mb-5">
                            <div class="flex items-center gap-4">
                                <div
                                        class="h-11 w-11 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center">
                                    <span class="material-symbols-outlined text-emerald-400">verified_user</span>
                                </div>
                                <div>
                                    <p class="text-sm font-black text-white">2FA is enabled</p>
                                    <p class="text-xs text-slate-500 font-bold mt-0.5">Via authenticator app</p>
                                </div>
                            </div>
                            <button
                                    class="bg-rose-500/10 border border-rose-500/20 text-rose-400 font-black px-5 py-2 rounded-xl text-xs hover:bg-rose-500/20 transition-all">Disable</button>
                        </div>
                        <p class="text-xs text-slate-500 font-bold">Two-factor authentication adds an extra layer of
                            security to your account.</p>
                    </div>

                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-2">Active Sessions</h3>
                        <p class="text-xs text-slate-500 font-bold mb-6">Devices currently signed in to your
                            account.</p>
                        <div class="space-y-3">
                            <div
                                    class="flex items-center justify-between p-4 rounded-2xl bg-primary/5 border border-primary/15">
                                <div class="flex items-center gap-3">
                                    <span class="material-symbols-outlined text-primary">computer</span>
                                    <div>
                                        <p class="text-sm font-black text-white">Chrome · Windows 11</p>
                                        <p class="text-[10px] text-slate-500 font-bold">Colombo, LK · Active now</p>
                                    </div>
                                </div>
                                <span
                                        class="text-[10px] font-black text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-2.5 py-1 rounded-lg">Current</span>
                            </div>
                            <div
                                    class="flex items-center justify-between p-4 rounded-2xl bg-white/[0.02] border border-white/5">
                                <div class="flex items-center gap-3">
                                    <span class="material-symbols-outlined text-slate-400">smartphone</span>
                                    <div>
                                        <p class="text-sm font-black text-white">Safari · iPhone</p>
                                        <p class="text-[10px] text-slate-500 font-bold">Colombo, LK · 2 days ago</p>
                                    </div>
                                </div>
                                <button
                                        class="text-rose-400 font-black text-xs hover:brightness-125 transition-all">Revoke</button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>
</div>

<script>
    // ── Tab switching ──────────────────────────────────────
    function showTab(name, btn) {
        ['profile', 'password', 'notifications', 'security'].forEach(t => {
            document.getElementById('tab-' + t).classList.add('hidden');
        });
        document.getElementById('tab-' + name).classList.remove('hidden');
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
    }

    function showMsg(id, text, type) {
        const el = document.getElementById(id);
        el.className = type === 'success' ? 'notify-success mb-5 flex items-center gap-2' : 'notify-error mb-5 flex items-center gap-2';
        el.innerHTML = '<span class="material-symbols-outlined text-lg">' + (type === 'success' ? 'check_circle' : 'error') + '</span>' + text;
        el.classList.remove('hidden');
        setTimeout(() => el.classList.add('hidden'), 3500);
    }

    function checkPw() {
        const np = document.getElementById('newPw').value;
        const cp = document.getElementById('confirmPw').value;
        if (np.length < 8) {
            showMsg('pwMsg', 'Password must be at least 8 characters.', 'error');
        } else if (np !== cp) {
            showMsg('pwMsg', 'New passwords do not match.', 'error');
        } else {
            showMsg('pwMsg', 'Password updated successfully!', 'success');
        }
    }

    function previewPhoto(input) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = e => {
                const p = document.getElementById('avatarPreview');
                p.innerHTML = '<img src="' + e.target.result + '" class="h-full w-full object-cover"/>';
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    // ── Role-aware sidebar ─────────────────────────────────
    (function buildSidebar() {
        const params = new URLSearchParams(window.location.search);
        let role = params.get('role') || localStorage.getItem('parkiyo_role') || 'user';
        localStorage.setItem('parkiyo_role', role);

        const nav = document.getElementById('sidebarNav');
        const a = 'flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold';
        const n = 'flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all';

        const lnk = (href, icon, label, active) =>
            `<a href="${href}" class="${active ? a : n}"><span class="material-symbols-outlined shrink-0">${icon}</span><span class="nav-label text-sm">${label}</span></a>`;

        const adminLinks = [
            lnk('dashboard_user.html', 'dashboard', 'Dashboard', false),
            lnk('entry.html', 'login', 'Vehicle Entry', false),
            lnk('exitvehicle.html', 'logout', 'Vehicle Exit', false),
            lnk('parking.html', 'local_parking', 'Active Parking', false),
            lnk('advancereservation.html', 'event_available', 'Reservation', false),
            lnk('paymenthistory_user.html', 'payments', 'Payments', false),
            lnk('receipt.html', 'receipt_long', 'Receipts', false),
            lnk('walletoverview.html', 'account_balance_wallet', 'Wallet', false),
            lnk('notification_user.html', 'notifications', 'Notifications', false),
            lnk('accountsetting_user.html', 'settings', 'Account Settings', true),
        ];

        const userLinks = [
            lnk('dashboard_user.html', 'dashboard', 'Dashboard', false),
            lnk('entry.html', 'login', 'Vehicle Entry', false),
            lnk('exitvehicle.html', 'logout', 'Vehicle Exit', false),
            lnk('parking.html', 'local_parking', 'Active Parking', false),
            lnk('advancereservation.html', 'event_available', 'Reservations', false),
            lnk('paymenthistory.html', 'payments', 'Payments', false),
            lnk('walletoverview.html', 'account_balance_wallet', 'Wallet', false),
            lnk('assistant.html', 'smart_toy', 'AI Assistant', false),
            lnk('accountsetting.html', 'settings', 'Settings', true),
        ];

        nav.innerHTML = (role === 'admin' ? adminLinks : userLinks).join('');

        // Update header badges
        document.getElementById('roleBadge').textContent = role === 'admin' ? 'Admin' : 'User';
        document.getElementById('headerName').textContent = role === 'admin' ? 'Alex Johnson' : 'Kamal Perera';
        document.getElementById('avatarName').textContent = role === 'admin' ? 'Alex Johnson' : 'Kamal Perera';
        document.getElementById('avatarRole').textContent = role === 'admin' ? 'Admin · PKW-00441-AJ' : 'User · PKW-00441-KP';
        document.getElementById('roleField').value = role === 'admin' ? 'Administrator' : 'User';
    })();
</script>

<script>
    (function buildSidebar() {
        const params = new URLSearchParams(window.location.search);
        let role = params.get('role') || localStorage.getItem('parkiyo_role') || 'user';
        localStorage.setItem('parkiyo_role', role);

        const nav = document.getElementById('sidebarNav');
        if (!nav) return;

        const a = 'flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group';
        const n = 'flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group';

        const lnk = (href, icon, label, isActive) =>
            `<a href="${href}" class="${isActive ? a : n}"><span class="material-symbols-outlined shrink-0">${icon}</span><span class="nav-label text-sm">${label}</span></a>`;

        // ── USER SIDEBAR (exact from dashboard_user.html reference) ──
        const userLinks = [
            lnk('dashboard_user.html', 'dashboard', 'Dashboard', false),
            lnk('entry.html', 'login', 'Vehicle Entry', false),
            lnk('exitvehicle.html', 'logout', 'Vehicle Exit', false),
            lnk('parking.html', 'local_parking', 'Active Parking', false),
            lnk('advancereservation.html', 'event_available', 'Reservation', false),
            lnk('paymenthistory_user.html', 'payments', 'Payments', false),
            lnk('receipt.html', 'receipt_long', 'Receipts', false),
            lnk('walletoverview.html', 'account_balance_wallet', 'Wallet', false),
            lnk('notification_user.html', 'notifications', 'Notifications', false),
            lnk('accountsetting_user.html', 'settings', 'Account Settings', true),
        ];

        // ── ADMIN SIDEBAR ──
        const adminLinks = [
            lnk('dashboard_user.html', 'dashboard', 'Dashboard', false),
            lnk('entry.html', 'login', 'Vehicle Entry', false),
            lnk('exitvehicle.html', 'logout', 'Vehicle Exit', false),
            lnk('parking.html', 'local_parking', 'Active Parking', false),
            lnk('advancereservation.html', 'event_available', 'Reservation', false),
            lnk('paymenthistory_user.html', 'payments', 'Payments', false),
            lnk('receipt.html', 'receipt_long', 'Receipts', false),
            lnk('walletoverview.html', 'account_balance_wallet', 'Wallet', false),
            lnk('notification_user.html', 'notifications', 'Notifications', false),
            lnk('accountsetting_user.html', 'settings', 'Account Settings', true),
        ];

        nav.innerHTML = (role === 'admin' ? adminLinks : userLinks).join('');

        // Update role badge if present
        const badge = document.getElementById('roleBadge');
        if (badge) badge.textContent = role === 'admin' ? 'Admin' : 'User';
    })();
</script>

</body>

</html>