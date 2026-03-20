<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Occupancy Report</title>
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

        .bar-fill {
            transition: width 0.9s cubic-bezier(0.4, 0, 0.2, 1);
        }

        .slot-cell {
            transition: all 0.2s ease;
        }

        .slot-cell:hover {
            transform: scale(1.08);
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
            <a href="/dashboard_admin"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">dashboard</span><span
                    class="nav-label text-sm">Dashboard</span></a>
            <a href="/entry"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle
                        Entry</span></a>
            <a href="/exitvehicle"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle
                        Exit</span></a>
            <a href="/slot_overview"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">grid_view</span><span
                    class="nav-label text-sm">Parking Slots</span></a>
            <a href="/usermanagement"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">group</span><span
                    class="nav-label text-sm">Users</span></a>
            <a href="/paymenthistory"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">payments</span><span
                    class="nav-label text-sm">Payments</span></a>
            <a href="/Repportshubpage"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span
                    class="material-symbols-outlined shrink-0">bar_chart</span><span
                    class="nav-label text-sm">Reports</span></a>
            <a href="/systemstatuspage"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">monitor_heart</span><span
                    class="nav-label text-sm">System Status</span></a>
            <a href="/accountsetting"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">settings</span><span
                    class="nav-label text-sm">Settings</span></a>
        </nav>
        <div class="p-4 border-t border-white/5">
            <button onclick="window.location.href='logout.html'"
                    class="flex items-center w-full px-4 py-4 text-rose-500 hover:bg-rose-500/10 rounded-xl text-sm font-black transition-all">
                    <span class="material-symbols-outlined shrink-0"><a
                            href="/logout">power_settings_new</a></span><span class="nav-label"><a
                    href="/logout">Logout</a></span>
            </button>
        </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden bg-subtle-radial">
        <!-- TOPBAR -->
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div class="flex items-center gap-4">
                <button onclick="window.location.href='Repportshubpage.html'"
                        class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Occupancy Report</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Slot Usage &
                        Traffic Analysis</p>
                </div>
            </div>
            <div class="flex items-center gap-4">
                <button
                        class="flex items-center gap-2 bg-primary/10 border border-primary/20 text-primary font-black px-5 py-2.5 rounded-xl hover:bg-primary/20 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">download</span> Export
                </button>
                <div class="flex items-center gap-4">
                    <div class="text-right hidden xl:block">
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

            <!-- KPI Cards -->
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Total Slots</p>
                    <h3 class="text-3xl font-black text-white">100</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2">Across all zones</p>
                </div>
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Currently
                        Occupied</p>
                    <h3 class="text-3xl font-black text-rose-400">58</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2">58% occupancy</p>
                </div>
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Available Now
                    </p>
                    <h3 class="text-3xl font-black text-emerald-400">42</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2">Ready for entry</p>
                </div>
                <div class="glass-card p-7 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Peak Occupancy
                    </p>
                    <h3 class="text-3xl font-black text-primary">94%</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2">Morning peak today</p>
                </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 mb-8">

                <!-- Hourly Traffic -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-8">Hourly Occupancy Pattern</h3>
                    <div class="space-y-4">
                        <div>
                            <div class="flex justify-between text-xs font-bold text-slate-400 mb-1.5"><span>6 AM – 8
                                        AM</span><span class="text-white">32%</span></div>
                            <div class="h-2.5 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary/40 rounded-full bar-fill" style="width:32%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="flex justify-between text-xs font-bold text-slate-400 mb-1.5"><span>8 AM –
                                        10 AM</span><span class="text-white font-black">85%</span></div>
                            <div class="h-2.5 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary rounded-full bar-fill" style="width:85%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="flex justify-between text-xs font-bold text-slate-400 mb-1.5"><span>10 AM –
                                        12 PM</span><span class="text-white font-black">94%</span></div>
                            <div class="h-2.5 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-rose-500 rounded-full bar-fill" style="width:94%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="flex justify-between text-xs font-bold text-slate-400 mb-1.5"><span>12 PM –
                                        2 PM</span><span class="text-white font-black">78%</span></div>
                            <div class="h-2.5 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-amber-500 rounded-full bar-fill" style="width:78%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="flex justify-between text-xs font-bold text-slate-400 mb-1.5"><span>2 PM – 4
                                        PM</span><span class="text-white font-black">65%</span></div>
                            <div class="h-2.5 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary/70 rounded-full bar-fill" style="width:65%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="flex justify-between text-xs font-bold text-slate-400 mb-1.5"><span>4 PM – 6
                                        PM</span><span class="text-white font-black">76%</span></div>
                            <div class="h-2.5 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary rounded-full bar-fill" style="width:76%"></div>
                            </div>
                        </div>
                        <div>
                            <div class="flex justify-between text-xs font-bold text-slate-400 mb-1.5"><span>6 PM –
                                        10 PM</span><span class="text-white">41%</span></div>
                            <div class="h-2.5 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary/40 rounded-full bar-fill" style="width:41%"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Zone Breakdown -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-8">Zone Occupancy Breakdown</h3>
                    <div class="space-y-5">
                        <div class="p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <div class="flex items-center justify-between mb-3">
                                <div>
                                    <p class="text-sm font-black text-white">Zone A</p>
                                    <p class="text-xs text-slate-500 font-medium">Ground Floor · 25 slots</p>
                                </div>
                                <span class="text-2xl font-black text-rose-400">92%</span>
                            </div>
                            <div class="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-rose-500 rounded-full bar-fill" style="width:92%"></div>
                            </div>
                        </div>
                        <div class="p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <div class="flex items-center justify-between mb-3">
                                <div>
                                    <p class="text-sm font-black text-white">Zone B</p>
                                    <p class="text-xs text-slate-500 font-medium">Level 1 · 30 slots</p>
                                </div>
                                <span class="text-2xl font-black text-amber-400">67%</span>
                            </div>
                            <div class="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-amber-500 rounded-full bar-fill" style="width:67%"></div>
                            </div>
                        </div>
                        <div class="p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <div class="flex items-center justify-between mb-3">
                                <div>
                                    <p class="text-sm font-black text-white">Zone C</p>
                                    <p class="text-xs text-slate-500 font-medium">Level 2 · 25 slots</p>
                                </div>
                                <span class="text-2xl font-black text-primary">44%</span>
                            </div>
                            <div class="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary rounded-full bar-fill" style="width:44%"></div>
                            </div>
                        </div>
                        <div class="p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <div class="flex items-center justify-between mb-3">
                                <div>
                                    <p class="text-sm font-black text-white">Zone D</p>
                                    <p class="text-xs text-slate-500 font-medium">Rooftop · 20 slots</p>
                                </div>
                                <span class="text-2xl font-black text-emerald-400">25%</span>
                            </div>
                            <div class="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-emerald-500 rounded-full bar-fill" style="width:25%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Weekly Trend -->
            <div class="glass-card rounded-[2.5rem] p-8">
                <div class="flex items-center justify-between mb-8">
                    <h3 class="text-lg font-black text-white">Weekly Occupancy Trend</h3>
                    <span class="text-[10px] font-black uppercase tracking-widest text-slate-500">This Week</span>
                </div>
                <div class="grid grid-cols-7 gap-3 items-end h-32">
                    <div class="flex flex-col items-center gap-2">
                        <span class="text-[10px] font-black text-white">72%</span>
                        <div class="w-full rounded-t-xl bg-primary/60" style="height:72%"></div>
                        <span class="text-[9px] font-black text-slate-500 uppercase tracking-wider">Mon</span>
                    </div>
                    <div class="flex flex-col items-center gap-2">
                        <span class="text-[10px] font-black text-white">81%</span>
                        <div class="w-full rounded-t-xl bg-primary/70" style="height:81%"></div>
                        <span class="text-[9px] font-black text-slate-500 uppercase tracking-wider">Tue</span>
                    </div>
                    <div class="flex flex-col items-center gap-2">
                        <span class="text-[10px] font-black text-white">94%</span>
                        <div class="w-full rounded-t-xl bg-rose-500" style="height:94%"></div>
                        <span class="text-[9px] font-black text-slate-500 uppercase tracking-wider">Wed</span>
                    </div>
                    <div class="flex flex-col items-center gap-2">
                        <span class="text-[10px] font-black text-white">88%</span>
                        <div class="w-full rounded-t-xl bg-primary" style="height:88%"></div>
                        <span class="text-[9px] font-black text-slate-500 uppercase tracking-wider">Thu</span>
                    </div>
                    <div class="flex flex-col items-center gap-2">
                        <span class="text-[10px] font-black text-white">91%</span>
                        <div class="w-full rounded-t-xl bg-amber-500" style="height:91%"></div>
                        <span class="text-[9px] font-black text-slate-500 uppercase tracking-wider">Fri</span>
                    </div>
                    <div class="flex flex-col items-center gap-2">
                        <span class="text-[10px] font-black text-white">68%</span>
                        <div class="w-full rounded-t-xl bg-primary/50" style="height:68%"></div>
                        <span class="text-[9px] font-black text-slate-500 uppercase tracking-wider">Sat</span>
                    </div>
                    <div class="flex flex-col items-center gap-2">
                        <span class="text-[10px] font-black text-white">45%</span>
                        <div class="w-full rounded-t-xl bg-primary/30" style="height:45%"></div>
                        <span class="text-[9px] font-black text-slate-500 uppercase tracking-wider">Sun</span>
                    </div>
                </div>
            </div>

        </div>
    </main>
</div>
</body>

</html>