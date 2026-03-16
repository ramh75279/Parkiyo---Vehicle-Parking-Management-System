<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Add Vehicle</title>
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
        .input-glass { background: rgba(255,255,255,0.04); border: 1px solid rgba(255,255,255,0.08); color: white; border-radius: 12px; padding: 11px 16px; font-size: 0.85rem; font-weight: 700; transition: all 0.2s; outline: none; width: 100%; }
        .input-glass:focus { border-color: rgba(31,104,249,0.5); box-shadow: 0 0 0 3px rgba(31,104,249,0.1); }
        .input-glass::placeholder { color: rgba(255,255,255,0.18); }
        select.input-glass option { background: #0f172a; }
        label.field-label { font-size: 0.7rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.1em; color: #64748b; margin-bottom: 6px; display: block; }
        .category-card { background: rgba(255,255,255,0.03); border: 2px solid rgba(255,255,255,0.07); border-radius: 16px; padding: 18px; cursor: pointer; transition: all 0.2s; display: flex; flex-direction: column; align-items: center; gap: 8px; }
        .category-card:hover { border-color: rgba(31,104,249,0.4); background: rgba(31,104,249,0.05); }
        .category-card.selected { border-color: #1f68f9; background: rgba(31,104,249,0.1); }
        .category-card span.icon { font-size: 2rem; }
        .category-card p { font-size: 0.72rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.06em; color: #94a3b8; }
        .category-card.selected p { color: #60a5fa; }
        ::-webkit-scrollbar { width: 5px;display:none; } ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
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
            <a href="dashboard_admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">dashboard</span><span class="nav-label text-sm">Dashboard</span></a>
            <a href="entry_admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle Entry</span></a>
            <a href="exitvehicle_admin.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle Exit</span></a>
            <a href="Vehicle_List_Page.html" class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span class="material-symbols-outlined shrink-0">directions_car</span><span class="nav-label text-sm">Vehicles</span></a>
            <a href="slot_overview.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">grid_view</span><span class="nav-label text-sm">Parking Slots</span></a>
            <a href="usermanagement.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">group</span><span class="nav-label text-sm">Users</span></a>
            <a href="paymenthistory.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">payments</span><span class="nav-label text-sm">Payments</span></a>
            <a href="Repportshubpage.html" class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span class="material-symbols-outlined shrink-0">bar_chart</span><span class="nav-label text-sm">Reports</span></a>
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
                <button onclick="window.location.href='Vehicle_List_Page.html'" class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Add Vehicle</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Register a new vehicle</p>
                </div>
            </div>
            <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                    <span class="material-symbols-outlined text-white/50">person</span>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-3xl mx-auto space-y-8">

                <!-- Vehicle Category -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-6">Vehicle Category</h3>
                    <div class="grid grid-cols-4 gap-4">
                        <div class="category-card selected" onclick="selectCategory(this)">
                            <span class="material-symbols-outlined icon text-primary">directions_car</span>
                            <p>Car</p>
                        </div>
                        <div class="category-card" onclick="selectCategory(this)">
                            <span class="material-symbols-outlined icon text-slate-400">two_wheeler</span>
                            <p>Motorcycle</p>
                        </div>
                        <div class="category-card" onclick="selectCategory(this)">
                            <span class="material-symbols-outlined icon text-slate-400">local_shipping</span>
                            <p>Truck</p>
                        </div>
                        <div class="category-card" onclick="selectCategory(this)">
                            <span class="material-symbols-outlined icon text-slate-400">airport_shuttle</span>
                            <p>Van</p>
                        </div>
                    </div>
                </div>

                <!-- Vehicle Details -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-6">Vehicle Details</h3>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                        <div class="sm:col-span-2">
                            <label class="field-label">License Plate Number <span class="text-rose-400">*</span></label>
                            <input type="text" placeholder="e.g. ABC-1234" class="input-glass bg-white/5 px-4 h-12 rounded-2xl uppercase tracking-widest" />
                            <p class="text-[10px] text-slate-500 font-bold mt-2">Enter the plate number exactly as it appears on the vehicle.</p>
                        </div>
                        <div>
                            <label class="field-label">Make <span class="text-rose-400">*</span></label>
                            <input type="text" placeholder="e.g. Toyota" class="input-glass bg-white/5 px-4 h-12 rounded-2xl" />
                        </div>
                        <div>
                            <label class="field-label">Model <span class="text-rose-400">*</span></label>
                            <input type="text" placeholder="e.g. Prius" class="input-glass bg-white/5 px-4 h-12 rounded-2xl" />
                        </div>
                        <div>
                            <label class="field-label">Year</label>
                            <input type="number" placeholder="e.g. 2022" class="input-glass bg-white/5 px-4 h-12 rounded-2xl" />
                        </div>
                        <div>
                            <label class="field-label">Color</label>
                            <select class="input-glass px-4 h-12 rounded-2xl">
                                <option value="">Select color…</option>
                                <option>White</option>
                                <option>Black</option>
                                <option>Silver</option>
                                <option>Blue</option>
                                <option>Red</option>
                                <option>Grey</option>
                                <option>Green</option>
                                <option>Other</option>
                            </select>
                        </div>
                    </div>
                </div>

                <!-- Owner Information -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-6">Owner Information</h3>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                        <div>
                            <label class="field-label">Owner Name <span class="text-rose-400">*</span></label>
                            <input type="text" placeholder="Full name" class="input-glass bg-white/5 px-4 h-12 rounded-2xl" />
                        </div>
                        <div>
                            <label class="field-label">Phone Number</label>
                            <input type="tel" placeholder="+94 77 123 45678" class="input-glass bg-white/5 px-4 h-12 rounded-2xl" />
                        </div>
                        <div class="sm:col-span-2">
                            <label class="field-label">Email Address</label>
                            <input type="email" placeholder="owner@email.com" class="input-glass bg-white/5 px-4 h-12 rounded-2xl" />
                        </div>
                        <div class="sm:col-span-2">
                            <label class="field-label">Link to Existing User Account</label>
                            <div class="relative">
                                <span class="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-slate-500 text-lg">search</span>
                                <input type="text" placeholder="Search by name or email…" class="input-glass bg-white/5 px-4 h-12 rounded-2xl pl-10" />
                            </div>
                            <p class="text-[10px] text-slate-500 font-bold mt-2">Optional — links this vehicle to a registered Parkiyo user account.</p>
                        </div>
                    </div>
                </div>

                <!-- Notes & Status -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-6">Additional Settings</h3>
                    <div class="space-y-6">
                        <div>
                            <label class="field-label">Notes</label>
                            <textarea rows="3" placeholder="Any notes about this vehicle (optional)…" class="input-glass rounded-2xl px-4 py-3 resize-none"></textarea>
                        </div>
                        <div class="flex items-center justify-between p-5 rounded-2xl bg-white/[0.02] border border-white/5">
                            <div>
                                <p class="text-sm font-black text-white">Set as Active</p>
                                <p class="text-[11px] text-slate-500 font-bold mt-0.5">Vehicle will be allowed to enter the parking facility</p>
                            </div>
                            <button onclick="this.classList.toggle('bg-primary'); this.classList.toggle('bg-white/10');" class="relative h-7 w-14 rounded-full bg-primary transition-all">
                                <span class="absolute top-1 right-1 h-5 w-5 rounded-full bg-white shadow transition-all"></span>
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Actions -->
                <div class="flex items-center justify-end gap-4 pb-4">
                    <button onclick="window.location.href='Vehicle_List_Page.html'" class="px-8 py-3.5 rounded-2xl bg-white/5 border border-white/10 text-slate-300 font-black text-sm hover:bg-white/10 transition-all">
                        Cancel
                    </button>
                    <button onclick="window.location.href='Vehicle_List_Page.html'" class="px-8 py-3.5 rounded-2xl bg-primary text-white font-black text-sm hover:bg-primary/80 transition-all shadow-[0_0_20px_rgba(31,104,249,0.3)] flex items-center gap-2">
                        <span class="material-symbols-outlined text-lg">check</span> Register Vehicle
                    </button>
                </div>

            </div>
        </div>
    </main>
</div>
<script>
    function selectCategory(el) {
        document.querySelectorAll('.category-card').forEach(c => {
            c.classList.remove('selected');
            c.querySelector('span.icon').classList.remove('text-primary');
            c.querySelector('span.icon').classList.add('text-slate-400');
        });
        el.classList.add('selected');
        el.querySelector('span.icon').classList.add('text-primary');
        el.querySelector('span.icon').classList.remove('text-slate-400');
    }
</script>
</body>
</html>
