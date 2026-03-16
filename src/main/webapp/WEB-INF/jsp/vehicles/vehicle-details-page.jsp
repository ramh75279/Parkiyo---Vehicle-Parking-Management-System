<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Vehicle Details</title>
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
        .status-parked { background: rgba(31,104,249,0.1); color: #60a5fa; border: 1px solid rgba(31,104,249,0.2); }
        .status-active { background: rgba(16,185,129,0.1); color: #34d399; border: 1px solid rgba(16,185,129,0.2); }
        .status-paid { background: rgba(31,104,249,0.1); color: #60a5fa; border: 1px solid rgba(31,104,249,0.2); }
        .status-pill { padding: 3px 11px; border-radius: 9px; font-size: 0.68rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.05em; }
        .meta-row { display: flex; justify-content: space-between; align-items: center; padding: 13px 0; border-bottom: 1px solid rgba(255,255,255,0.05); }
        .meta-row:last-child { border-bottom: none; }
        ::-webkit-scrollbar { width: 5px; display: none;} ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
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
            <a href="dashboard-admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">dashboard</span><span class="nav-label text-sm">Dashboard</span></a>
            <a href="entry-admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle Entry</span></a>
            <a href="exitvehicle-admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle Exit</span></a>
            <a href="vehicle-list-page.html" class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span class="material-symbols-outlined shrink-0">directions_car</span><span class="nav-label text-sm">Vehicles</span></a>
            <a href="slot-overview.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">grid_view</span><span class="nav-label text-sm">Parking Slots</span></a>
            <a href="usermanagement.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">group</span><span class="nav-label text-sm">Users</span></a>
            <a href="paymenthistory.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">payments</span><span class="nav-label text-sm">Payments</span></a>
            <a href="repportshubpage.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">bar_chart</span><span class="nav-label text-sm">Reports</span></a>
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
                <button onclick="window.location.href='vehicle-list-page.html'" class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Vehicle Details</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">VEH-00041</p>
                </div>
            </div>
            <div class="flex items-center gap-3">
                <a href="edit-vehicle-page.html" class="flex items-center gap-2 bg-primary/10 border border-primary/20 text-primary font-black px-5 py-2.5 rounded-xl hover:bg-primary/20 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">edit</span> Edit Vehicle
                </a>
                <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-5xl mx-auto space-y-8">

                <!-- Hero Card -->
                <div class="glass-card rounded-[2.5rem] p-8 relative overflow-hidden">
                    <div class="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-transparent pointer-events-none"></div>
                    <div class="relative flex items-center gap-8 flex-wrap">
                        <!-- Plate display -->
                        <div class="flex flex-col items-center justify-center bg-white/5 border border-white/10 rounded-2xl px-10 py-6 min-w-[180px]">
                            <p class="text-[9px] font-black uppercase tracking-[0.2em] text-slate-500 mb-2">License Plate</p>
                            <p class="text-4xl font-black text-white tracking-[0.15em]">ABC</p>
                            <div class="h-px w-full bg-white/10 my-2"></div>
                            <p class="text-4xl font-black text-white tracking-[0.15em]">1234</p>
                        </div>
                        <!-- Info -->
                        <div class="flex-1 min-w-[200px]">
                            <div class="flex items-center gap-3 mb-3">
                                <span class="status-pill status-parked">Currently Parked</span>
                                <span class="text-[10px] font-black uppercase tracking-wider text-slate-500">Slot A-12 · Zone A</span>
                            </div>
                            <h3 class="text-3xl font-black text-white">Toyota Prius</h3>
                            <p class="text-slate-400 font-bold mt-1">2022 · Silver · Car</p>
                            <p class="text-slate-500 text-sm font-bold mt-4">Owner: <span class="text-white">Kamal Perera</span></p>
                        </div>
                        <!-- Stats -->
                        <div class="grid grid-cols-2 gap-4 shrink-0">
                            <div class="glass-card p-5 rounded-2xl text-center">
                                <p class="text-2xl font-black text-white">47</p>
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Total Visits</p>
                            </div>
                            <div class="glass-card p-5 rounded-2xl text-center">
                                <p class="text-2xl font-black text-emerald-400">$621</p>
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Total Paid</p>
                            </div>
                            <div class="glass-card p-5 rounded-2xl text-center">
                                <p class="text-2xl font-black text-primary">3h 12m</p>
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Avg Duration</p>
                            </div>
                            <div class="glass-card p-5 rounded-2xl text-center">
                                <p class="text-2xl font-black text-amber-400">1</p>
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Unpaid</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">

                    <!-- Vehicle Info -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h4 class="text-sm font-black uppercase tracking-widest text-slate-400 mb-5">Vehicle Information</h4>
                        <div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Vehicle ID</span><span class="text-sm font-black text-white font-mono">VEH-00041</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Category</span><span class="text-sm font-black text-white">Car</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Make</span><span class="text-sm font-black text-white">Toyota</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Model</span><span class="text-sm font-black text-white">Prius</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Year</span><span class="text-sm font-black text-white">2022</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Color</span>
                                <span class="flex items-center gap-2 text-sm font-black text-white"><span class="h-3.5 w-3.5 rounded-full bg-slate-300 border border-white/20"></span>Silver</span>
                            </div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Status</span><span class="status-pill status-parked">Parked</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Registered On</span><span class="text-sm font-black text-white">2025-11-02</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Notes</span><span class="text-sm font-bold text-slate-400 text-right max-w-[200px]">Regular visitor. Monthly permit holder.</span></div>
                        </div>
                    </div>

                    <!-- Owner Info -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h4 class="text-sm font-black uppercase tracking-widest text-slate-400 mb-5">Owner Information</h4>
                        <div class="flex items-center gap-4 p-4 rounded-2xl bg-white/[0.02] border border-white/5 mb-5">
                            <div class="h-12 w-12 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px] shrink-0">
                                <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                                    <span class="material-symbols-outlined text-white/60">person</span>
                                </div>
                            </div>
                            <div>
                                <p class="text-white font-black">Kamal Perera</p>
                                <p class="text-primary text-[10px] font-black uppercase tracking-wider">Linked Account · USR-00088</p>
                            </div>
                        </div>
                        <div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Phone</span><span class="text-sm font-black text-white">+94 77 123 4567</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Email</span><span class="text-sm font-black text-white">kamal@email.com</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">User ID</span><span class="text-sm font-black text-white font-mono">USR-00088</span></div>
                            <div class="meta-row"><span class="text-xs font-bold text-slate-500">Wallet Balance</span><span class="text-sm font-black text-emerald-400">$42.00</span></div>
                        </div>
                    </div>
                </div>

                <!-- Current Session -->
                <div class="glass-card rounded-[2.5rem] p-8 border border-primary/10 bg-primary/5">
                    <div class="flex items-center justify-between mb-6">
                        <h4 class="text-sm font-black uppercase tracking-widest text-primary">Active Parking Session</h4>
                        <span class="flex items-center gap-2 text-[10px] font-black uppercase tracking-widest text-emerald-400">
                            <span class="h-2 w-2 rounded-full bg-emerald-400 animate-pulse"></span> Live
                        </span>
                    </div>
                    <div class="grid grid-cols-2 sm:grid-cols-4 gap-6">
                        <div><p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Slot</p><p class="text-xl font-black text-white">A-12</p></div>
                        <div><p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Entry Time</p><p class="text-xl font-black text-white">10:45 AM</p></div>
                        <div><p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Duration</p><p class="text-xl font-black text-primary">2h 14m</p></div>
                        <div><p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Running Fee</p><p class="text-xl font-black text-emerald-400">$12.25</p></div>
                    </div>
                    <div class="flex gap-3 mt-6">
                        <a href="parkingticket.html" class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                            <span class="material-symbols-outlined text-base">receipt</span> View Ticket
                        </a>
                        <a href="exitvehicle.html" class="flex items-center gap-2 bg-primary text-white font-black px-5 py-2.5 rounded-xl hover:bg-primary/80 transition-all text-xs uppercase tracking-widest">
                            <span class="material-symbols-outlined text-base">logout</span> Process Exit
                        </a>
                    </div>
                </div>

                <!-- Parking History -->
                <div class="glass-card rounded-[2.5rem] overflow-hidden">
                    <div class="p-8 border-b border-white/5 flex items-center justify-between">
                        <h4 class="text-base font-black text-white">Parking History</h4>
                        <span class="text-[10px] font-black uppercase tracking-widest text-slate-500">47 sessions total</span>
                    </div>
                    <div class="overflow-x-auto">
                        <table class="w-full text-left">
                            <thead>
                                <tr class="text-slate-500 text-[10px] font-black uppercase tracking-widest border-b border-white/5">
                                    <th class="px-8 py-4">Date</th>
                                    <th class="px-8 py-4">Slot</th>
                                    <th class="px-8 py-4">Entry</th>
                                    <th class="px-8 py-4">Exit</th>
                                    <th class="px-8 py-4">Duration</th>
                                    <th class="px-8 py-4">Fee</th>
                                    <th class="px-8 py-4">Status</th>
                                </tr>
                            </thead>
                            <tbody class="text-sm font-bold text-slate-300 divide-y divide-white/5">
                                <tr class="hover:bg-white/[0.02]">
                                    <td class="px-8 py-4 text-slate-400 text-xs">2026-03-09</td>
                                    <td class="px-8 py-4">A-12</td>
                                    <td class="px-8 py-4 opacity-60">10:45 AM</td>
                                    <td class="px-8 py-4 opacity-50 italic">Active</td>
                                    <td class="px-8 py-4 text-primary">2h 14m+</td>
                                    <td class="px-8 py-4 text-amber-400">$12.25</td>
                                    <td class="px-8 py-4"><span class="status-pill status-parked">Parked</span></td>
                                </tr>
                                <tr class="hover:bg-white/[0.02]">
                                    <td class="px-8 py-4 text-slate-400 text-xs">2026-03-07</td>
                                    <td class="px-8 py-4">B-05</td>
                                    <td class="px-8 py-4 opacity-60">09:10 AM</td>
                                    <td class="px-8 py-4 opacity-60">12:30 PM</td>
                                    <td class="px-8 py-4 opacity-70">3h 20m</td>
                                    <td class="px-8 py-4 text-emerald-400">$18.50</td>
                                    <td class="px-8 py-4"><span class="status-pill status-paid">Paid</span></td>
                                </tr>
                                <tr class="hover:bg-white/[0.02]">
                                    <td class="px-8 py-4 text-slate-400 text-xs">2026-03-05</td>
                                    <td class="px-8 py-4">A-12</td>
                                    <td class="px-8 py-4 opacity-60">08:00 AM</td>
                                    <td class="px-8 py-4 opacity-60">10:15 AM</td>
                                    <td class="px-8 py-4 opacity-70">2h 15m</td>
                                    <td class="px-8 py-4 text-emerald-400">$12.50</td>
                                    <td class="px-8 py-4"><span class="status-pill status-paid">Paid</span></td>
                                </tr>
                                <tr class="hover:bg-white/[0.02]">
                                    <td class="px-8 py-4 text-slate-400 text-xs">2026-03-03</td>
                                    <td class="px-8 py-4">C-08</td>
                                    <td class="px-8 py-4 opacity-60">11:30 AM</td>
                                    <td class="px-8 py-4 opacity-60">02:00 PM</td>
                                    <td class="px-8 py-4 opacity-70">2h 30m</td>
                                    <td class="px-8 py-4 text-emerald-400">$14.00</td>
                                    <td class="px-8 py-4"><span class="status-pill status-paid">Paid</span></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="px-8 py-5 border-t border-white/5 flex justify-between items-center">
                        <p class="text-xs font-bold text-slate-500">Showing 4 of 47 sessions</p>
                        <button class="text-primary text-xs font-black hover:brightness-125 transition-all">View all →</button>
                    </div>
                </div>

            </div>
        </div>
    </main>
</div>
</body>
</html>
