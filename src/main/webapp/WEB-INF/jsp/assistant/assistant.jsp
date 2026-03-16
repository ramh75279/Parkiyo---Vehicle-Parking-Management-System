<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | AI Assistant</title>
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

        /* Chat bubbles */
        .bubble-ai {
            background: rgba(255, 255, 255, 0.04);
            border: 1px solid rgba(255, 255, 255, 0.08);
            border-radius: 0 20px 20px 20px;
            padding: 14px 18px;
            max-width: 78%;
            font-size: 0.875rem;
            font-weight: 500;
            line-height: 1.6;
            color: #cbd5e1;
        }

        .bubble-user {
            background: rgba(31, 104, 249, 0.15);
            border: 1px solid rgba(31, 104, 249, 0.25);
            border-radius: 20px 0 20px 20px;
            padding: 14px 18px;
            max-width: 78%;
            font-size: 0.875rem;
            font-weight: 600;
            line-height: 1.6;
            color: #e2e8f0;
            margin-left: auto;
        }

        /* Typing dots */
        .typing-dot {
            width: 7px;
            height: 7px;
            border-radius: 50%;
            background: #475569;
            animation: typing-bounce 1.2s ease-in-out infinite;
        }

        .typing-dot:nth-child(2) {
            animation-delay: 0.2s;
        }

        .typing-dot:nth-child(3) {
            animation-delay: 0.4s;
        }

        @keyframes typing-bounce {

            0%,
            60%,
            100% {
                transform: translateY(0)
            }

            30% {
                transform: translateY(-7px)
            }
        }

        /* Suggestion chips */
        .chip {
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid rgba(255, 255, 255, 0.08);
            border-radius: 100px;
            padding: 8px 16px;
            font-size: 0.72rem;
            font-weight: 700;
            color: #94a3b8;
            cursor: pointer;
            transition: all 0.2s;
            white-space: nowrap;
        }

        .chip:hover {
            background: rgba(31, 104, 249, 0.08);
            border-color: rgba(31, 104, 249, 0.3);
            color: #60a5fa;
        }

        /* Compose input */
        .compose-input {
            background: rgba(255, 255, 255, 0.04);
            border: 1.5px solid rgba(255, 255, 255, 0.1);
            color: white;
            border-radius: 18px;
            padding: 14px 56px 14px 18px;
            font-size: 0.875rem;
            font-weight: 600;
            transition: all 0.2s;
            outline: none;
            width: 100%;
            resize: none;
            min-height: 52px;
            max-height: 140px;
            line-height: 1.5;
        }

        .compose-input:focus {
            border-color: rgba(31, 104, 249, 0.5);
            box-shadow: 0 0 0 3px rgba(31, 104, 249, 0.08);
        }

        .compose-input::placeholder {
            color: rgba(255, 255, 255, 0.2);
        }

        /* AI avatar glow */
        .ai-avatar {
            background: linear-gradient(135deg, #1f68f9, #3b82f6);
            box-shadow: 0 0 16px rgba(31, 104, 249, 0.4);
        }

        .ai-avatar-pulse {
            animation: avatar-glow 2.5s ease-in-out infinite;
        }

        @keyframes avatar-glow {

            0%,
            100% {
                box-shadow: 0 0 12px rgba(31, 104, 249, 0.3)
            }

            50% {
                box-shadow: 0 0 24px rgba(31, 104, 249, 0.6)
            }
        }

        /* Action cards */
        .action-card {
            background: rgba(255, 255, 255, 0.025);
            border: 1px solid rgba(255, 255, 255, 0.07);
            border-radius: 16px;
            padding: 14px 16px;
            cursor: pointer;
            transition: all 0.2s;
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .action-card:hover {
            background: rgba(31, 104, 249, 0.06);
            border-color: rgba(31, 104, 249, 0.25);
            transform: translateY(-1px);
        }

        /* Message fade-in */
        .msg-appear {
            animation: msg-in 0.3s ease-out forwards;
        }

        @keyframes msg-in {
            from {
                opacity: 0;
                transform: translateY(8px)
            }

            to {
                opacity: 1;
                transform: translateY(0)
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
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
                    class="material-symbols-outlined shrink-0">local_parking</span><span
                    class="nav-label text-sm">Active Parking</span></a>
            <a href="advancereservation.html"
               class="flex items-center px-4 py-4 text-slate-400 hover:text-white hover:bg-white/5 rounded-xl text-sm font-bold transition-all"><span
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
            <a href="assistant.html"
               class="flex items-center px-4 py-4 rounded-xl text-primary bg-primary/10 border-r-4 border-primary font-bold"><span
                    class="material-symbols-outlined shrink-0">smart_toy</span><span class="nav-label text-sm">AI
                        Assistant</span></a>
            <a href="accountsetting-user.html"
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
        <!-- Header -->
        <header
                class="h-20 border-b border-white/5 flex items-center justify-between px-10 bg-background-dark/30 premium-blur shrink-0">
            <div class="flex items-center gap-4">
                <div
                        class="h-10 w-10 rounded-[14px] ai-avatar ai-avatar-pulse flex items-center justify-center shrink-0">
                    <span class="material-symbols-outlined text-white text-lg">smart_toy</span>
                </div>
                <div>
                    <h2 class="text-xl font-black text-white">Parky <span
                            class="text-[11px] text-primary font-black uppercase tracking-widest ml-1 bg-primary/10 border border-primary/20 px-2 py-0.5 rounded-lg">AI</span>
                    </h2>
                    <div class="flex items-center gap-2 mt-0.5">
                        <span class="h-2 w-2 rounded-full bg-emerald-400 inline-block"></span>
                        <p class="text-[10px] text-emerald-400 font-black uppercase tracking-wider">Online · Ready
                            to help</p>
                    </div>
                </div>
            </div>
            <div class="flex items-center gap-3">
                <button onclick="clearChat()"
                        class="flex items-center gap-2 bg-white/5 border border-white/10 text-slate-400 font-black px-5 py-2.5 rounded-xl hover:bg-white/10 transition-all text-xs uppercase tracking-widest">
                    <span class="material-symbols-outlined text-base">refresh</span> New Chat
                </button>
                <div class="h-10 w-10 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[2px]">
                    <div class="h-full w-full rounded-[14px] bg-background-dark flex items-center justify-center">
                        <span class="material-symbols-outlined text-white/50">person</span>
                    </div>
                </div>
            </div>
        </header>

        <!-- Chat area -->
        <div class="flex-1 flex overflow-hidden">
            <div class="flex-1 flex flex-col overflow-hidden">

                <!-- Messages -->
                <div class="flex-1 overflow-y-auto px-8 py-8 space-y-6" id="chatMessages">

                    <!-- Welcome message -->
                    <div class="flex gap-4 msg-appear" id="welcomeMsg">
                        <div
                                class="h-9 w-9 rounded-[14px] ai-avatar flex items-center justify-center shrink-0 mt-1">
                            <span class="material-symbols-outlined text-white text-base">smart_toy</span>
                        </div>
                        <div class="flex-1 max-w-2xl">
                            <div class="flex items-center gap-2 mb-2">
                                    <span class="text-xs font-black text-primary uppercase tracking-wider">Parky
                                        AI</span>
                                <span class="text-[10px] text-slate-600 font-bold">Just now</span>
                            </div>
                            <div class="bubble-ai">
                                <p class="mb-2">👋 Hi there, <strong class="text-white">Kamal</strong>! I'm <strong
                                        class="text-white">Parky</strong>, your Parkiyo AI assistant.</p>
                                <p>I can help you with reservations, check slot availability, look up payment
                                    history, process entries and exits, answer parking policy questions, and much
                                    more. What can I do for you today?</p>
                            </div>
                            <!-- Action shortcut cards -->
                            <div class="mt-3 grid grid-cols-2 gap-2">
                                <div class="action-card" onclick="sendQuick('Check available slots right now')">
                                        <span
                                                class="material-symbols-outlined text-emerald-400 text-lg shrink-0">grid_view</span>
                                    <div>
                                        <p class="text-xs font-black text-white">Slot Availability</p>
                                        <p class="text-[9px] text-slate-500 font-bold">See free slots now</p>
                                    </div>
                                </div>
                                <div class="action-card" onclick="sendQuick('Make a reservation for tomorrow')">
                                        <span
                                                class="material-symbols-outlined text-primary text-lg shrink-0">event_available</span>
                                    <div>
                                        <p class="text-xs font-black text-white">Book a Slot</p>
                                        <p class="text-[9px] text-slate-500 font-bold">Reserve in advance</p>
                                    </div>
                                </div>
                                <div class="action-card" onclick="sendQuick('Show my recent payments')">
                                        <span
                                                class="material-symbols-outlined text-amber-400 text-lg shrink-0">payments</span>
                                    <div>
                                        <p class="text-xs font-black text-white">Payment History</p>
                                        <p class="text-[9px] text-slate-500 font-bold">Recent transactions</p>
                                    </div>
                                </div>
                                <div class="action-card" onclick="sendQuick('What are the parking rates?')">
                                        <span
                                                class="material-symbols-outlined text-violet-400 text-lg shrink-0">info</span>
                                    <div>
                                        <p class="text-xs font-black text-white">Pricing Info</p>
                                        <p class="text-[9px] text-slate-500 font-bold">Rates & policies</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Suggestion chips -->
                <div class="px-8 pb-4 flex gap-2 overflow-x-auto shrink-0" id="chips" style="scrollbar-width:none;">
                    <div class="chip" onclick="sendQuick('How many slots are available in Zone A?')">Zone A
                        availability</div>
                    <div class="chip" onclick="sendQuick('What is my wallet balance?')">Wallet balance</div>
                    <div class="chip" onclick="sendQuick('Show my active parking session')">Active session</div>
                    <div class="chip" onclick="sendQuick('How do I cancel a reservation?')">Cancel reservation</div>
                    <div class="chip" onclick="sendQuick('What is the daily maximum rate?')">Daily max rate</div>
                    <div class="chip" onclick="sendQuick('Help me top up my wallet')">Top up wallet</div>
                </div>

                <!-- Compose bar -->
                <div class="px-8 pb-8 shrink-0">
                    <div class="relative">
                            <textarea id="composeInput" class="compose-input"
                                      placeholder="Ask Parky anything about parking, reservations, payments…" rows="1"
                                      onkeydown="if(event.key==='Enter'&&!event.shiftKey){event.preventDefault();sendMessage();}"
                                      oninput="autoResize(this)"></textarea>
                        <button onclick="sendMessage()"
                                class="absolute right-3 bottom-3 h-9 w-9 rounded-xl bg-primary flex items-center justify-center hover:bg-primary/80 transition-all shadow-[0_0_12px_rgba(31,104,249,0.4)]">
                            <span class="material-symbols-outlined text-white text-lg">send</span>
                        </button>
                    </div>
                    <p class="text-center text-[10px] text-slate-600 font-bold mt-2">Parky AI · Powered by Parkiyo
                        Intelligence · Not a substitute for live operator assistance</p>
                </div>
            </div>

            <!-- Side panel — context -->
            <div
                    class="w-64 border-l border-white/5 flex flex-col bg-background-dark/30 premium-blur shrink-0 p-6 overflow-y-auto">
                <h3 class="text-xs font-black uppercase tracking-widest text-slate-500 mb-4">Your Context</h3>
                <div class="space-y-3 mb-6">
                    <div class="glass-card rounded-xl p-3">
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Logged in as
                        </p>
                        <p class="text-sm font-black text-white">Kamal Perera</p>
                        <p class="text-[10px] text-slate-500 font-bold">Operator</p>
                    </div>
                    <div class="glass-card rounded-xl p-3">
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Wallet
                            Balance</p>
                        <p class="text-lg font-black text-emerald-400">$48.50</p>
                    </div>
                    <div class="glass-card rounded-xl p-3">
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Active
                            Sessions</p>
                        <p class="text-lg font-black text-primary">51</p>
                    </div>
                    <div class="glass-card rounded-xl p-3">
                        <p class="text-[9px] font-black uppercase tracking-widest text-slate-500 mb-1">Available
                            Slots</p>
                        <p class="text-lg font-black text-white">42 <span
                                class="text-[10px] text-slate-500 font-bold">/ 100</span></p>
                    </div>
                </div>

                <h3 class="text-xs font-black uppercase tracking-widest text-slate-500 mb-3">Quick Links</h3>
                <div class="space-y-2">
                    <a href="slot-overview.html" class="action-card text-xs"><span
                            class="material-symbols-outlined text-primary text-sm">map</span><span
                            class="text-slate-300 font-bold">Slot Map</span></a>
                    <a href="entry.html" class="action-card text-xs"><span
                            class="material-symbols-outlined text-emerald-400 text-sm">login</span><span
                            class="text-slate-300 font-bold">Process Entry</span></a>
                    <a href="exitvehicle.html" class="action-card text-xs"><span
                            class="material-symbols-outlined text-rose-400 text-sm">logout</span><span
                            class="text-slate-300 font-bold">Process Exit</span></a>
                    <a href="advancereservation.html" class="action-card text-xs"><span
                            class="material-symbols-outlined text-amber-400 text-sm">event_available</span><span
                            class="text-slate-300 font-bold">Reservations</span></a>
                </div>
            </div>
        </div>
    </main>
</div>

<script>
    const responses = {
        'Check available slots right now': {
            text: "Here's the current slot availability across all zones:",
            extra: `<div class="mt-3 grid grid-cols-2 gap-2">
                <div class="p-3 rounded-xl bg-emerald-500/8 border border-emerald-500/15 text-center" style="background:rgba(16,185,129,0.06)"><p class="text-lg font-black text-emerald-400">10</p><p class="text-[9px] text-slate-500 font-black uppercase">Zone A</p></div>
                <div class="p-3 rounded-xl bg-emerald-500/8 border border-emerald-500/15 text-center" style="background:rgba(16,185,129,0.06)"><p class="text-lg font-black text-emerald-400">15</p><p class="text-[9px] text-slate-500 font-black uppercase">Zone B</p></div>
                <div class="p-3 rounded-xl bg-emerald-500/8 border border-emerald-500/15 text-center" style="background:rgba(16,185,129,0.06)"><p class="text-lg font-black text-emerald-400">5</p><p class="text-[9px] text-slate-500 font-black uppercase">Zone C</p></div>
                <div class="p-3 rounded-xl bg-emerald-500/8 border border-emerald-500/15 text-center" style="background:rgba(16,185,129,0.06)"><p class="text-lg font-black text-emerald-400">12</p><p class="text-[9px] text-slate-500 font-black uppercase">Zone D</p></div>
            </div>
            <p class="mt-3 text-xs"><strong class="text-emerald-400">42 slots</strong> available right now out of 100 total. Would you like to <a href="slot-overview.html" class="text-primary underline">view the slot map</a> or make a reservation?</p>`
        },
        'Make a reservation for tomorrow': {
            text: "Sure! To book a slot for tomorrow, I'll need a few details. You can use the quick reservation form or I can guide you through it. Which vehicle would you like to reserve for?",
            extra: `<div class="mt-3 flex gap-2">
                <a href="advancereservation.html" class="flex-1 flex items-center justify-center gap-1.5 bg-primary/10 border border-primary/20 text-primary font-black py-2.5 rounded-xl text-xs hover:bg-primary/20 transition-all"><span class="material-symbols-outlined text-sm">add</span>New Reservation</a>
            </div>`
        },
        'Show my recent payments': {
            text: "Here are your last 3 transactions:",
            extra: `<div class="mt-3 space-y-2">
                <div class="flex items-center justify-between p-3 rounded-xl bg-white/[0.03] border border-white/6" style="border-color:rgba(255,255,255,0.06)"><div><p class="text-xs font-black text-white">XYZ-8899 · Reservation</p><p class="text-[9px] text-slate-500 font-bold">10 Mar 2026</p></div><span class="text-emerald-400 font-black text-sm">-$16.50</span></div>
                <div class="flex items-center justify-between p-3 rounded-xl bg-white/[0.03] border border-white/6" style="border-color:rgba(255,255,255,0.06)"><div><p class="text-xs font-black text-white">ABC-1234 · Walk-in</p><p class="text-[9px] text-slate-500 font-bold">09 Mar 2026</p></div><span class="text-emerald-400 font-black text-sm">-$18.75</span></div>
                <div class="flex items-center justify-between p-3 rounded-xl bg-white/[0.03] border border-white/6" style="border-color:rgba(255,255,255,0.06)"><div><p class="text-xs font-black text-white">GHI-7721 · Walk-in</p><p class="text-[9px] text-slate-500 font-bold">08 Mar 2026</p></div><span class="text-emerald-400 font-black text-sm">-$13.75</span></div>
            </div>
            <p class="mt-2 text-xs">Total spent this month: <strong class="text-white">$124.75</strong>. <a href="paymenthistory.html" class="text-primary underline">View full history →</a></p>`
        },
        'What are the parking rates?': {
            text: "Here's our current pricing structure:",
            extra: `<div class="mt-3 space-y-2">
                <div class="flex justify-between p-3 rounded-xl bg-white/[0.03] border border-white/6" style="border-color:rgba(255,255,255,0.06)"><span class="text-xs font-bold text-slate-400">Standard Slot</span><span class="text-xs font-black text-white">$5.50 / hr</span></div>
                <div class="flex justify-between p-3 rounded-xl bg-white/[0.03] border border-white/6" style="border-color:rgba(255,255,255,0.06)"><span class="text-xs font-bold text-slate-400">EV Charging</span><span class="text-xs font-black text-white">$6.00 / hr</span></div>
                <div class="flex justify-between p-3 rounded-xl bg-white/[0.03] border border-white/6" style="border-color:rgba(255,255,255,0.06)"><span class="text-xs font-bold text-slate-400">Oversized</span><span class="text-xs font-black text-white">$7.00 / hr</span></div>
                <div class="flex justify-between p-3 rounded-xl bg-white/[0.03] border border-white/6" style="border-color:rgba(255,255,255,0.06)"><span class="text-xs font-bold text-slate-400">Daily Maximum</span><span class="text-xs font-black text-emerald-400">$30.00 / day</span></div>
            </div>`
        },
        'How many slots are available in Zone A?': {
            text: "Zone A (Ground Floor) currently has <strong class='text-emerald-400'>10 available slots</strong> out of 25 total. 13 are occupied and 2 are under maintenance.",
            extra: `<div class="mt-3"><a href="slot-overview.html" class="flex items-center gap-2 text-primary text-xs font-black hover:brightness-125 transition-all"><span class="material-symbols-outlined text-sm">map</span>View Zone A on slot map →</a></div>`
        },
        'What is my wallet balance?': {
            text: "Your Parkiyo Wallet balance is currently <strong class='text-emerald-400 text-lg'>$48.50</strong>. Your wallet ID is PKW-00441-KP.",
            extra: `<div class="mt-3 flex gap-2"><a href="walletoverview.html" class="flex items-center gap-2 bg-emerald-500/10 border border-emerald-500/20 text-emerald-400 font-black px-4 py-2 rounded-xl text-xs hover:bg-emerald-500/15 transition-all"><span class="material-symbols-outlined text-sm">add_circle</span>Top Up Wallet</a></div>`
        },
        'Show my active parking session': {
            text: "You have <strong class='text-white'>51 active sessions</strong> facility-wide right now. Your most recent entry was <strong class='text-white'>ABC-1234</strong> at slot A-12 (entered 10:45 AM, 2h 14m ago).",
            extra: `<div class="mt-3"><a href="parking.html" class="flex items-center gap-2 text-primary text-xs font-black hover:brightness-125 transition-all"><span class="material-symbols-outlined text-sm">local_parking</span>View all active sessions →</a></div>`
        },
        'How do I cancel a reservation?': {
            text: "To cancel a reservation, go to <strong class='text-white'>Reservations</strong> from the sidebar, find the booking you want to cancel, and click the red × cancel button on the right. Cancellations made <strong class='text-white'>more than 2 hours before</strong> arrival are free. Cancellations within 2 hours may incur a small fee.",
            extra: `<div class="mt-3"><a href="advancereservation.html" class="flex items-center gap-2 text-primary text-xs font-black hover:brightness-125 transition-all"><span class="material-symbols-outlined text-sm">event_available</span>Go to My Reservations →</a></div>`
        },
        'What is the daily maximum rate?': {
            text: "The daily maximum rate at Parkiyo is <strong class='text-white'>$30.00</strong>. This means regardless of how long you park, you won't be charged more than $30 in a single calendar day. This applies to standard slots — EV and oversized slots have a daily max of <strong class='text-white'>$35.00</strong>."
        },
        'Help me top up my wallet': {
            text: "I'll take you to the wallet top-up page right now! You can add $10, $20, $50, $100, $200, or a custom amount using your card or online banking.",
            extra: `<div class="mt-3"><a href="walletoverview.html" class="flex items-center gap-2 bg-primary/10 border border-primary/20 text-primary font-black px-4 py-2 rounded-xl text-xs hover:bg-primary/20 transition-all inline-flex"><span class="material-symbols-outlined text-sm">account_balance_wallet</span>Open Wallet →</a></div>`
        }
    };

    const defaultResponse = "I can help with that! For detailed information or to take action, please use the navigation links on the left or check the relevant section of Parkiyo. Is there anything else I can assist you with?";

    function autoResize(el) {
        el.style.height = 'auto';
        el.style.height = Math.min(el.scrollHeight, 140) + 'px';
    }

    function appendMessage(role, text, extra = '') {
        const container = document.getElementById('chatMessages');
        const wrapper = document.createElement('div');
        wrapper.className = `flex gap-4 msg-appear ${role === 'user' ? 'flex-row-reverse' : ''}`;

        const now = new Date().toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });

        if (role === 'ai') {
            wrapper.innerHTML = `
                <div class="h-9 w-9 rounded-[14px] ai-avatar flex items-center justify-center shrink-0 mt-1">
                    <span class="material-symbols-outlined text-white text-base">smart_toy</span>
                </div>
                <div class="flex-1 max-w-2xl">
                    <div class="flex items-center gap-2 mb-2">
                        <span class="text-xs font-black text-primary uppercase tracking-wider">Parky AI</span>
                        <span class="text-[10px] text-slate-600 font-bold">${now}</span>
                    </div>
                    <div class="bubble-ai">${text}${extra}</div>
                </div>`;
        } else {
            wrapper.innerHTML = `
                <div class="flex-1 max-w-2xl flex flex-col items-end">
                    <div class="flex items-center gap-2 mb-2">
                        <span class="text-[10px] text-slate-600 font-bold">${now}</span>
                        <span class="text-xs font-black text-slate-300 uppercase tracking-wider">You</span>
                    </div>
                    <div class="bubble-user">${text}</div>
                </div>
                <div class="h-9 w-9 rounded-[14px] bg-gradient-to-tr from-primary to-blue-400 p-[1.5px] shrink-0 mt-1">
                    <div class="h-full w-full rounded-[12px] bg-background-dark flex items-center justify-center" style="background:#020617">
                        <span class="material-symbols-outlined text-white/50 text-base">person</span>
                    </div>
                </div>`;
        }
        container.appendChild(wrapper);
        container.scrollTop = container.scrollHeight;
    }

    function showTyping() {
        const container = document.getElementById('chatMessages');
        const el = document.createElement('div');
        el.className = 'flex gap-4 msg-appear';
        el.id = 'typingIndicator';
        el.innerHTML = `
            <div class="h-9 w-9 rounded-[14px] ai-avatar flex items-center justify-center shrink-0">
                <span class="material-symbols-outlined text-white text-base">smart_toy</span>
            </div>
            <div class="bubble-ai flex items-center gap-2 py-4">
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            </div>`;
        container.appendChild(el);
        container.scrollTop = container.scrollHeight;
    }

    function removeTyping() {
        const el = document.getElementById('typingIndicator');
        if (el) el.remove();
    }

    function sendQuick(text) {
        sendMessage(text);
    }

    function sendMessage(overrideText) {
        const input = document.getElementById('composeInput');
        const text = overrideText || input.value.trim();
        if (!text) return;

        appendMessage('user', text);
        input.value = '';
        input.style.height = 'auto';

        showTyping();
        const delay = 600 + Math.random() * 600;

        setTimeout(() => {
            removeTyping();
            const r = responses[text];
            if (r) {
                appendMessage('ai', r.text, r.extra || '');
            } else {
                appendMessage('ai', defaultResponse);
            }
        }, delay);
    }

    function clearChat() {
        const container = document.getElementById('chatMessages');
        container.innerHTML = '';
        // Re-add welcome
        const welcome = document.getElementById('welcomeMsg');
        // just reload to reset
        window.location.reload();
    }
</script>
</body>

</html>