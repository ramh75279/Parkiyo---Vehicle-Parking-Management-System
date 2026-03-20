<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" rel="stylesheet"/>
    <script>
        tailwind.config = {
            darkMode: "class",
            theme: {
                extend: {
                    colors: {
                        primary: "#1f68f9",
                        "background-dark": "#020617",
                    },
                    fontFamily: {
                        display: ["Public Sans", "sans-serif"]
                    },
                    borderRadius: {
                        squircle: "14px",
                        xl: "1.5rem",
                        "2xl": "2.1rem",
                        "3xl": "3rem",
                    },
                },
            },
        };
    </script>
    <style>
        body { font-family: 'Public Sans', sans-serif; background-color: #020617; }
        .premium-blur { backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px); }
        .bg-subtle-radial { background: radial-gradient(circle at 0% 0%, #1e293b 0%, #020617 100%); }
        .glass-card { background: rgba(255, 255, 255, 0.03); border: 1px solid rgba(255, 255, 255, 0.08); }

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

        .status-pill {
            padding: 4px 12px;
            border-radius: 10px;
            font-size: 0.7rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }
        .status-active { background: rgba(34, 197, 94, 0.1); color: #4ade80; border: 1px solid rgba(34, 197, 94, 0.2); }
        .status-exited { background: rgba(244, 63, 94, 0.1); color: #fb7185; border: 1px solid rgba(244, 63, 94, 0.2); }
        .status-paid { background: rgba(31, 104, 249, 0.1); color: #60a5fa; border: 1px solid rgba(31, 104, 249, 0.2); }
        .status-unpaid { background: rgba(245, 158, 11, 0.1); color: #fbbf24; border: 1px solid rgba(245, 158, 11, 0.2); }
        .status-available { background: rgba(34, 197, 94, 0.1); color: #4ade80; border: 1px solid rgba(34, 197, 94, 0.2); }
        .status-occupied { background: rgba(239, 68, 68, 0.1); color: #f87171; border: 1px solid rgba(239, 68, 68, 0.2); }

        .search-glass {
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
        }

        ::-webkit-scrollbar { width: 5px; display: none; }
        ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
    </style>
</head>
<body class="text-slate-100 font-display antialiased h-screen flex flex-col overflow-hidden">

<div class="h-1.5 w-full bg-gradient-to-r from-primary via-blue-400 to-primary shrink-0"></div>

<div class="flex flex-1 overflow-hidden">
    <aside class="sidebar-container border-r border-white/5 premium-blur flex flex-col shrink-0 z-50">
        <div class="p-6 mb-4 flex items-center">
            <div class="flex h-10 w-10 shrink-0 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>

        <nav class="flex-1 px-3 space-y-2 overflow-y-auto">
            <a href="/dashboard_admin" class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
                <span class="material-symbols-outlined shrink-0">dashboard</span>
                <span class="nav-label text-sm">Dashboard</span>
            </a>
            <a href="/entry_admin" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">login</span>
                <span class="nav-label text-sm">Vehicle Entry</span>
            </a>
            <a href="/exitvehicle_admin" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">logout</span>
                <span class="nav-label text-sm">Vehicle Exit</span>
            </a>
            <a href="/slot_overview" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">grid_view</span>
                <span class="nav-label text-sm">Parking Slots</span>
            </a>
            <a href="/Vehicle_List_Page" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">directions_car</span>
                <span class="nav-label text-sm">Vehicles</span>
            </a>
            <a href="/usermanagement" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">group</span>
                <span class="nav-label text-sm">Users</span>
            </a>
            <a href="/paymenthistory" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">payments</span>
                <span class="nav-label text-sm">Payments</span>
            </a>
            <a href="/Repportshubpage" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">bar_chart</span>
                <span class="nav-label text-sm">Reports</span>
            </a>
            <a href="/notification" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">notifications</span>
                <span class="nav-label text-sm">Notifications</span>
            </a>
            <a href="/systemstatuspage" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">monitor_heart</span>
                <span class="nav-label text-sm">System Status</span>
            </a>
            <a href="/accountsetting" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">settings</span>
                <span class="nav-label text-sm">Settings</span>
            </a>
        </nav>

        <div class="p-4 border-t border-white/5">
            <button onclick="window.location.href='logout.html'"
                    class="flex items-center w-full px-4 py-4 text-rose-500 hover:bg-rose-500/10 rounded-xl text-sm font-black transition-all">
                <span class="material-symbols-outlined shrink-0"><a href="/logout">power_settings_new</a></span><span
                    class="nav-label"><a href="/logout">Logout</a></span>
            </button>
        </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden bg-subtle-radial">
        <header class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur">
            <div>
                <h2 class="text-xl font-black text-white">Admin System Overview</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Parkiyo Control Center</p>
            </div>

            <div class="flex items-center gap-6">
                <div class="hidden xl:flex items-center h-12 w-80 rounded-2xl px-4 search-glass">
                    <span class="material-symbols-outlined text-slate-500">search</span>
                    <input type="text" placeholder="Search plate, slot, user..." class="bg-transparent border-none focus:ring-0 w-full text-sm text-white placeholder:text-slate-500 px-3">
                </div>

                <div class="hidden md:flex items-center gap-2 rounded-xl border border-emerald-500/20 bg-emerald-500/10 px-3 py-2">
                    <span class="material-symbols-outlined text-emerald-400 text-base">account_balance_wallet</span>
                    <span class="text-[11px] font-black uppercase tracking-[0.16em] text-emerald-400">$45.8K</span>
                </div>

                <div class="relative">
                    <span class="material-symbols-outlined text-slate-400 cursor-pointer hover:text-white transition-colors" onclick="openNotif()">notifications</span>
                    <span class="absolute -top-1 -right-1 h-2 w-2 bg-primary rounded-full ring-4 ring-background-dark"></span>
                </div>

                <div class="h-8 w-[1px] bg-white/10"></div>

                <div class="flex items-center gap-4">
                    <div class="text-right">
                        <p class="text-xs font-black text-white uppercase tracking-widest">Alex Johnson</p>
                        <p class="text-[10px] text-primary font-bold uppercase tracking-tighter">Admin</p>
                    </div>
                    <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                        <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center overflow-hidden">
                            <span class="material-symbols-outlined text-white/50">person</span>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-10">
                <div class="glass-card p-8 rounded-3xl relative overflow-hidden group">
                    <span class="material-symbols-outlined absolute -right-4 -bottom-4 text-8xl text-white/[0.02] group-hover:text-primary/5 transition-all">check_circle</span>
                    <p class="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-1">Available Slots</p>
                    <h3 class="text-3xl font-black text-white">42 <span class="text-sm text-slate-500 font-bold">/ 100</span></h3>
                    <div class="mt-4 h-1.5 w-full bg-white/5 rounded-full overflow-hidden">
                        <div class="h-full bg-primary w-[42%]"></div>
                    </div>
                </div>

                <div class="glass-card p-8 rounded-3xl relative overflow-hidden group">
                    <span class="material-symbols-outlined absolute -right-4 -bottom-4 text-8xl text-white/[0.02] group-hover:text-primary/5 transition-all">payments</span>
                    <p class="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-1">Daily Revenue</p>
                    <h3 class="text-3xl font-black text-white">$1,240.50</h3>
                    <p class="text-emerald-500 text-[10px] font-bold mt-2 flex items-center gap-1">
                        <span class="material-symbols-outlined text-xs">trending_up</span> +12% from yesterday
                    </p>
                </div>

                <div class="glass-card p-8 rounded-3xl relative overflow-hidden group">
                    <span class="material-symbols-outlined absolute -right-4 -bottom-4 text-8xl text-white/[0.02] group-hover:text-primary/5 transition-all">directions_car</span>
                    <p class="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-1">Active Sessions</p>
                    <h3 class="text-3xl font-black text-white">58</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2 tracking-wide">92% occupancy rate</p>
                </div>

                <div class="glass-card p-8 rounded-3xl relative overflow-hidden group">
                    <span class="material-symbols-outlined absolute -right-4 -bottom-4 text-8xl text-white/[0.02] group-hover:text-primary/5 transition-all">group</span>
                    <p class="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-1">Active Users</p>
                    <h3 class="text-3xl font-black text-primary">18</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2 tracking-wide text-white/40">4 admins · 14 users</p>
                </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-10">
                <div class="lg:col-span-2 glass-card rounded-[2.5rem] overflow-hidden">
                    <div class="p-8 border-b border-white/5 flex items-center justify-between">
                        <h3 class="text-lg font-black text-white">Live Operations</h3>
                        <button class="text-primary text-[10px] font-black uppercase tracking-widest hover:brightness-125">View All</button>
                    </div>
                    <div class="overflow-x-auto">
                        <table class="w-full text-left">
                            <thead>
                            <tr class="text-slate-500 text-[10px] font-black uppercase tracking-widest">
                                <th class="px-8 py-5">Vehicle ID</th>
                                <th class="px-8 py-5">Bay</th>
                                <th class="px-8 py-5">Entry</th>
                                <th class="px-8 py-5">Status</th>
                                <th class="px-8 py-5">Payment</th>
                            </tr>
                            </thead>
                            <tbody class="text-sm font-bold text-slate-300 divide-y divide-white/5">
                            <tr class="hover:bg-white/[0.02] transition-colors">
                                <td class="px-8 py-6 text-white font-black">ABC-1234</td>
                                <td class="px-8 py-6">A-12</td>
                                <td class="px-8 py-6 opacity-60">10:45 AM</td>
                                <td class="px-8 py-6"><span class="status-pill status-active">Active</span></td>
                                <td class="px-8 py-6"><span class="status-pill status-unpaid">Pending</span></td>
                            </tr>
                            <tr class="hover:bg-white/[0.02] transition-colors">
                                <td class="px-8 py-6 text-white font-black">XYZ-8899</td>
                                <td class="px-8 py-6">B-04</td>
                                <td class="px-8 py-6 opacity-60">09:15 AM</td>
                                <td class="px-8 py-6"><span class="status-pill status-exited">Exited</span></td>
                                <td class="px-8 py-6"><span class="status-pill status-paid">Settled</span></td>
                            </tr>
                            <tr class="hover:bg-white/[0.02] transition-colors">
                                <td class="px-8 py-6 text-white font-black">KLM-9012</td>
                                <td class="px-8 py-6">C-09</td>
                                <td class="px-8 py-6 opacity-60">11:05 AM</td>
                                <td class="px-8 py-6"><span class="status-pill status-active">Active</span></td>
                                <td class="px-8 py-6"><span class="status-pill status-paid">Settled</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="glass-card rounded-[2.5rem] p-8 flex flex-col">
                    <h3 class="text-lg font-black text-white mb-8">Traffic Density</h3>
                    <div class="flex-1 space-y-8">
                        <div>
                            <div class="flex justify-between text-[10px] font-black uppercase tracking-widest text-slate-500 mb-2">
                                <span>Morning Peak</span>
                                <span class="text-white">85%</span>
                            </div>
                            <div class="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary rounded-full" style="width: 85%"></div>
                            </div>
                        </div>

                        <div>
                            <div class="flex justify-between text-[10px] font-black uppercase tracking-widest text-slate-500 mb-2">
                                <span>Evening Peak</span>
                                <span class="text-white">76%</span>
                            </div>
                            <div class="h-2 w-full bg-white/5 rounded-full overflow-hidden">
                                <div class="h-full bg-primary rounded-full" style="width: 76%"></div>
                            </div>
                        </div>

                        <div class="mt-8 p-6 rounded-2xl bg-primary/5 border border-primary/10">
                            <div class="flex items-start gap-4">
                                <span class="material-symbols-outlined text-primary">analytics</span>
                                <div>
                                    <h4 class="text-xs font-black text-white uppercase tracking-wider">AI Insight</h4>
                                    <p class="text-[11px] text-slate-500 font-medium mt-1 leading-relaxed">
                                        Evening surge detected. System suggests auxiliary gate activation and temporary overflow slot allocation.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-6">User Summary</h3>
                    <div class="space-y-5">
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Active Users</span>
                            <span class="status-pill status-active">18</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Disabled Users</span>
                            <span class="status-pill status-exited">3</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Admins</span>
                            <span class="text-white font-black">4</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Users</span>
                            <span class="text-white font-black">17</span>
                        </div>
                    </div>
                </div>

                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-6">Slot Status</h3>
                    <div class="space-y-5">
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Available</span>
                            <span class="status-pill status-available">42</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Occupied</span>
                            <span class="status-pill status-occupied">58</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Reserved</span>
                            <span class="text-white font-black">12</span>
                        </div>
                    </div>
                </div>

                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-6">Quick Admin Actions</h3>
                    <div class="grid grid-cols-2 gap-4">
                        <a href="/createuser" class="rounded-2xl bg-white/5 border border-white/10 p-4 hover:bg-white/10 transition">
                            <p class="text-xs font-black uppercase tracking-widest text-white">Add User</p>
                        </a>
                        <a href="/add_slot" class="rounded-2xl bg-white/5 border border-white/10 p-4 hover:bg-white/10 transition">
                            <p class="text-xs font-black uppercase tracking-widest text-white">Add Slot</p>
                        </a>
                        <a href="/Vehicle_Import_Page" class="rounded-2xl bg-white/5 border border-white/10 p-4 hover:bg-white/10 transition">
                            <p class="text-xs font-black uppercase tracking-widest text-white">Import Vehicles</p>
                        </a>
                        <a href="/Repportshubpage" class="rounded-2xl bg-white/5 border border-white/10 p-4 hover:bg-white/10 transition">
                            <p class="text-xs font-black uppercase tracking-widest text-white">View Reports</p>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <footer class="h-16 border-t border-white/5 bg-background-dark/30 premium-blur flex items-center justify-between px-10 shrink-0">
            <p class="text-[10px] font-bold text-slate-600 uppercase tracking-[0.3em]">© 2026 Parkiyo. All Rights Reserved.</p>
            <div class="flex gap-8">
                <span class="text-[10px] font-black text-primary uppercase tracking-widest">Admin Console</span>
                <span class="text-[10px] font-bold text-slate-500 uppercase tracking-widest italic">Terminal: PKY-HQ-01</span>
            </div>
        </footer>
    </main>
</div>

<!-- Notification Panel Overlay -->
<div id="notifBackdrop" class="fixed inset-0 z-40 hidden" onclick="closeNotif()"></div>
<div id="notifPanel" class="fixed top-0 right-0 h-full w-96 z-50 flex flex-col translate-x-full transition-transform duration-300 ease-in-out"
     style="background:rgba(2,6,23,0.97);border-left:1px solid rgba(255,255,255,0.08);backdrop-filter:blur(24px);">
    <!-- Panel header -->
    <div class="flex items-center justify-between px-6 py-5 border-b border-white/5">
        <div>
            <h3 class="text-base font-black text-white">Notifications</h3>
            <p class="text-[10px] text-slate-500 font-black uppercase tracking-widest mt-0.5">3 unread</p>
        </div>
        <div class="flex items-center gap-2">
            <button onclick="markAllRead()" class="text-[10px] font-black uppercase tracking-widest text-primary hover:brightness-125 transition-all">Mark all read</button>
            <button onclick="closeNotif()" class="h-9 w-9 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all ml-2">
                <span class="material-symbols-outlined text-slate-400 text-lg">close</span>
            </button>
        </div>
    </div>
    <!-- Tabs -->
    <div class="flex border-b border-white/5 px-6">
        <button onclick="switchTab('all')" id="tabAll" class="pb-3 pt-3 text-xs font-black uppercase tracking-widest text-white border-b-2 border-primary mr-5">All</button>
        <button onclick="switchTab('unread')" id="tabUnread" class="pb-3 pt-3 text-xs font-black uppercase tracking-widest text-slate-500 border-b-2 border-transparent hover:text-white transition-all mr-5">Unread</button>
        <button onclick="switchTab('system')" id="tabSystem" class="pb-3 pt-3 text-xs font-black uppercase tracking-widest text-slate-500 border-b-2 border-transparent hover:text-white transition-all">System</button>
    </div>
    <!-- Items -->
    <div class="flex-1 overflow-y-auto py-3" id="notifList">
        <!-- Unread -->
        <div class="notif-item unread px-6 py-4 border-b border-white/5 cursor-pointer hover:bg-white/[0.025] transition-all flex gap-4" data-type="unread">
            <div class="h-10 w-10 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center shrink-0 mt-0.5">
                <span class="material-symbols-outlined text-rose-400 text-lg">error</span>
            </div>
            <div class="flex-1">
                <div class="flex items-start justify-between gap-2">
                    <p class="text-sm font-black text-white leading-tight">Slot B-12 offline</p>
                    <span class="h-2 w-2 bg-primary rounded-full shrink-0 mt-1.5 notif-dot"></span>
                </div>
                <p class="text-xs text-slate-400 font-bold mt-1">Sensor fault detected on slot B-12. Maintenance required.</p>
                <p class="text-[10px] text-slate-600 font-bold mt-2">2 min ago</p>
            </div>
        </div>
        <div class="notif-item unread px-6 py-4 border-b border-white/5 cursor-pointer hover:bg-white/[0.025] transition-all flex gap-4" data-type="unread">
            <div class="h-10 w-10 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center shrink-0 mt-0.5">
                <span class="material-symbols-outlined text-emerald-400 text-lg">payments</span>
            </div>
            <div class="flex-1">
                <div class="flex items-start justify-between gap-2">
                    <p class="text-sm font-black text-white leading-tight">Payment received</p>
                    <span class="h-2 w-2 bg-primary rounded-full shrink-0 mt-1.5 notif-dot"></span>
                </div>
                <p class="text-xs text-slate-400 font-bold mt-1">$16.50 collected from XYZ-8899 via Parkiyo Wallet.</p>
                <p class="text-[10px] text-slate-600 font-bold mt-2">14 min ago</p>
            </div>
        </div>
        <div class="notif-item unread px-6 py-4 border-b border-white/5 cursor-pointer hover:bg-white/[0.025] transition-all flex gap-4" data-type="unread">
            <div class="h-10 w-10 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center shrink-0 mt-0.5">
                <span class="material-symbols-outlined text-amber-400 text-lg">local_parking</span>
            </div>
            <div class="flex-1">
                <div class="flex items-start justify-between gap-2">
                    <p class="text-sm font-black text-white leading-tight">Occupancy at 92%</p>
                    <span class="h-2 w-2 bg-primary rounded-full shrink-0 mt-1.5 notif-dot"></span>
                </div>
                <p class="text-xs text-slate-400 font-bold mt-1">Only 8 slots remaining across all zones. Consider opening overflow.</p>
                <p class="text-[10px] text-slate-600 font-bold mt-2">31 min ago</p>
            </div>
        </div>
        <!-- Read -->
        <div class="notif-item read px-6 py-4 border-b border-white/5 cursor-pointer hover:bg-white/[0.025] transition-all flex gap-4 opacity-60" data-type="system">
            <div class="h-10 w-10 rounded-xl bg-primary/10 border border-primary/20 flex items-center justify-center shrink-0 mt-0.5">
                <span class="material-symbols-outlined text-primary text-lg">system_update</span>
            </div>
            <div class="flex-1">
                <p class="text-sm font-black text-white leading-tight">System update scheduled</p>
                <p class="text-xs text-slate-400 font-bold mt-1">Maintenance window: 2:00–3:00 AM on 12 Mar 2026. Brief downtime expected.</p>
                <p class="text-[10px] text-slate-600 font-bold mt-2">2 hours ago</p>
            </div>
        </div>
        <div class="notif-item read px-6 py-4 border-b border-white/5 cursor-pointer hover:bg-white/[0.025] transition-all flex gap-4 opacity-60" data-type="system">
            <div class="h-10 w-10 rounded-xl bg-violet-500/10 border border-violet-500/20 flex items-center justify-center shrink-0 mt-0.5">
                <span class="material-symbols-outlined text-violet-400 text-lg">person_add</span>
            </div>
            <div class="flex-1">
                <p class="text-sm font-black text-white leading-tight">New user registered</p>
                <p class="text-xs text-slate-400 font-bold mt-1">Saman Kumara joined as Operator. Awaiting role assignment.</p>
                <p class="text-[10px] text-slate-600 font-bold mt-2">Yesterday</p>
            </div>
        </div>
        <div class="notif-item read px-6 py-4 border-b border-white/5 cursor-pointer hover:bg-white/[0.025] transition-all flex gap-4 opacity-60" data-type="unread">
            <div class="h-10 w-10 rounded-xl bg-slate-700/30 border border-white/10 flex items-center justify-center shrink-0 mt-0.5">
                <span class="material-symbols-outlined text-slate-400 text-lg">receipt_long</span>
            </div>
            <div class="flex-1">
                <p class="text-sm font-black text-white leading-tight">Daily report ready</p>
                <p class="text-xs text-slate-400 font-bold mt-1">9 Mar 2026 revenue report has been generated.</p>
                <p class="text-[10px] text-slate-600 font-bold mt-2">Yesterday</p>
            </div>
        </div>
    </div>
    <!-- Footer -->
    <div class="px-6 py-4 border-t border-white/5">
        <a href="/notification" class="flex items-center justify-center gap-2 text-primary text-xs font-black hover:brightness-125 transition-all">
            <span class="material-symbols-outlined text-sm">open_in_new</span> View all notifications
        </a>
    </div>
</div>

<script>
    function openNotif() {
        document.getElementById('notifPanel').classList.remove('translate-x-full');
        document.getElementById('notifBackdrop').classList.remove('hidden');
    }
    function closeNotif() {
        document.getElementById('notifPanel').classList.add('translate-x-full');
        document.getElementById('notifBackdrop').classList.add('hidden');
    }
    function markAllRead() {
        document.querySelectorAll('.notif-dot').forEach(d => d.remove());
        document.querySelectorAll('.notif-item.unread').forEach(el => {
            el.classList.remove('unread');
            el.classList.add('read','opacity-60');
        });
        document.querySelector('#notifPanel .text-\[10px\].text-slate-500').textContent = '0 unread';
    }
    function switchTab(tab) {
        ['all','unread','system'].forEach(t => {
            const btn = document.getElementById('tab' + t.charAt(0).toUpperCase() + t.slice(1));
            btn.classList.remove('text-white','border-primary');
            btn.classList.add('text-slate-500','border-transparent');
        });
        const active = document.getElementById('tab' + tab.charAt(0).toUpperCase() + tab.slice(1));
        active.classList.add('text-white','border-primary');
        active.classList.remove('text-slate-500','border-transparent');

        document.querySelectorAll('.notif-item').forEach(el => {
            if (tab == 'all') { el.style.display = ''; }
            else if (tab == 'unread') { el.style.display = el.dataset.type == 'unread' ? '' : 'none'; }
            else if (tab == 'system') { el.style.display = el.dataset.type == 'system' ? '' : 'none'; }
        });
    }
</script>

</body>
</html>