<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Select a Slot</title>
    <script src="https://cdn.tailwindcss.com?plugins=forms,container-queries"></script>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700;800;900&display=swap"
          rel="stylesheet" />
    <link
            href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"
            rel="stylesheet" />
    <script>tailwind.config = { darkMode: "class", theme: { extend: { colors: { primary: "#1f68f9", "background-dark": "#020617" }, fontFamily: { display: ["Public Sans", "sans-serif"] } } } }</script>
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

        /* Slot cells */
        .slot-cell {
            width: 72px;
            height: 90px;
            border-radius: 12px;
            border: 2px solid;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 5px;
            cursor: pointer;
            transition: all 0.18s;
            position: relative;
        }

        .slot-cell.available {
            background: rgba(16, 185, 129, 0.07);
            border-color: rgba(16, 185, 129, 0.3);
        }

        .slot-cell.available:hover {
            background: rgba(16, 185, 129, 0.16);
            border-color: #10b981;
            transform: scale(1.06);
        }

        .slot-cell.selected {
            background: rgba(31, 104, 249, 0.15);
            border-color: #1f68f9;
            transform: scale(1.06);
            box-shadow: 0 0 0 3px rgba(31, 104, 249, 0.2);
        }

        .slot-cell.occupied {
            background: rgba(239, 68, 68, 0.05);
            border-color: rgba(239, 68, 68, 0.2);
            cursor: not-allowed;
            opacity: 0.45;
        }

        .slot-cell.reserved {
            background: rgba(245, 158, 11, 0.06);
            border-color: rgba(245, 158, 11, 0.2);
            cursor: not-allowed;
            opacity: 0.5;
        }

        .slot-cell .slot-code {
            font-size: 0.7rem;
            font-weight: 900;
            letter-spacing: 0.06em;
        }

        .slot-cell.available .slot-code {
            color: #34d399;
        }

        .slot-cell.selected .slot-code {
            color: #60a5fa;
        }

        .slot-cell.occupied .slot-code {
            color: #f87171;
        }

        .slot-cell.reserved .slot-code {
            color: #fbbf24;
        }

        .slot-cell .slot-sub {
            font-size: 0.55rem;
            font-weight: 800;
            letter-spacing: 0.03em;
            opacity: 0.6;
        }

        .slot-tooltip {
            position: absolute;
            bottom: calc(100% + 8px);
            left: 50%;
            transform: translateX(-50%);
            background: #0f172a;
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: 10px;
            padding: 7px 12px;
            font-size: 0.68rem;
            font-weight: 700;
            white-space: nowrap;
            pointer-events: none;
            opacity: 0;
            transition: opacity 0.15s;
            z-index: 50;
        }

        .slot-cell:hover .slot-tooltip {
            opacity: 1;
        }

        .zone-tab {
            padding: 8px 20px;
            border-radius: 12px;
            font-size: 0.72rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.07em;
            transition: all 0.2s;
            cursor: pointer;
            border: none;
        }

        .zone-tab.active {
            background: rgba(31, 104, 249, 0.15);
            color: #60a5fa;
            border: 1px solid rgba(31, 104, 249, 0.25);
        }

        .zone-tab:not(.active) {
            background: transparent;
            color: #475569;
            border: 1px solid transparent;
        }

        .zone-tab:not(.active):hover {
            color: #94a3b8;
            background: rgba(255, 255, 255, 0.04);
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
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-[14px] bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-1 overflow-y-auto">
            <a href="dashboard_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">dashboard</span><span
                    class="nav-label text-sm">Dashboard</span></a>
            <a href="entry.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">login</span><span class="nav-label text-sm">Vehicle
                        Entry</span></a>
            <a href="exitvehicle.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">logout</span><span class="nav-label text-sm">Vehicle
                        Exit</span></a>
            <a href="parking.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">local_parking</span><span
                    class="nav-label text-sm">Active Parking</span></a>
            <a href="advancereservation.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span
                    class="material-symbols-outlined shrink-0">event_available</span><span
                    class="nav-label text-sm">Reservations</span></a>
            <a href="paymenthistory.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">payments</span><span
                    class="nav-label text-sm">Payments</span></a>
            <a href="walletoverview.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">account_balance_wallet</span><span
                    class="nav-label text-sm">Wallet</span></a>
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
                <button onclick="window.location.href='advancereservation.html'"
                        class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Select a Slot</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">XYZ-8899 · 11
                        Mar 2026 · 09:00 – 12:00</p>
                </div>
            </div>
            <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                    <span class="material-symbols-outlined text-white/50">person</span>
                </div>
            </div>
        </header>

        <div class="flex-1 flex overflow-hidden">

            <!-- Map area -->
            <div class="flex-1 flex flex-col overflow-hidden">

                <!-- Zone tabs + legend -->
                <div class="px-8 pt-6 pb-4 flex items-center justify-between gap-4 flex-wrap shrink-0">
                    <div class="flex gap-1 bg-white/[0.03] border border-white/5 rounded-xl p-1">
                        <button class="zone-tab active" onclick="setZone(this,'A')">Zone A</button>
                        <button class="zone-tab" onclick="setZone(this,'B')">Zone B</button>
                        <button class="zone-tab" onclick="setZone(this,'C')">Zone C</button>
                        <button class="zone-tab" onclick="setZone(this,'D')">Zone D</button>
                    </div>
                    <div class="flex items-center gap-5 flex-wrap">
                            <span class="flex items-center gap-2 text-xs font-bold text-emerald-400"><span
                                    class="h-3 w-3 rounded-sm bg-emerald-500/20 border border-emerald-500/50 inline-block"></span>Available</span>
                        <span class="flex items-center gap-2 text-xs font-bold text-primary"><span
                                class="h-3 w-3 rounded-sm bg-primary/25 border border-primary/60 inline-block"></span>Selected</span>
                        <span class="flex items-center gap-2 text-xs font-bold text-rose-400"><span
                                class="h-3 w-3 rounded-sm bg-rose-500/20 border border-rose-500/40 inline-block"></span>Occupied</span>
                        <span class="flex items-center gap-2 text-xs font-bold text-amber-400"><span
                                class="h-3 w-3 rounded-sm bg-amber-500/15 border border-amber-500/35 inline-block"></span>Reserved</span>
                    </div>
                </div>

                <!-- Zone info bar -->
                <div id="zoneInfo"
                     class="mx-8 mb-5 p-4 rounded-2xl bg-white/[0.02] border border-white/5 flex items-center gap-4 shrink-0">
                    <span class="material-symbols-outlined text-slate-500">info</span>
                    <div>
                        <p class="text-sm font-black text-white">Zone A — Ground Floor</p>
                        <p class="text-[10px] text-slate-500 font-bold">25 slots · <span class="text-emerald-400">14
                                    available</span> during your time window</p>
                    </div>
                    <span class="ml-auto text-[10px] font-black text-slate-500 uppercase tracking-widest">Standard ·
                            $5.50/hr</span>
                </div>

                <!-- Slot grid -->
                <div class="flex-1 overflow-y-auto px-8 pb-8">
                    <div id="slotGrid" class="flex flex-wrap gap-3">
                        <!-- Zone A slots rendered by JS -->
                    </div>
                </div>
            </div>

            <!-- Confirm panel -->
            <div class="w-72 border-l border-white/5 flex flex-col bg-background-dark/40 premium-blur shrink-0 p-6">
                <h3 class="text-sm font-black text-white mb-5">Reservation Summary</h3>

                <div class="space-y-3 mb-6 text-xs">
                    <div class="flex justify-between"><span class="text-slate-500 font-bold">Vehicle</span><span
                            class="text-white font-black">XYZ-8899</span></div>
                    <div class="flex justify-between"><span class="text-slate-500 font-bold">Date</span><span
                            class="text-white font-black">11 Mar 2026</span></div>
                    <div class="flex justify-between"><span class="text-slate-500 font-bold">Arrival</span><span
                            class="text-white font-black">09:00 AM</span></div>
                    <div class="flex justify-between"><span class="text-slate-500 font-bold">Departure</span><span
                            class="text-white font-black">12:00 PM</span></div>
                    <div class="flex justify-between"><span class="text-slate-500 font-bold">Duration</span><span
                            class="text-white font-black">3 hours</span></div>
                    <div class="flex justify-between border-t border-white/5 pt-3"><span
                            class="text-slate-500 font-bold">Slot</span><span class="text-primary font-black"
                                                                              id="selectedSlotLabel">—</span></div>
                    <div class="flex justify-between"><span class="text-slate-500 font-bold">Zone</span><span
                            class="text-slate-300 font-black" id="selectedZoneLabel">—</span></div>
                </div>

                <div class="p-4 rounded-2xl bg-primary/5 border border-primary/15 mb-6">
                    <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Estimated Fee</p>
                    <p class="text-2xl font-black text-white">$16.50</p>
                    <p class="text-[9px] text-slate-500 font-bold mt-1">3h × $5.50/hr</p>
                </div>

                <button id="confirmBtn" onclick="confirmSlot()" disabled
                        class="w-full flex items-center justify-center gap-2 bg-primary/30 text-white/40 font-black py-3.5 rounded-2xl text-sm cursor-not-allowed transition-all"
                        style="pointer-events:none">
                    <span class="material-symbols-outlined text-lg">check_circle</span> Confirm Slot
                </button>
                <p id="selectHint" class="text-center text-[10px] text-slate-600 font-bold mt-3">Select a slot on
                    the map</p>

                <div class="mt-auto pt-6 border-t border-white/5">
                    <button onclick="window.location.href='advancereservation.html'"
                            class="w-full text-slate-500 font-bold text-xs hover:text-slate-300 transition-all py-2">←
                        Back to Reservations</button>
                </div>
            </div>
        </div>
    </main>
</div>

<!-- Success toast -->
<div id="successToast" class="hidden fixed bottom-6 left-1/2 -translate-x-1/2 z-50">
    <div
            class="flex items-center gap-3 bg-emerald-500/15 border border-emerald-500/30 text-emerald-300 font-black px-6 py-4 rounded-2xl shadow-[0_0_30px_rgba(16,185,129,0.2)] backdrop-blur-xl">
        <span class="material-symbols-outlined text-emerald-400">check_circle</span>
        <span>Slot reserved! Redirecting to payment…</span>
    </div>
</div>

<script>
    const zones = {
        A: {
            label: 'Zone A — Ground Floor', sub: '25 slots · 14 available during your time window', rate: 'Standard · $5.50/hr',
            slots: [
                { code: 'A-01', s: 'available' }, { code: 'A-02', s: 'occupied' }, { code: 'A-03', s: 'available' }, { code: 'A-04', s: 'occupied' },
                { code: 'A-05', s: 'available' }, { code: 'A-06', s: 'reserved' }, { code: 'A-07', s: 'available' }, { code: 'A-08', s: 'occupied' },
                { code: 'A-09', s: 'occupied' }, { code: 'A-10', s: 'available' }, { code: 'A-11', s: 'available' }, { code: 'A-12', s: 'occupied' },
                { code: 'A-13', s: 'available' }, { code: 'A-14', s: 'available' }, { code: 'A-15', s: 'reserved' }, { code: 'A-16', s: 'available' },
                { code: 'A-17', s: 'available' }, { code: 'A-18', s: 'occupied' }, { code: 'A-19', s: 'available' }, { code: 'A-20', s: 'available' },
            ]
        },
        B: {
            label: 'Zone B — Level 1', sub: '30 slots · 18 available during your time window', rate: 'Standard · $5.50/hr',
            slots: [
                { code: 'B-01', s: 'available' }, { code: 'B-02', s: 'available' }, { code: 'B-03', s: 'occupied' }, { code: 'B-04', s: 'available' },
                { code: 'B-05', s: 'available' }, { code: 'B-06', s: 'reserved' }, { code: 'B-07', s: 'available' }, { code: 'B-08', s: 'occupied' },
                { code: 'B-09', s: 'available' }, { code: 'B-10', s: 'available' }, { code: 'B-11', s: 'occupied' }, { code: 'B-12', s: 'available' },
                { code: 'B-13', s: 'available' }, { code: 'B-14', s: 'reserved' }, { code: 'B-15', s: 'available' },
            ]
        },
        C: {
            label: 'Zone C — Level 2', sub: '25 slots · 12 available during your time window', rate: 'Standard · $5.50/hr',
            slots: [
                { code: 'C-01', s: 'available' }, { code: 'C-02', s: 'occupied' }, { code: 'C-03', s: 'available' }, { code: 'C-04', s: 'available' },
                { code: 'C-05', s: 'reserved' }, { code: 'C-06', s: 'available' }, { code: 'C-07', s: 'occupied' }, { code: 'C-08', s: 'available' },
                { code: 'C-09', s: 'occupied' }, { code: 'C-10', s: 'available' }, { code: 'C-11', s: 'available' }, { code: 'C-12', s: 'reserved' },
            ]
        },
        D: {
            label: 'Zone D — Rooftop', sub: '20 slots · 17 available during your time window', rate: 'Standard · $5.50/hr',
            slots: [
                { code: 'D-01', s: 'available' }, { code: 'D-02', s: 'available' }, { code: 'D-03', s: 'available' }, { code: 'D-04', s: 'occupied' },
                { code: 'D-05', s: 'available' }, { code: 'D-06', s: 'available' }, { code: 'D-07', s: 'available' }, { code: 'D-08', s: 'available' },
                { code: 'D-09', s: 'available' }, { code: 'D-10', s: 'occupied' }, { code: 'D-11', s: 'available' }, { code: 'D-12', s: 'available' },
            ]
        },
    };

    let currentZone = 'A';
    let selectedSlot = null;

    const iconMap = { available: 'check', occupied: 'directions_car', reserved: 'event_busy' };

    function renderZone(z) {
        const zd = zones[z];
        document.getElementById('zoneInfo').children[1].children[0].textContent = zd.label;
        document.getElementById('zoneInfo').children[1].children[1].innerHTML = `${zd.sub.split('·')[0]}· <span class="text-emerald-400">${zd.sub.split('·')[1]}</span>`;
        document.getElementById('zoneInfo').children[2].textContent = zd.rate;

        const grid = document.getElementById('slotGrid');
        grid.innerHTML = '';
        zd.slots.forEach(slot => {
            const tip = slot.s === 'available' ? `${slot.code} · Available` : slot.s === 'occupied' ? `${slot.code} · Occupied` : `${slot.code} · Reserved`;
            const cell = document.createElement('div');
            cell.className = `slot-cell ${slot.s}`;
            cell.innerHTML = `
            <span class="material-symbols-outlined text-lg ${slot.s === 'available' ? 'text-emerald-400' : slot.s === 'occupied' ? 'text-rose-400' : 'text-amber-400'}">${iconMap[slot.s]}</span>
            <span class="slot-code">${slot.code}</span>
            <div class="slot-tooltip text-white">${tip}</div>`;
            if (slot.s === 'available') {
                cell.onclick = () => selectSlot(cell, slot.code, z);
            }
            grid.appendChild(cell);
        });
    }

    function selectSlot(el, code, zone) {
        document.querySelectorAll('.slot-cell.selected').forEach(c => {
            c.classList.remove('selected');
            c.classList.add('available');
        });
        el.classList.remove('available');
        el.classList.add('selected');
        selectedSlot = code;
        document.getElementById('selectedSlotLabel').textContent = code;
        document.getElementById('selectedZoneLabel').textContent = `Zone ${zone}`;
        const btn = document.getElementById('confirmBtn');
        btn.disabled = false;
        btn.style.pointerEvents = 'auto';
        btn.className = 'w-full flex items-center justify-center gap-2 bg-primary text-white font-black py-3.5 rounded-2xl text-sm hover:bg-primary/80 transition-all shadow-[0_0_20px_rgba(31,104,249,0.3)]';
        document.getElementById('selectHint').textContent = `Slot ${code} selected ✓`;
        document.getElementById('selectHint').classList.add('text-primary');
        document.getElementById('selectHint').classList.remove('text-slate-600');
    }

    function setZone(el, z) {
        document.querySelectorAll('.zone-tab').forEach(b => b.classList.remove('active'));
        el.classList.add('active');
        currentZone = z;
        selectedSlot = null;
        document.getElementById('selectedSlotLabel').textContent = '—';
        document.getElementById('selectedZoneLabel').textContent = '—';
        renderZone(z);
    }

    function confirmSlot() {
        if (!selectedSlot) return;
        document.getElementById('successToast').classList.remove('hidden');
        setTimeout(() => window.location.href = 'pendingpayment.html', 1800);
    }

    renderZone('A');
</script>
</body>

</html>