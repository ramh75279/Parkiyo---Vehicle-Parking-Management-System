<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | System Status</title>
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

        @keyframes pulse-dot {

            0%,
            100% {
                opacity: 1;
                transform: scale(1);
            }

            50% {
                opacity: 0.5;
                transform: scale(1.4);
            }
        }

        .live-dot {
            animation: pulse-dot 2s ease-in-out infinite;
        }

        .uptime-bar {
            height: 6px;
            border-radius: 3px;
            background: rgba(255, 255, 255, 0.05);
            overflow: hidden;
        }

        .uptime-fill {
            height: 100%;
            border-radius: 3px;
            transition: width 1s ease;
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
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
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
                <h2 class="text-xl font-black text-white">System Status</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Platform Health
                    Monitor</p>
            </div>
            <div class="flex items-center gap-6">
                <div
                        class="flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-500/10 border border-emerald-500/20">
                    <span class="h-2 w-2 bg-emerald-400 rounded-full live-dot"></span>
                    <span class="text-[11px] font-black uppercase tracking-widest text-emerald-400">All Systems
                            Operational</span>
                </div>
                <div class="flex items-center gap-4">
                    <div class="text-right">
                        <p class="text-xs font-black text-white uppercase tracking-widest">Alex Johnson</p>
                        <p class="text-[10px] text-primary font-bold uppercase tracking-tighter">Admin</p>
                    </div>
                    <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                        <div
                                class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                            <span class="material-symbols-outlined text-white/50">person</span>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">

            <!-- Overall Status Banner -->
            <div
                    class="glass-card rounded-3xl p-8 mb-8 flex items-center gap-6 border border-emerald-500/15 bg-emerald-500/5">
                <div
                        class="h-16 w-16 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center shrink-0">
                    <span class="material-symbols-outlined text-emerald-400 text-3xl">verified</span>
                </div>
                <div class="flex-1">
                    <p class="text-lg font-black text-white mb-1">All Systems Operational</p>
                    <p class="text-sm text-slate-400 font-medium">No incidents detected. Last checked: <span
                            class="text-white font-bold">Today at 11:58 AM</span></p>
                </div>
                <div class="text-right">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">System Uptime
                    </p>
                    <p class="text-3xl font-black text-emerald-400">99.9%</p>
                </div>
            </div>

            <!-- Service Status Cards -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">

                <!-- Core App -->
                <div class="glass-card rounded-3xl p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div class="flex items-center gap-3">
                            <div
                                    class="h-11 w-11 rounded-xl bg-primary/10 border border-primary/20 flex items-center justify-center">
                                <span class="material-symbols-outlined text-primary">web</span>
                            </div>
                            <div>
                                <p class="text-sm font-black text-white">Core Application</p>
                                <p class="text-[10px] text-slate-500 font-bold">Main platform service</p>
                            </div>
                        </div>
                        <span
                                class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Operational</span>
                    </div>
                    <div class="uptime-bar mb-2">
                        <div class="uptime-fill bg-emerald-500" style="width: 99.9%"></div>
                    </div>
                    <div
                            class="flex justify-between text-[10px] font-black text-slate-500 uppercase tracking-wider">
                        <span>Uptime</span><span class="text-emerald-400">99.9%</span>
                    </div>
                </div>

                <!-- Database -->
                <div class="glass-card rounded-3xl p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div class="flex items-center gap-3">
                            <div
                                    class="h-11 w-11 rounded-xl bg-violet-500/10 border border-violet-500/20 flex items-center justify-center">
                                <span class="material-symbols-outlined text-violet-400">database</span>
                            </div>
                            <div>
                                <p class="text-sm font-black text-white">Database</p>
                                <p class="text-[10px] text-slate-500 font-bold">Primary data store</p>
                            </div>
                        </div>
                        <span
                                class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Operational</span>
                    </div>
                    <div class="uptime-bar mb-2">
                        <div class="uptime-fill bg-violet-500" style="width: 100%"></div>
                    </div>
                    <div
                            class="flex justify-between text-[10px] font-black text-slate-500 uppercase tracking-wider">
                        <span>Uptime</span><span class="text-emerald-400">100%</span>
                    </div>
                </div>

                <!-- Payment Service -->
                <div class="glass-card rounded-3xl p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div class="flex items-center gap-3">
                            <div
                                    class="h-11 w-11 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center">
                                <span class="material-symbols-outlined text-emerald-400">payments</span>
                            </div>
                            <div>
                                <p class="text-sm font-black text-white">Payment Service</p>
                                <p class="text-[10px] text-slate-500 font-bold">Transaction processing</p>
                            </div>
                        </div>
                        <span
                                class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Operational</span>
                    </div>
                    <div class="uptime-bar mb-2">
                        <div class="uptime-fill bg-emerald-500" style="width: 99.7%"></div>
                    </div>
                    <div
                            class="flex justify-between text-[10px] font-black text-slate-500 uppercase tracking-wider">
                        <span>Uptime</span><span class="text-emerald-400">99.7%</span>
                    </div>
                </div>

                <!-- QR / Auth Service -->
                <div class="glass-card rounded-3xl p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div class="flex items-center gap-3">
                            <div
                                    class="h-11 w-11 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center">
                                <span class="material-symbols-outlined text-amber-400">qr_code_scanner</span>
                            </div>
                            <div>
                                <p class="text-sm font-black text-white">QR & Auth Service</p>
                                <p class="text-[10px] text-slate-500 font-bold">Login & scanning</p>
                            </div>
                        </div>
                        <span
                                class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Operational</span>
                    </div>
                    <div class="uptime-bar mb-2">
                        <div class="uptime-fill bg-amber-500" style="width: 99.5%"></div>
                    </div>
                    <div
                            class="flex justify-between text-[10px] font-black text-slate-500 uppercase tracking-wider">
                        <span>Uptime</span><span class="text-emerald-400">99.5%</span>
                    </div>
                </div>

                <!-- Backup Service -->
                <div class="glass-card rounded-3xl p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div class="flex items-center gap-3">
                            <div
                                    class="h-11 w-11 rounded-xl bg-primary/10 border border-primary/20 flex items-center justify-center">
                                <span class="material-symbols-outlined text-primary">backup</span>
                            </div>
                            <div>
                                <p class="text-sm font-black text-white">Backup Service</p>
                                <p class="text-[10px] text-slate-500 font-bold">Data backup system</p>
                            </div>
                        </div>
                        <span
                                class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Operational</span>
                    </div>
                    <div class="uptime-bar mb-2">
                        <div class="uptime-fill bg-primary" style="width: 98.8%"></div>
                    </div>
                    <div
                            class="flex justify-between text-[10px] font-black text-slate-500 uppercase tracking-wider">
                        <span>Last Backup</span><span class="text-primary">2h ago</span>
                    </div>
                </div>

                <!-- Notifications -->
                <div class="glass-card rounded-3xl p-7">
                    <div class="flex items-center justify-between mb-5">
                        <div class="flex items-center gap-3">
                            <div
                                    class="h-11 w-11 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center">
                                <span class="material-symbols-outlined text-rose-400">notifications</span>
                            </div>
                            <div>
                                <p class="text-sm font-black text-white">Notification Service</p>
                                <p class="text-[10px] text-slate-500 font-bold">Alerts & messaging</p>
                            </div>
                        </div>
                        <span
                                class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Operational</span>
                    </div>
                    <div class="uptime-bar mb-2">
                        <div class="uptime-fill bg-rose-500" style="width: 99.2%"></div>
                    </div>
                    <div
                            class="flex justify-between text-[10px] font-black text-slate-500 uppercase tracking-wider">
                        <span>Uptime</span><span class="text-emerald-400">99.2%</span>
                    </div>
                </div>

            </div>

            <!-- Recent Incidents -->
            <div class="glass-card rounded-[2.5rem] overflow-hidden">
                <div class="p-8 border-b border-white/5 flex items-center justify-between">
                    <h3 class="text-lg font-black text-white">Recent Incidents</h3>
                    <span class="text-[10px] font-black uppercase tracking-widest text-slate-500">Last 30
                            days</span>
                </div>
                <div class="divide-y divide-white/5">
                    <div class="p-7 flex items-start gap-5">
                        <div
                                class="h-10 w-10 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center shrink-0 mt-0.5">
                            <span class="material-symbols-outlined text-amber-400 text-lg">warning</span>
                        </div>
                        <div class="flex-1">
                            <div class="flex items-center justify-between gap-4 mb-1">
                                <p class="text-sm font-black text-white">Payment Service — Elevated Latency</p>
                                <span
                                        class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20 shrink-0">Resolved</span>
                            </div>
                            <p class="text-xs text-slate-400 font-medium leading-relaxed">Transaction processing
                                experienced elevated response times for approximately 12 minutes. Root cause
                                identified as a database connection pool exhaustion. Issue resolved and pool limits
                                increased.</p>
                            <p class="text-[10px] text-slate-600 font-bold uppercase tracking-wider mt-2">Feb 28,
                                2026 · 02:14 AM — 02:26 AM</p>
                        </div>
                    </div>
                    <div class="p-7 flex items-start gap-5">
                        <div
                                class="h-10 w-10 rounded-xl bg-primary/10 border border-primary/20 flex items-center justify-center shrink-0 mt-0.5">
                            <span class="material-symbols-outlined text-primary text-lg">info</span>
                        </div>
                        <div class="flex-1">
                            <div class="flex items-center justify-between gap-4 mb-1">
                                <p class="text-sm font-black text-white">Scheduled Maintenance — Database</p>
                                <span
                                        class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20 shrink-0">Completed</span>
                            </div>
                            <p class="text-xs text-slate-400 font-medium leading-relaxed">Planned maintenance window
                                for database index optimisation. All services were temporarily in read-only mode.
                                Completed ahead of schedule with no data loss.</p>
                            <p class="text-[10px] text-slate-600 font-bold uppercase tracking-wider mt-2">Feb 15,
                                2026 · 01:00 AM — 01:45 AM</p>
                        </div>
                    </div>
                    <div class="p-7 flex items-center gap-5">
                        <div
                                class="h-10 w-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center shrink-0">
                            <span class="material-symbols-outlined text-emerald-400 text-lg">check_circle</span>
                        </div>
                        <p class="text-sm text-slate-500 font-medium">No other incidents in the past 30 days.</p>
                    </div>
                </div>
            </div>

        </div>
    </main>
</div>
</body>

</html>