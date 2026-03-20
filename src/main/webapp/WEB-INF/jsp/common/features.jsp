<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Features</title>
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
                    borderRadius: { squircle: "14px", xl: "1.5rem", "2xl": "2rem", "3xl": "3rem" },
                },
            },
        }
    </script>
    <style>
        body { font-family: 'Public Sans', sans-serif; }
        .premium-blur { backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px); }
        .bg-subtle-radial { background: radial-gradient(circle at 50% 0%, #1e293b 0%, #020617 70%); }
        .glass-card { background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.08); }
        ::-webkit-scrollbar { width: 5px; } ::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.1); border-radius: 10px; }
    </style>
</head>
<body class="bg-background-dark text-slate-100 font-display antialiased bg-subtle-radial">
<header class="sticky top-0 z-50 w-full border-b border-white/5 bg-background-dark/75 premium-blur">
    <div class="container mx-auto flex h-20 items-center justify-between px-6 lg:px-12">
        <a href="home.html" class="flex items-center gap-4">
            <div class="flex h-11 w-11 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_20px_rgba(31,104,249,0.4)]">
                <span class="material-symbols-outlined font-bold">local_parking</span>
            </div>
            <span class="text-2xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </a>
        <nav class="hidden lg:flex items-center gap-8 text-sm font-bold text-slate-400">
            <a class="hover:text-primary transition-colors" href="home.html">Home</a>
            <a class="text-primary" href="features.html">Features</a>
            <a class="hover:text-primary transition-colors" href="solutions.html">Solutions</a>
            <a class="hover:text-primary transition-colors" href="analytics.html">Analytics</a>
            <a class="hover:text-primary transition-colors" href="faq.html">Support</a>
        </nav>
        <div class="flex items-center gap-6">
            <a href="login.html" class="text-sm font-bold text-slate-300 hover:text-white">Login</a>
            <a href="register.html" class="bg-primary text-white text-sm font-bold px-7 py-3 rounded-xl hover:scale-105 transition-all shadow-lg shadow-primary/30">Get Started</a>
        </div>
    </div>
</header>

<main>
    <!-- HERO -->
    <section class="container mx-auto px-6 lg:px-12 pt-24 pb-20">
        <div class="text-center max-w-4xl mx-auto">
            <div class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 border border-primary/20 text-primary text-[10px] font-black uppercase tracking-[0.22em] mb-6">Platform Features</div>
            <h1 class="text-6xl lg:text-7xl font-black leading-[1.02] tracking-tight text-white">Everything you need to run parking at its best.</h1>
            <p class="mt-8 text-xl text-slate-400 max-w-2xl mx-auto leading-relaxed font-medium">Parkiyo packages every critical operation into one premium platform — from first gate entry to final revenue report.</p>
        </div>
    </section>

    <!-- FEATURE: ENTRY / EXIT -->
    <section class="container mx-auto px-6 lg:px-12 pb-24">
        <div class="glass-card rounded-[3rem] overflow-hidden">
            <div class="grid grid-cols-1 lg:grid-cols-2">
                <div class="p-14 lg:p-20">
                    <div class="h-16 w-16 rounded-2xl bg-primary/10 border border-primary/20 flex items-center justify-center mb-10">
                        <span class="material-symbols-outlined text-primary text-3xl">swap_horiz</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-primary mb-3">Entry & Exit</p>
                    <h2 class="text-4xl font-black text-white mb-6">Frictionless vehicle processing at every gate.</h2>
                    <p class="text-slate-400 leading-relaxed mb-8">From the moment a vehicle arrives to when it leaves, Parkiyo handles every step — plate lookup, slot assignment, duration tracking and exit confirmation.</p>
                    <ul class="space-y-4">
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-primary text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Manual plate entry or QR code scan</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-primary text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Automatic entry time stamping</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-primary text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Exit processing with fee calculation</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-primary text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Printable parking ticket on entry</span></li>
                    </ul>
                </div>
                <div class="bg-white/[0.02] border-l border-white/5 p-14 lg:p-20 flex flex-col justify-center">
                    <div class="space-y-4">
                        <div class="p-6 rounded-2xl bg-emerald-500/5 border border-emerald-500/15 flex items-center gap-5">
                            <div class="h-12 w-12 rounded-xl bg-emerald-500/10 flex items-center justify-center shrink-0"><span class="material-symbols-outlined text-emerald-400">login</span></div>
                            <div><p class="text-sm font-black text-white">Vehicle Entered</p><p class="text-xs text-slate-500 mt-0.5">ABC-1234 · Slot A-12 · 10:45 AM</p></div>
                            <span class="ml-auto text-[9px] font-black uppercase tracking-widest text-emerald-400">Just now</span>
                        </div>
                        <div class="p-6 rounded-2xl bg-primary/5 border border-primary/10 flex items-center gap-5">
                            <div class="h-12 w-12 rounded-xl bg-primary/10 flex items-center justify-center shrink-0"><span class="material-symbols-outlined text-primary">receipt_long</span></div>
                            <div><p class="text-sm font-black text-white">Ticket Generated</p><p class="text-xs text-slate-500 mt-0.5">#TKT-00291 · 2h 15m tracked</p></div>
                            <span class="ml-auto text-[9px] font-black uppercase tracking-widest text-primary">Active</span>
                        </div>
                        <div class="p-6 rounded-2xl glass-card flex items-center gap-5">
                            <div class="h-12 w-12 rounded-xl bg-white/5 flex items-center justify-center shrink-0"><span class="material-symbols-outlined text-rose-400">logout</span></div>
                            <div><p class="text-sm font-black text-white">Exit Processed</p><p class="text-xs text-slate-500 mt-0.5">$12.50 due · Payment pending</p></div>
                            <span class="ml-auto text-[9px] font-black uppercase tracking-widest text-amber-400">Pending</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- FEATURE: SLOT MANAGEMENT -->
    <section class="container mx-auto px-6 lg:px-12 pb-24">
        <div class="glass-card rounded-[3rem] overflow-hidden">
            <div class="grid grid-cols-1 lg:grid-cols-2">
                <div class="bg-white/[0.02] border-r border-white/5 p-14 lg:p-20 flex flex-col justify-center order-2 lg:order-1">
                    <p class="text-[10px] font-black uppercase tracking-widest text-slate-500 mb-4">Live Slot Grid Preview</p>
                    <div class="grid grid-cols-5 gap-3">
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">A1</div>
                        <div class="h-12 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center text-[10px] font-black text-rose-400">A2</div>
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">A3</div>
                        <div class="h-12 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center text-[10px] font-black text-rose-400">A4</div>
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">A5</div>
                        <div class="h-12 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center text-[10px] font-black text-rose-400">B1</div>
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">B2</div>
                        <div class="h-12 rounded-xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center text-[10px] font-black text-amber-400">B3</div>
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">B4</div>
                        <div class="h-12 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center text-[10px] font-black text-rose-400">B5</div>
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">C1</div>
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">C2</div>
                        <div class="h-12 rounded-xl bg-slate-500/10 border border-slate-500/20 flex items-center justify-center text-[10px] font-black text-slate-500">C3</div>
                        <div class="h-12 rounded-xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center text-[10px] font-black text-emerald-400">C4</div>
                        <div class="h-12 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center text-[10px] font-black text-rose-400">C5</div>
                    </div>
                    <div class="flex items-center gap-6 mt-6">
                        <div class="flex items-center gap-2"><span class="h-3 w-3 rounded bg-emerald-500/30 border border-emerald-500/30"></span><span class="text-[10px] font-black text-slate-500 uppercase tracking-wide">Available</span></div>
                        <div class="flex items-center gap-2"><span class="h-3 w-3 rounded bg-rose-500/30 border border-rose-500/30"></span><span class="text-[10px] font-black text-slate-500 uppercase tracking-wide">Occupied</span></div>
                        <div class="flex items-center gap-2"><span class="h-3 w-3 rounded bg-amber-500/30 border border-amber-500/30"></span><span class="text-[10px] font-black text-slate-500 uppercase tracking-wide">Reserved</span></div>
                        <div class="flex items-center gap-2"><span class="h-3 w-3 rounded bg-slate-500/30 border border-slate-500/30"></span><span class="text-[10px] font-black text-slate-500 uppercase tracking-wide">Disabled</span></div>
                    </div>
                </div>
                <div class="p-14 lg:p-20 order-1 lg:order-2">
                    <div class="h-16 w-16 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 flex items-center justify-center mb-10">
                        <span class="material-symbols-outlined text-emerald-400 text-3xl">grid_view</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-emerald-400 mb-3">Slot Management</p>
                    <h2 class="text-4xl font-black text-white mb-6">See your entire parking floor in real time.</h2>
                    <p class="text-slate-400 leading-relaxed mb-8">A live visual grid shows every bay's status the moment it changes. Admins can add, edit, disable slots and run batch generation for large facilities.</p>
                    <ul class="space-y-4">
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-emerald-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Visual slot grid with live status</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-emerald-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Zone and category filtering</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-emerald-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Bulk slot generation for large floors</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-emerald-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Full slot usage history and audit trail</span></li>
                    </ul>
                </div>
            </div>
        </div>
    </section>

    <!-- FEATURE: PAYMENTS -->
    <section class="container mx-auto px-6 lg:px-12 pb-24">
        <div class="glass-card rounded-[3rem] overflow-hidden">
            <div class="grid grid-cols-1 lg:grid-cols-2">
                <div class="p-14 lg:p-20">
                    <div class="h-16 w-16 rounded-2xl bg-violet-500/10 border border-violet-500/20 flex items-center justify-center mb-10">
                        <span class="material-symbols-outlined text-violet-400 text-3xl">payments</span>
                    </div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-violet-400 mb-3">Payment & Receipts</p>
                    <h2 class="text-4xl font-black text-white mb-6">Automate every payment. Generate every receipt.</h2>
                    <p class="text-slate-400 leading-relaxed mb-8">Parkiyo calculates parking fees automatically based on duration and rate configuration. Payments flow through a secure wallet system with full receipt generation.</p>
                    <ul class="space-y-4">
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-violet-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Auto fee calculation on exit</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-violet-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Wallet-based payment processing</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-violet-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Instant digital receipt generation</span></li>
                        <li class="flex items-start gap-4"><span class="material-symbols-outlined text-violet-400 text-lg mt-0.5">check_circle</span><span class="text-slate-300 text-sm font-medium">Full payment history and audit log</span></li>
                    </ul>
                </div>
                <div class="bg-white/[0.02] border-l border-white/5 p-14 lg:p-20 flex flex-col justify-center">
                    <div class="glass-card rounded-3xl p-8">
                        <div class="flex items-center justify-between mb-6">
                            <p class="text-xs font-black uppercase tracking-widest text-slate-500">Receipt #PR-2024-001</p>
                            <span class="px-3 py-1 bg-emerald-500/10 text-emerald-400 text-[9px] font-black uppercase rounded-full border border-emerald-500/20">Paid</span>
                        </div>
                        <div class="space-y-4 mb-6">
                            <div class="flex justify-between text-sm"><span class="text-slate-500 font-medium">Plate Number</span><span class="text-white font-black">ABC-1234</span></div>
                            <div class="flex justify-between text-sm"><span class="text-slate-500 font-medium">Slot Assigned</span><span class="text-white font-black">A-12</span></div>
                            <div class="flex justify-between text-sm"><span class="text-slate-500 font-medium">Entry Time</span><span class="text-white font-black">10:45 AM</span></div>
                            <div class="flex justify-between text-sm"><span class="text-slate-500 font-medium">Exit Time</span><span class="text-white font-black">01:00 PM</span></div>
                            <div class="flex justify-between text-sm"><span class="text-slate-500 font-medium">Duration</span><span class="text-white font-black">2h 15m</span></div>
                        </div>
                        <div class="border-t border-white/10 pt-5 flex justify-between items-center">
                            <span class="text-sm font-bold text-slate-400">Total Amount</span>
                            <span class="text-2xl font-black text-primary">$12.50</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- FEATURE: RESERVATIONS + ROLES in 2-col -->
    <section class="container mx-auto px-6 lg:px-12 pb-24">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div class="glass-card rounded-[2.5rem] p-12">
                <div class="h-14 w-14 rounded-2xl bg-amber-500/10 border border-amber-500/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-amber-400 text-2xl">calendar_month</span>
                </div>
                <p class="text-[10px] font-black uppercase tracking-[0.22em] text-amber-400 mb-3">Reservations</p>
                <h3 class="text-3xl font-black text-white mb-5">Let drivers book before they arrive.</h3>
                <p class="text-slate-400 leading-relaxed mb-6">Advance slot reservations reduce gate congestion and give drivers confidence before arriving. Admins see all upcoming reservations in one place.</p>
                <ul class="space-y-3">
                    <li class="flex items-center gap-3"><span class="material-symbols-outlined text-amber-400 text-base">check</span><span class="text-slate-300 text-sm font-medium">Date & time-based reservations</span></li>
                    <li class="flex items-center gap-3"><span class="material-symbols-outlined text-amber-400 text-base">check</span><span class="text-slate-300 text-sm font-medium">Slot preference selection</span></li>
                    <li class="flex items-center gap-3"><span class="material-symbols-outlined text-amber-400 text-base">check</span><span class="text-slate-300 text-sm font-medium">Availability checking in real time</span></li>
                </ul>
            </div>

            <div class="glass-card rounded-[2.5rem] p-12">
                <div class="h-14 w-14 rounded-2xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center mb-8">
                    <span class="material-symbols-outlined text-rose-400 text-2xl">shield_person</span>
                </div>
                <p class="text-[10px] font-black uppercase tracking-[0.22em] text-rose-400 mb-3">Role-Based Access</p>
                <h3 class="text-3xl font-black text-white mb-5">Admin and operator separation built in.</h3>
                <p class="text-slate-400 leading-relaxed mb-6">Admins get full system control — user management, reports, system status, audit logs. Operators see only what they need to run day-to-day.</p>
                <ul class="space-y-3">
                    <li class="flex items-center gap-3"><span class="material-symbols-outlined text-rose-400 text-base">check</span><span class="text-slate-300 text-sm font-medium">Separate admin & user dashboards</span></li>
                    <li class="flex items-center gap-3"><span class="material-symbols-outlined text-rose-400 text-base">check</span><span class="text-slate-300 text-sm font-medium">Access-denied guardrails</span></li>
                    <li class="flex items-center gap-3"><span class="material-symbols-outlined text-rose-400 text-base">check</span><span class="text-slate-300 text-sm font-medium">Admin creates and manages all accounts</span></li>
                </ul>
            </div>
        </div>
    </section>
</main>

<footer class="border-t border-white/5 bg-background-dark pt-24 pb-12">
    <div class="container mx-auto px-6 lg:px-12">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-16 mb-20">
            <div class="col-span-2">
                <div class="flex items-center gap-4 mb-8">
                    <div class="flex h-11 w-11 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_20px_rgba(31,104,249,0.4)]">
                        <span class="material-symbols-outlined font-bold text-2xl">local_parking</span>
                    </div>
                    <span class="text-2xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
                </div>
                <p class="text-slate-500 max-w-sm leading-relaxed mb-8 text-sm font-medium">Premium parking management software for secure operations, cleaner workflows and real-time visibility across modern facilities.</p>
            </div>
            <div>
                <h5 class="text-white font-black mb-8 text-[11px] uppercase tracking-[0.2em] opacity-80">Platform</h5>
                <ul class="space-y-4 text-slate-500 text-sm font-bold">
                    <li><a href="features.html" class="hover:text-primary transition-colors">Features</a></li>
                    <li><a href="solutions.html" class="hover:text-primary transition-colors">Solutions</a></li>
                    <li><a href="analytics.html" class="hover:text-primary transition-colors">Analytics</a></li>
                    <li><a href="faq.html" class="hover:text-primary transition-colors">Support</a></li>
                </ul>
            </div>
            <div>
                <h5 class="text-white font-black mb-8 text-[11px] uppercase tracking-[0.2em] opacity-80">Company</h5>
                <ul class="space-y-4 text-slate-500 text-sm font-bold">
                    <li><a href="faq.html" class="hover:text-primary transition-colors">Help & FAQ</a></li>
                    <li><a href="privacy.html" class="hover:text-primary transition-colors">Privacy</a></li>
                    <li><a href="login.html" class="hover:text-primary transition-colors">Login</a></li>
                    <li><a href="register.html" class="hover:text-primary transition-colors">Register</a></li>
                </ul>
            </div>
        </div>
        <div class="pt-10 border-t border-white/5 flex flex-col md:flex-row justify-between items-center gap-6">
            <p class="text-slate-600 text-[9px] tracking-[0.3em] uppercase font-bold">© 2026 Parkiyo. All rights reserved.</p>
            <div class="flex gap-8">
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="home.html">language</a></span>
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="privacy.html">shield_person</a></span>
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="faq.html">support_agent</a></span>
            </div>
        </div>
    </div>
</footer>
</body>
</html>