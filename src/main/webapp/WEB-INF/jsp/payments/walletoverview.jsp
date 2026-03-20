<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Wallet Overview</title>
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

        label.field-label {
            font-size: 0.7rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.1em;
            color: #64748b;
            margin-bottom: 6px;
            display: block;
        }

        .txn-in {
            color: #34d399;
        }

        .txn-out {
            color: #f87171;
        }

        .pill-topup {
            background: rgba(16, 185, 129, 0.1);
            color: #34d399;
            border: 1px solid rgba(16, 185, 129, 0.2);
        }

        .pill-payment {
            background: rgba(239, 68, 68, 0.1);
            color: #f87171;
            border: 1px solid rgba(239, 68, 68, 0.2);
        }

        .pill-refund {
            background: rgba(100, 116, 139, 0.1);
            color: #94a3b8;
            border: 1px solid rgba(100, 116, 139, 0.2);
        }

        .type-pill {
            padding: 3px 11px;
            border-radius: 9px;
            font-size: 0.67rem;
            font-weight: 800;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .txn-row {
            transition: background 0.15s;
        }

        .txn-row:hover {
            background: rgba(255, 255, 255, 0.02);
        }

        .topup-amount {
            background: rgba(255, 255, 255, 0.04);
            border: 2px solid rgba(255, 255, 255, 0.08);
            border-radius: 14px;
            padding: 14px;
            cursor: pointer;
            transition: all 0.2s;
            text-align: center;
            font-weight: 900;
            font-size: 1rem;
            color: #94a3b8;
        }

        .topup-amount:hover {
            border-color: rgba(31, 104, 249, 0.4);
            color: white;
        }

        .topup-amount.selected {
            border-color: #1f68f9;
            background: rgba(31, 104, 249, 0.1);
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

    <aside class="sidebar-container border-r border-white/5 premium-blur flex flex-col shrink-0 z-50">
        <div class="p-6 mb-4 flex items-center">
            <div
                    class="flex h-10 w-10 shrink-0 items-center justify-center rounded-[14px] bg-primary text-white shadow-[0_0_15px_rgba(31,104,249,0.3)]">
                <span class="material-symbols-outlined font-bold text-xl">local_parking</span>
            </div>
            <span class="nav-label text-xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </div>
        <nav class="flex-1 px-3 space-y-2 overflow-y-auto">
            <a href="/dashboard_user"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">dashboard</span>
                <span class="nav-label text-sm">Dashboard</span>
            </a>
            <a href="/entry"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">login</span>
                <span class="nav-label text-sm">Vehicle Entry</span>
            </a>
            <a href="/exitvehicle"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">logout</span>
                <span class="nav-label text-sm">Vehicle Exit</span>
            </a>
            <a href="/parking"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">local_parking</span>
                <span class="nav-label text-sm">Active Parking</span>
            </a>
            <a href="/advancereservation"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">event_available</span>
                <span class="nav-label text-sm">Reservation</span>
            </a>
            <a href="/paymenthistory_user"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">payments</span>
                <span class="nav-label text-sm">Payments</span>
            </a>
            <a href="/receipt"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">receipt_long</span>
                <span class="nav-label text-sm">Receipts</span>
            </a>
            <a href="/walletoverview"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold group">
                <span class="material-symbols-outlined shrink-0">account_balance_wallet</span>
                <span class="nav-label text-sm">Wallet</span>
            </a>
            <a href="/notification_user"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">notifications</span>
                <span class="nav-label text-sm">Notifications</span>
            </a>
            <a href="/accountsetting_user"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all group">
                <span class="material-symbols-outlined shrink-0">settings</span>
                <span class="nav-label text-sm">Account Settings</span>
            </a>
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
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div>
                <h2 class="text-xl font-black text-white">Parkiyo Wallet</h2>
                <p class="text-[11px] uppercase tracking-[0.18em] text-slate-500 font-black mt-1">Balance & top-up
                </p>
            </div>
            <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                    <span class="material-symbols-outlined text-white/50">person</span>
                </div>
            </div>
        </header>

        <div class="flex-1 overflow-y-auto p-10">
            <div class="max-w-4xl mx-auto space-y-8">

                <!-- Wallet hero card -->
                <div class="rounded-[2.5rem] p-8 relative overflow-hidden"
                     style="background:linear-gradient(135deg,#1a3a6e 0%,#1f68f9 50%,#3b82f6 100%);">
                    <div class="absolute inset-0 pointer-events-none"
                         style="background:radial-gradient(circle at 80% 50%,rgba(255,255,255,0.08) 0%,transparent 60%)">
                    </div>
                    <div class="relative z-10">
                        <div class="flex items-center justify-between mb-8">
                            <div class="flex items-center gap-3">
                                <div class="h-10 w-10 rounded-xl bg-white/15 flex items-center justify-center">
                                        <span
                                                class="material-symbols-outlined text-white text-xl">account_balance_wallet</span>
                                </div>
                                <span class="text-white/80 font-black text-sm uppercase tracking-widest">Parkiyo
                                        Wallet</span>
                            </div>
                            <span
                                    class="text-[10px] font-black uppercase tracking-widest text-white/50 bg-white/10 px-3 py-1.5 rounded-lg">Active</span>
                        </div>
                        <p class="text-white/60 text-xs font-black uppercase tracking-widest mb-2">Available Balance
                        </p>
                        <p class="text-6xl font-black text-white mb-6">$48.50</p>
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-white/50 text-[10px] font-black uppercase tracking-widest">Wallet ID
                                </p>
                                <p class="text-white/80 font-mono text-sm font-bold mt-0.5">PKW-00441-KP</p>
                            </div>
                            <div class="text-right">
                                <p class="text-white/50 text-[10px] font-black uppercase tracking-widest">Owner</p>
                                <p class="text-white font-black text-sm mt-0.5">Kamal Perera</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Quick actions -->
                <div class="grid grid-cols-3 gap-4">
                    <button onclick="document.getElementById('topupPanel').scrollIntoView({behavior:'smooth'})"
                            class="glass-card p-5 rounded-2xl flex flex-col items-center gap-2 hover:bg-white/5 transition-all">
                        <div
                                class="h-11 w-11 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-emerald-400">add_circle</span>
                        </div>
                        <p class="text-xs font-black text-white">Top Up</p>
                    </button>
                    <a href="/paymenthistory"
                       class="glass-card p-5 rounded-2xl flex flex-col items-center gap-2 hover:bg-white/5 transition-all">
                        <div
                                class="h-11 w-11 rounded-xl bg-primary/10 border border-primary/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-primary">history</span>
                        </div>
                        <p class="text-xs font-black text-white">History</p>
                    </a>
                    <button
                            class="glass-card p-5 rounded-2xl flex flex-col items-center gap-2 hover:bg-white/5 transition-all">
                        <div
                                class="h-11 w-11 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center">
                            <span class="material-symbols-outlined text-amber-400">download</span>
                        </div>
                        <p class="text-xs font-black text-white">Statement</p>
                    </button>
                </div>

                <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">

                    <!-- Top up panel -->
                    <div class="glass-card rounded-[2.5rem] p-8" id="topupPanel">
                        <h3 class="text-base font-black text-white mb-6">Top Up Wallet</h3>
                        <p class="text-[11px] text-slate-500 font-bold mb-5">Select an amount or enter a custom
                            value.</p>
                        <div class="grid grid-cols-3 gap-3 mb-5">
                            <div class="topup-amount" onclick="selectTopup(this,'10')">$10</div>
                            <div class="topup-amount" onclick="selectTopup(this,'20')">$20</div>
                            <div class="topup-amount selected" onclick="selectTopup(this,'50')">$50</div>
                            <div class="topup-amount" onclick="selectTopup(this,'100')">$100</div>
                            <div class="topup-amount" onclick="selectTopup(this,'200')">$200</div>
                            <div class="topup-amount" onclick="selectTopup(this,'custom')" id="customBtn">Custom
                            </div>
                        </div>
                        <div id="customInput" class="hidden mb-5">
                            <label class="field-label">Custom Amount ($)</label>
                            <input type="number" placeholder="e.g. 75" class="input-glass bg-white/5 px-4 h-12 rounded-2xl"
                                   min="1" max="500" />
                        </div>
                        <div class="mb-6">
                            <label class="field-label">Payment Method</label>
                            <select class="input-glass px-4 h-12 rounded-2xl">
                                <option>Credit / Debit Card</option>
                                <option>Online Banking</option>
                            </select>
                        </div>
                        <div
                                class="p-4 rounded-2xl bg-emerald-500/5 border border-emerald-500/15 flex items-center justify-between mb-5">
                            <p class="text-sm font-black text-white">New Balance After Top-Up</p>
                            <p class="text-xl font-black text-emerald-400" id="newBalance">$98.50</p>
                        </div>
                        <button onclick="topupDone()"
                                class="w-full flex items-center justify-center gap-2 bg-emerald-500 text-white font-black py-4 rounded-2xl hover:bg-emerald-600 transition-all text-sm shadow-[0_0_20px_rgba(16,185,129,0.3)]">
                            <span class="material-symbols-outlined text-lg">add_circle</span>
                            Top Up <span id="topupBtnAmt">$50.00</span>
                        </button>
                        <div id="topupSuccess"
                             class="hidden mt-4 flex items-center gap-3 p-4 rounded-2xl bg-emerald-500/10 border border-emerald-500/20">
                            <span class="material-symbols-outlined text-emerald-400">check_circle</span>
                            <p class="text-sm font-black text-emerald-300">Top-up successful! Balance updated.</p>
                        </div>
                    </div>

                    <!-- Recent wallet activity -->
                    <div class="glass-card rounded-[2.5rem] p-8">
                        <h3 class="text-base font-black text-white mb-6">Recent Activity</h3>
                        <div class="space-y-0 divide-y divide-white/5">
                            <div class="txn-row py-4 flex items-center gap-4">
                                <div
                                        class="h-10 w-10 rounded-xl bg-emerald-500/10 flex items-center justify-center shrink-0">
                                    <span class="material-symbols-outlined text-emerald-400 text-lg">add</span>
                                </div>
                                <div class="flex-1">
                                    <p class="text-sm font-black text-white">Top-Up</p>
                                    <p class="text-[10px] text-slate-500 font-bold">08 Mar · Card ending 4512</p>
                                </div>
                                <span class="text-emerald-400 font-black">+$50.00</span>
                            </div>
                            <div class="txn-row py-4 flex items-center gap-4">
                                <div
                                        class="h-10 w-10 rounded-xl bg-rose-500/10 flex items-center justify-center shrink-0">
                                        <span
                                                class="material-symbols-outlined text-rose-400 text-lg">local_parking</span>
                                </div>
                                <div class="flex-1">
                                    <p class="text-sm font-black text-white">Parking · XYZ-8899</p>
                                    <p class="text-[10px] text-slate-500 font-bold">10 Mar · RES-20260311-047</p>
                                </div>
                                <span class="text-rose-400 font-black">-$16.50</span>
                            </div>
                            <div class="txn-row py-4 flex items-center gap-4">
                                <div
                                        class="h-10 w-10 rounded-xl bg-rose-500/10 flex items-center justify-center shrink-0">
                                        <span
                                                class="material-symbols-outlined text-rose-400 text-lg">local_parking</span>
                                </div>
                                <div class="flex-1">
                                    <p class="text-sm font-black text-white">Parking · GHI-7721</p>
                                    <p class="text-[10px] text-slate-500 font-bold">08 Mar · Walk-in</p>
                                </div>
                                <span class="text-rose-400 font-black">-$13.75</span>
                            </div>
                            <div class="txn-row py-4 flex items-center gap-4">
                                <div
                                        class="h-10 w-10 rounded-xl bg-slate-700/30 flex items-center justify-center shrink-0">
                                    <span class="material-symbols-outlined text-slate-400 text-lg">undo</span></div>
                                <div class="flex-1">
                                    <p class="text-sm font-black text-white">Refund · TRK-5502</p>
                                    <p class="text-[10px] text-slate-500 font-bold">05 Mar · TXN-88281044</p>
                                </div>
                                <span class="text-slate-300 font-black">+$14.75</span>
                            </div>
                            <div class="txn-row py-4 flex items-center gap-4">
                                <div
                                        class="h-10 w-10 rounded-xl bg-emerald-500/10 flex items-center justify-center shrink-0">
                                    <span class="material-symbols-outlined text-emerald-400 text-lg">add</span>
                                </div>
                                <div class="flex-1">
                                    <p class="text-sm font-black text-white">Top-Up</p>
                                    <p class="text-[10px] text-slate-500 font-bold">01 Mar · Online Banking</p>
                                </div>
                                <span class="text-emerald-400 font-black">+$100.00</span>
                            </div>
                            <div class="txn-row py-4 flex items-center gap-4">
                                <div
                                        class="h-10 w-10 rounded-xl bg-rose-500/10 flex items-center justify-center shrink-0">
                                        <span
                                                class="material-symbols-outlined text-rose-400 text-lg">local_parking</span>
                                </div>
                                <div class="flex-1">
                                    <p class="text-sm font-black text-white">Parking · ABC-1234</p>
                                    <p class="text-[10px] text-slate-500 font-bold">28 Feb · Walk-in</p>
                                </div>
                                <span class="text-rose-400 font-black">-$18.75</span>
                            </div>
                        </div>
                        <a href="/paymenthistory"
                           class="mt-5 flex items-center gap-2 text-primary text-xs font-black hover:brightness-125 transition-all">
                            <span class="material-symbols-outlined text-base">open_in_new</span> View full
                            transaction history
                        </a>
                    </div>
                </div>

            </div>
        </div>
    </main>
</div>

<script>
    let selectedAmt = 50;

    function selectTopup(el, val) {
        document.querySelectorAll('.topup-amount').forEach(a => a.classList.remove('selected'));
        el.classList.add('selected');
        if (val == 'custom') {
            document.getElementById('customInput').classList.remove('hidden');
            selectedAmt = 0;
        } else {
            document.getElementById('customInput').classList.add('hidden');
            selectedAmt = parseFloat(val);
            document.getElementById('newBalance').textContent = '$' + (48.50 + selectedAmt).toFixed(2);
            document.getElementById('topupBtnAmt').textContent = '$' + selectedAmt.toFixed(2);
        }
    }

    function topupDone() {
        document.getElementById('topupSuccess').classList.remove('hidden');
        document.getElementById('topupSuccess').style.display = 'flex';
        setTimeout(() => document.getElementById('topupSuccess').classList.add('hidden'), 3500);
    }
</script>
</body>

</html>