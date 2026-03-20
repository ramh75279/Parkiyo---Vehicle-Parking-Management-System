<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Audit Log Details</title>
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
        .badge-update { background: rgba(31,104,249,0.12); color: #60a5fa; border: 1px solid rgba(31,104,249,0.2); padding: 4px 14px; border-radius: 10px; font-size: 0.7rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.06em; }
        .code-block { background: rgba(0,0,0,0.4); border: 1px solid rgba(255,255,255,0.06); border-radius: 14px; font-family: 'Courier New', monospace; font-size: 0.78rem; color: #94a3b8; line-height: 1.7; }
        .diff-add { color: #34d399; }
        .diff-rem { color: #f87171; }
        .meta-row { display: flex; justify-content: space-between; align-items: center; padding: 14px 0; border-bottom: 1px solid rgba(255,255,255,0.05); }
        .meta-row:last-child { border-bottom: none; }
        ::-webkit-scrollbar { width: 5px;display: none; } ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
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
            <a href="/dashboard_admin" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">dashboard</span><span class="nav-label text-sm">Dashboard</span></a>
            <a href="/entry_admin" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle Entry</span></a>
            <a href="/exitvehicle_admin" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle Exit</span></a>
            <a href="/slot_overview" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">grid_view</span><span class="nav-label text-sm">Parking Slots</span></a>
            <a href="/usermanagement" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">group</span><span class="nav-label text-sm">Users</span></a>
            <a href="/paymenthistory" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">payments</span><span class="nav-label text-sm">Payments</span></a>
            <a href="/Repportshubpage" class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span class="material-symbols-outlined shrink-0">bar_chart</span><span class="nav-label text-sm">Reports</span></a>
            <a href="/systemstatuspage" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">monitor_heart</span><span class="nav-label text-sm">System Status</span></a>
            <a href="/accountsetting" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">settings</span><span class="nav-label text-sm">Settings</span></a>
        </nav>
        <div class="p-4 border-t border-white/5">
            <button onclick="window.location.href='logout.html'" class="flex items-center w-full px-4 py-4 text-rose-500 hover:bg-rose-500/10 rounded-xl text-sm font-black transition-all">
                <span class="material-symbols-outlined shrink-0"><a href="/logout">power_settings_new</a></span><span class="nav-label"><a href="/logout">Logout</a></span>
            </button>
        </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden bg-subtle-radial">
        <!-- TOPBAR -->
        <header class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div class="flex items-center gap-4">
                <button onclick="window.location.href='auditlog.html'" class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Audit Log Details</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Event ID: EVT-2026-003847</p>
                </div>
            </div>
            <div class="flex items-center gap-4">
                <button class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">download</span> Export
                </button>
                <div class="flex items-center gap-4">
                    <div class="text-right hidden xl:block">
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
            <div class="max-w-5xl mx-auto space-y-8">

                <!-- Event Header Card -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <div class="flex items-start justify-between gap-6 flex-wrap">
                        <div class="flex items-center gap-5">
                            <div class="h-16 w-16 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-primary text-3xl">edit</span>
                            </div>
                            <div>
                                <div class="flex items-center gap-3 mb-1">
                                    <span class="badge-update">Update</span>
                                    <span class="text-[10px] font-black uppercase tracking-widest text-slate-500">Slot Management</span>
                                </div>
                                <h3 class="text-xl font-black text-white">Updated slot A-12 to Maintenance</h3>
                                <p class="text-slate-500 text-sm font-bold mt-1">Parking slot status changed by administrator</p>
                            </div>
                        </div>
                        <div class="text-right">
                            <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Timestamp</p>
                            <p class="text-white font-black text-sm">2026-03-09</p>
                            <p class="text-primary font-black text-lg">14:32:01</p>
                        </div>
                    </div>
                </div>

                <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">

                    <!-- Event Metadata -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h4 class="text-sm font-black uppercase tracking-widest text-slate-400 mb-6">Event Metadata</h4>
                        <div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Event ID</span>
                                <span class="text-sm font-black text-white font-mono">EVT-2026-003847</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Event Type</span>
                                <span class="badge-update">Update</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Module</span>
                                <span class="text-sm font-black text-white">Slot Management</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Resource</span>
                                <span class="text-sm font-black text-white">Slot A-12</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Resource ID</span>
                                <span class="text-sm font-black text-white font-mono">SLT-00012</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Status</span>
                                <span class="text-[10px] font-black uppercase tracking-wider text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-3 py-1 rounded-lg">Success</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Duration</span>
                                <span class="text-sm font-black text-white">42ms</span>
                            </div>
                        </div>
                    </div>

                    <!-- Actor Info -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h4 class="text-sm font-black uppercase tracking-widest text-slate-400 mb-6">Performed By</h4>
                        <div class="flex items-center gap-4 mb-6 p-4 rounded-2xl bg-white/[0.02] border border-white/5">
                            <div class="h-12 w-12 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px] shrink-0">
                                <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                                    <span class="material-symbols-outlined text-white/60">person</span>
                                </div>
                            </div>
                            <div>
                                <p class="text-white font-black">Alex Johnson</p>
                                <p class="text-primary text-[10px] font-black uppercase tracking-wider">Administrator</p>
                            </div>
                        </div>
                        <div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">User ID</span>
                                <span class="text-sm font-black text-white font-mono">USR-00001</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Email</span>
                                <span class="text-sm font-black text-white">alex@parkiyo.com</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">IP Address</span>
                                <span class="text-sm font-black text-white font-mono">192.168.1.10</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Location</span>
                                <span class="text-sm font-black text-white">Colombo, LK</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">Session ID</span>
                                <span class="text-sm font-black text-slate-400 font-mono">ses_8f2k39xp</span>
                            </div>
                            <div class="meta-row">
                                <span class="text-xs font-bold text-slate-500">User Agent</span>
                                <span class="text-xs font-bold text-slate-400 truncate max-w-[200px]">Chrome 122 / macOS</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Change Diff -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h4 class="text-sm font-black uppercase tracking-widest text-slate-400 mb-6">Change Diff</h4>
                    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
                        <div>
                            <p class="text-[10px] font-black uppercase tracking-widest text-rose-400 mb-3 flex items-center gap-2"><span class="material-symbols-outlined text-sm">remove</span> Before</p>
                            <div class="code-block p-5">
                                <p><span class="text-slate-600">{</span></p>
                                <p class="pl-4">"slot_id": <span class="text-white">"SLT-00012"</span>,</p>
                                <p class="pl-4">"slot_code": <span class="text-white">"A-12"</span>,</p>
                                <p class="pl-4 diff-rem">"status": <span>"available"</span>,</p>
                                <p class="pl-4">"zone": <span class="text-white">"A"</span>,</p>
                                <p class="pl-4">"floor": <span class="text-white">"Ground"</span>,</p>
                                <p class="pl-4 diff-rem">"updated_at": <span>"2026-03-08T09:15:00Z"</span></p>
                                <p><span class="text-slate-600">}</span></p>
                            </div>
                        </div>
                        <div>
                            <p class="text-[10px] font-black uppercase tracking-widest text-emerald-400 mb-3 flex items-center gap-2"><span class="material-symbols-outlined text-sm">add</span> After</p>
                            <div class="code-block p-5">
                                <p><span class="text-slate-600">{</span></p>
                                <p class="pl-4">"slot_id": <span class="text-white">"SLT-00012"</span>,</p>
                                <p class="pl-4">"slot_code": <span class="text-white">"A-12"</span>,</p>
                                <p class="pl-4 diff-add">"status": <span>"maintenance"</span>,</p>
                                <p class="pl-4">"zone": <span class="text-white">"A"</span>,</p>
                                <p class="pl-4">"floor": <span class="text-white">"Ground"</span>,</p>
                                <p class="pl-4 diff-add">"updated_at": <span>"2026-03-09T14:32:01Z"</span></p>
                                <p><span class="text-slate-600">}</span></p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Raw Payload -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <div class="flex items-center justify-between mb-6">
                        <h4 class="text-sm font-black uppercase tracking-widest text-slate-400">Raw Request Payload</h4>
                        <button class="text-[10px] font-black uppercase tracking-widest text-primary hover:brightness-125 transition-all flex items-center gap-1">
                            <span class="material-symbols-outlined text-sm">content_copy</span> Copy
                        </button>
                    </div>
                    <div class="code-block p-5">
                        <p><span class="text-slate-600">{</span></p>
                        <p class="pl-4">"action": <span class="text-emerald-300">"UPDATE_SLOT_STATUS"</span>,</p>
                        <p class="pl-4">"payload": <span class="text-slate-600">{</span></p>
                        <p class="pl-8">"slot_id": <span class="text-amber-300">"SLT-00012"</span>,</p>
                        <p class="pl-8">"new_status": <span class="text-amber-300">"maintenance"</span>,</p>
                        <p class="pl-8">"reason": <span class="text-amber-300">"Scheduled floor inspection"</span>,</p>
                        <p class="pl-8">"notify_admin": <span class="text-primary">true</span></p>
                        <p class="pl-4"><span class="text-slate-600">},</span></p>
                        <p class="pl-4">"performed_by": <span class="text-amber-300">"USR-00001"</span>,</p>
                        <p class="pl-4">"timestamp": <span class="text-amber-300">"2026-03-09T14:32:01.482Z"</span></p>
                        <p><span class="text-slate-600">}</span></p>
                    </div>
                </div>

                <!-- Navigation -->
                <div class="flex items-center justify-between">
                    <button class="flex items-center gap-2 glass-card px-6 py-3.5 rounded-2xl text-slate-400 hover:text-white font-black text-sm transition-all">
                        <span class="material-symbols-outlined">arrow_back</span> Previous Event
                    </button>
                    <a href="auditlog.html" class="flex items-center gap-2 bg-primary/10 border border-primary/20 text-primary px-6 py-3.5 rounded-2xl hover:bg-primary/20 font-black text-sm transition-all">
                        <span class="material-symbols-outlined">list</span> Back to Log
                    </a>
                    <button class="flex items-center gap-2 glass-card px-6 py-3.5 rounded-2xl text-slate-400 hover:text-white font-black text-sm transition-all">
                        Next Event <span class="material-symbols-outlined">arrow_forward</span>
                    </button>
                </div>

            </div>
        </div>
    </main>
</div>
</body>
</html>
