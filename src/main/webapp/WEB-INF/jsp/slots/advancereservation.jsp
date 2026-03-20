<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Advance Reservations</title>
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
            font-weight: 700;
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

        label.field-label {
            font-size: 0.7rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.1em;
            color: #64748b;
            margin-bottom: 6px;
            display: block;
        }

        .res-pill-upcoming {
            background: rgba(31, 104, 249, 0.1);
            color: #60a5fa;
            border: 1px solid rgba(31, 104, 249, 0.2);
        }

        .res-pill-active {
            background: rgba(16, 185, 129, 0.1);
            color: #34d399;
            border: 1px solid rgba(16, 185, 129, 0.2);
        }

        .res-pill-cancelled {
            background: rgba(100, 116, 139, 0.1);
            color: #94a3b8;
            border: 1px solid rgba(100, 116, 139, 0.2);
        }

        .res-pill-completed {
            background: rgba(245, 158, 11, 0.08);
            color: #fbbf24;
            border: 1px solid rgba(245, 158, 11, 0.18);
        }

        .res-pill {
            padding: 3px 11px;
            border-radius: 9px;
            font-size: 0.67rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .res-card {
            background: rgba(255, 255, 255, 0.025);
            border: 1px solid rgba(255, 255, 255, 0.07);
            border-radius: 20px;
            padding: 20px;
            transition: all 0.2s;
        }

        .res-card:hover {
            background: rgba(255, 255, 255, 0.04);
            border-color: rgba(255, 255, 255, 0.12);
        }

        .tab-btn {
            padding: 8px 20px;
            border-radius: 12px;
            font-size: 0.72rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.07em;
            transition: all 0.2s;
            cursor: pointer;
            border: none;
        }

        .tab-btn.active {
            background: rgba(31, 104, 249, 0.15);
            color: #60a5fa;
            border: 1px solid rgba(31, 104, 249, 0.25);
        }

        .tab-btn:not(.active) {
            background: transparent;
            color: #475569;
        }

        .tab-btn:not(.active):hover {
            color: #94a3b8;
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

    <aside class="sidebar-container border-r border-white/5 premium-blur flex flex-col shrink-0 z-50">
        <div class="p-6 mb-4 flex items-center">
            <div
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-[14px] bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-2 overflow-y-auto">
            <a href="dashboard_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">dashboard</span>
                <span class="nav-label text-sm">Dashboard</span>
            </a>
            <a href="entry.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">login</span>
                <span class="nav-label text-sm">Vehicle Entry</span>
            </a>
            <a href="exitvehicle.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">logout</span>
                <span class="nav-label text-sm">Vehicle Exit</span>
            </a>
            <a href="parking.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">local_parking</span>
                <span class="nav-label text-sm">Active Parking</span>
            </a>
            <a href="advancereservation.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
                <span class="material-symbols-outlined shrink-0">event_available</span>
                <span class="nav-label text-sm">Reservation</span>
            </a>
            <a href="paymenthistory_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">payments</span>
                <span class="nav-label text-sm">Payments</span>
            </a>
            <a href="receipt.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">receipt_long</span>
                <span class="nav-label text-sm">Receipts</span>
            </a>
            <a href="walletoverview.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">account_balance_wallet</span>
                <span class="nav-label text-sm">Wallet</span>
            </a>
            <a href="notification_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">notifications</span>
                <span class="nav-label text-sm">Notifications</span>
            </a>
            <a href="accountsetting_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">settings</span>
                <span class="nav-label text-sm">Account Settings</span>
            </a>
        </nav>
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
                <h2 class="text-xl font-black text-white">Advance Reservations</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Book & manage slot
                    reservations</p>
            </div>
            <div class="flex items-center gap-3">
                <button onclick="document.getElementById('newResModal').classList.remove('hidden')"
                        class="flex items-center gap-2 bg-primary text-white font-black px-5 py-2.5 rounded-xl hover:bg-primary/80 transition-all text-xs uppercase tracking-widest shadow-[0_0_20px_rgba(31,104,249,0.25)]">
                    <span class="material-symbols-outlined text-lg">add</span> New Reservation
                </button>
                <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">

            <!-- KPI strip -->
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-5 mb-8">
                <div class="glass-card p-5 rounded-2xl">
                    <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Upcoming</p>
                    <p class="text-3xl font-black text-primary">8</p>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">Next 7 days</p>
                </div>
                <div class="glass-card p-5 rounded-2xl">
                    <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Today</p>
                    <p class="text-3xl font-black text-emerald-400">3</p>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">Arrive today</p>
                </div>
                <div class="glass-card p-5 rounded-2xl">
                    <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">This Month</p>
                    <p class="text-3xl font-black text-white">31</p>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">Total bookings</p>
                </div>
                <div class="glass-card p-5 rounded-2xl">
                    <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Cancellations</p>
                    <p class="text-3xl font-black text-amber-400">2</p>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">This month</p>
                </div>
            </div>

            <!-- Tabs + filters -->
            <div class="glass-card rounded-[2rem] p-5 mb-6 flex flex-wrap gap-4 items-center">
                <div class="flex gap-1 bg-white/[0.03] border border-white/5 rounded-xl p-1">
                    <button class="tab-btn active" onclick="setTab(this)">All</button>
                    <button class="tab-btn" onclick="setTab(this)">Upcoming</button>
                    <button class="tab-btn" onclick="setTab(this)">Today</button>
                    <button class="tab-btn" onclick="setTab(this)">Completed</button>
                    <button class="tab-btn" onclick="setTab(this)">Cancelled</button>
                </div>
                <div class="flex-1 min-w-[180px] relative ml-auto">
                        <span
                                class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-500 text-lg">search</span>
                    <input type="text" placeholder="Search plate or name…"
                           class="input-glass bg-white/5 backdrop-blur-md border border-white/20 rounded-2xl px-4 h-12 pl-10" />
                </div>
                <input type="date" class="input-glass px-4 h-12 rounded-2xl w-auto" value="2026-03-10" />
            </div>

            <!-- Reservation cards -->
            <div class="space-y-4">

                <!-- Today - active -->
                <div class="res-card border-l-4 border-emerald-500">
                    <div class="flex flex-wrap items-start justify-between gap-4">
                        <div class="flex items-center gap-4">
                            <div
                                    class="h-11 w-11 rounded-xl bg-emerald-500/10 border border-emerald-500/15 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-emerald-400">event_available</span>
                            </div>
                            <div>
                                <div class="flex items-center gap-2 mb-1">
                                    <p class="text-base font-black text-white tracking-widest">ABC-1234</p>
                                    <span class="res-pill res-pill-active">Active</span>
                                </div>
                                <p class="text-xs text-slate-400 font-bold">Kamal Perera · Toyota Prius</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-6 flex-wrap">
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Slot</p>
                                <p class="text-sm font-black text-primary mt-0.5">A-12</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Arrival
                                </p>
                                <p class="text-sm font-black text-white mt-0.5">10:30 AM</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Until</p>
                                <p class="text-sm font-black text-white mt-0.5">02:30 PM</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Fee</p>
                                <p class="text-sm font-black text-emerald-400 mt-0.5">$22.00</p>
                            </div>
                            <div class="flex gap-2">
                                <button
                                        class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all"><span
                                        class="material-symbols-outlined text-sm text-slate-400">edit</span></button>
                                <a href="parkingrecorddetails.html"
                                   class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-primary/20 transition-all"><span
                                        class="material-symbols-outlined text-sm text-slate-400">open_in_new</span></a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Today - upcoming -->
                <div class="res-card border-l-4 border-primary">
                    <div class="flex flex-wrap items-start justify-between gap-4">
                        <div class="flex items-center gap-4">
                            <div
                                    class="h-11 w-11 rounded-xl bg-primary/10 border border-primary/15 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-primary">schedule</span>
                            </div>
                            <div>
                                <div class="flex items-center gap-2 mb-1">
                                    <p class="text-base font-black text-white tracking-widest">GHI-7721</p>
                                    <span class="res-pill res-pill-upcoming">Upcoming</span>
                                    <span
                                            class="text-[9px] font-black text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-2 py-0.5 rounded-lg">Today</span>
                                </div>
                                <p class="text-xs text-slate-400 font-bold">Chamari W. · Suzuki Alto</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-6 flex-wrap">
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Slot</p>
                                <p class="text-sm font-black text-primary mt-0.5">B-05</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Arrival
                                </p>
                                <p class="text-sm font-black text-white mt-0.5">02:00 PM</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Until</p>
                                <p class="text-sm font-black text-white mt-0.5">05:00 PM</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Fee</p>
                                <p class="text-sm font-black text-emerald-400 mt-0.5">$16.50</p>
                            </div>
                            <div class="flex gap-2">
                                <button
                                        class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all"><span
                                        class="material-symbols-outlined text-sm text-slate-400">edit</span></button>
                                <button
                                        class="h-9 w-9 rounded-xl bg-rose-500/10 border border-rose-500/15 flex items-center justify-center hover:bg-rose-500/20 transition-all"><span
                                        class="material-symbols-outlined text-sm text-rose-400">cancel</span></button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Tomorrow -->
                <div class="res-card border-l-4 border-primary/40">
                    <div class="flex flex-wrap items-start justify-between gap-4">
                        <div class="flex items-center gap-4">
                            <div
                                    class="h-11 w-11 rounded-xl bg-primary/10 border border-primary/15 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-primary">schedule</span>
                            </div>
                            <div>
                                <div class="flex items-center gap-2 mb-1">
                                    <p class="text-base font-black text-white tracking-widest">XYZ-8899</p>
                                    <span class="res-pill res-pill-upcoming">Upcoming</span>
                                </div>
                                <p class="text-xs text-slate-400 font-bold">Nimal Silva · Nissan Axio</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-6 flex-wrap">
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Slot</p>
                                <p class="text-sm font-black text-primary mt-0.5">C-03</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Date</p>
                                <p class="text-sm font-black text-white mt-0.5">11 Mar</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Time</p>
                                <p class="text-sm font-black text-white mt-0.5">09:00–12:00</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Fee</p>
                                <p class="text-sm font-black text-emerald-400 mt-0.5">$16.50</p>
                            </div>
                            <div class="flex gap-2">
                                <button
                                        class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all"><span
                                        class="material-symbols-outlined text-sm text-slate-400">edit</span></button>
                                <button
                                        class="h-9 w-9 rounded-xl bg-rose-500/10 border border-rose-500/15 flex items-center justify-center hover:bg-rose-500/20 transition-all"><span
                                        class="material-symbols-outlined text-sm text-rose-400">cancel</span></button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Completed -->
                <div class="res-card opacity-60">
                    <div class="flex flex-wrap items-start justify-between gap-4">
                        <div class="flex items-center gap-4">
                            <div
                                    class="h-11 w-11 rounded-xl bg-amber-500/10 border border-amber-500/15 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-amber-400">check_circle</span>
                            </div>
                            <div>
                                <div class="flex items-center gap-2 mb-1">
                                    <p class="text-base font-black text-white tracking-widest">MNO-3310</p>
                                    <span class="res-pill res-pill-completed">Completed</span>
                                </div>
                                <p class="text-xs text-slate-400 font-bold">Dilshan K. · Hyundai i10</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-6 flex-wrap">
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Slot</p>
                                <p class="text-sm font-black text-slate-400 mt-0.5">A-07</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Date</p>
                                <p class="text-sm font-black text-slate-400 mt-0.5">08 Mar</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Time</p>
                                <p class="text-sm font-black text-slate-400 mt-0.5">14:00–16:30</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Paid</p>
                                <p class="text-sm font-black text-emerald-400 mt-0.5">$13.75</p>
                            </div>
                            <a href="receipt.html"
                               class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all"><span
                                    class="material-symbols-outlined text-sm text-slate-400">receipt_long</span></a>
                        </div>
                    </div>
                </div>

                <!-- Cancelled -->
                <div class="res-card opacity-50">
                    <div class="flex flex-wrap items-start justify-between gap-4">
                        <div class="flex items-center gap-4">
                            <div
                                    class="h-11 w-11 rounded-xl bg-slate-700/30 border border-slate-700/30 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-slate-500">cancel</span>
                            </div>
                            <div>
                                <div class="flex items-center gap-2 mb-1">
                                    <p class="text-base font-black text-slate-400 tracking-widest line-through">
                                        TRK-5502</p>
                                    <span class="res-pill res-pill-cancelled">Cancelled</span>
                                </div>
                                <p class="text-xs text-slate-500 font-bold">Roshan Mendis · Cancelled 07 Mar</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-6 flex-wrap">
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-600">Slot</p>
                                <p class="text-sm font-black text-slate-500 mt-0.5">C-08</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-600">Date</p>
                                <p class="text-sm font-black text-slate-500 mt-0.5">09 Mar</p>
                            </div>
                            <div class="text-center">
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-600">Refund</p>
                                <p class="text-sm font-black text-slate-400 mt-0.5">$0.00</p>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>
</div>

<!-- New Reservation Modal -->
<div id="newResModal" class="hidden fixed inset-0 z-50 flex items-center justify-center p-6">
    <div class="absolute inset-0 bg-black/60 premium-blur"
         onclick="document.getElementById('newResModal').classList.add('hidden')"></div>
    <div class="relative glass-card rounded-[2.5rem] p-8 w-full max-w-lg border border-white/10 z-10">
        <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-black text-white">New Reservation</h3>
            <button onclick="document.getElementById('newResModal').classList.add('hidden')"
                    class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                <span class="material-symbols-outlined text-slate-400 text-lg">close</span>
            </button>
        </div>
        <div class="space-y-5">
            <div>
                <label class="field-label">Vehicle Plate <span class="text-rose-400">*</span></label>
                <input type="text" placeholder="e.g. ABC-1234"
                       class="input-glass bg-white/5 rounded-2xl px-4 h-12 uppercase tracking-widest" />
            </div>
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="field-label">Date <span class="text-rose-400">*</span></label>
                    <input type="date" class="input-glass px-4 h-12 rounded-2xl" value="2026-03-11" />
                </div>
                <div>
                    <label class="field-label">Zone Preference</label>
                    <select class="input-glass rounded-2xl px-4 h-12">
                        <option>Any Zone</option>
                        <option>Zone A</option>
                        <option>Zone B</option>
                        <option>Zone C</option>
                        <option>Zone D</option>
                    </select>
                </div>
            </div>
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="field-label">Arrival Time <span class="text-rose-400">*</span></label>
                    <input type="time" class="input-glass bg-white/5 rounded-2xl" value="09:00" />
                </div>
                <div>
                    <label class="field-label">Departure Time <span class="text-rose-400">*</span></label>
                    <input type="time" class="input-glass bg-white/5 rounded-2xl" value="12:00" />
                </div>
            </div>
            <div class="p-4 rounded-2xl bg-primary/5 border border-primary/15 flex items-center justify-between">
                <div>
                    <p class="text-xs font-black text-white">Estimated Fee</p>
                    <p class="text-[10px] text-slate-500 font-bold">Based on 3h × $5.50/hr</p>
                </div>
                <p class="text-2xl font-black text-white">$16.50</p>
            </div>
            <button
                    onclick="document.getElementById('newResModal').classList.add('hidden');document.getElementById('slotSelHint').classList.remove('hidden')"
                    class="w-full flex items-center justify-center gap-2 bg-primary text-white font-black py-4 rounded-2xl hover:bg-primary/80 transition-all text-sm shadow-[0_0_20px_rgba(31,104,249,0.25)]">
                <span class="material-symbols-outlined">event_available</span> Continue to Slot Selection
            </button>
        </div>
    </div>
</div>

<!-- Slot selection hint banner -->
<div id="slotSelHint" class="hidden fixed bottom-6 right-6 z-50">
    <div
            class="flex items-center gap-3 bg-primary text-white font-black px-5 py-3.5 rounded-2xl shadow-[0_0_30px_rgba(31,104,249,0.4)]">
        <span class="material-symbols-outlined">info</span>
        <span class="text-sm">Select a slot to complete</span>
        <a href="slotselection.html" class="ml-2 underline text-blue-200 text-sm">Open Slot Selection →</a>
        <button onclick="document.getElementById('slotSelHint').classList.add('hidden')"
                class="ml-2 opacity-60 hover:opacity-100"><span
                class="material-symbols-outlined text-base">close</span></button>
    </div>
</div>

<script>
    function setTab(el) {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        el.classList.add('active');
    }
</script>
</body>

</html>