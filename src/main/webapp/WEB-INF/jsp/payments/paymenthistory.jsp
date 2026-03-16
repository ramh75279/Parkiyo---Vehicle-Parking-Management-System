<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Payment History</title>
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

        .pill-paid {
            background: rgba(16, 185, 129, 0.1);
            color: #34d399;
            border: 1px solid rgba(16, 185, 129, 0.2);
        }

        .pill-pending {
            background: rgba(245, 158, 11, 0.1);
            color: #fbbf24;
            border: 1px solid rgba(245, 158, 11, 0.2);
        }

        .pill-refunded {
            background: rgba(100, 116, 139, 0.1);
            color: #94a3b8;
            border: 1px solid rgba(100, 116, 139, 0.2);
        }

        .pill-failed {
            background: rgba(239, 68, 68, 0.1);
            color: #f87171;
            border: 1px solid rgba(239, 68, 68, 0.2);
        }

        .status-pill {
            padding: 3px 11px;
            border-radius: 9px;
            font-size: 0.67rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .txn-row {
            transition: background 0.15s;
            cursor: pointer;
        }

        .txn-row:hover {
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

    <aside class="sidebar-container border-r border-white/5 premium-blur flex flex-col shrink-0 z-50">
        <div class="p-6 mb-4 flex items-center">
            <div
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-[14px] bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
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
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
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
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
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
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div>
                <h2 class="text-xl font-black text-white">Payment History</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">All transactions
                </p>
            </div>
            <div class="flex items-center gap-3">
                <button
                        class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">download</span> Export
                </button>
                <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">

            <!-- KPI cards -->
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Total Paid</p>
                    <h3 class="text-3xl font-black text-emerald-400">$842.50</h3>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">All time</p>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">This Month</p>
                    <h3 class="text-3xl font-black text-white">$124.75</h3>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">Mar 2026</p>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Transactions</p>
                    <h3 class="text-3xl font-black text-white">47</h3>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">All time</p>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Avg per Session
                    </p>
                    <h3 class="text-3xl font-black text-primary">$17.92</h3>
                    <p class="text-[10px] text-slate-500 font-bold mt-1">Per transaction</p>
                </div>
            </div>

            <!-- Monthly spend chart -->
            <div class="glass-card rounded-[2.5rem] p-8 mb-8">
                <h3 class="text-base font-black text-white mb-6">Monthly Spend</h3>
                <div class="grid grid-cols-6 gap-3 items-end h-24">
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">$68</span>
                        <div class="w-full rounded-t-xl bg-primary/40" style="height:55%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Oct</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">$95</span>
                        <div class="w-full rounded-t-xl bg-primary/55" style="height:77%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Nov</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">$82</span>
                        <div class="w-full rounded-t-xl bg-primary/50" style="height:66%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Dec</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">$112</span>
                        <div class="w-full rounded-t-xl bg-primary/65" style="height:91%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Jan</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">$88</span>
                        <div class="w-full rounded-t-xl bg-primary/55" style="height:71%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Feb</span>
                    </div>
                    <div class="flex flex-col items-center gap-2"><span
                            class="text-[10px] font-black text-white">$125</span>
                        <div class="w-full rounded-t-xl bg-primary" style="height:100%"></div><span
                                class="text-[9px] font-black text-slate-500 uppercase">Mar</span>
                    </div>
                </div>
            </div>

            <!-- Filters -->
            <div class="glass-card rounded-[2rem] p-5 mb-6 flex flex-wrap gap-4 items-center">
                <div class="flex-1 min-w-[180px] relative">
                        <span
                                class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-500 text-lg">search</span>
                    <input type="text" placeholder="Search by plate or transaction ID…"
                           class="bg-white/5 border border-white/20 input-glass rounded-2xl px-4 h-12 w-full pl-10" />
                </div>
                <input type="date" class="input-glass px-4 h-12 rounded-2xl" value="2026-03-01" />
                <input type="date" class="input-glass px-4 h-12 rounded-2xl" value="2026-03-10" />
                <select class="input-glass rounded-2xl px-4 h-12">
                    <option>All Statuses</option>
                    <option>Paid</option>
                    <option>Pending</option>
                    <option>Refunded</option>
                    <option>Failed</option>
                </select>
                <select class="input-glass rounded-2xl px-4 h-12">
                    <option>All Methods</option>
                    <option>Wallet</option>
                    <option>Card</option>
                    <option>Cash</option>
                </select>
            </div>

            <!-- Transaction table -->
            <div class="glass-card rounded-[2.5rem] overflow-hidden">
                <div class="p-8 border-b border-white/5 flex items-center justify-between">
                    <h3 class="text-lg font-black text-white">Transactions</h3>
                    <span class="text-[10px] font-black uppercase tracking-widest text-slate-500">47 total</span>
                </div>
                <div class="overflow-x-auto">
                    <table class="w-full text-left">
                        <thead>
                        <tr
                                class="text-slate-500 text-[10px] font-black uppercase tracking-widest border-b border-white/5">
                            <th class="px-8 py-5">Transaction ID</th>
                            <th class="px-8 py-5">Date</th>
                            <th class="px-8 py-5">Vehicle</th>
                            <th class="px-8 py-5">Type</th>
                            <th class="px-8 py-5">Method</th>
                            <th class="px-8 py-5">Amount</th>
                            <th class="px-8 py-5">Status</th>
                            <th class="px-8 py-5">Receipt</th>
                        </tr>
                        </thead>
                        <tbody class="text-sm font-bold text-slate-300 divide-y divide-white/5">
                        <tr class="txn-row" onclick="window.location.href='receipt.html'">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">TXN-88291047</td>
                            <td class="px-8 py-5 opacity-70 text-xs">10 Mar 2026</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">XYZ-8899</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Reservation</td>
                            <td class="px-8 py-5 opacity-70 flex items-center gap-1.5 text-xs"><span
                                    class="material-symbols-outlined text-emerald-400 text-sm">account_balance_wallet</span>Wallet
                            </td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$16.50</td>
                            <td class="px-8 py-5"><span class="status-pill pill-paid">Paid</span></td>
                            <td class="px-8 py-5" onclick="event.stopPropagation()"><a href="receipt.html"
                                                                                       class="h-8 w-8 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-primary/20 transition-all"><span
                                    class="material-symbols-outlined text-sm text-slate-400">receipt_long</span></a>
                            </td>
                        </tr>
                        <tr class="txn-row" onclick="window.location.href='receipt.html'">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">TXN-88290031</td>
                            <td class="px-8 py-5 opacity-70 text-xs">09 Mar 2026</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">ABC-1234</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Walk-in</td>
                            <td class="px-8 py-5 opacity-70 flex items-center gap-1.5 text-xs"><span
                                    class="material-symbols-outlined text-primary text-sm">credit_card</span>Card
                            </td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$18.75</td>
                            <td class="px-8 py-5"><span class="status-pill pill-paid">Paid</span></td>
                            <td class="px-8 py-5" onclick="event.stopPropagation()"><a href="receipt.html"
                                                                                       class="h-8 w-8 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-primary/20 transition-all"><span
                                    class="material-symbols-outlined text-sm text-slate-400">receipt_long</span></a>
                            </td>
                        </tr>
                        <tr class="txn-row" onclick="window.location.href='receipt.html'">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">TXN-88288714</td>
                            <td class="px-8 py-5 opacity-70 text-xs">08 Mar 2026</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">GHI-7721</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Walk-in</td>
                            <td class="px-8 py-5 opacity-70 flex items-center gap-1.5 text-xs"><span
                                    class="material-symbols-outlined text-emerald-400 text-sm">account_balance_wallet</span>Wallet
                            </td>
                            <td class="px-8 py-5 text-emerald-400 font-black">$13.75</td>
                            <td class="px-8 py-5"><span class="status-pill pill-paid">Paid</span></td>
                            <td class="px-8 py-5" onclick="event.stopPropagation()"><a href="receipt.html"
                                                                                       class="h-8 w-8 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-primary/20 transition-all"><span
                                    class="material-symbols-outlined text-sm text-slate-400">receipt_long</span></a>
                            </td>
                        </tr>
                        <tr class="txn-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">TXN-88286200</td>
                            <td class="px-8 py-5 opacity-70 text-xs">07 Mar 2026</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">MNO-3310</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Reservation</td>
                            <td class="px-8 py-5 opacity-70 flex items-center gap-1.5 text-xs"><span
                                    class="material-symbols-outlined text-emerald-400 text-sm">account_balance_wallet</span>Wallet
                            </td>
                            <td class="px-8 py-5 text-amber-400 font-black">$22.00</td>
                            <td class="px-8 py-5"><span class="status-pill pill-pending">Pending</span></td>
                            <td class="px-8 py-5"><span
                                    class="h-8 w-8 rounded-xl bg-white/[0.02] border border-white/5 flex items-center justify-center opacity-30"><span
                                    class="material-symbols-outlined text-sm text-slate-600">receipt_long</span></span>
                            </td>
                        </tr>
                        <tr class="txn-row" onclick="window.location.href='receipt.html'">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">TXN-88281044</td>
                            <td class="px-8 py-5 opacity-70 text-xs">05 Mar 2026</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">TRK-5502</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Walk-in</td>
                            <td class="px-8 py-5 opacity-70 flex items-center gap-1.5 text-xs"><span
                                    class="material-symbols-outlined text-amber-400 text-sm">payments</span>Cash
                            </td>
                            <td class="px-8 py-5 text-slate-400 font-black line-through">$14.75</td>
                            <td class="px-8 py-5"><span class="status-pill pill-refunded">Refunded</span></td>
                            <td class="px-8 py-5" onclick="event.stopPropagation()"><a href="receipt.html"
                                                                                       class="h-8 w-8 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-primary/20 transition-all"><span
                                    class="material-symbols-outlined text-sm text-slate-400">receipt_long</span></a>
                            </td>
                        </tr>
                        <tr class="txn-row">
                            <td class="px-8 py-5 font-mono text-xs text-slate-400">TXN-88278912</td>
                            <td class="px-8 py-5 opacity-70 text-xs">04 Mar 2026</td>
                            <td class="px-8 py-5"><span
                                    class="text-white font-black tracking-wider bg-white/5 px-2.5 py-1 rounded-lg text-xs">PQR-6641</span>
                            </td>
                            <td class="px-8 py-5 opacity-70">Reservation</td>
                            <td class="px-8 py-5 opacity-70 flex items-center gap-1.5 text-xs"><span
                                    class="material-symbols-outlined text-primary text-sm">credit_card</span>Card
                            </td>
                            <td class="px-8 py-5 text-rose-400 font-black">$9.00</td>
                            <td class="px-8 py-5"><span class="status-pill pill-failed">Failed</span></td>
                            <td class="px-8 py-5"><span
                                    class="h-8 w-8 rounded-xl bg-white/[0.02] border border-white/5 flex items-center justify-center opacity-30"><span
                                    class="material-symbols-outlined text-sm text-slate-600">receipt_long</span></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="px-8 py-5 border-t border-white/5 flex items-center justify-between">
                    <p class="text-xs font-bold text-slate-500">Showing 6 of 47 transactions · Page 1 of 8</p>
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
                                class="h-9 px-4 rounded-xl bg-white/5 border border-white/10 text-slate-400 font-black text-xs hover:bg-white/10 transition-all">8</button>
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