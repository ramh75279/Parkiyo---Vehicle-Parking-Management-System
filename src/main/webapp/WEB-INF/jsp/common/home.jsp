<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Slot Overview</title>
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

        /* Slot cells */
        .slot-cell {
            width: 68px;
            height: 88px;
            border-radius: 10px;
            border: 1.5px solid;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 4px;
            cursor: pointer;
            transition: all 0.18s;
            position: relative;
        }

        .slot-cell:hover {
            transform: scale(1.07);
            z-index: 10;
        }

        .slot-cell.available {
            background: rgba(16, 185, 129, 0.08);
            border-color: rgba(16, 185, 129, 0.35);
        }

        .slot-cell.available:hover {
            background: rgba(16, 185, 129, 0.18);
            border-color: #10b981;
        }

        .slot-cell.occupied {
            background: rgba(239, 68, 68, 0.08);
            border-color: rgba(239, 68, 68, 0.35);
        }

        .slot-cell.occupied:hover {
            background: rgba(239, 68, 68, 0.18);
            border-color: #ef4444;
        }

        .slot-cell.maintenance {
            background: rgba(245, 158, 11, 0.08);
            border-color: rgba(245, 158, 11, 0.35);
        }

        .slot-cell.maintenance:hover {
            background: rgba(245, 158, 11, 0.18);
            border-color: #f59e0b;
        }

        .slot-cell.disabled {
            background: rgba(100, 116, 139, 0.06);
            border-color: rgba(100, 116, 139, 0.2);
            cursor: default;
        }

        .slot-cell .slot-code {
            font-size: 0.68rem;
            font-weight: 900;
            letter-spacing: 0.06em;
        }

        .slot-cell.available .slot-code {
            color: #34d399;
        }

        .slot-cell.occupied .slot-code {
            color: #f87171;
        }

        .slot-cell.maintenance .slot-code {
            color: #fbbf24;
        }

        .slot-cell.disabled .slot-code {
            color: #475569;
        }

        .slot-cell .plate {
            font-size: 0.55rem;
            font-weight: 800;
            letter-spacing: 0.04em;
            opacity: 0.7;
        }

        .slot-cell.occupied .plate {
            color: #fca5a5;
        }

        /* Tooltip */
        .slot-tooltip {
            position: absolute;
            bottom: calc(100% + 8px);
            left: 50%;
            transform: translateX(-50%);
            background: #0f172a;
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: 10px;
            padding: 8px 12px;
            font-size: 0.7rem;
            font-weight: 700;
            white-space: nowrap;
            pointer-events: none;
            opacity: 0;
            transition: opacity 0.15s;
            z-index: 50;
        }

        .slot-cell:hover .slot-tooltip {
            opacity: 1;
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
            <a href="dashboard_admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">dashboard</span>
                <span class="nav-label text-sm">Dashboard</span>
            </a>
            <a href="entry_admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">login</span>
                <span class="nav-label text-sm">Vehicle Entry</span>
            </a>
            <a href="exitvehicle_admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">logout</span>
                <span class="nav-label text-sm">Vehicle Exit</span>
            </a>
            <a href="slot_overview.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
                <span class="material-symbols-outlined shrink-0">grid_view</span>
                <span class="nav-label text-sm">Parking Slots</span>
            </a>
            <a href="Vehicle_List_Page.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">directions_car</span>
                <span class="nav-label text-sm">Vehicles</span>
            </a>
            <a href="usermanagement.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">group</span>
                <span class="nav-label text-sm">Users</span>
            </a>
            <a href="paymenthistory.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">payments</span>
                <span class="nav-label text-sm">Payments</span>
            </a>
            <a href="Repportshubpage.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">bar_chart</span>
                <span class="nav-label text-sm">Reports</span>
            </a>
            <a href="notification.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">notifications</span>
                <span class="nav-label text-sm">Notifications</span>
            </a>
            <a href="systemstatuspage.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">monitor_heart</span>
                <span class="nav-label text-sm">System Status</span>
            </a>
            <a href="accountsetting.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">settings</span>
                <span class="nav-label text-sm">Settings</span>
            </a>
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
        <!-- TOPBAR -->
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div>
                <h2 class="text-xl font-black text-white">Slot Overview</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Live parking map
                </p>
            </div>
            <div class="flex items-center gap-3">
                <a href="slot_list.html"
                   class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">list</span> List View
                </a>
                <a href="add_slot.html"
                   class="flex items-center gap-2 bg-primary text-white font-black px-5 py-2.5 rounded-xl hover:bg-primary/80 transition-all text-xs uppercase tracking-widest shadow-[0_0_20px_rgba(31,104,249,0.25)]">
                    <span class="material-symbols-outlined text-lg">add</span> Add Slot
                </a>
                <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px] ml-1">
                    <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-8">

            <!-- KPI strip -->
            <div class="grid grid-cols-4 gap-5 mb-8">
                <div class="glass-card p-5 rounded-2xl flex items-center gap-4">
                    <div class="h-10 w-10 rounded-xl bg-white/5 flex items-center justify-center shrink-0"><span
                            class="material-symbols-outlined text-slate-400">grid_view</span></div>
                    <div>
                        <p class="text-2xl font-black text-white">100</p>
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Total</p>
                    </div>
                </div>
                <div class="glass-card p-5 rounded-2xl flex items-center gap-4">
                    <div class="h-10 w-10 rounded-xl bg-emerald-500/10 flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-emerald-400">check_circle</span>
                    </div>
                    <div>
                        <p class="text-2xl font-black text-emerald-400">42</p>
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Available</p>
                    </div>
                </div>
                <div class="glass-card p-5 rounded-2xl flex items-center gap-4">
                    <div class="h-10 w-10 rounded-xl bg-rose-500/10 flex items-center justify-center shrink-0"><span
                            class="material-symbols-outlined text-rose-400">directions_car</span></div>
                    <div>
                        <p class="text-2xl font-black text-rose-400">51</p>
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Occupied</p>
                    </div>
                </div>
                <div class="glass-card p-5 rounded-2xl flex items-center gap-4">
                    <div class="h-10 w-10 rounded-xl bg-amber-500/10 flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-amber-400">build</span>
                    </div>
                    <div>
                        <p class="text-2xl font-black text-amber-400">7</p>
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Maintenance</p>
                    </div>
                </div>
            </div>

            <!-- Legend -->
            <div class="flex items-center gap-6 mb-6 flex-wrap">
                <p class="text-[10px] font-black uppercase tracking-widest text-slate-500">Legend:</p>
                <span class="flex items-center gap-2 text-xs font-bold text-emerald-400"><span
                        class="h-3.5 w-3.5 rounded-sm bg-emerald-500/20 border border-emerald-500/50 inline-block"></span>Available</span>
                <span class="flex items-center gap-2 text-xs font-bold text-rose-400"><span
                        class="h-3.5 w-3.5 rounded-sm bg-rose-500/20 border border-rose-500/50 inline-block"></span>Occupied</span>
                <span class="flex items-center gap-2 text-xs font-bold text-amber-400"><span
                        class="h-3.5 w-3.5 rounded-sm bg-amber-500/20 border border-amber-500/50 inline-block"></span>Maintenance</span>
                <span class="flex items-center gap-2 text-xs font-bold text-slate-500"><span
                        class="h-3.5 w-3.5 rounded-sm bg-slate-700/30 border border-slate-600/30 inline-block"></span>Disabled</span>
                <span class="ml-auto text-[10px] font-bold text-slate-500">Click any slot to edit</span>
            </div>

            <!-- Zone A -->
            <div class="glass-card rounded-[2.5rem] p-7 mb-6">
                <div class="flex items-center justify-between mb-5">
                    <div>
                        <h3 class="text-base font-black text-white">Zone A — Ground Floor</h3>
                        <p class="text-[10px] text-slate-500 font-bold mt-0.5">25 slots · 10 available · 13 occupied
                            · 2 maintenance</p>
                    </div>
                    <div class="h-2 w-32 bg-white/5 rounded-full overflow-hidden">
                        <div class="h-full bg-rose-500 rounded-full" style="width:52%"></div>
                    </div>
                </div>
                <div class="flex flex-wrap gap-3">
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-01</span><span class="plate">ABC-1234</span>
                        <div class="slot-tooltip text-white">A-01 · Occupied<br>ABC-1234 · 2h 14m</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">A-02</span>
                        <div class="slot-tooltip text-white">A-02 · Available</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-03</span><span class="plate">XYZ-8899</span>
                        <div class="slot-tooltip text-white">A-03 · Occupied<br>XYZ-8899 · 1h 05m</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-04</span><span class="plate">MNO-3310</span>
                        <div class="slot-tooltip text-white">A-04 · Occupied</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">A-05</span>
                        <div class="slot-tooltip text-white">A-05 · Available</div>
                    </div>
                    <div class="slot-cell maintenance"><span
                            class="material-symbols-outlined text-amber-400 text-lg">build</span><span
                            class="slot-code">A-06</span>
                        <div class="slot-tooltip text-white">A-06 · Maintenance</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">A-07</span>
                        <div class="slot-tooltip text-white">A-07 · Available</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-08</span><span class="plate">GHI-7721</span>
                        <div class="slot-tooltip text-white">A-08 · Occupied</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-09</span><span class="plate">KLM-9012</span>
                        <div class="slot-tooltip text-white">A-09 · Occupied</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">A-10</span>
                        <div class="slot-tooltip text-white">A-10 · Available</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-11</span><span class="plate">PQR-6641</span>
                        <div class="slot-tooltip text-white">A-11 · Occupied</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-12</span><span class="plate">ABC-1234</span>
                        <div class="slot-tooltip text-white">A-12 · Occupied<br>ABC-1234 · 2h 14m</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">A-13</span>
                        <div class="slot-tooltip text-white">A-13 · Available</div>
                    </div>
                    <div class="slot-cell maintenance"><span
                            class="material-symbols-outlined text-amber-400 text-lg">build</span><span
                            class="slot-code">A-14</span>
                        <div class="slot-tooltip text-white">A-14 · Maintenance</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">A-15</span><span class="plate">TRK-5502</span>
                        <div class="slot-tooltip text-white">A-15 · Occupied</div>
                    </div>
                </div>
            </div>

            <!-- Zone B -->
            <div class="glass-card rounded-[2.5rem] p-7 mb-6">
                <div class="flex items-center justify-between mb-5">
                    <div>
                        <h3 class="text-base font-black text-white">Zone B — Level 1</h3>
                        <p class="text-[10px] text-slate-500 font-bold mt-0.5">30 slots · 15 available · 12 occupied
                            · 3 maintenance</p>
                    </div>
                    <div class="h-2 w-32 bg-white/5 rounded-full overflow-hidden">
                        <div class="h-full bg-amber-500 rounded-full" style="width:40%"></div>
                    </div>
                </div>
                <div class="flex flex-wrap gap-3">
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">B-01</span>
                        <div class="slot-tooltip text-white">B-01 · Available</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">B-02</span>
                        <div class="slot-tooltip text-white">B-02 · Available</div>
                    </div>
                    <div class="slot-cell maintenance"><span
                            class="material-symbols-outlined text-amber-400 text-lg">build</span><span
                            class="slot-code">B-03</span>
                        <div class="slot-tooltip text-white">B-03 · Maintenance</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">B-04</span><span class="plate">XYZ-8899</span>
                        <div class="slot-tooltip text-white">B-04 · Occupied</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">B-05</span>
                        <div class="slot-tooltip text-white">B-05 · Available</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">B-06</span>
                        <div class="slot-tooltip text-white">B-06 · Available</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">B-07</span><span class="plate">BIK-3311</span>
                        <div class="slot-tooltip text-white">B-07 · Occupied</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">B-08</span>
                        <div class="slot-tooltip text-white">B-08 · Available</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">B-09</span><span class="plate">JKL-9988</span>
                        <div class="slot-tooltip text-white">B-09 · Occupied</div>
                    </div>
                    <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-emerald-400 text-lg">check</span><span
                            class="slot-code">B-10</span>
                        <div class="slot-tooltip text-white">B-10 · Available</div>
                    </div>
                    <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                            class="material-symbols-outlined text-rose-400 text-lg">directions_car</span><span
                            class="slot-code">B-11</span><span class="plate">PQR-6641</span>
                        <div class="slot-tooltip text-white">B-11 · Occupied</div>
                    </div>
                    <div class="slot-cell maintenance"><span
                            class="material-symbols-outlined text-amber-400 text-lg">build</span><span
                            class="slot-code">B-12</span>
                        <div class="slot-tooltip text-white">B-12 · Maintenance</div>
                    </div>
                </div>
            </div>

            <!-- Zone C & D — compact -->
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <div class="glass-card rounded-[2.5rem] p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div>
                            <h3 class="text-base font-black text-white">Zone C — Level 2</h3>
                            <p class="text-[10px] text-slate-500 font-bold mt-0.5">25 slots · 11 available · 11
                                occupied · 3 other</p>
                        </div>
                        <div class="h-2 w-24 bg-white/5 rounded-full overflow-hidden">
                            <div class="h-full bg-primary rounded-full" style="width:44%"></div>
                        </div>
                    </div>
                    <div class="flex flex-wrap gap-2.5">
                        <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-rose-400 text-base">directions_car</span><span
                                class="slot-code" style="font-size:0.6rem">C-01</span><span
                                class="plate">MKL-0033</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">C-02</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">C-03</span></div>
                        <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-rose-400 text-base">directions_car</span><span
                                class="slot-code" style="font-size:0.6rem">C-04</span><span
                                class="plate">GHI-7721</span></div>
                        <div class="slot-cell maintenance"><span
                                class="material-symbols-outlined text-amber-400 text-base">build</span><span
                                class="slot-code" style="font-size:0.6rem">C-05</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">C-06</span></div>
                        <div class="slot-cell disabled"><span
                                class="material-symbols-outlined text-slate-600 text-base">block</span><span
                                class="slot-code" style="font-size:0.6rem">C-07</span></div>
                        <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-rose-400 text-base">directions_car</span><span
                                class="slot-code" style="font-size:0.6rem">C-08</span><span
                                class="plate">TRK-5502</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">C-09</span></div>
                        <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-rose-400 text-base">directions_car</span><span
                                class="slot-code" style="font-size:0.6rem">C-10</span><span
                                class="plate">JKL-9988</span></div>
                    </div>
                </div>

                <div class="glass-card rounded-[2.5rem] p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div>
                            <h3 class="text-base font-black text-white">Zone D — Rooftop</h3>
                            <p class="text-[10px] text-slate-500 font-bold mt-0.5">20 slots · 15 available · 5
                                occupied</p>
                        </div>
                        <div class="h-2 w-24 bg-white/5 rounded-full overflow-hidden">
                            <div class="h-full bg-emerald-500 rounded-full" style="width:25%"></div>
                        </div>
                    </div>
                    <div class="flex flex-wrap gap-2.5">
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">D-01</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">D-02</span></div>
                        <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-rose-400 text-base">directions_car</span><span
                                class="slot-code" style="font-size:0.6rem">D-03</span><span
                                class="plate">BIK-3311</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">D-04</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">D-05</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">D-06</span></div>
                        <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-rose-400 text-base">directions_car</span><span
                                class="slot-code" style="font-size:0.6rem">D-07</span><span
                                class="plate">MKL-0033</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">D-08</span></div>
                        <div class="slot-cell available" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-emerald-400 text-base">check</span><span
                                class="slot-code" style="font-size:0.6rem">D-09</span></div>
                        <div class="slot-cell occupied" onclick="window.location.href='edit_slot.html'"><span
                                class="material-symbols-outlined text-rose-400 text-base">directions_car</span><span
                                class="slot-code" style="font-size:0.6rem">D-10</span><span
                                class="plate">ABC-1234</span></div>
                    </div>
                </div>
            </div>

        </div>
    </main>
</div>
</body>

</html>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Premium Parking Management</title>
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
                    borderRadius: { squircle: "14px", xl: "1.5rem", "2xl": "2rem", "3xl": "3rem" },
                },
            },
        }
    </script>
    <style>
        body { font-family: 'Public Sans', sans-serif; }
        .premium-blur { backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px); }
        .bg-subtle-radial { background: radial-gradient(circle at 50% 0%, #1e293b 0%, #020617 60%); }
        .glass-card { background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.08); }
        .hero-glow { background: radial-gradient(ellipse 80% 50% at 50% -10%, rgba(31,104,249,0.25) 0%, transparent 70%); }
        .ticker-track { display: flex; gap: 4rem; animation: ticker 28s linear infinite; }
        @keyframes ticker { from { transform: translateX(0); } to { transform: translateX(-50%); } }
        .feature-card { transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1); }
        .feature-card:hover { transform: translateY(-6px); border-color: rgba(31,104,249,0.3); background: rgba(31,104,249,0.04); }
        ::-webkit-scrollbar { width: 5px; } ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
    </style>
</head>
<body class="bg-background-dark text-slate-100 font-display antialiased overflow-x-hidden">

<!-- NAV -->
<header class="sticky top-0 z-50 w-full border-b border-white/5 bg-background-dark/75 premium-blur">
    <div class="container mx-auto flex h-20 items-center justify-between px-6 lg:px-12">
        <a href="home.html" class="flex items-center gap-4">
            <div class="flex h-11 w-11 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_20px_rgba(31,104,249,0.4)]">
                <span class="material-symbols-outlined font-bold">local_parking</span>
            </div>
            <span class="text-2xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </a>
        <nav class="hidden lg:flex items-center gap-8 text-sm font-bold text-slate-400">
            <a class="text-primary" href="home.html">Home</a>
            <a class="hover:text-primary transition-colors" href="features.html">Features</a>
            <a class="hover:text-primary transition-colors" href="solutions.html">Solutions</a>
            <a class="hover:text-primary transition-colors" href="analytics.html">Analytics</a>
            <a class="hover:text-primary transition-colors" href="faq.html">Support</a>
        </nav>
        <div class="flex items-center gap-6">
            <a href="login.html" class="text-sm font-bold text-slate-300 hover:text-white transition-colors">Login</a>
            <a href="register.html" class="bg-primary text-white text-sm font-bold px-7 py-3 rounded-xl hover:scale-105 transition-all shadow-lg shadow-primary/30">Get Started</a>
        </div>
    </div>
</header>

<main>
    <!-- HERO -->
    <section class="relative min-h-screen flex items-center bg-subtle-radial overflow-hidden">
        <div class="hero-glow absolute inset-0 pointer-events-none"></div>
        <!-- Grid bg -->
        <div class="absolute inset-0 opacity-[0.03]" style="background-image: linear-gradient(rgba(255,255,255,0.5) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,0.5) 1px, transparent 1px); background-size: 60px 60px;"></div>

        <div class="container mx-auto px-6 lg:px-12 py-32 relative z-10">
            <div class="max-w-5xl mx-auto text-center">
                <div class="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 border border-primary/20 text-primary text-[10px] font-black uppercase tracking-[0.22em] mb-8">
                    <span class="h-1.5 w-1.5 rounded-full bg-primary animate-pulse"></span>
                    Platform Now Live · 2026
                </div>

                <h1 class="text-6xl lg:text-[7rem] font-black leading-[0.95] tracking-tight text-white mb-8">
                    The operating<br/>
                    <span class="text-transparent bg-clip-text bg-gradient-to-r from-primary to-blue-300">system for</span><br/>
                    modern parking.
                </h1>

                <p class="text-xl text-slate-400 max-w-2xl mx-auto leading-relaxed font-medium mb-12">
                    Parkiyo gives parking operators a premium command layer — slot intelligence, payment automation, real-time analytics and role-based control in one unified platform.
                </p>

                <div class="flex flex-col sm:flex-row items-center justify-center gap-5">
                    <a href="register.html" class="w-full sm:w-auto bg-primary text-white font-black px-10 py-5 rounded-2xl hover:scale-105 transition-all shadow-2xl shadow-primary/30 text-base flex items-center justify-center gap-3">
                        Start for free
                        <span class="material-symbols-outlined text-xl">arrow_forward</span>
                    </a>
                    <a href="features.html" class="w-full sm:w-auto glass-card text-white font-bold px-10 py-5 rounded-2xl hover:bg-white/5 transition-all text-base flex items-center justify-center gap-3">
                        <span class="material-symbols-outlined text-xl text-primary">play_circle</span>
                        See how it works
                    </a>
                </div>

                <div class="flex items-center justify-center gap-12 mt-16">
                    <div class="text-center">
                        <p class="text-3xl font-black text-white">2.4K+</p>
                        <p class="text-[10px] text-slate-500 uppercase tracking-widest font-bold mt-1">Daily vehicles</p>
                    </div>
                    <div class="h-12 w-px bg-white/10"></div>
                    <div class="text-center">
                        <p class="text-3xl font-black text-white">99.9%</p>
                        <p class="text-[10px] text-slate-500 uppercase tracking-widest font-bold mt-1">Uptime SLA</p>
                    </div>
                    <div class="h-12 w-px bg-white/10"></div>
                    <div class="text-center">
                        <p class="text-3xl font-black text-white">$45.8K</p>
                        <p class="text-[10px] text-slate-500 uppercase tracking-widest font-bold mt-1">Revenue tracked</p>
                    </div>
                </div>
            </div>

            <!-- Hero visual -->
            <div class="mt-24 max-w-5xl mx-auto relative">
                <div class="absolute -inset-8 bg-gradient-to-t from-background-dark via-transparent to-transparent pointer-events-none z-10 bottom-0 h-40" style="top:auto"></div>
                <div class="glass-card rounded-[2.5rem] overflow-hidden border border-white/10 shadow-[0_40px_120px_rgba(0,0,0,0.7)]">
                    <div class="h-2 w-full bg-gradient-to-r from-primary via-blue-400 to-primary"></div>
                    <div class="p-6 border-b border-white/5 flex items-center justify-between">
                        <div class="flex items-center gap-3">
                            <span class="h-3 w-3 rounded-full bg-rose-500 opacity-70"></span>
                            <span class="h-3 w-3 rounded-full bg-yellow-400 opacity-70"></span>
                            <span class="h-3 w-3 rounded-full bg-emerald-400 opacity-70"></span>
                        </div>
                        <div class="text-[10px] font-black uppercase tracking-widest text-slate-500">Parkiyo Admin Console · Live</div>
                        <div class="flex items-center gap-2">
                            <span class="h-2 w-2 bg-emerald-400 rounded-full animate-pulse"></span>
                            <span class="text-[10px] text-emerald-400 font-bold uppercase tracking-wider">System Healthy</span>
                        </div>
                    </div>
                    <div class="grid grid-cols-4 gap-0 divide-x divide-white/5 border-b border-white/5">
                        <div class="p-6">
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-2">Available</p>
                            <p class="text-2xl font-black text-white">42 <span class="text-sm text-slate-500 font-bold">/100</span></p>
                        </div>
                        <div class="p-6">
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-2">Revenue</p>
                            <p class="text-2xl font-black text-white">$1,240</p>
                        </div>
                        <div class="p-6">
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-2">Active</p>
                            <p class="text-2xl font-black text-primary">58</p>
                        </div>
                        <div class="p-6">
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-2">Occupancy</p>
                            <p class="text-2xl font-black text-emerald-400">94%</p>
                        </div>
                    </div>
                    <div class="p-4 overflow-x-auto">
                        <table class="w-full text-xs">
                            <thead>
                            <tr class="text-slate-600 font-black uppercase tracking-widest">
                                <th class="px-4 py-3 text-left">Plate</th>
                                <th class="px-4 py-3 text-left">Slot</th>
                                <th class="px-4 py-3 text-left">Entry</th>
                                <th class="px-4 py-3 text-left">Duration</th>
                                <th class="px-4 py-3 text-left">Status</th>
                                <th class="px-4 py-3 text-left">Payment</th>
                            </tr>
                            </thead>
                            <tbody class="divide-y divide-white/5 font-bold text-slate-300">
                            <tr class="hover:bg-white/[0.02]"><td class="px-4 py-4 text-white font-black">ABC-1234</td><td class="px-4 py-4">A-12</td><td class="px-4 py-4 opacity-50">10:45 AM</td><td class="px-4 py-4 opacity-50">2h 15m</td><td class="px-4 py-4"><span class="px-2 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Active</span></td><td class="px-4 py-4"><span class="px-2 py-1 bg-amber-500/10 text-amber-400 text-[9px] font-black uppercase rounded-full border border-amber-500/20">Pending</span></td></tr>
                            <tr class="hover:bg-white/[0.02]"><td class="px-4 py-4 text-white font-black">XYZ-8899</td><td class="px-4 py-4">B-04</td><td class="px-4 py-4 opacity-50">09:15 AM</td><td class="px-4 py-4 opacity-50">3h 50m</td><td class="px-4 py-4"><span class="px-2 py-1 bg-rose-500/10 text-rose-400 text-[9px] font-black uppercase rounded-full border border-rose-500/20">Exited</span></td><td class="px-4 py-4"><span class="px-2 py-1 bg-primary/10 text-blue-400 text-[9px] font-black uppercase rounded-full border border-primary/20">Settled</span></td></tr>
                            <tr class="hover:bg-white/[0.02]"><td class="px-4 py-4 text-white font-black">KLM-9012</td><td class="px-4 py-4">C-09</td><td class="px-4 py-4 opacity-50">11:05 AM</td><td class="px-4 py-4 opacity-50">1h 30m</td><td class="px-4 py-4"><span class="px-2 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Active</span></td><td class="px-4 py-4"><span class="px-2 py-1 bg-primary/10 text-blue-400 text-[9px] font-black uppercase rounded-full border border-primary/20">Settled</span></td></tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- TICKER -->
    <div class="border-y border-white/5 py-5 overflow-hidden bg-white/[0.01]">
        <div class="ticker-track whitespace-nowrap">
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Real-time Slot Intelligence</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Automated Payment Processing</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Role-Based Access Control</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">QR Code Entry System</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Advance Reservations</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Digital Receipts</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Occupancy Analytics</span>
            <span class="text-primary">·</span>
            <!-- duplicate for infinite loop -->
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Real-time Slot Intelligence</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Automated Payment Processing</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Role-Based Access Control</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">QR Code Entry System</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Advance Reservations</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Digital Receipts</span>
            <span class="text-primary">·</span>
            <span class="text-[10px] font-black uppercase tracking-[0.3em] text-slate-600">Occupancy Analytics</span>
            <span class="text-primary">·</span>
        </div>
    </div>

    <!-- FEATURES GRID -->
    <section class="container mx-auto px-6 lg:px-12 py-32">
        <div class="text-center mb-20">
            <div class="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-primary/10 border border-primary/20 text-primary text-[10px] font-black uppercase tracking-[0.22em] mb-6">Core Platform</div>
            <h2 class="text-5xl lg:text-6xl font-black text-white tracking-tight leading-tight">Everything your facility needs.<br/><span class="text-slate-500">Nothing it doesn't.</span></h2>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div class="feature-card glass-card rounded-[2.5rem] p-10 col-span-1 md:col-span-2 lg:col-span-1">
                <div class="h-14 w-14 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-primary text-2xl">directions_car</span>
                </div>
                <h3 class="text-2xl font-black text-white mb-4">Vehicle Entry & Exit</h3>
                <p class="text-slate-400 leading-relaxed">Seamless vehicle processing from first arrival to final exit. Plate recognition, QR codes and manual entry all supported.</p>
            </div>

            <div class="feature-card glass-card rounded-[2.5rem] p-10">
                <div class="h-14 w-14 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-emerald-400 text-2xl">grid_view</span>
                </div>
                <h3 class="text-2xl font-black text-white mb-4">Slot Intelligence</h3>
                <p class="text-slate-400 leading-relaxed">Visual real-time slot grid. Know every bay status at a glance — available, occupied, reserved or disabled.</p>
            </div>

            <div class="feature-card glass-card rounded-[2.5rem] p-10">
                <div class="h-14 w-14 rounded-2xl bg-violet-500/10 border border-violet-500/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-violet-400 text-2xl">payments</span>
                </div>
                <h3 class="text-2xl font-black text-white mb-4">Payment Automation</h3>
                <p class="text-slate-400 leading-relaxed">Automated fee calculation, wallet payments and digital receipts generated instantly on exit.</p>
            </div>

            <div class="feature-card glass-card rounded-[2.5rem] p-10">
                <div class="h-14 w-14 rounded-2xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-amber-400 text-2xl">calendar_month</span>
                </div>
                <h3 class="text-2xl font-black text-white mb-4">Advance Reservations</h3>
                <p class="text-slate-400 leading-relaxed">Let drivers reserve slots before arrival. Reduce congestion, eliminate uncertainty at the gate.</p>
            </div>

            <div class="feature-card glass-card rounded-[2.5rem] p-10">
                <div class="h-14 w-14 rounded-2xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-rose-400 text-2xl">shield_person</span>
                </div>
                <h3 class="text-2xl font-black text-white mb-4">Role-Based Access</h3>
                <p class="text-slate-400 leading-relaxed">Separate admin and operator views. Controlled permissions that protect sensitive operations.</p>
            </div>

            <div class="feature-card glass-card rounded-[2.5rem] p-10">
                <div class="h-14 w-14 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-primary text-2xl">bar_chart</span>
                </div>
                <h3 class="text-2xl font-black text-white mb-4">Live Analytics</h3>
                <p class="text-slate-400 leading-relaxed">Occupancy patterns, revenue trends, peak traffic periods — all surfaced in a clean analytics dashboard.</p>
            </div>
        </div>
    </section>

    <!-- SOLUTIONS TEASER -->
    <section class="container mx-auto px-6 lg:px-12 pb-32">
        <div class="glass-card rounded-[3rem] overflow-hidden">
            <div class="grid grid-cols-1 lg:grid-cols-2">
                <div class="p-14 lg:p-20">
                    <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 border border-primary/20 text-primary text-[10px] font-black uppercase tracking-[0.22em] mb-8">Who Uses Parkiyo</div>
                    <h2 class="text-4xl lg:text-5xl font-black text-white leading-tight mb-8">Built for real parking environments.</h2>
                    <div class="space-y-5">
                        <div class="flex items-center gap-5 p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <span class="material-symbols-outlined text-primary">local_mall</span>
                            <div>
                                <p class="text-sm font-black text-white">Shopping Malls</p>
                                <p class="text-xs text-slate-500 font-medium">High-volume, peak-hour management</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-5 p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <span class="material-symbols-outlined text-primary">hotel</span>
                            <div>
                                <p class="text-sm font-black text-white">Hotels & Hospitality</p>
                                <p class="text-xs text-slate-500 font-medium">Premium guest experience at arrival</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-5 p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <span class="material-symbols-outlined text-primary">school</span>
                            <div>
                                <p class="text-sm font-black text-white">Campuses & Enterprises</p>
                                <p class="text-xs text-slate-500 font-medium">Multi-zone, large-footprint operations</p>
                            </div>
                        </div>
                    </div>
                    <a href="solutions.html" class="mt-10 inline-flex items-center gap-3 text-primary font-black text-sm hover:gap-5 transition-all">
                        Explore all solutions <span class="material-symbols-outlined text-base">arrow_forward</span>
                    </a>
                </div>
                <div class="relative overflow-hidden min-h-[500px]">
                    <img src="https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&q=80&w=1400" alt="Facility" class="absolute inset-0 w-full h-full object-cover opacity-60"/>
                    <div class="absolute inset-0 bg-gradient-to-r from-background-dark/90 via-transparent to-transparent lg:block hidden"></div>
                </div>
            </div>
        </div>
    </section>

    <!-- CTA -->
    <section class="container mx-auto px-6 lg:px-12 pb-32">
        <div class="relative overflow-hidden rounded-[3rem] bg-primary p-16 lg:p-24 text-center">
            <div class="absolute inset-0 opacity-10" style="background-image: radial-gradient(circle at 20% 50%, white 1px, transparent 1px), radial-gradient(circle at 80% 20%, white 1px, transparent 1px); background-size: 40px 40px;"></div>
            <div class="relative z-10">
                <h2 class="text-4xl lg:text-6xl font-black text-white leading-tight mb-6">Ready to upgrade your parking operations?</h2>
                <p class="text-blue-200 text-lg mb-12 font-medium max-w-xl mx-auto">Join forward-thinking facilities using Parkiyo to run cleaner, smarter, more profitable parking.</p>
                <div class="flex flex-col sm:flex-row items-center justify-center gap-5">
                    <a href="register.html" class="w-full sm:w-auto bg-white text-primary font-black px-10 py-5 rounded-2xl hover:scale-105 transition-all text-base">Start for free today</a>
                    <a href="login.html" class="w-full sm:w-auto bg-white/10 text-white font-bold px-10 py-5 rounded-2xl hover:bg-white/20 transition-all text-base border border-white/20">Already have an account</a>
                </div>
            </div>
        </div>
    </section>
</main>

<!-- FOOTER -->
<footer class="border-t border-white/5 bg-background-dark pt-24 pb-12">
    <div class="container mx-auto px-6 lg:px-12">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-16 mb-20">
            <div class="col-span-2">
                <div class="flex items-center gap-4 mb-8">
                    <div class="flex h-11 w-11 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_20px_rgba(31,104,249,0.4)]">
                        <span class="material-symbols-outlined font-bold text-2xl">local_parking</span>
                    </div>
                    <span class="text-2xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
                </div>
                <p class="text-slate-500 max-w-sm leading-relaxed mb-8 text-sm font-medium">Premium parking management software for secure operations, cleaner workflows and real-time visibility across modern facilities.</p>
            </div>
            <div>
                <h5 class="text-white font-black mb-8 text-[11px] uppercase tracking-[0.2em] opacity-80">Platform</h5>
                <ul class="space-y-4 text-slate-500 text-sm font-bold">
                    <li><a href="features.html" class="hover:text-primary transition-colors">Features</a></li>
                    <li><a href="solutions.html" class="hover:text-primary transition-colors">Solutions</a></li>
                    <li><a href="analytics.html" class="hover:text-primary transition-colors">Analytics</a></li>
                    <li><a href="faq.html" class="hover:text-primary transition-colors">Support</a></li>
                </ul>
            </div>
            <div>
                <h5 class="text-white font-black mb-8 text-[11px] uppercase tracking-[0.2em] opacity-80">Company</h5>
                <ul class="space-y-4 text-slate-500 text-sm font-bold">
                    <li><a href="faq.html" class="hover:text-primary transition-colors">Help & FAQ</a></li>
                    <li><a href="privacy.html" class="hover:text-primary transition-colors">Privacy</a></li>
                    <li><a href="login.html" class="hover:text-primary transition-colors">Login</a></li>
                    <li><a href="register.html" class="hover:text-primary transition-colors">Register</a></li>
                </ul>
            </div>
        </div>
        <div class="pt-10 border-t border-white/5 flex flex-col md:flex-row justify-between items-center gap-6">
            <p class="text-slate-600 text-[9px] tracking-[0.3em] uppercase font-bold">© 2026 Parkiyo. All rights reserved.</p>
            <div class="flex gap-8">
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="home.html">language</a></span>
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="privacy.html">shield_person</a></span>
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="faq.html">support_agent</a></span>
            </div>
        </div>
    </div>
</footer>
</body>
</html>