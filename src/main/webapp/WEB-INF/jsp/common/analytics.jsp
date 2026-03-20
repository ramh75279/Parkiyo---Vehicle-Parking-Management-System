<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">

<head>
    <meta charset="utf-8" />
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <title>Parkiyo | Analytics</title>
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
                    borderRadius: { squircle: "14px", xl: "1.5rem", "2xl": "2rem", "3xl": "3rem" },
                },
            },
        }
    </script>
    <style>
        body {
            font-family: 'Public Sans', sans-serif;
        }

        .premium-blur {
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
        }

        .bg-subtle-radial {
            background: radial-gradient(circle at 50% 0%, #1e293b 0%, #020617 70%);
        }

        .glass-card {
            background: rgba(255, 255, 255, 0.03);
            border: 1px solid rgba(255, 255, 255, 0.08);
        }
    </style>
</head>

<body class="bg-background-dark text-slate-100 font-display antialiased bg-subtle-radial">
<header class="sticky top-0 z-50 w-full border-b border-white/5 bg-background-dark/75 premium-blur">
    <div class="container mx-auto flex h-20 items-center justify-between px-6 lg:px-12">
        <a href="/home" class="flex items-center gap-4">
            <div
                    class="flex h-11 w-11 items-center justify-center rounded-squircle bg-primary text-white shadow-[0_0_20px_rgba(31,104,249,0.4)]">
                <span class="material-symbols-outlined font-bold">local_parking</span>
            </div>
            <span class="text-2xl font-black tracking-tighter text-white uppercase">Parkiyo</span>
        </a>
        <nav class="hidden lg:flex items-center gap-8 text-sm font-bold text-slate-400">
            <a class="hover:text-primary" href="/home">Home</a>
            <a class="hover:text-primary" href="features.html">Features</a>
            <a class="hover:text-primary" href="solutions.html">Solutions</a>
            <a class="text-primary" href="analytics.html">Analytics</a>
            <a class="hover:text-primary" href="faq.html">Support</a>
        </nav>
        <div class="flex items-center gap-6">
            <a href="login.html" class="text-sm font-bold text-slate-300 hover:text-white">Login</a>
            <a href="register.html"
               class="bg-primary text-white text-sm font-bold px-7 py-3 rounded-xl hover:scale-105 transition-all shadow-lg shadow-primary/30">Get
                Started</a>
        </div>
    </div>
</header>

<main>
    <section class="container mx-auto px-6 pt-24 pb-20 lg:px-12">
        <div class="text-center max-w-3xl mx-auto">
            <div
                    class="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary/10 border border-primary/20 text-primary text-[10px] font-bold uppercase tracking-[0.2em] mb-6">
                Data & Visibility
            </div>
            <h1 class="text-5xl lg:text-7xl font-black leading-[1.05] tracking-tight text-white">
                Analytics that make parking operations smarter.
            </h1>
            <p class="mt-8 text-lg text-slate-400 leading-relaxed">
                Parkiyo surfaces occupancy, revenue, traffic flow and payment data in a premium dashboard experience
                designed for decision-making.
            </p>
        </div>
    </section>

    <section class="container mx-auto px-6 pb-24 lg:px-12">
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
            <div class="glass-card rounded-[2.5rem] p-10 text-center">
                <p class="text-sm font-bold text-slate-500 mb-2 uppercase tracking-widest">Occupancy</p>
                <h3 class="text-5xl font-black text-white">94%</h3>
                <p class="text-emerald-400 text-sm font-bold mt-3">Peak hour visibility</p>
            </div>
            <div class="glass-card rounded-[2.5rem] p-10 text-center">
                <p class="text-sm font-bold text-slate-500 mb-2 uppercase tracking-widest">Revenue</p>
                <h3 class="text-5xl font-black text-white">$45.8K</h3>
                <p class="text-emerald-400 text-sm font-bold mt-3">Platform wallet overview</p>
            </div>
            <div class="glass-card rounded-[2.5rem] p-10 text-center">
                <p class="text-sm font-bold text-slate-500 mb-2 uppercase tracking-widest">Daily Flow</p>
                <h3 class="text-5xl font-black text-white">2.4K</h3>
                <p class="text-emerald-400 text-sm font-bold mt-3">Vehicles processed</p>
            </div>
        </div>
    </section>

    <section class="container mx-auto px-6 pb-24 lg:px-12">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
            <div class="glass-card rounded-[3rem] p-10">
                <p class="text-[10px] font-black uppercase tracking-[0.22em] text-primary mb-4">Traffic Density</p>
                <div class="space-y-8">
                    <div>
                        <div class="flex justify-between text-sm text-slate-400 mb-2">
                            <span>Morning Peak</span>
                            <span class="text-white font-bold">85%</span>
                        </div>
                        <div class="h-3 w-full bg-white/5 rounded-full overflow-hidden">
                            <div class="h-full w-[85%] bg-primary rounded-full"></div>
                        </div>
                    </div>
                    <div>
                        <div class="flex justify-between text-sm text-slate-400 mb-2">
                            <span>Evening Peak</span>
                            <span class="text-white font-bold">76%</span>
                        </div>
                        <div class="h-3 w-full bg-white/5 rounded-full overflow-hidden">
                            <div class="h-full w-[76%] bg-primary rounded-full"></div>
                        </div>
                    </div>
                    <div>
                        <div class="flex justify-between text-sm text-slate-400 mb-2">
                            <span>Weekend Demand</span>
                            <span class="text-white font-bold">68%</span>
                        </div>
                        <div class="h-3 w-full bg-white/5 rounded-full overflow-hidden">
                            <div class="h-full w-[68%] bg-primary rounded-full"></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="glass-card rounded-[3rem] overflow-hidden">
                <img src="https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&q=80&w=1400"
                     alt="City analytics" class="h-full min-h-[360px] w-full object-cover" />
            </div>
        </div>
    </section>

    <section class="container mx-auto px-6 pb-28 lg:px-12">
        <div class="glass-card rounded-[3rem] p-10 lg:p-14">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <div>
                    <p class="text-[10px] font-black uppercase tracking-[0.22em] text-primary mb-3">Insights</p>
                    <h2 class="text-3xl font-black text-white">See where performance improves.</h2>
                </div>
                <div class="rounded-2xl bg-white/[0.03] p-6 border border-white/5">
                    <p class="text-2xl font-black text-white">Occupancy reports</p>
                    <p class="text-slate-400 mt-2">Understand how your parking spaces are being used.</p>
                </div>
                <div class="rounded-2xl bg-white/[0.03] p-6 border border-white/5">
                    <p class="text-2xl font-black text-white">Revenue monitoring</p>
                    <p class="text-slate-400 mt-2">Track earnings, payments, and system activity more clearly.</p>
                </div>
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
                <p class="text-slate-500 max-w-sm leading-relaxed mb-8 text-sm font-medium">
                    Premium parking management software for secure operations, cleaner workflows and real-time visibility across modern facilities.
                </p>
            </div>

            <div>
                <h5 class="text-white font-black mb-8 text-[11px] uppercase tracking-[0.2em] opacity-80">Platform</h5>
                <ul class="space-y-4 text-slate-500 text-sm font-bold">
                    <li><a href="features.html" class="hover:text-primary transition-colors">Features</a></li>
                    <li><a href="solutions.html"class="text-primary transition-colors">Solutions</a></li>
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
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="/home">language</a></span>
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="privacy.html">shield_person</a></span>
                <span class="material-symbols-outlined text-slate-600 cursor-pointer hover:text-white transition-colors"><a href="faq.html">support_agent</a></span>
            </div>
        </div>
    </div>
</footer>


</body>

</html>