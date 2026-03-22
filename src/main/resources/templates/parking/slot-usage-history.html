<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Slot Usage History</title>
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700;800;900&display=swap"
          rel="stylesheet" />
    <link
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"
            rel="stylesheet" />
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
            padding: 10px 16px;
            font-size: 0.8rem;
            font-weight: 700;
            transition: all 0.2s;
            outline: none;
        }

        .input-glass:focus {
            border-color: rgba(31, 104, 249, 0.5);
            box-shadow: 0 0 0 3px rgba(31, 104, 249, 0.1);
        }

        .input-glass::placeholder {
            color: rgba(255, 255, 255, 0.2);
        }

        select.input-glass option {
            background: #0f172a;
        }

        .status-paid {
            background: rgba(31, 104, 249, 0.1);
            color: #60a5fa;
            border: 1px solid rgba(31, 104, 249, 0.2);
        }

        .status-active {
            background: rgba(16, 185, 129, 0.1);
            color: #34d399;
            border: 1px solid rgba(16, 185, 129, 0.2);
        }

        .status-pill {
            padding: 3px 11px;
            border-radius: 9px;
            font-size: 0.67rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .usage-row {
            transition: background 0.15s;
            cursor: pointer;
        }

        .usage-row:hover {
            background: rgba(255, 255, 255, 0.025);
        }

        ::-webkit-scrollbar {
            width: 5px;
            display: none;
        }

        ::-webkit-scrollbar-thumb {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
        }

        /* ── Date input dark override ─────────────────────────────── */
        input[type="date"].input-glass,
        input[type="datetime-local"].input-glass {
            background: rgba(255, 255, 255, 0.04) !important;
            color: rgba(148, 163, 184, 0.85) !important;
            color-scheme: dark;
        }

        input[type="date"].input-glass:focus,
        input[type="datetime-local"].input-glass:focus {
            background: rgba(31, 104, 249, 0.07) !important;
            color: white !important;
        }

        input[type="date"].input-glass::-webkit-calendar-picker-indicator {
            filter: invert(0.6) sepia(1) saturate(3) hue-rotate(190deg);
            opacity: 0.5;
            cursor: pointer;
        }

        input[type="date"].input-glass::-webkit-datetime-edit {
            color: rgba(148, 163, 184, 0.85);
        }

        input[type="date"].input-glass::-webkit-datetime-edit-fields-wrapper {
            color: rgba(148, 163, 184, 0.85);
        }

        input[type="date"].input-glass::-webkit-datetime-edit-text {
            color: rgba(100, 116, 139, 0.7);
        }

        input[type="date"].input-glass::-webkit-datetime-edit-month-field,
        input[type="date"].input-glass::-webkit-datetime-edit-day-field,
        input[type="date"].input-glass::-webkit-datetime-edit-year-field {
            color: rgba(148, 163, 184, 0.85);
        }

        input[type="date"].input-glass::-webkit-datetime-edit-month-field:focus,
        input[type="date"].input-glass::-webkit-datetime-edit-day-field:focus,
        input[type="date"].input-glass::-webkit-datetime-edit-year-field:focus {
            background: rgba(31, 104, 249, 0.2);
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
            <div
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-1 overflow-y-auto">
            <a href="dashboard-admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">dashboard</span><span
                    class="nav-label text-sm">Dashboard</span></a>
            <a href="entry.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle
                        Entry</span></a>
            <a href="exitvehicle.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle
                        Exit</span></a>
            <a href="vehicle-list-page.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">directions_car</span><span
                    class="nav-label text-sm">Vehicles</span></a>
            <a href="slot-list.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span
                    class="material-symbols-outlined shrink-0">grid_view</span><span
                    class="nav-label text-sm">Parking Slots</span></a>
            <a href="usermanagement.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">group</span><span
                    class="nav-label text-sm">Users</span></a>
            <a href="paymenthistory.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">payments</span><span
                    class="nav-label text-sm">Payments</span></a>
            <a href="repportshubpage.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">bar_chart</span><span
                    class="nav-label text-sm">Reports</span></a>
            <a href="accountsetting.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">settings</span><span
                    class="nav-label text-sm">Settings</span></a>
        </nav>
        <div class="p-4 border-t border-white/5">
            <button onclick="window.location.href='logout.html'"
                    class="flex items-center w-full px-4 py-4 text-rose-500 hover:bg-rose-500/10 rounded-xl text-sm font-black transition-all">
                    <span class="material-symbols-outlined shrink-0"><a
                            href="logout.html">power_settings_new</a></span><span class="nav-label"><a
                    href="logout.html">Logout</a></span>
            </button>
        </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden bg-subtle-radial">
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div class="flex items-center gap-4">
                <button onclick="window.location.href='edit-slot.html'"
                        class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Slot Usage History</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">SLT-00012 ·
                        A-12 · Zone A</p>
                </div>
            </div>
            <div class="flex items-center gap-3">
                <button
                        class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">download</span> Export CSV
                </button>
                <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">

            <!-- Slot identity + stats -->
            <div class="glass-card rounded-[2.5rem] p-8 mb-8">
                <div class="flex items-center gap-6 flex-wrap">
                    <div
                            class="h-16 w-16 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-primary text-3xl">grid_view</span>
                    </div>
                    <div class="flex-1">
                        <div class="flex items-center gap-3 mb-1">
                            <h3 class="text-3xl font-black text-white tracking-widest">A-12</h3>
                            <span
                                    class="text-[10px] font-black uppercase tracking-wider text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-3 py-1 rounded-lg">Available</span>
                        </div>
                        <p class="text-slate-400 font-bold text-sm">Zone A · Ground Floor · Standard · $5.50/hr</p>
                    </div>
                    <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 shrink-0">
                        <div class="glass-card p-4 rounded-2xl text-center">
                            <p class="text-2xl font-black text-white">214</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Total
                                Uses</p>
                        </div>
                        <div class="glass-card p-4 rounded-2xl text-center">
                            <p class="text-2xl font-black text-emerald-400">$1,177</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Revenue
                            </p>
                        </div>
                        <div class="glass-card p-4 rounded-2xl text-center">
                            <p class="text-2xl font-black text-primary">78%</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">
                                Utilisation</p>
                        </div>
                        <div class="glass-card p-4 rounded-2xl text-center">
                            <p class="text-2xl font-black text-white">3h 4m</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Avg
                                Duration</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Weekly usage chart -->
            <div class="glass-card rounded-[2.5rem] p-8 mb-8">
                <h3 class="text-base font-black text-white mb-6">Daily Usage — Last 7 Days</h3>
                <div class="grid grid-cols-7 gap-3 items-end h-28">
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">8</span>
                        <div class="w-full rounded-t-xl bg-primary/50" style="height:53%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Mon</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">11</span>
                        <div class="w-full rounded-t-xl bg-primary/65" style="height:73%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Tue</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">15</span>
                        <div class="w-full rounded-t-xl bg-primary" style="height:100%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Wed</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">13</span>
                        <div class="w-full rounded-t-xl bg-primary/80" style="height:87%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Thu</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">14</span>
                        <div class="w-full rounded-t-xl bg-primary/90" style="height:93%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Fri</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">6</span>
                        <div class="w-full rounded-t-xl bg-primary/40" style="height:40%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Sat</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">4</span>
                        <div class="w-full rounded-t-xl bg-primary/25" style="height:27%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Sun</span>
                    </div>
                </div>
            </div>

            <!-- Filters -->
            <div class="glass-card rounded-[2rem] p-5 mb-6 flex flex-wrap gap-4 items-center">
                <div class="flex-1 min-w-[180px] relative">
                        <span
                                class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-500 text-lg">search</span>
                    <input type="text" placeholder="Search by plate or session ID…"
                           class="input-glass bg-white/5 rounded-2xl px-4 h-12 w-full pl-10" />
                </div>
                <input type="date" class="input-glass px-4 h-12 rounded-2xl" value="2026-03-01" />
                <input type="date" class="input-glass px-4 h-12 rounded-2xl" value="2026-03-09" />
                <select class="input-glass rounded-2xl px-4 h-12">
                    <option>All Sessions</option>
                    <option>Paid</option>
                    <option>Active</option>
                </select>
            </div>

            <!-- Usage table -->
            <div class="glass-card rounded-[2.5rem] overflow-hidden">
                <div class="p-8 border-b border-white/5 flex items-center justify-between">
                    <h3 class="text-lg font-black text-white">Usage Log</h3>
                    <span class="text-[10px] font-black uppercase tracking-widest text-slate-500">214 sessions
                            total</span>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full text-left">
                        <thead>
                        <tr
                                class="text-slate-500 text-[10px] font-black uppercase tracking-widest border-b border-white/5">
                            <th class="px-8 py-5">Session ID</th>
                            <th class="px-8 py-5">Vehicle</th>
                            <th class="px-8 py-5">Owner</th>
                            <th class="px-8 py-5">Entry</th>
                            <th class="px-8 py-5">Exit</th>
                            <th class="px-8 py-5">Duration</th>
                            <th class="px-8 py-5">Fee</th>
                            <th class="px-8 py-5">Status</th>
                        </tr>
                        </thead>
                        <tbody class="text-sm font-bold text-slate-300 divide-y divide-white/5">
                        <tr class="usage-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">SES-20260309-001</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">ABC-1234</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Kamal Perera</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-09 10:45</td>
                            <td class="px-8 py-5 opacity-50 italic text-xs">Active</td>
                            <td class="px-8 py-5 text-primary font-black">2h 14m+</td>
                            <td class="px-8 py-5 text-amber-400 font-black">$12.25</td>
                            <td class="px-8 py-5"><span class="status-pill status-active">Active</span></td>
                        </tr>
                        <tr class="usage-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">SES-20260308-047</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">XYZ-8899</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Nimal Silva</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-08 09:10</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-08 12:35</td>
                            <td class="px-8 py-5 opacity-70">3h 25m</td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$18.75</td>
                            <td class="px-8 py-5"><span class="status-pill status-paid">Paid</span></td>
                        </tr>
                        <tr class="usage-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">SES-20260307-033</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">GHI-7721</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Chamari W.</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-07 11:00</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-07 13:30</td>
                            <td class="px-8 py-5 opacity-70">2h 30m</td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$13.75</td>
                            <td class="px-8 py-5"><span class="status-pill status-paid">Paid</span></td>
                        </tr>
                        <tr class="usage-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">SES-20260306-021</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">MKL-0033</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Sunethra Raj</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-06 08:00</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-06 09:30</td>
                            <td class="px-8 py-5 opacity-70">1h 30m</td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$8.25</td>
                            <td class="px-8 py-5"><span class="status-pill status-paid">Paid</span></td>
                        </tr>
                        <tr class="usage-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">SES-20260305-018</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">ABC-1234</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Kamal Perera</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-05 08:00</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-05 10:15</td>
                            <td class="px-8 py-5 opacity-70">2h 15m</td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$12.50</td>
                            <td class="px-8 py-5"><span class="status-pill status-paid">Paid</span></td>
                        </tr>
                        <tr class="usage-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">SES-20260304-011</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">TRK-5502</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Roshan Mendis</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-04 14:20</td>
                            <td class="px-8 py-5 opacity-60 text-xs">2026-03-04 17:00</td>
                            <td class="px-8 py-5 opacity-70">2h 40m</td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$14.75</td>
                            <td class="px-8 py-5"><span class="status-pill status-paid">Paid</span></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="px-8 py-5 border-t border-white/5 flex items-center justify-between">
                    <p class="text-xs font-bold text-slate-500">Showing 6 of 214 sessions · Page 1 of 36</p>
                    <div class="flex items-center gap-2">
                        <button
                                class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center text-slate-400"
                                disabled><span class="material-symbols-outlined text-sm">chevron_left</span></button>
                        <button class="h-9 px-4 rounded-xl bg-primary text-white font-black text-xs">1</button>
                        <button
                                class="h-9 px-4 rounded-xl bg-white/5 border border-white/10 text-slate-400 font-black text-xs hover:bg-white/10 transition-all">2</button>
                        <button
                                class="h-9 px-4 rounded-xl bg-white/5 border border-white/10 text-slate-400 font-black text-xs hover:bg-white/10 transition-all">3</button>
                        <span class="text-slate-500 text-xs px-1">…</span>
                        <button
                                class="h-9 px-4 rounded-xl bg-white/5 border border-white/10 text-slate-400 font-black text-xs hover:bg-white/10 transition-all">36</button>
                        <button
                                class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center text-slate-400 hover:bg-white/10 transition-all"><span
                                class="material-symbols-outlined text-sm">chevron_right</span></button>
                    </div>
                </div>
            </div>

        </div>
    </main>
</div>
</body>

</html>