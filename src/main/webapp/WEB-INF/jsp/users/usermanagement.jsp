<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | User Management</title>
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
                    borderRadius: { squircle: "14px", xl: "1.5rem", "2xl": "2.1rem", "3xl": "3rem" },
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
        .status-pill { padding: 4px 12px; border-radius: 10px; font-size: 0.7rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.05em; }
        .status-active { background: rgba(34,197,94,0.1); color: #4ade80; border: 1px solid rgba(34,197,94,0.2); }
        .status-disabled { background: rgba(244,63,94,0.1); color: #fb7185; border: 1px solid rgba(244,63,94,0.2); }
        .role-admin { background: rgba(31,104,249,0.1); color: #60a5fa; border: 1px solid rgba(31,104,249,0.2); padding: 4px 12px; border-radius: 10px; font-size: 0.7rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.05em; }
        .role-user { background: rgba(139,92,246,0.1); color: #a78bfa; border: 1px solid rgba(139,92,246,0.2); padding: 4px 12px; border-radius: 10px; font-size: 0.7rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.05em; }
        .search-glass { background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); }
        .search-glass input { background: transparent; }
        .search-glass input:focus { outline: none; }
        tr:hover td { background: rgba(255,255,255,0.01); }
        ::-webkit-scrollbar { width: 5px;display: none; } ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
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
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
                <span class="material-symbols-outlined shrink-0">group</span>
                <span class="nav-label text-sm">Users</span>
            </a>
            <a href="paymenthistory.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">payments</span>
                <span class="nav-label text-sm">Payments</span>
            </a>
            <a href="repportshubpage.html"
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
        <header class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div>
                <h2 class="text-xl font-black text-white">User Management</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Manage Platform Accounts</p>
            </div>
            <div class="flex items-center gap-6">
                <a href="createuser.html" class="flex items-center gap-2 bg-primary text-white text-sm font-black px-6 py-3 rounded-xl hover:scale-105 transition-all shadow-lg shadow-primary/20">
                    <span class="material-symbols-outlined text-lg">person_add</span> Add User
                </a>
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
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <!-- Stats -->
            <div class="grid grid-cols-3 gap-6 mb-8">
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Total Users</p>
                    <h3 class="text-2xl font-black text-white">18</h3>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Admins</p>
                    <h3 class="text-2xl font-black text-primary">4</h3>
                </div>
                <div class="glass-card p-6 rounded-3xl">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Active</p>
                    <h3 class="text-2xl font-black text-emerald-400">16</h3>
                </div>
            </div>

            <!-- Filters -->
            <div class="glass-card rounded-[2.5rem] overflow-hidden">
                <div class="p-7 border-b border-white/5 flex items-center justify-between gap-6 flex-wrap">
                    <div class="flex items-center gap-4 flex-wrap">
                        <div class="flex items-center h-12 w-72 rounded-2xl px-4 search-glass">
                            <span class="material-symbols-outlined text-slate-500 text-lg">search</span>
                            <input id="searchInput" type="text" placeholder="Search by name or email..." class="border-none text-sm text-white placeholder:text-slate-500 px-3 w-full" oninput="filterTable()"/>
                        </div>
                        <select id="roleFilter" class="h-12 px-4 rounded-2xl text-sm font-bold text-slate-400 search-glass appearance-none cursor-pointer" onchange="filterTable()" style="background:rgba(255,255,255,0.04)">
                            <option value="">All Roles</option>
                            <option value="Admin">Admin</option>
                            <option value="User">User</option>
                        </select>
                        <select id="statusFilter" class="h-12 px-4 rounded-2xl text-sm font-bold text-slate-400 search-glass appearance-none cursor-pointer" onchange="filterTable()" style="background:rgba(255,255,255,0.04)">
                            <option value="">All Status</option>
                            <option value="Active">Active</option>
                            <option value="Disabled">Disabled</option>
                        </select>
                    </div>
                </div>

                <div class="overflow-x-auto">
                    <table class="w-full text-left">
                        <thead>
                        <tr class="text-slate-500 text-[10px] font-black uppercase tracking-widest border-b border-white/5">
                            <th class="px-8 py-5">User</th>
                            <th class="px-8 py-5">Email</th>
                            <th class="px-8 py-5">Phone</th>
                            <th class="px-8 py-5">Role</th>
                            <th class="px-8 py-5">Status</th>
                            <th class="px-8 py-5">Last Login</th>
                            <th class="px-8 py-5">Actions</th>
                        </tr>
                        </thead>
                        <tbody id="userTable" class="text-sm font-bold text-slate-300 divide-y divide-white/5">
                        <tr data-role="Admin" data-status="Active">
                            <td class="px-8 py-5"><div class="flex items-center gap-3"><div class="h-9 w-9 rounded-xl bg-gradient-to-tr from-primary to-blue-400 p-[1.5px]"><div class="h-full w-full rounded-xl bg-slate-900 flex items-center justify-center"><span class="text-[10px] font-black text-white">AJ</span></div></div><span class="text-white font-black">Alex Johnson</span></div></td>
                            <td class="px-8 py-5 opacity-70">alex.johnson@parkiyo.com</td>
                            <td class="px-8 py-5 opacity-70">+1 555 000 1010</td>
                            <td class="px-8 py-5"><span class="role-admin">Admin</span></td>
                            <td class="px-8 py-5"><span class="status-pill status-active">Active</span></td>
                            <td class="px-8 py-5 opacity-50">Today, 11:00 AM</td>
                            <td class="px-8 py-5">
                                <div class="flex items-center gap-2">
                                    <button onclick="window.location.href='edituser.html'" class="h-8 w-8 rounded-xl bg-primary/10 text-primary hover:bg-primary/20 transition-all flex items-center justify-center"><span class="material-symbols-outlined text-sm">edit</span></button>
                                    <button class="h-8 w-8 rounded-xl bg-rose-500/10 text-rose-400 hover:bg-rose-500/20 transition-all flex items-center justify-center" onclick="openDeleteModal('Alex Johnson')"><span class="material-symbols-outlined text-sm">delete</span></button>
                                </div>
                            </td>
                        </tr>
                        <tr data-role="User" data-status="Active">
                            <td class="px-8 py-5"><div class="flex items-center gap-3"><div class="h-9 w-9 rounded-xl bg-gradient-to-tr from-violet-500 to-purple-400 p-[1.5px]"><div class="h-full w-full rounded-xl bg-slate-900 flex items-center justify-center"><span class="text-[10px] font-black text-white">SP</span></div></div><span class="text-white font-black">Sam Perera</span></div></td>
                            <td class="px-8 py-5 opacity-70">sam.perera@email.com</td>
                            <td class="px-8 py-5 opacity-70">+94 77 000 0000</td>
                            <td class="px-8 py-5"><span class="role-user">User</span></td>
                            <td class="px-8 py-5"><span class="status-pill status-active">Active</span></td>
                            <td class="px-8 py-5 opacity-50">Today, 10:30 AM</td>
                            <td class="px-8 py-5">
                                <div class="flex items-center gap-2">
                                    <button onclick="window.location.href='edituser.html'" class="h-8 w-8 rounded-xl bg-primary/10 text-primary hover:bg-primary/20 transition-all flex items-center justify-center"><span class="material-symbols-outlined text-sm">edit</span></button>
                                    <button class="h-8 w-8 rounded-xl bg-rose-500/10 text-rose-400 hover:bg-rose-500/20 transition-all flex items-center justify-center" onclick="openDeleteModal('Sam Perera')"><span class="material-symbols-outlined text-sm">delete</span></button>
                                </div>
                            </td>
                        </tr>
                        <tr data-role="User" data-status="Active">
                            <td class="px-8 py-5"><div class="flex items-center gap-3"><div class="h-9 w-9 rounded-xl bg-gradient-to-tr from-emerald-500 to-green-400 p-[1.5px]"><div class="h-full w-full rounded-xl bg-slate-900 flex items-center justify-center"><span class="text-[10px] font-black text-white">ML</span></div></div><span class="text-white font-black">Maria Lopez</span></div></td>
                            <td class="px-8 py-5 opacity-70">m.lopez@email.com</td>
                            <td class="px-8 py-5 opacity-70">+34 600 000 100</td>
                            <td class="px-8 py-5"><span class="role-user">User</span></td>
                            <td class="px-8 py-5"><span class="status-pill status-active">Active</span></td>
                            <td class="px-8 py-5 opacity-50">Yesterday, 3:00 PM</td>
                            <td class="px-8 py-5">
                                <div class="flex items-center gap-2">
                                    <button onclick="window.location.href='edituser.html'" class="h-8 w-8 rounded-xl bg-primary/10 text-primary hover:bg-primary/20 transition-all flex items-center justify-center"><span class="material-symbols-outlined text-sm">edit</span></button>
                                    <button class="h-8 w-8 rounded-xl bg-rose-500/10 text-rose-400 hover:bg-rose-500/20 transition-all flex items-center justify-center" onclick="openDeleteModal('Maria Lopez')"><span class="material-symbols-outlined text-sm">delete</span></button>
                                </div>
                            </td>
                        </tr>
                        <tr data-role="Admin" data-status="Active">
                            <td class="px-8 py-5"><div class="flex items-center gap-3"><div class="h-9 w-9 rounded-xl bg-gradient-to-tr from-amber-500 to-yellow-400 p-[1.5px]"><div class="h-full w-full rounded-xl bg-slate-900 flex items-center justify-center"><span class="text-[10px] font-black text-white">JD</span></div></div><span class="text-white font-black">James Diaz</span></div></td>
                            <td class="px-8 py-5 opacity-70">james.diaz@parkiyo.com</td>
                            <td class="px-8 py-5 opacity-70">+1 555 100 2020</td>
                            <td class="px-8 py-5"><span class="role-admin">Admin</span></td>
                            <td class="px-8 py-5"><span class="status-pill status-active">Active</span></td>
                            <td class="px-8 py-5 opacity-50">Today, 9:15 AM</td>
                            <td class="px-8 py-5">
                                <div class="flex items-center gap-2">
                                    <button onclick="window.location.href='edituser.html'" class="h-8 w-8 rounded-xl bg-primary/10 text-primary hover:bg-primary/20 transition-all flex items-center justify-center"><span class="material-symbols-outlined text-sm">edit</span></button>
                                    <button class="h-8 w-8 rounded-xl bg-rose-500/10 text-rose-400 hover:bg-rose-500/20 transition-all flex items-center justify-center" onclick="openDeleteModal('James Diaz')"><span class="material-symbols-outlined text-sm">delete</span></button>
                                </div>
                            </td>
                        </tr>
                        <tr data-role="User" data-status="Disabled">
                            <td class="px-8 py-5"><div class="flex items-center gap-3"><div class="h-9 w-9 rounded-xl bg-gradient-to-tr from-slate-600 to-slate-500 p-[1.5px]"><div class="h-full w-full rounded-xl bg-slate-900 flex items-center justify-center"><span class="text-[10px] font-black text-white">KL</span></div></div><span class="text-white font-black opacity-50">Kevin Lee</span></div></td>
                            <td class="px-8 py-5 opacity-40">kevin.lee@email.com</td>
                            <td class="px-8 py-5 opacity-40">+82 10 0000 0000</td>
                            <td class="px-8 py-5"><span class="role-user opacity-50">User</span></td>
                            <td class="px-8 py-5"><span class="status-pill status-disabled">Disabled</span></td>
                            <td class="px-8 py-5 opacity-40">3 days ago</td>
                            <td class="px-8 py-5">
                                <div class="flex items-center gap-2">
                                    <button onclick="window.location.href='edituser.html'" class="h-8 w-8 rounded-xl bg-primary/10 text-primary hover:bg-primary/20 transition-all flex items-center justify-center"><span class="material-symbols-outlined text-sm">edit</span></button>
                                    <button class="h-8 w-8 rounded-xl bg-emerald-500/10 text-emerald-400 hover:bg-emerald-500/20 transition-all flex items-center justify-center" title="Enable"><span class="material-symbols-outlined text-sm">check_circle</span></button>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="px-8 py-5 border-t border-white/5 flex items-center justify-between text-xs font-bold text-slate-500">
                    <p>Showing 5 of 18 users</p>
                    <div class="flex items-center gap-2">
                        <button class="h-8 w-8 rounded-xl bg-white/5 hover:bg-white/10 transition-all flex items-center justify-center"><span class="material-symbols-outlined text-sm">chevron_left</span></button>
                        <span class="h-8 w-8 rounded-xl bg-primary/10 text-primary flex items-center justify-center">1</span>
                        <button class="h-8 w-8 rounded-xl bg-white/5 hover:bg-white/10 transition-all flex items-center justify-center">2</button>
                        <button class="h-8 w-8 rounded-xl bg-white/5 hover:bg-white/10 transition-all flex items-center justify-center"><span class="material-symbols-outlined text-sm">chevron_right</span></button>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

<!-- Delete modal -->
<div id="deleteModal" class="hidden fixed inset-0 z-50 flex items-center justify-center bg-black/60 px-6 premium-blur">
    <div class="glass-card rounded-3xl p-10 max-w-md w-full shadow-2xl">
        <div class="h-14 w-14 rounded-2xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center mb-6">
            <span class="material-symbols-outlined text-rose-400 text-2xl">person_remove</span>
        </div>
        <h3 class="text-xl font-black text-white mb-3">Delete User?</h3>
        <p class="text-slate-400 text-sm leading-relaxed mb-2">You are about to delete <strong id="deleteUserName" class="text-white"></strong>.</p>
        <p class="text-slate-500 text-xs leading-relaxed mb-8">This action cannot be undone. All associated records will be removed.</p>
        <div class="flex gap-4">
            <button onclick="document.getElementById('deleteModal').classList.add('hidden')" class="flex-1 h-12 bg-white/5 border border-white/10 text-slate-300 font-bold rounded-2xl hover:bg-white/10 transition-all">Cancel</button>
            <button onclick="document.getElementById('deleteModal').classList.add('hidden')" class="flex-1 h-12 bg-rose-500 text-white font-black rounded-2xl hover:bg-rose-600 transition-all">Delete</button>
        </div>
    </div>
</div>

<script>
    function filterTable() {
        const search = document.getElementById('searchInput').value.toLowerCase();
        const role = document.getElementById('roleFilter').value;
        const status = document.getElementById('statusFilter').value;
        document.querySelectorAll('#userTable tr').forEach(row => {
            const text = row.textContent.toLowerCase();
            const rMatch = !role || row.dataset.role === role;
            const sMatch = !status || row.dataset.status === status;
            const tMatch = !search || text.includes(search);
            row.style.display = (rMatch && sMatch && tMatch) ? '' : 'none';
        });
    }
    function openDeleteModal(name) {
        document.getElementById('deleteUserName').textContent = name;
        document.getElementById('deleteModal').classList.remove('hidden');
    }
</script>
</body>
</html>
