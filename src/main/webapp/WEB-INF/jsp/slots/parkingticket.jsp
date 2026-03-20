<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Parking Ticket</title>
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

        /* Ticket styles */
        .ticket-wrap {
            background: #0d1526;
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 28px;
            overflow: hidden;
            max-width: 420px;
            margin: 0 auto;
        }

        .ticket-header {
            background: linear-gradient(135deg, #1f68f9 0%, #3b82f6 100%);
            padding: 28px 28px 24px;
        }

        .ticket-body {
            padding: 24px 28px;
        }

        .ticket-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px dashed rgba(255, 255, 255, 0.06);
        }

        .ticket-row:last-child {
            border-bottom: none;
        }

        .ticket-row .lbl {
            font-size: 0.68rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.1em;
            color: #64748b;
        }

        .ticket-row .val {
            font-size: 0.85rem;
            font-weight: 800;
            color: white;
            text-align: right;
        }

        .tear {
            height: 1px;
            background: repeating-linear-gradient(90deg, transparent, transparent 8px, rgba(255, 255, 255, 0.08) 8px, rgba(255, 255, 255, 0.08) 16px);
            margin: 0 -4px;
            position: relative;
        }

        .tear::before,
        .tear::after {
            content: '';
            position: absolute;
            top: -10px;
            width: 20px;
            height: 20px;
            background: #020617;
            border-radius: 50%;
        }

        .tear::before {
            left: -14px;
        }

        .tear::after {
            right: -14px;
        }

        .qr-box {
            background: white;
            border-radius: 12px;
            padding: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .qr-svg rect {
            rx: 2;
        }

        @media print {
            body * {
                visibility: hidden;
            }

            .ticket-wrap,
            .ticket-wrap * {
                visibility: visible;
            }

            .ticket-wrap {
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
            }

            .no-print {
                display: none !important;
            }
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
    <aside class="sidebar-container border-r border-white/5 premium-blur flex flex-col shrink-0 z-50 no-print">
        <div class="p-6 mb-4 flex items-center">
            <div
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-[14px] bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-1 overflow-y-auto">
            <a href="/dashboard_user"
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
            <a href="/parking"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span
                    class="material-symbols-outlined shrink-0">local_parking</span><span
                    class="nav-label text-sm">Active Parking</span></a>
            <a href="/paymenthistory"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">payments</span><span
                    class="nav-label text-sm">Payments</span></a>
            <a href="/walletoverview"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">account_balance_wallet</span><span
                    class="nav-label text-sm">Wallet</span></a>
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
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0 no-print">
            <div class="flex items-center gap-4">
                <button onclick="window.location.href='parking.html'"
                        class="h-10 w-10 rounded-xl bg-white/5 border border-white/10 flex items-center justify-center hover:bg-white/10 transition-all">
                    <span class="material-symbols-outlined text-slate-400">arrow_back</span>
                </button>
                <div>
                    <h2 class="text-xl font-black text-white">Parking Ticket</h2>
                    <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Session
                        SES-20260309-092</p>
                </div>
            </div>
            <div class="flex items-center gap-3">
                <button onclick="window.print()"
                        class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">print</span> Print
                </button>
                <button
                        class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-lg">share</span> Share
                </button>
                <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-lg mx-auto">

                <!-- Live accrual banner -->
                <div
                        class="flex items-center gap-3 mb-6 p-4 rounded-2xl bg-emerald-500/8 border border-emerald-500/15 no-print">
                    <span class="pulse-dot"></span>
                    <p class="text-sm font-black text-emerald-400">Session Active — Fee is accruing</p>
                    <span class="ml-auto text-2xl font-black text-white" id="liveFee">$12.25</span>
                </div>

                <!-- THE TICKET -->
                <div class="ticket-wrap">
                    <!-- Header -->
                    <div class="ticket-header">
                        <div class="flex items-center justify-between mb-5">
                            <div class="flex items-center gap-3">
                                <div class="h-9 w-9 rounded-xl bg-white/20 flex items-center justify-center">
                                    <span class="material-symbols-outlined text-white text-lg">local_parking</span>
                                </div>
                                <span class="text-xl font-black text-white uppercase tracking-tight">Parkiyo</span>
                            </div>
                            <span class="text-[10px] font-black uppercase tracking-widest text-blue-200/70">Parking
                                    Ticket</span>
                        </div>
                        <!-- Plate display -->
                        <div class="bg-white/15 rounded-2xl px-6 py-4 text-center border border-white/20">
                            <p class="text-[9px] font-black uppercase tracking-widest text-blue-200/60 mb-1">License
                                Plate</p>
                            <p class="text-4xl font-black text-white tracking-[0.25em]">ABC-1234</p>
                        </div>
                    </div>

                    <!-- Body -->
                    <div class="ticket-body">
                        <div class="ticket-row">
                            <span class="lbl">Session ID</span>
                            <span class="val font-mono text-xs text-slate-400">SES-20260309-092</span>
                        </div>
                        <div class="ticket-row">
                            <span class="lbl">Vehicle</span>
                            <span class="val">Toyota Prius · Silver</span>
                        </div>
                        <div class="ticket-row">
                            <span class="lbl">Owner</span>
                            <span class="val">Kamal Perera</span>
                        </div>
                        <div class="ticket-row">
                            <span class="lbl">Slot</span>
                            <span class="val text-primary">A-12 · Zone A · Ground Floor</span>
                        </div>
                        <div class="ticket-row">
                            <span class="lbl">Entry Time</span>
                            <span class="val">09 Mar 2026, 10:45 AM</span>
                        </div>
                        <div class="ticket-row">
                            <span class="lbl">Exit Time</span>
                            <span class="val text-emerald-400 flex items-center gap-1.5 justify-end"><span
                                    class="pulse-dot" style="width:6px;height:6px"></span>Active</span>
                        </div>
                        <div class="ticket-row">
                            <span class="lbl">Duration</span>
                            <span class="val" id="ticketDur">2h 14m</span>
                        </div>
                        <div class="ticket-row">
                            <span class="lbl">Rate</span>
                            <span class="val">$5.50 / hr</span>
                        </div>
                    </div>

                    <!-- Tear line -->
                    <div class="tear mx-4"></div>

                    <!-- Footer section -->
                    <div class="ticket-body pt-5">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">
                                    Accrued Fee</p>
                                <p class="text-3xl font-black text-white" id="ticketFee">$12.25</p>
                                <p class="text-[9px] text-slate-500 font-bold mt-1">Updated live · unpaid</p>
                            </div>
                            <!-- QR code (decorative SVG) -->
                            <div class="qr-box">
                                <svg width="72" height="72" viewBox="0 0 72 72" fill="none"
                                     xmlns="http://www.w3.org/2000/svg">
                                    <rect x="2" y="2" width="28" height="28" rx="3" fill="none" stroke="#111"
                                          stroke-width="4" />
                                    <rect x="10" y="10" width="12" height="12" rx="1" fill="#111" />
                                    <rect x="42" y="2" width="28" height="28" rx="3" fill="none" stroke="#111"
                                          stroke-width="4" />
                                    <rect x="50" y="10" width="12" height="12" rx="1" fill="#111" />
                                    <rect x="2" y="42" width="28" height="28" rx="3" fill="none" stroke="#111"
                                          stroke-width="4" />
                                    <rect x="10" y="50" width="12" height="12" rx="1" fill="#111" />
                                    <rect x="42" y="42" width="8" height="8" rx="1" fill="#111" />
                                    <rect x="54" y="42" width="8" height="8" rx="1" fill="#111" />
                                    <rect x="42" y="54" width="8" height="8" rx="1" fill="#111" />
                                    <rect x="54" y="54" width="16" height="16" rx="1" fill="#111" />
                                </svg>
                            </div>
                        </div>
                        <p class="text-center text-[9px] text-slate-600 font-bold mt-5">Scan QR at exit kiosk ·
                            Thank you for parking with Parkiyo</p>
                    </div>
                </div>

                <!-- Action buttons -->
                <div class="flex gap-3 mt-6 no-print">
                    <a href="/exitvehicle"
                       class="flex-1 flex items-center justify-center gap-2 bg-rose-500/10 border border-rose-500/20 text-rose-400 font-black py-3.5 rounded-2xl hover:bg-rose-500/20 transition-all text-sm">
                        <span class="material-symbols-outlined text-lg">logout</span> Process Exit
                    </a>
                    <a href="parkingrecorddetails.html"
                       class="flex-1 flex items-center justify-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black py-3.5 rounded-2xl hover:bg-white/10 transition-all text-sm">
                        <span class="material-symbols-outlined text-lg">info</span> Full Details
                    </a>
                </div>

            </div>
        </div>
    </main>
</div>
<script>
    // Simulate live fee
    let cents = 1225;
    setInterval(() => {
        cents += 1;
        const fee = '$' + (cents / 100).toFixed(2);
        document.getElementById('liveFee').textContent = fee;
        document.getElementById('ticketFee').textContent = fee;
    }, 3600); // tick every ~3.6s ≈ $1/hr visual rate
</script>
</body>

</html>