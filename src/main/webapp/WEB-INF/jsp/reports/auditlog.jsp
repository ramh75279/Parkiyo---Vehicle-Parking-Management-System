<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Audit Log</title>
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" rel="stylesheet"/>
    <script>
        tailwind.config = {
            darkMode: "class",
            theme: {
                extend: {
                    colors: { primary: "#1f68f9", "background-dark": "#020617" },
                    fontFamily: { display: ["Public Sans", "sans-serif"] },
                    borderRadius: { squircle: "14px" },
                },
            },
        }
    </script>
    <style>
        body { font-family: 'Public Sans', sans-serif; background-color: #020617; }
        .premium-blur { backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px); }
        .bg-subtle-radial { background: radial-gradient(circle at 0% 0%, #1e293b 0%, #020617 100%); }
        .glass-card { background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.08); }
        .sidebar-container { width: 80px; transition: all 0.4s cubic-bezier(0.4,0,0.2,1); background: rgba(2,6,23,0.6); overflow: hidden; white-space: nowrap; }
        .sidebar-container:hover { width: 280px; background: rgba(2,6,23,0.95); }
        .nav-label { opacity: 0; transition: opacity 0.3s ease; margin-left: 1rem; }
        .sidebar-container:hover .nav-label { opacity: 1; }
        .input-glass { background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); color: white; border-radius: 12px; padding: 10px 16px; font-size: 0.8rem; font-weight: 700; transition: all 0.2s; outline: none; }
        .input-glass:focus { border-color: rgba(31,104,249,0.5); box-shadow: 0 0 0 3px rgba(31,104,249,0.1); }
        .input-glass::placeholder { color: rgba(255,255,255,0.2); }
        .badge { padding: 3px 10px; border-radius: 8px; font-size: 0.68rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.05em; }
        .badge-create { background: rgba(16,185,129,0.12); color: #34d399; border: 1px solid rgba(16,185,129,0.2); }
        .badge-update { background: rgba(31,104,249,0.12); color: #60a5fa; border: 1px solid rgba(31,104,249,0.2); }
        .badge-delete { background: rgba(239,68,68,0.12); color: #f87171; border: 1px solid rgba(239,68,68,0.2); }
        .badge-login { background: rgba(139,92,246,0.12); color: #a78bfa; border: 1px solid rgba(139,92,246,0.2); }
        .badge-export { background: rgba(245,158,11,0.12); color: #fbbf24; border: 1px solid rgba(245,158,11,0.2); }
        .badge-system { background: rgba(100,116,139,0.15); color: #94a3b8; border: 1px solid rgba(100,116,139,0.2); }
        .log-row { transition: background 0.15s; cursor: pointer; }
        .log-row:hover { background: rgba(255,255,255,0.025); }
        ::-webkit-scrollbar { width: 5px;display: none; } ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }

        /* ── Date input dark override ─────────────────────────────── */
        input[type="date"].input-glass,
        input[type="datetime-local"].input-glass {
            background: rgba(255,255,255,0.04) !important;
            color: rgba(148,163,184,0.85) !important;
            color-scheme: dark;
        }
        input[type="date"].input-glass:focus,
        input[type="datetime-local"].input-glass:focus {
            background: rgba(31,104,249,0.07) !important;
            color: white !important;
        }
        input[type="date"].input-glass::-webkit-calendar-picker-indicator {
            filter: invert(0.6) sepia(1) saturate(3) hue-rotate(190deg);
            opacity: 0.5;
            cursor: pointer;
        }
        input[type="date"].input-glass::-webkit-datetime-edit { color: rgba(148,163,184,0.85); }
        input[type="date"].input-glass::-webkit-datetime-edit-fields-wrapper { color: rgba(148,163,184,0.85); }
        input[type="date"].input-glass::-webkit-datetime-edit-text { color: rgba(100,116,139,0.7); }
        input[type="date"].input-glass::-webkit-datetime-edit-month-field,
        input[type="date"].input-glass::-webkit-datetime-edit-day-field,
        input[type="date"].input-glass::-webkit-datetime-edit-year-field {
            color: rgba(148,163,184,0.85);
        }
        input[type="date"].input-glass::-webkit-datetime-edit-month-field:focus,
        input[type="date"].input-glass::-webkit-datetime-edit-day-field:focus,
        input[type="date"].input-glass::-webkit-datetime-edit-year-field:focus {
            background: rgba(31,104,249,0.2);
            color: white;
            border-radius: 4px;
        }
    </style>
</head>
<body class="text-slate-100 font-display antialiased h-screen flex flex-col overflow-hidden">
<div class="h-1.5 w-full bg-gradient-to-r from-primary via-blue-400 to-primary shrink-0"></div>
<div class="flex flex-1 overflow-hidden">

    <!-- SIDEBAR -->
    <aside class="sidebar-container border-r border-white/5 premium-blur flex flex-col shrink-0 z-50">
        <div class="p-6 mb-4 flex items-center">
            <div class="flex h-10 w-10 shrink-0 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-1 overflow-y-auto">
            <a href="dashboard_admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">dashboard</span><span class="nav-label text-sm">Dashboard</span></a>
            <a href="entry_admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle Entry</span></a>
            <a href="exitvehicle_admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle Exit</span></a>
            <a href="slot_overview.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">grid_view</span><span class="nav-label text-sm">Parking Slots</span></a>
            <a href="usermanagement.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">group</span><span class="nav-label text-sm">Users</span></a>
            <a href="paymenthistory.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">payments</span><span class="nav-label text-sm">Payments</span></a>
            <a href="Repportshubpage.html" class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span class="material-symbols-outlined shrink-0">bar_chart</span><span class="nav-label text-sm">Reports</span></a>
            <a href="systemstatuspage.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">monitor_heart</span><span class="nav-label text-sm">System Status</span></a>
            <a href="accountsetting.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">settings</span><span class="nav-label text-sm">Settings</span></a>
        </nav>
        <div class="p-4 border-t border-white/5">
            <button onclick="window.location.href='logout.html'" class="flex items-center w-full px-4 py-4 text-rose-500 hover:bg-rose-500/10 rounded-xl text-sm font-black transition-all">
                <span class="material-symbols-outlined shrink-0"><a href="logout.html">power_settings_new</a></span><span class="nav-label"><a href="logout.html">Logout</a></span>
            </button>
        </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden bg-subtle-radial">
        <!-- TOPBAR -->
        <header class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div class="flex items-center gap-4">
                <button onclick="window.location.href='Repportshubpage.html'" class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Audit Log</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">System Activity & Event Trail</p>
                </div>
            </div>
            <div class="flex items-center gap-4">
                <button class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">download</span> Export CSV
                </button>
                <div class="flex items-center gap-4">
                    <div class="text-right hidden xl:block">
                        <p class="text-xs font-black text-white uppercase tracking-widest">Alex Johnson</p>
                        <p class="text-[10px] text-primary font-bold uppercase tracking-tighter">Admin</p>
                    </div>
                    <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                        <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                            <span class="material-symbols-outlined text-white/50">person</span>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">

            <!-- Stats -->
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Today's Events</p>
                    <h3 class="text-3xl font-black text-white">248</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-1">Last 24 hours</p>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Active Users</p>
                    <h3 class="text-3xl font-black text-emerald-400">14</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-1">Currently logged in</p>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Warnings</p>
                    <h3 class="text-3xl font-black text-amber-400">3</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-1">Needs review</p>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Critical Events</p>
                    <h3 class="text-3xl font-black text-rose-400">0</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-1">Last 24 hours</p>
                </div>
            </div>

            <!-- Filters -->
            <div class="glass-card rounded-[2rem] p-6 mb-6 flex flex-wrap gap-4 items-center">
                <div class="flex-1 min-w-[200px]">
                    <input type="text" placeholder="Search by user, action, or IP…" class="input-glass bg-white/5 backdrop-blur-md border border-white/20 rounded-2xl px-4 h-12 w-full" />
                </div>
                <select class="input-glass rounded-2xl px-4 h-12">
                    <option value="">All Event Types</option>
                    <option>Login</option>
                    <option>Create</option>
                    <option>Update</option>
                    <option>Delete</option>
                    <option>Export</option>
                    <option>System</option>
                </select>
                <select class="input-glass rounded-2xl px-4 h-12">
                    <option>All Users</option>
                    <option>Alex Johnson (Admin)</option>
                    <option>Maria Santos (Operator)</option>
                    <option>Dev Chen (Operator)</option>
                </select>
                <input type="date" class="input-glass px-4 h-12 rounded-2xl" value="2026-03-09" />
                <button class="bg-primary text-white font-black px-6 py-2.5 rounded-xl text-xs uppercase tracking-widest hover:bg-primary/80 transition-all">Filter</button>
                <button class="text-slate-400 font-bold text-xs hover:text-white transition-all">Clear</button>
            </div>

            <!-- Log Table -->
            <div class="glass-card rounded-[2.5rem] overflow-hidden">
                <div class="p-8 border-b border-white/5 flex items-center justify-between">
                    <h3 class="text-lg font-black text-white">Event Log</h3>
                    <span class="text-[10px] font-black uppercase tracking-widest text-slate-500">248 events today · Click a row for details</span>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full text-left">
                        <thead>
                        <tr class="text-slate-500 text-[10px] font-black uppercase tracking-widest border-b border-white/5">
                            <th class="px-8 py-5">Timestamp</th>
                            <th class="px-8 py-5">User</th>
                            <th class="px-8 py-5">Role</th>
                            <th class="px-8 py-5">Event Type</th>
                            <th class="px-8 py-5">Action</th>
                            <th class="px-8 py-5">IP Address</th>
                            <th class="px-8 py-5">Details</th>
                        </tr>
                        </thead>
                        <tbody class="text-sm font-bold text-slate-300 divide-y divide-white/5">
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 14:32:01</td>
                            <td class="px-8 py-5 text-white font-black">Alex Johnson</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-primary uppercase tracking-wider">Admin</span></td>
                            <td class="px-8 py-5"><span class="badge badge-update">Update</span></td>
                            <td class="px-8 py-5 text-slate-300">Updated slot A-12 to Maintenance</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">192.168.1.10</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 14:15:44</td>
                            <td class="px-8 py-5 text-white font-black">Maria Santos</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-emerald-400 uppercase tracking-wider">Operator</span></td>
                            <td class="px-8 py-5"><span class="badge badge-create">Create</span></td>
                            <td class="px-8 py-5 text-slate-300">Registered vehicle ABC-9910</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">192.168.1.22</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 13:58:30</td>
                            <td class="px-8 py-5 text-white font-black">Dev Chen</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-emerald-400 uppercase tracking-wider">Operator</span></td>
                            <td class="px-8 py-5"><span class="badge badge-login">Login</span></td>
                            <td class="px-8 py-5 text-slate-300">User logged in successfully</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">10.0.0.55</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 13:41:10</td>
                            <td class="px-8 py-5 text-white font-black">Alex Johnson</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-primary uppercase tracking-wider">Admin</span></td>
                            <td class="px-8 py-5"><span class="badge badge-export">Export</span></td>
                            <td class="px-8 py-5 text-slate-300">Exported revenue report (PDF)</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">192.168.1.10</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 13:20:05</td>
                            <td class="px-8 py-5 text-white font-black">Alex Johnson</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-primary uppercase tracking-wider">Admin</span></td>
                            <td class="px-8 py-5"><span class="badge badge-delete">Delete</span></td>
                            <td class="px-8 py-5 text-slate-300">Deleted user account ID #0041</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">192.168.1.10</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 12:55:18</td>
                            <td class="px-8 py-5 text-slate-500 font-black italic">System</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-slate-500 uppercase tracking-wider">System</span></td>
                            <td class="px-8 py-5"><span class="badge badge-system">System</span></td>
                            <td class="px-8 py-5 text-slate-300">Automated backup completed</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">—</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 12:30:47</td>
                            <td class="px-8 py-5 text-white font-black">Maria Santos</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-emerald-400 uppercase tracking-wider">Operator</span></td>
                            <td class="px-8 py-5"><span class="badge badge-update">Update</span></td>
                            <td class="px-8 py-5 text-slate-300">Processed exit for vehicle XYZ-8899</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">192.168.1.22</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        <tr class="log-row" onclick="window.location.href='auditlogdeatils.html'">
                            <td class="px-8 py-5 text-slate-400 text-xs font-black whitespace-nowrap">2026-03-09 11:44:09</td>
                            <td class="px-8 py-5 text-white font-black">Alex Johnson</td>
                            <td class="px-8 py-5"><span class="text-[10px] font-black text-primary uppercase tracking-wider">Admin</span></td>
                            <td class="px-8 py-5"><span class="badge badge-create">Create</span></td>
                            <td class="px-8 py-5 text-slate-300">Created new user account for K. Perera</td>
                            <td class="px-8 py-5 text-slate-500 font-mono text-xs">192.168.1.10</td>
                            <td class="px-8 py-5"><span class="text-primary text-xs hover:underline">View →</span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <!-- Pagination -->
                <div class="px-8 py-5 border-t border-white/5 flex items-center justify-between">
                    <p class="text-xs font-bold text-slate-500">Showing 8 of 248 events · Page 1 of 31</p>
                    <div class="flex items-center gap-2">
                        <button class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center text-slate-400 hover:bg-white/10 transition-all disabled:opacity-30" disabled>
                            <span class="material-symbols-outlined text-sm">chevron_left</span>
                        </button>
                        <button class="h-9 px-4 rounded-xl bg-primary text-white font-black text-xs">1</button>
                        <button class="h-9 px-4 rounded-xl bg-white/5 border border-white/10 text-slate-400 font-black text-xs hover:bg-white/10 transition-all">2</button>
                        <button class="h-9 px-4 rounded-xl bg-white/5 border border-white/10 text-slate-400 font-black text-xs hover:bg-white/10 transition-all">3</button>
                        <span class="text-slate-500 text-xs font-bold px-1">…</span>
                        <button class="h-9 px-4 rounded-xl bg-white/5 border border-white/10 text-slate-400 font-black text-xs hover:bg-white/10 transition-all">31</button>
                        <button class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center text-slate-400 hover:bg-white/10 transition-all">
                            <span class="material-symbols-outlined text-sm">chevron_right</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
</body>
</html>
