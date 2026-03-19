<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Reports Hub</title>
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
                    borderRadius: { squircle: "14px", xl: "1.5rem", "2xl": "2.1rem", "3xl": "3rem" },
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

        .report-card {
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }

        .report-card:hover {
            transform: translateY(-4px);
            border-color: rgba(31, 104, 249, 0.25);
            background: rgba(31, 104, 249, 0.03);
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
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-1 overflow-y-auto">
            <a href="dashboard-admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">dashboard</span>
                <span class="nav-label text-sm">Dashboard</span>
            </a>
            <a href="entry-admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">login</span>
                <span class="nav-label text-sm">Vehicle Entry</span>
            </a>
            <a href="exitvehicle-admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">logout</span>
                <span class="nav-label text-sm">Vehicle Exit</span>
            </a>
            <a href="slot-overview.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">grid_view</span>
                <span class="nav-label text-sm">Parking Slots</span>
            </a>
            <a href="vehicle-list-page.html"
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
            <a href="repportshubpage.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
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
                <h2 class="text-xl font-black text-white">Reports Hub</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Analytics &
                    Reporting Centre</p>
            </div>
            <div class="flex items-center gap-4">
                <div class="text-right">
                    <p class="text-xs font-black text-white uppercase tracking-widest">Alex Johnson</p>
                    <p class="text-[10px] text-primary font-bold uppercase tracking-tighter">Admin</p>
                </div>
                <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">

            <!-- Summary Stats -->
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Total Revenue
                    </p>
                    <h3 class="text-2xl font-black text-white">$45,820</h3>
                    <p class="text-emerald-400 text-[10px] font-bold mt-2 flex items-center gap-1"><span
                            class="material-symbols-outlined text-xs">trending_up</span> +14% this month</p>
                </div>
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Total Sessions
                    </p>
                    <h3 class="text-2xl font-black text-white">3,241</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2">This month</p>
                </div>
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Avg Occupancy
                    </p>
                    <h3 class="text-2xl font-black text-primary">87%</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2">Peak: 94%</p>
                </div>
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Receipts Issued
                    </p>
                    <h3 class="text-2xl font-black text-white">3,198</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2">98.7% payment rate</p>
                </div>
            </div>

            <!-- Report Cards Grid -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

                <div class="report-card glass-card rounded-[2.5rem] p-8 cursor-pointer"
                     onclick="window.location.href='revenuerepoartpage.html'">
                    <div
                            class="h-14 w-14 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center mb-6">
                        <span class="material-symbols-outlined text-emerald-400 text-2xl">attach_money</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-emerald-400 mb-2">Financial
                    </p>
                    <h3 class="text-xl font-black text-white mb-3">Revenue Report</h3>
                    <p class="text-slate-400 text-sm font-medium leading-relaxed mb-6">Total revenue, payment method
                        breakdown, daily and monthly trends, and receipt summaries.</p>
                    <div class="flex items-center justify-between">
                        <span class="text-xs text-slate-500 font-bold">Last updated: Today</span>
                        <div
                                class="h-9 w-9 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-emerald-400 text-lg">arrow_forward</span>
                        </div>
                    </div>
                </div>

                <div class="report-card glass-card rounded-[2.5rem] p-8 cursor-pointer"
                     onclick="window.location.href='occupancyrepoartpage.html'">
                    <div
                            class="h-14 w-14 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center mb-6">
                        <span class="material-symbols-outlined text-primary text-2xl">grid_view</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-primary mb-2">Operations</p>
                    <h3 class="text-xl font-black text-white mb-3">Occupancy Report</h3>
                    <p class="text-slate-400 text-sm font-medium leading-relaxed mb-6">Slot usage patterns, peak
                        times, available vs occupied trends, and zone-level breakdowns.</p>
                    <div class="flex items-center justify-between">
                        <span class="text-xs text-slate-500 font-bold">Last updated: Today</span>
                        <div
                                class="h-9 w-9 rounded-xl bg-primary/10 border border-primary/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-primary text-lg">arrow_forward</span>
                        </div>
                    </div>
                </div>

                <div class="report-card glass-card rounded-[2.5rem] p-8 cursor-pointer"
                     onclick="window.location.href='dailyrevenuereport.html'">
                    <div
                            class="h-14 w-14 rounded-2xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center mb-6">
                        <span class="material-symbols-outlined text-amber-400 text-2xl">today</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-amber-400 mb-2">Daily</p>
                    <h3 class="text-xl font-black text-white mb-3">Daily Revenue Report</h3>
                    <p class="text-slate-400 text-sm font-medium leading-relaxed mb-6">Today's revenue total,
                        successful and pending payments, and full transaction list for the day.</p>
                    <div class="flex items-center justify-between">
                        <span class="text-xs text-slate-500 font-bold">Today only</span>
                        <div
                                class="h-9 w-9 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-amber-400 text-lg">arrow_forward</span>
                        </div>
                    </div>
                </div>

                <div class="report-card glass-card rounded-[2.5rem] p-8 cursor-pointer"
                     onclick="window.location.href='auditlog.html'">
                    <div
                            class="h-14 w-14 rounded-2xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center mb-6">
                        <span class="material-symbols-outlined text-rose-400 text-2xl">fact_check</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-rose-400 mb-2">Security</p>
                    <h3 class="text-xl font-black text-white mb-3">Audit Log</h3>
                    <p class="text-slate-400 text-sm font-medium leading-relaxed mb-6">Full system action log with
                        timestamps, user, action type, affected module, and status.</p>
                    <div class="flex items-center justify-between">
                        <span class="text-xs text-slate-500 font-bold">Real-time log</span>
                        <div
                                class="h-9 w-9 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-rose-400 text-lg">arrow_forward</span>
                        </div>
                    </div>
                </div>

                <div class="report-card glass-card rounded-[2.5rem] p-8 cursor-pointer"
                     onclick="window.location.href='paymenthistory.html'">
                    <div
                            class="h-14 w-14 rounded-2xl bg-violet-500/10 border border-violet-500/20 flex items-center justify-center mb-6">
                        <span class="material-symbols-outlined text-violet-400 text-2xl">receipt_long</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-violet-400 mb-2">Payments</p>
                    <h3 class="text-xl font-black text-white mb-3">Payment Summary</h3>
                    <p class="text-slate-400 text-sm font-medium leading-relaxed mb-6">All transactions, receipts,
                        payment statuses and wallet activity across the entire platform.</p>
                    <div class="flex items-center justify-between">
                        <span class="text-xs text-slate-500 font-bold">All time</span>
                        <div
                                class="h-9 w-9 rounded-xl bg-violet-500/10 border border-violet-500/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-violet-400 text-lg">arrow_forward</span>
                        </div>
                    </div>
                </div>

                <div class="report-card glass-card rounded-[2.5rem] p-8 cursor-pointer"
                     onclick="window.location.href='slot-usage-history.html'">
                    <div
                            class="h-14 w-14 rounded-2xl bg-cyan-500/10 border border-cyan-500/20 flex items-center justify-center mb-6">
                        <span class="material-symbols-outlined text-cyan-400 text-2xl">history</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-cyan-400 mb-2">Slots</p>
                    <h3 class="text-xl font-black text-white mb-3">Slot Usage History</h3>
                    <p class="text-slate-400 text-sm font-medium leading-relaxed mb-6">Per-slot occupancy history,
                        reservation records, and usage timeline for every bay in the system.</p>
                    <div class="flex items-center justify-between">
                        <span class="text-xs text-slate-500 font-bold">Full history</span>
                        <div
                                class="h-9 w-9 rounded-xl bg-cyan-500/10 border border-cyan-500/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-cyan-400 text-lg">arrow_forward</span>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>
</div>
</body>

</html>