<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Edit Slot</title>
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
                    borderRadius: { squircle: "14px" },
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

        .input-glass {
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            color: white;
            border-radius: 12px;
            padding: 11px 16px;
            font-size: 0.85rem;
            font-weight: 700;
            transition: all 0.2s;
            outline: none;
            width: 100%;
        }

        .input-glass:focus {
            border-color: rgba(31, 104, 249, 0.5);
            box-shadow: 0 0 0 3px rgba(31, 104, 249, 0.1);
        }

        .input-glass::placeholder {
            color: rgba(255, 255, 255, 0.18);
        }

        .input-readonly {
            background: rgba(255, 255, 255, 0.02);
            border: 1px solid rgba(255, 255, 255, 0.05);
            color: #475569;
            border-radius: 12px;
            padding: 11px 16px;
            font-size: 0.85rem;
            font-weight: 700;
            width: 100%;
            cursor: not-allowed;
        }

        select.input-glass option {
            background: #0f172a;
        }

        label.field-label {
            font-size: 0.7rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.1em;
            color: #64748b;
            margin-bottom: 6px;
            display: block;
        }

        .type-card {
            background: rgba(255, 255, 255, 0.03);
            border: 2px solid rgba(255, 255, 255, 0.07);
            border-radius: 16px;
            padding: 14px;
            cursor: pointer;
            transition: all 0.2s;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 7px;
        }

        .type-card:hover {
            border-color: rgba(31, 104, 249, 0.4);
            background: rgba(31, 104, 249, 0.05);
        }

        .type-card.selected {
            border-color: #1f68f9;
            background: rgba(31, 104, 249, 0.1);
        }

        .type-card p {
            font-size: 0.68rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.06em;
            color: #64748b;
        }

        .type-card.selected p {
            color: #60a5fa;
        }

        .status-btn {
            padding: 10px 18px;
            border-radius: 12px;
            font-size: 0.72rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.07em;
            cursor: pointer;
            transition: all 0.2s;
            border: 2px solid transparent;
        }

        .status-btn.sel-avail {
            background: rgba(16, 185, 129, 0.12);
            border-color: #10b981;
            color: #34d399;
        }

        .status-btn.sel-maint {
            background: rgba(245, 158, 11, 0.12);
            border-color: #f59e0b;
            color: #fbbf24;
        }

        .status-btn.sel-disabled {
            background: rgba(239, 68, 68, 0.12);
            border-color: #ef4444;
            color: #f87171;
        }

        .status-btn:not(.sel-avail):not(.sel-maint):not(.sel-disabled) {
            background: rgba(255, 255, 255, 0.03);
            border-color: rgba(255, 255, 255, 0.08);
            color: #475569;
        }

        .meta-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 11px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
        }

        .meta-row:last-child {
            border-bottom: none;
        }

        ::-webkit-scrollbar {
            width: 5px;
            display: none;
        }

        ::-webkit-scrollbar-thumb {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 10px;
        }


        input[type=number].no-spin::-webkit-outer-spin-button,
        input[type=number].no-spin::-webkit-inner-spin-button {
            -webkit-appearance: none;
            margin: 0;
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
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">dashboard</span><span
                    class="nav-label text-sm">Dashboard</span></a>
            <a href="entry_admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle
                        Entry</span></a>
            <a href="exitvehicle_admin.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle
                        Exit</span></a>
            <a href="Vehicle_List_Page.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">directions_car</span><span
                    class="nav-label text-sm">Vehicles</span></a>
            <a href="slot_list.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span
                    class="material-symbols-outlined shrink-0">grid_view</span><span
                    class="nav-label text-sm">Parking Slots</span></a>
            <a href="usermanagement.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">group</span><span
                    class="nav-label text-sm">Users</span></a>
            <a href="paymenthistory.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">payments</span><span
                    class="nav-label text-sm">Payments</span></a>
            <a href="Repportshubpage.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">bar_chart</span><span
                    class="nav-label text-sm">Reports</span></a>
            <a href="accountsetting.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">settings</span><span
                    class="nav-label text-sm">Settings</span></a>
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
            <div class="flex items-center gap-4">
                <button onclick="saveSlot()"
                        class="px-8 py-3.5 rounded-2xl bg-primary text-white font-black text-sm hover:bg-primary/80 transition-all shadow-[0_0_20px_rgba(31,104,249,0.3)] flex items-center gap-2">
                    <span class="material-symbols-outlined text-lg">save</span> Save Changes
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Edit Slot</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">SLT-00012 ·
                        A-12</p>
                </div>
            </div>
            <div class="flex items-center gap-3">
                <a href="slot_overview.html"
                   class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">grid_view</span> View in Map
                </a>
                <div class="h-10 w-10 rounded-squircle bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-squircle bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-3xl mx-auto space-y-8">

                <!-- Current state banner -->
                <div
                        class="glass-card rounded-[2.5rem] p-7 flex items-center gap-5 border border-emerald-500/15 bg-emerald-500/5">
                    <div
                            class="h-14 w-14 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center shrink-0">
                        <span class="material-symbols-outlined text-emerald-400 text-2xl">directions_car</span>
                    </div>
                    <div class="flex-1">
                        <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Currently
                            Editing</p>
                        <h3 class="text-2xl font-black text-white tracking-widest">A-12</h3>
                        <p class="text-slate-400 text-sm font-bold">Zone A · Ground Floor · Standard · Available</p>
                    </div>
                    <div class="text-right">
                        <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Slot ID</p>
                        <p class="text-white font-black font-mono text-sm">SLT-00012</p>
                        <p class="text-[10px] text-slate-500 font-bold mt-1">Created 2024-09-15</p>
                    </div>
                </div>

                <!-- Slot identity — code locked -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-6">Slot Identity</h3>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                        <div>
                            <label class="field-label">Slot Code</label>
                            <div class="relative">
                                <input type="text" value="A-12"
                                       class="input-readonly bg-white/5 rounded-2xl uppercase tracking-widest pr-10"
                                       readonly />
                                <span
                                        class="material-symbols-outlined absolute right-4 top-1/2 -translate-y-1/2 text-slate-600 text-base">lock</span>
                            </div>
                            <p class="text-[10px] text-slate-600 font-bold mt-2">Slot code cannot be changed after
                                creation.</p>
                        </div>
                        <div>
                            <label class="field-label">Zone <span class="text-rose-400">*</span></label>
                            <select class="input-glass px-4 h-12 rounded-2xl">
                                <option selected>Zone A — Ground Floor</option>
                                <option>Zone B — Level 1</option>
                                <option>Zone C — Level 2</option>
                                <option>Zone D — Rooftop</option>
                            </select>
                        </div>
                        <div>
                            <label class="field-label">Floor / Level</label>
                            <select class="input-glass px-4 h-12 rounded-2xl">
                                <option selected>Ground Floor</option>
                                <option>Level 1</option>
                                <option>Level 2</option>
                                <option>Rooftop</option>
                            </select>
                        </div>
                        <div>
                            <label class="field-label">Row / Section</label>
                            <input type="text" placeholder="Row 1, Section A"
                                   class="input-glass bg-white/5 rounded-2xl px-4 h-12 rounded-2xl" />
                        </div>
                    </div>
                </div>

                <!-- Slot type -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-6">Slot Type</h3>
                    <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
                        <div class="type-card selected" onclick="selectType(this)">
                            <span class="material-symbols-outlined text-primary text-2xl">directions_car</span>
                            <p>Standard</p>
                        </div>
                        <div class="type-card" onclick="selectType(this)">
                            <span class="material-symbols-outlined text-slate-400 text-2xl">accessible</span>
                            <p>Disabled</p>
                        </div>
                        <div class="type-card" onclick="selectType(this)">
                            <span class="material-symbols-outlined text-slate-400 text-2xl">electric_car</span>
                            <p>EV Charging</p>
                        </div>
                        <div class="type-card" onclick="selectType(this)">
                            <span class="material-symbols-outlined text-slate-400 text-2xl">local_shipping</span>
                            <p>Oversized</p>
                        </div>
                    </div>
                </div>

                <!-- Status override -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-2">Slot Status</h3>
                    <p class="text-[11px] text-slate-500 font-bold mb-6">Manually set the slot status. Note:
                        occupied slots cannot be set to available until vehicle exits.</p>
                    <div class="flex flex-wrap gap-3">
                        <button class="status-btn sel-avail" onclick="setStatus(this,'avail')">
                            <span class="material-symbols-outlined text-base align-middle mr-1">check_circle</span>
                            Available
                        </button>
                        <button class="status-btn" onclick="setStatus(this,'maint')">
                            <span class="material-symbols-outlined text-base align-middle mr-1">build</span>
                            Maintenance
                        </button>
                        <button class="status-btn" onclick="setStatus(this,'disabled')">
                            <span class="material-symbols-outlined text-base align-middle mr-1">block</span>
                            Disabled
                        </button>
                    </div>
                </div>

                <!-- Pricing -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-6">Pricing & Notes</h3>
                    <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                        <div>
                            <label class="field-label">Hourly Rate ($)</label>
                            <input type="number" placeholder="5.50" step="0.50" inputmode="decimal"
                                   class="input-glass bg-white/5 px-4 h-12 rounded-2xl no-spin"
                                   oninput="this.value=this.value.replace(/[^0-9.]*/g,'');" />
                        </div>
                        <div>
                            <label class="field-label">Daily Max Rate ($)</label>
                            <input type="number" placeholder="30.00" step="1.00" inputmode="decimal"
                                   class="input-glass bg-white/5 px-4 h-12 rounded-2xl no-spin"
                                   oninput="this.value=this.value.replace(/[^0-9.]*/g,'');" />
                        </div>
                        <div class="sm:col-span-2">
                            <label class="field-label">Notes</label>
                            <textarea rows="2"
                                      class="input-glass rounded-2xl px-4 py-3 resize-none">Near main entrance. Preferred for regular visitors.</textarea>
                        </div>
                    </div>
                </div>

                <!-- Slot stats (read-only) -->
                <div class="glass-card rounded-[2.5rem] p-8">
                    <h3 class="text-base font-black text-white mb-5">Slot Statistics</h3>
                    <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
                        <div class="p-4 rounded-2xl bg-white/[0.02] border border-white/5 text-center">
                            <p class="text-2xl font-black text-white">214</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Total
                                Uses</p>
                        </div>
                        <div class="p-4 rounded-2xl bg-white/[0.02] border border-white/5 text-center">
                            <p class="text-2xl font-black text-emerald-400">$1,177</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Revenue
                            </p>
                        </div>
                        <div class="p-4 rounded-2xl bg-white/[0.02] border border-white/5 text-center">
                            <p class="text-2xl font-black text-primary">78%</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">
                                Utilisation</p>
                        </div>
                        <div class="p-4 rounded-2xl bg-white/[0.02] border border-white/5 text-center">
                            <p class="text-2xl font-black text-white">3h 4m</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Avg
                                Duration</p>
                        </div>
                    </div>
                    <a href="slot_usage_history.html"
                       class="mt-5 flex items-center gap-2 text-primary text-xs font-black hover:brightness-125 transition-all">
                        <span class="material-symbols-outlined text-base">history</span> View full usage history
                    </a>
                </div>

                <!-- Danger zone -->
                <div class="glass-card rounded-[2.5rem] p-7 border border-rose-500/10 bg-rose-500/5">
                    <p class="text-sm font-black text-rose-400 mb-1">Danger Zone</p>
                    <p class="text-[11px] text-slate-500 font-bold mb-4">Permanently delete this slot. All
                        historical data will be retained but the slot will no longer be available.</p>
                    <button
                            class="flex items-center gap-2 bg-rose-500/10 border border-rose-500/20 text-rose-400 font-black px-5 py-2.5 rounded-xl hover:bg-rose-500/20 transition-all text-xs uppercase tracking-widest">
                        <span class="material-symbols-outlined text-base">delete_forever</span> Delete Slot
                    </button>
                </div>

                <!-- Actions -->
                <div class="flex items-center justify-end gap-4 pb-4">
                    <button onclick="window.location.href='slot_list.html'"
                            class="px-8 py-3.5 rounded-2xl bg-white/5 border border-white/10 text-slate-300 font-black text-sm hover:bg-white/10 transition-all">Cancel</button>
                    <button onclick="window.location.href='slot_list.html'"
                            class="px-8 py-3.5 rounded-2xl bg-primary text-white font-black text-sm hover:bg-primary/80 transition-all shadow-[0_0_20px_rgba(31,104,249,0.3)] flex items-center gap-2">
                        <span class="material-symbols-outlined text-lg">save</span> Save Changes
                    </button>
                </div>

            </div>
        </div>
    </main>
</div>
<script>
    function selectType(el) {
        document.querySelectorAll('.type-card').forEach(c => {
            c.classList.remove('selected');
            c.querySelector('span').classList.remove('text-primary');
            c.querySelector('span').classList.add('text-slate-400');
        });
        el.classList.add('selected');
        el.querySelector('span').classList.add('text-primary');
        el.querySelector('span').classList.remove('text-slate-400');
    }
    function setStatus(el, type) {
        document.querySelectorAll('.status-btn').forEach(b => {
            b.classList.remove('sel-avail', 'sel-maint', 'sel-disabled');
        });
        if (type === 'avail') el.classList.add('sel-avail');
        else if (type === 'maint') el.classList.add('sel-maint');
        else el.classList.add('sel-disabled');
    }
</script>
</body>

</html>