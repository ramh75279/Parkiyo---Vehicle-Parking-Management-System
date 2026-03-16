<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Exit Vehicle</title>
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

        .plate-input {
            background: rgba(255, 255, 255, 0.04) !important;
            border: 1.5px solid rgba(255, 255, 255, 0.1);
            color: white !important;
            border-radius: 20px;
            padding: 22px 28px;
            font-size: 2.4rem;
            font-weight: 900;
            font-family: 'Public Sans', sans-serif;
            letter-spacing: 0.22em;
            text-align: center;
            text-transform: uppercase;
            transition: border-color 0.25s, box-shadow 0.25s, background 0.25s;
            outline: none;
            width: 100%;
            backdrop-filter: blur(8px);
        }

        .plate-input:hover {
            border-color: rgba(239, 68, 68, 0.25);
            background: rgba(255, 255, 255, 0.06) !important;
        }

        .plate-input:focus {
            background: rgba(239, 68, 68, 0.05) !important;
            border-color: rgba(239, 68, 68, 0.5) !important;
            box-shadow: 0 0 0 4px rgba(239, 68, 68, 0.08), 0 0 40px rgba(239, 68, 68, 0.06);
        }

        .plate-input::placeholder {
            color: rgba(148, 163, 184, 0.3);
            font-size: 1.6rem;
            letter-spacing: 0.12em;
            font-weight: 600;
        }

        .plate-input:-webkit-autofill,
        .plate-input:-webkit-autofill:focus {
            -webkit-text-fill-color: white !important;
            -webkit-box-shadow: 0 0 0px 1000px #0a1628 inset !important;
            caret-color: white;
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

        .pay-method {
            background: rgba(255, 255, 255, 0.03);
            border: 2px solid rgba(255, 255, 255, 0.07);
            border-radius: 14px;
            padding: 14px 16px;
            cursor: pointer;
            transition: all 0.2s;
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .pay-method:hover {
            border-color: rgba(31, 104, 249, 0.4);
        }

        .pay-method.selected {
            border-color: #1f68f9;
            background: rgba(31, 104, 249, 0.08);
        }

        .pay-method p.name {
            font-size: 0.82rem;
            font-weight: 800;
            color: white;
        }

        .pay-method p.sub {
            font-size: 0.65rem;
            font-weight: 700;
            color: #64748b;
            margin-top: 1px;
        }

        .pay-method.selected p.name {
            color: #60a5fa;
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
        <nav class="flex-1 px-3 space-y-2 overflow-y-auto">
            <a href="dashboard_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">dashboard</span>
                <span class="nav-label text-sm">Dashboard</span>
            </a>
            <a href="entry.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">login</span>
                <span class="nav-label text-sm">Vehicle Entry</span>
            </a>
            <a href="exitvehicle.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
                <span class="material-symbols-outlined shrink-0">logout</span>
                <span class="nav-label text-sm">Vehicle Exit</span>
            </a>
            <a href="parking.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">local_parking</span>
                <span class="nav-label text-sm">Active Parking</span>
            </a>
            <a href="advancereservation.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">event_available</span>
                <span class="nav-label text-sm">Reservation</span>
            </a>
            <a href="paymenthistory_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">payments</span>
                <span class="nav-label text-sm">Payments</span>
            </a>
            <a href="receipt.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">receipt_long</span>
                <span class="nav-label text-sm">Receipts</span>
            </a>
            <a href="walletoverview.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">account_balance_wallet</span>
                <span class="nav-label text-sm">Wallet</span>
            </a>
            <a href="notification_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">notifications</span>
                <span class="nav-label text-sm">Notifications</span>
            </a>
            <a href="accountsetting_user.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">settings</span>
                <span class="nav-label text-sm">Account Settings</span>
            </a>
        </nav>
        <div class="p-4 border-t border-white/5">
            <button onclick="window.location.href='login.html'"
                    class="flex items-center w-full px-4 py-4 text-rose-500 hover:bg-rose-500/10 rounded-xl text-sm font-black transition-all">
                <span class="material-symbols-outlined shrink-0"><a href="logout.html">power_settings_new</a></span><span
                    class="nav-label"><a href="logout.html">logout</a></span>
            </button>
        </div>
    </aside>

    <main class="flex-1 flex flex-col overflow-hidden bg-subtle-radial">
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div>
                <h2 class="text-xl font-black text-white">Vehicle Exit</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Process departure
                    & collect payment</p>
            </div>
            <div class="flex items-center gap-4">
                <div class="text-right hidden xl:block">
                    <p class="text-xs font-black text-white uppercase tracking-widest">Maria Santos</p>
                    <p class="text-[10px] text-primary font-bold uppercase tracking-tighter">User</p>
                </div>
                <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-emerald-400 to-primary p-[2px]">
                    <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-2xl mx-auto space-y-8">

                <!-- Step 1: Plate lookup -->
                <div class="glass-card rounded-[2.5rem] p-8" id="step1">
                    <h3 class="text-base font-black text-white mb-2">Step 1 — Enter Plate Number</h3>
                    <p class="text-[11px] text-slate-500 font-bold mb-6">Look up the vehicle that is departing the
                        facility.</p>
                    <input id="plateField" type="text" placeholder="e.g. ABC-1234"
                           class="pt-3 rounded-3xl plate-input mb-4 text-sm font-bold" oninput="this.value=this.value.toUpperCase()"
                           onkeydown="if(event.key==='Enter')lookupPlate()" />
                    <div class="flex gap-3">
                        <button onclick="lookupPlate()"
                                class="flex-1 flex items-center justify-center gap-2 bg-rose-500 text-white font-black py-3.5 rounded-2xl hover:bg-rose-600 transition-all text-sm uppercase tracking-widest shadow-[0_0_20px_rgba(239,68,68,0.2)]">
                            <span class="material-symbols-outlined text-lg">search</span> Look Up
                        </button>
                        <button
                                class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black px-5 py-3.5 rounded-2xl hover:bg-white/10 transition-all text-sm">
                            <span class="material-symbols-outlined text-lg">qr_code_scanner</span> Scan
                        </button>
                    </div>
                </div>

                <!-- Step 2: Session summary (hidden) -->
                <div class="glass-card rounded-[2.5rem] p-8 hidden border border-rose-500/10" id="sessionCard">
                    <h3 class="text-base font-black text-white mb-5">Step 2 — Session Summary</h3>

                    <!-- Vehicle strip -->
                    <div class="flex items-center gap-4 p-5 rounded-2xl bg-black/20 border border-white/5 mb-6">
                        <div
                                class="h-11 w-11 rounded-xl bg-primary/10 border border-primary/20 flex items-center justify-center shrink-0">
                            <span class="material-symbols-outlined text-primary">directions_car</span>
                        </div>
                        <div class="flex-1">
                            <p class="text-xl font-black text-white tracking-widest">ABC-1234</p>
                            <p class="text-slate-400 text-xs font-bold">Toyota Prius · Silver · Kamal Perera</p>
                        </div>
                        <div class="text-right">
                            <p class="text-[9px] font-black uppercase tracking-widest text-slate-500">Slot</p>
                            <p class="text-white font-black text-sm">A-12</p>
                        </div>
                    </div>

                    <!-- Summary rows -->
                    <div class="mb-6">
                        <div class="meta-row"><span class="meta-lbl">Entry Time</span><span class="meta-val">09 Mar
                                    2026 · 10:45 AM</span></div>
                        <div class="meta-row"><span class="meta-lbl">Exit Time</span><span
                                class="meta-val text-emerald-400" id="exitTimeDisplay">—</span></div>
                        <div class="meta-row"><span class="meta-lbl">Duration</span><span class="meta-val">2h
                                    18m</span></div>
                        <div class="meta-row"><span class="meta-lbl">Rate</span><span class="meta-val">$5.50 /
                                    hr</span></div>
                        <div class="meta-row border-t border-white/10 pt-3 mt-1">
                            <span class="text-sm font-black text-white">Total Due</span>
                            <span class="text-2xl font-black text-white">$12.65</span>
                        </div>
                    </div>

                    <!-- Payment method -->
                    <h4 class="text-sm font-black text-white mb-3">Step 3 — Payment Method</h4>
                    <div class="grid grid-cols-1 gap-3 mb-6">
                        <div class="pay-method selected" onclick="selectPay(this)">
                            <div
                                    class="h-9 w-9 rounded-xl bg-emerald-500/10 flex items-center justify-center shrink-0">
                                    <span
                                            class="material-symbols-outlined text-emerald-400 text-lg">account_balance_wallet</span>
                            </div>
                            <div class="flex-1">
                                <p class="name">Parkiyo Wallet</p>
                                <p class="sub">Balance: $48.50 · Sufficient</p>
                            </div>
                            <span
                                    class="text-[10px] font-black text-emerald-400 bg-emerald-500/10 border border-emerald-500/20 px-2 py-0.5 rounded-lg">Recommended</span>
                        </div>
                        <div class="pay-method" onclick="selectPay(this)">
                            <div class="h-9 w-9 rounded-xl bg-primary/10 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-primary text-lg">credit_card</span>
                            </div>
                            <div>
                                <p class="name">Card / Tap to Pay</p>
                                <p class="sub">Visa, Mastercard, contactless</p>
                            </div>
                        </div>
                        <div class="pay-method" onclick="selectPay(this)">
                            <div
                                    class="h-9 w-9 rounded-xl bg-amber-500/10 flex items-center justify-center shrink-0">
                                <span class="material-symbols-outlined text-amber-400 text-lg">payments</span>
                            </div>
                            <div>
                                <p class="name">Cash</p>
                                <p class="sub">Collect at booth</p>
                            </div>
                        </div>
                    </div>

                    <button onclick="confirmExit()"
                            class="w-full flex items-center justify-center gap-3 bg-rose-500 text-white font-black py-4 rounded-2xl hover:bg-rose-600 transition-all text-base shadow-[0_0_25px_rgba(239,68,68,0.25)]">
                        <span class="material-symbols-outlined text-xl">logout</span> Confirm Exit & Collect $12.65
                    </button>
                </div>

                <!-- Success state -->
                <div id="successCard" class="hidden">
                    <div class="glass-card rounded-[2.5rem] p-8 border border-emerald-500/20 bg-emerald-500/5">
                        <div class="flex items-center gap-4 mb-6">
                            <div
                                    class="h-14 w-14 rounded-2xl bg-emerald-500/15 border border-emerald-500/25 flex items-center justify-center shrink-0">
                                    <span
                                            class="material-symbols-outlined text-emerald-400 text-3xl">check_circle</span>
                            </div>
                            <div>
                                <p class="text-xl font-black text-white">Exit Confirmed!</p>
                                <p class="text-emerald-400 text-sm font-bold mt-0.5">Payment collected · Slot A-12
                                    is now free</p>
                            </div>
                            <span class="ml-auto text-[10px] font-black uppercase tracking-widest text-slate-500"
                                  id="exitTimestamp"></span>
                        </div>
                        <div class="grid grid-cols-2 gap-3 mb-6">
                            <div class="p-4 rounded-2xl bg-black/20 border border-white/5">
                                <p class="text-[9px] font-black text-slate-500 uppercase tracking-wider mb-1">Plate
                                </p>
                                <p class="text-white font-black tracking-widest">ABC-1234</p>
                            </div>
                            <div class="p-4 rounded-2xl bg-black/20 border border-white/5">
                                <p class="text-[9px] font-black text-slate-500 uppercase tracking-wider mb-1">Amount
                                    Paid</p>
                                <p class="text-emerald-400 font-black text-lg">$12.65</p>
                            </div>
                            <div class="p-4 rounded-2xl bg-black/20 border border-white/5">
                                <p class="text-[9px] font-black text-slate-500 uppercase tracking-wider mb-1">Method
                                </p>
                                <p class="text-white font-black">Parkiyo Wallet</p>
                            </div>
                            <div class="p-4 rounded-2xl bg-black/20 border border-white/5">
                                <p class="text-[9px] font-black text-slate-500 uppercase tracking-wider mb-1">
                                    Duration</p>
                                <p class="text-white font-black">2h 18m</p>
                            </div>
                        </div>
                        <div class="flex gap-3">
                            <a href="receipt.html"
                               class="flex-1 flex items-center justify-center gap-2 bg-primary/10 border border-primary/20 text-primary font-black py-3 rounded-2xl hover:bg-primary/20 transition-all text-sm">
                                <span class="material-symbols-outlined text-base">receipt_long</span> View Receipt
                            </a>
                            <button onclick="resetForm()"
                                    class="flex-1 flex items-center justify-center gap-2 bg-white/5 border border-white/10 text-slate-300 font-black py-3 rounded-2xl hover:bg-white/10 transition-all text-sm">
                                <span class="material-symbols-outlined text-base">add</span> New Exit
                            </button>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </main>
</div>
<script>
    let selectedPayEl = document.querySelector('.pay-method.selected');

    function lookupPlate() {
        const val = document.getElementById('plateField').value.trim();
        if (!val) return;
        const now = new Date();
        document.getElementById('exitTimeDisplay').textContent = now.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
        document.getElementById('sessionCard').classList.remove('hidden');
        document.getElementById('sessionCard').scrollIntoView({ behavior: 'smooth', block: 'start' });
    }

    function selectPay(el) {
        document.querySelectorAll('.pay-method').forEach(p => p.classList.remove('selected'));
        el.classList.add('selected');
    }

    function confirmExit() {
        document.getElementById('sessionCard').classList.add('hidden');
        document.getElementById('successCard').classList.remove('hidden');
        const now = new Date();
        document.getElementById('exitTimestamp').textContent = now.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
        document.getElementById('successCard').scrollIntoView({ behavior: 'smooth' });
    }

    function resetForm() {
        document.getElementById('plateField').value = '';
        document.getElementById('sessionCard').classList.add('hidden');
        document.getElementById('successCard').classList.add('hidden');
        window.scrollTo({ top: 0, behavior: 'smooth' });
        document.getElementById('plateField').focus();
    }
</script>
</body>

</html>