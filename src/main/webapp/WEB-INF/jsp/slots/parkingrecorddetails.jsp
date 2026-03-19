<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Parking Record Details</title>
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

        .meta-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
        }

        .meta-row:last-child {
            border-bottom: none;
        }

        .meta-lbl {
            font-size: 0.68rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.1em;
            color: #64748b;
        }

        .meta-val {
            font-size: 0.85rem;
            font-weight: 800;
            color: white;
            text-align: right;
        }

        .pulse-dot {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background: #10b981;
            animation: pulse-ring 1.8s ease-out infinite;
            display: inline-block;
        }

        @keyframes pulse-ring {
            0% {
                box-shadow: 0 0 0 0 rgba(16, 185, 129, 0.5)
            }

            70% {
                box-shadow: 0 0 0 8px rgba(16, 185, 129, 0)
            }

            100% {
                box-shadow: 0 0 0 0 rgba(16, 185, 129, 0)
            }
        }

        .timeline-dot {
            width: 10px;
            height: 10px;
            border-radius: 50%;
            shrink: 0;
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
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-[14px] bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-1 overflow-y-auto">
            <a href="dashboard-user.html"
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
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span
                    class="material-symbols-outlined shrink-0">local_parking</span><span
                    class="nav-label text-sm">Active Parking</span></a>
            <a href="paymenthistory.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">payments</span><span
                    class="nav-label text-sm">Payments</span></a>
            <a href="walletoverview.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">account_balance_wallet</span><span
                    class="nav-label text-sm">Wallet</span></a>
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
                <button onclick="window.location.href='parking.html'"
                        class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Parking Record</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">
                        SES-20260309-092</p>
                </div>
            </div>
            <div class="flex items-center gap-3">
                <a href="parkingticket.html"
                   class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">confirmation_number</span> View Ticket
                </a>
                <a href="exitvehicle.html"
                   class="flex items-center gap-2 bg-rose-500/10 border border-rose-500/20 text-rose-400 font-black px-5 py-2.5 rounded-xl hover:bg-rose-500/20 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">logout</span> Process Exit
                </a>
                <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-4xl mx-auto space-y-8">

                <!-- Hero -->
                <div class="glass-card rounded-[2.5rem] p-8 border border-emerald-500/10 bg-emerald-500/[0.03]">
                    <div class="flex flex-wrap items-center gap-6">
                        <div
                                class="h-16 w-16 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center shrink-0">
                            <span class="material-symbols-outlined text-primary text-3xl">directions_car</span>
                        </div>
                        <div class="flex-1">
                            <div class="flex items-center gap-3 flex-wrap mb-1">
                                <h3 class="text-3xl font-black text-white tracking-widest">ABC-1234</h3>
                                <span
                                        class="flex items-center gap-1.5 text-[10px] font-black text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-3 py-1 rounded-lg uppercase tracking-wider">
                                        <span class="pulse-dot" style="width:6px;height:6px"></span>Active
                                    </span>
                            </div>
                            <p class="text-slate-400 font-bold">Toyota Prius · Silver · Kamal Perera</p>
                        </div>
                        <div class="text-right">
                            <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-1">Accrued
                                Fee</p>
                            <p class="text-4xl font-black text-white" id="liveFee">$12.25</p>
                            <p class="text-[10px] text-emerald-400 font-bold mt-1">Ticking at $5.50/hr</p>
                        </div>
                    </div>

                    <!-- 4 quick stats -->
                    <div class="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-6">
                        <div class="p-4 rounded-2xl bg-black/20 border border-white/5 text-center">
                            <p class="text-xl font-black text-white" id="liveDur">2h 14m</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Duration
                            </p>
                        </div>
                        <div class="p-4 rounded-2xl bg-black/20 border border-white/5 text-center">
                            <p class="text-xl font-black text-primary">A-12</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Slot</p>
                        </div>
                        <div class="p-4 rounded-2xl bg-black/20 border border-white/5 text-center">
                            <p class="text-xl font-black text-white">10:45</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Entry
                                Time</p>
                        </div>
                        <div class="p-4 rounded-2xl bg-black/20 border border-white/5 text-center">
                            <p class="text-xl font-black text-amber-400">—</p>
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mt-1">Exit Time
                            </p>
                        </div>
                    </div>
                </div>

                <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    <!-- Session details -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-5">Session Details</h3>
                        <div class="meta-row"><span class="meta-lbl">Session ID</span><span
                                class="meta-val font-mono text-xs text-slate-400">SES-20260309-092</span></div>
                        <div class="meta-row"><span class="meta-lbl">Entry Date</span><span class="meta-val">09 Mar
                                    2026</span></div>
                        <div class="meta-row"><span class="meta-lbl">Entry Time</span><span class="meta-val">10:45
                                    AM</span></div>
                        <div class="meta-row"><span class="meta-lbl">Exit Time</span><span
                                class="meta-val text-emerald-400 flex items-center gap-2 justify-end"><span
                                class="pulse-dot" style="width:6px;height:6px"></span>In progress</span></div>
                        <div class="meta-row"><span class="meta-lbl">Operator</span><span class="meta-val">Maria
                                    Santos</span></div>
                        <div class="meta-row"><span class="meta-lbl">Entry Method</span><span
                                class="meta-val">Manual · Plate lookup</span></div>
                        <div class="meta-row"><span class="meta-lbl">Notes</span><span
                                class="meta-val text-slate-400">—</span></div>
                    </div>

                    <!-- Vehicle & owner -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-5">Vehicle & Owner</h3>
                        <div class="meta-row"><span class="meta-lbl">Plate</span><span
                                class="meta-val tracking-widest">ABC-1234</span></div>
                        <div class="meta-row"><span class="meta-lbl">Make / Model</span><span
                                class="meta-val">Toyota Prius</span></div>
                        <div class="meta-row"><span class="meta-lbl">Year</span><span class="meta-val">2021</span>
                        </div>
                        <div class="meta-row"><span class="meta-lbl">Color</span>
                            <span class="meta-val flex items-center gap-2 justify-end"><span
                                    class="h-4 w-4 rounded-full bg-slate-300 border border-white/20 inline-block"></span>Silver</span>
                        </div>
                        <div class="meta-row"><span class="meta-lbl">Category</span><span class="meta-val">Standard
                                    Car</span></div>
                        <div class="meta-row"><span class="meta-lbl">Owner</span><span class="meta-val">Kamal
                                    Perera</span></div>
                        <div class="meta-row"><span class="meta-lbl">Phone</span><span
                                class="meta-val text-primary">+94 77 123 4567</span></div>
                    </div>

                    <!-- Slot & pricing -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-5">Slot & Pricing</h3>
                        <div class="meta-row"><span class="meta-lbl">Slot Code</span><span
                                class="meta-val text-primary">A-12</span></div>
                        <div class="meta-row"><span class="meta-lbl">Zone</span><span class="meta-val">Zone A</span>
                        </div>
                        <div class="meta-row"><span class="meta-lbl">Floor</span><span class="meta-val">Ground
                                    Floor</span></div>
                        <div class="meta-row"><span class="meta-lbl">Type</span><span
                                class="meta-val">Standard</span></div>
                        <div class="meta-row"><span class="meta-lbl">Hourly Rate</span><span
                                class="meta-val">$5.50</span></div>
                        <div class="meta-row"><span class="meta-lbl">Daily Max</span><span
                                class="meta-val">$30.00</span></div>
                        <div class="meta-row"><span class="meta-lbl">Accrued Fee</span><span
                                class="meta-val text-white text-lg" id="feeSmall">$12.25</span></div>
                    </div>

                    <!-- Session timeline -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-6">Session Timeline</h3>
                        <div class="relative pl-6">
                            <div class="absolute left-[9px] top-3 bottom-3 w-[2px] bg-white/5 rounded-full"></div>

                            <div class="relative mb-6">
                                <div
                                        class="absolute -left-6 top-1 w-3 h-3 rounded-full bg-primary border-2 border-background-dark">
                                </div>
                                <p class="text-xs font-black text-white">Vehicle Entry Registered</p>
                                <p class="text-[10px] text-slate-500 font-bold mt-0.5">09 Mar 2026 · 10:45 AM</p>
                                <p class="text-[10px] text-slate-600 font-bold mt-0.5">Operator: Maria Santos · Slot
                                    A-12 assigned</p>
                            </div>

                            <div class="relative mb-6">
                                <div
                                        class="absolute -left-6 top-1 w-3 h-3 rounded-full bg-amber-500 border-2 border-background-dark">
                                </div>
                                <p class="text-xs font-black text-white">1-Hour Threshold Passed</p>
                                <p class="text-[10px] text-slate-500 font-bold mt-0.5">09 Mar 2026 · 11:45 AM</p>
                                <p class="text-[10px] text-slate-600 font-bold mt-0.5">Fee: $5.50 ·
                                    Auto-notification sent</p>
                            </div>

                            <div class="relative mb-6">
                                <div
                                        class="absolute -left-6 top-1 w-3 h-3 rounded-full bg-amber-500 border-2 border-background-dark">
                                </div>
                                <p class="text-xs font-black text-white">2-Hour Threshold Passed</p>
                                <p class="text-[10px] text-slate-500 font-bold mt-0.5">09 Mar 2026 · 12:45 PM</p>
                                <p class="text-[10px] text-slate-600 font-bold mt-0.5">Fee: $11.00</p>
                            </div>

                            <div class="relative">
                                <div
                                        class="absolute -left-6 top-1 w-3 h-3 rounded-full bg-emerald-500 border-2 border-background-dark animate-pulse">
                                </div>
                                <p class="text-xs font-black text-emerald-400 flex items-center gap-2"><span
                                        class="pulse-dot" style="width:6px;height:6px"></span>Session Active</p>
                                <p class="text-[10px] text-slate-500 font-bold mt-0.5">Now · 12:59 PM</p>
                                <p class="text-[10px] text-slate-600 font-bold mt-0.5">Accruing at $5.50/hr</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action footer -->
                <div class="flex flex-wrap gap-3 pb-4">
                    <a href="parkingticket.html"
                       class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-6 py-3.5 rounded-2xl hover:bg-white/10 transition-all text-sm">
                        <span class="material-symbols-outlined text-lg">confirmation_number</span> Print Ticket
                    </a>
                    <a href="vehicle-details-page.html"
                       class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-6 py-3.5 rounded-2xl hover:bg-white/10 transition-all text-sm">
                        <span class="material-symbols-outlined text-lg">directions_car</span> Vehicle Profile
                    </a>
                    <a href="exitvehicle.html"
                       class="ml-auto flex items-center gap-2 bg-rose-500/10 border border-rose-500/20 text-rose-400 font-black px-8 py-3.5 rounded-2xl hover:bg-rose-500/20 transition-all text-sm">
                        <span class="material-symbols-outlined text-lg">logout</span> Process Exit Now
                    </a>
                </div>

            </div>
        </div>
    </main>
</div>
<script>
    let cents = 1225;
    setInterval(() => {
        cents++;
        const fee = '$' + (cents / 100).toFixed(2);
        document.getElementById('liveFee').textContent = fee;
        document.getElementById('feeSmall').textContent = fee;
    }, 3600);
</script>
</body>

</html>