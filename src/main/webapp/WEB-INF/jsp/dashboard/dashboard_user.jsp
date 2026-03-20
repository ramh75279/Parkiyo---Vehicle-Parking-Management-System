<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | User Dashboard</title>
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

        .search-glass {
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
        }

        ::-webkit-scrollbar { width: 5px;display: none; }
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
            <a href="/dashboard_user" class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
                <span class="material-symbols-outlined shrink-0">dashboard</span>
                <span class="nav-label text-sm">Dashboard</span>
            </a>
            <a href="/entry" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">login</span>
                <span class="nav-label text-sm">Vehicle Entry</span>
            </a>
            <a href="/exitvehicle" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">logout</span>
                <span class="nav-label text-sm">Vehicle Exit</span>
            </a>
            <a href="/parking" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">local_parking</span>
                <span class="nav-label text-sm">Active Parking</span>
            </a>
            <a href="/advancereservation" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">event_available</span>
                <span class="nav-label text-sm">Reservation</span>
            </a>
            <a href="/paymenthistory_user" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">payments</span>
                <span class="nav-label text-sm">Payments</span>
            </a>
            <a href="/receipt" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">receipt_long</span>
                <span class="nav-label text-sm">Receipts</span>
            </a>
            <a href="/walletoverview" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">account_balance_wallet</span>
                <span class="nav-label text-sm">Wallet</span>
            </a>
            <a href="/notification_user" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">notifications</span>
                <span class="nav-label text-sm">Notifications</span>
            </a>
            <a href="/accountsetting_user" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">settings</span>
                <span class="nav-label text-sm">Account Settings</span>
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
                <h2 class="text-xl font-black text-white">User Dashboard</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Parkiyo Operations Panel</p>
            </div>

            <div class="flex items-center gap-6">
                <div class="hidden xl:flex items-center h-12 w-80 rounded-2xl px-4 search-glass">
                    <span class="material-symbols-outlined text-slate-500">search</span>
                    <input type="text" placeholder="Search plate, slot, receipt..." class="bg-transparent border-none focus:ring-0 w-full text-sm text-white placeholder:text-slate-500 px-3">
                </div>

                <div class="hidden md:flex items-center gap-2 rounded-xl border border-emerald-500/20 bg-emerald-500/10 px-3 py-2">
                    <span class="material-symbols-outlined text-emerald-400 text-base">account_balance_wallet</span>
                    <span class="text-[11px] font-black uppercase tracking-[0.16em] text-emerald-400">$420</span>
                </div>

                <div class="relative">
                    <span class="material-symbols-outlined text-slate-400 cursor-pointer hover:text-white transition-colors" onclick="openNotif()">notifications</span>
                    <span class="absolute -top-1 -right-1 h-2 w-2 bg-primary rounded-full ring-4 ring-background-dark"></span>
                </div>

                <div class="h-8 w-[1px] bg-white/10"></div>

                <div class="flex items-center gap-4">
                    <div class="text-right">
                        <p class="text-xs font-black text-white uppercase tracking-widest">Sam Perera</p>
                        <p class="text-[10px] text-primary font-bold uppercase tracking-tighter">User</p>
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
                    <h3 class="text-3xl font-black text-white">42</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2 tracking-wide">Ready for new entries</p>
                </div>

                <div class="glass-card p-8 rounded-3xl relative overflow-hidden group">
                    <span class="material-symbols-outlined absolute -right-4 -bottom-4 text-8xl text-white/[0.02] group-hover:text-primary/5 transition-all">directions_car</span>
                    <p class="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-1">Active Sessions</p>
                    <h3 class="text-3xl font-black text-white">18</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2 tracking-wide">Currently parked vehicles</p>
                </div>

                <div class="glass-card p-8 rounded-3xl relative overflow-hidden group">
                    <span class="material-symbols-outlined absolute -right-4 -bottom-4 text-8xl text-white/[0.02] group-hover:text-primary/5 transition-all">payments</span>
                    <p class="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-1">Pending Payments</p>
                    <h3 class="text-3xl font-black text-white">7</h3>
                    <p class="text-amber-400 text-[10px] font-bold mt-2 tracking-wide">Needs attention</p>
                </div>

                <div class="glass-card p-8 rounded-3xl relative overflow-hidden group">
                    <span class="material-symbols-outlined absolute -right-4 -bottom-4 text-8xl text-white/[0.02] group-hover:text-primary/5 transition-all">task_alt</span>
                    <p class="text-slate-500 text-[10px] font-black uppercase tracking-widest mb-1">Vehicles Processed Today</p>
                    <h3 class="text-3xl font-black text-primary">36</h3>
                    <p class="text-slate-500 text-[10px] font-bold mt-2 tracking-wide">Entries and exits completed</p>
                </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8 mb-10">
                <div class="glass-card rounded-[2.5rem] p-8 lg:col-span-1">
                    <h3 class="text-lg font-black text-white mb-6">Quick Actions</h3>
                    <div class="grid grid-cols-2 gap-4">
                        <a href="/entry" class="rounded-2xl bg-white/5 border border-white/10 p-5 hover:bg-primary/15 transition">
                            <span class="material-symbols-outlined text-white mb-2">login</span>
                            <p class="text-xs font-black uppercase tracking-widest text-white">New Entry</p>
                        </a>
                        <a href="/exitvehicle" class="rounded-2xl bg-white/5 border border-white/10 p-5 hover:bg-white/10 transition">
                            <span class="material-symbols-outlined text-white mb-2">logout</span>
                            <p class="text-xs font-black uppercase tracking-widest text-white">Process Exit</p>
                        </a>
                        <a href="/slotselection" class="rounded-2xl bg-white/5 border border-white/10 p-5 hover:bg-white/10 transition">
                            <span class="material-symbols-outlined text-white mb-2">grid_view</span>
                            <p class="text-xs font-black uppercase tracking-widest text-white">Assign Slot</p>
                        </a>
                        <a href="/pendingpayment" class="rounded-2xl bg-white/5 border border-white/10 p-5 hover:bg-white/10 transition">
                            <span class="material-symbols-outlined text-white mb-2">payments</span>
                            <p class="text-xs font-black uppercase tracking-widest text-white">Take Payment</p>
                        </a>
                    </div>
                </div>

                <div class="lg:col-span-2 glass-card rounded-[2.5rem] overflow-hidden">
                    <div class="p-8 border-b border-white/5 flex items-center justify-between">
                        <h3 class="text-lg font-black text-white">Recent Parking Activity</h3>
                        <button class="text-primary text-[10px] font-black uppercase tracking-widest hover:brightness-125">View All</button>
                    </div>
                    <div class="overflow-x-auto">
                        <table class="w-full text-left">
                            <thead>
                            <tr class="text-slate-500 text-[10px] font-black uppercase tracking-widest">
                                <th class="px-8 py-5">Plate Number</th>
                                <th class="px-8 py-5">Slot</th>
                                <th class="px-8 py-5">Time</th>
                                <th class="px-8 py-5">Status</th>
                                <th class="px-8 py-5">Payment</th>
                            </tr>
                            </thead>
                            <tbody class="text-sm font-bold text-slate-300 divide-y divide-white/5">
                            <tr class="hover:bg-white/[0.02] transition-colors">
                                <td class="px-8 py-6 text-white font-black">CAA-4587</td>
                                <td class="px-8 py-6">B-11</td>
                                <td class="px-8 py-6 opacity-60">11:10 AM</td>
                                <td class="px-8 py-6"><span class="status-pill status-active">Entered</span></td>
                                <td class="px-8 py-6"><span class="status-pill status-unpaid">Pending</span></td>
                            </tr>
                            <tr class="hover:bg-white/[0.02] transition-colors">
                                <td class="px-8 py-6 text-white font-black">KDG-2301</td>
                                <td class="px-8 py-6">A-03</td>
                                <td class="px-8 py-6 opacity-60">10:50 AM</td>
                                <td class="px-8 py-6"><span class="status-pill status-exited">Exited</span></td>
                                <td class="px-8 py-6"><span class="status-pill status-paid">Paid</span></td>
                            </tr>
                            <tr class="hover:bg-white/[0.02] transition-colors">
                                <td class="px-8 py-6 text-white font-black">BHZ-7734</td>
                                <td class="px-8 py-6">C-07</td>
                                <td class="px-8 py-6 opacity-60">10:30 AM</td>
                                <td class="px-8 py-6"><span class="status-pill status-active">Active</span></td>
                                <td class="px-8 py-6"><span class="status-pill status-unpaid">Pending</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-6">Slot Snapshot</h3>
                    <div class="space-y-5">
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Available Now</span>
                            <span class="text-white font-black">42</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Occupied Now</span>
                            <span class="text-white font-black">58</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Reserved</span>
                            <span class="text-white font-black">8</span>
                        </div>
                    </div>
                </div>

                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-6">My Shift Summary</h3>
                    <div class="space-y-5">
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Entries Handled</span>
                            <span class="text-white font-black">21</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Exits Handled</span>
                            <span class="text-white font-black">15</span>
                        </div>
                        <div class="flex items-center justify-between">
                            <span class="text-sm text-slate-400">Payments Collected</span>
                            <span class="text-primary font-black">$420.00</span>
                        </div>
                    </div>
                </div>

                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-lg font-black text-white mb-6">Assistant Note</h3>
                    <div class="rounded-2xl bg-primary/5 border border-primary/10 p-6">
                        <div class="flex items-start gap-4">
                            <span class="material-symbols-outlined text-primary">support_agent</span>
                            <div>
                                <h4 class="text-xs font-black text-white uppercase tracking-wider">Operational Tip</h4>
                                <p class="text-[11px] text-slate-500 font-medium mt-1 leading-relaxed">
                                    Two pending exits are awaiting payment confirmation. Process payment before generating final receipts.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <footer class="h-16 border-t border-white/5 bg-background-dark/30 premium-blur flex items-center justify-between px-10 shrink-0">
            <p class="text-[10px] font-bold text-slate-600 uppercase tracking-[0.3em]">© 2026 Parkiyo. All Rights Reserved.</p>
            <div class="flex gap-8">
                <span class="text-[10px] font-black text-primary uppercase tracking-widest">User Console</span>
                <span class="text-[10px] font-bold text-slate-500 uppercase tracking-widest italic">Shift: Morning</span>
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