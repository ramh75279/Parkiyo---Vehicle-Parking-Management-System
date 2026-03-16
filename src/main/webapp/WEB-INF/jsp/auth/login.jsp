<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html class="dark" lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <title>Parkiyo | Login</title>
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
                    borderRadius: { squircle: "14px", xl: "1.5rem", "2xl": "2.1rem", "3xl": "3rem" },
                },
            },
        }
    </script>
    <style>
        body { font-family: 'Public Sans', sans-serif; }
        .premium-blur { backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px); }
        .bg-subtle-radial { background: radial-gradient(circle at 50% 0%, #1e293b 0%, #020617 70%); }
        .glass-card { background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.08); }
        .input-glass {
            background: rgba(255,255,255,0.04) !important;
            border: 1px solid rgba(255,255,255,0.09);
            color: white !important;
            border-radius: 14px;
            padding: 12px 16px;
            font-size: 0.875rem;
            font-weight: 600;
            font-family: 'Public Sans', sans-serif;
            transition: border-color 0.2s, box-shadow 0.2s, background 0.2s;
            outline: none;
            width: 100%;
        }
        .input-glass:hover {
            border-color: rgba(255,255,255,0.15);
            background: rgba(255,255,255,0.055) !important;
        }
        .input-glass:focus {
            background: rgba(31,104,249,0.07) !important;
            border-color: rgba(31,104,249,0.55) !important;
            box-shadow: 0 0 0 3px rgba(31,104,249,0.12), inset 0 1px 0 rgba(255,255,255,0.06);
        }
        .input-glass::placeholder { color: rgba(148,163,184,0.45); font-weight: 500; }
        select.input-glass option { background: #0d1829; color: white; }
        select.input-glass { cursor: pointer; }
        textarea.input-glass { resize: none; }
        .input-glass:-webkit-autofill,
        .input-glass:-webkit-autofill:hover,
        .input-glass:-webkit-autofill:focus,
        .input-glass:-webkit-autofill:active {
            -webkit-text-fill-color: white !important;
            -webkit-box-shadow: 0 0 0px 1000px #0a1628 inset !important;
            caret-color: white;
        }
        input:-webkit-autofill, input:-webkit-autofill:hover, input:-webkit-autofill:focus { -webkit-text-fill-color: white !important; -webkit-box-shadow: 0 0 0px 1000px #0f172a inset !important; transition: background-color 5000s ease-in-out 0s; }
        .notify-error { background: linear-gradient(180deg, rgba(244,63,94,0.14), rgba(244,63,94,0.06)); border: 1px solid rgba(244,63,94,0.2); }
        .notify-success { background: linear-gradient(180deg, rgba(34,197,94,0.14), rgba(34,197,94,0.06)); border: 1px solid rgba(34,197,94,0.2); }
        @keyframes qrPulse { 0%,100% { opacity: 0.4; } 50% { opacity: 1; } }
        .qr-pulse { animation: qrPulse 2s ease-in-out infinite; }
        @keyframes slideDown { from { transform: translateY(-10px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
        .msg-visible { animation: slideDown 0.3s ease-out forwards; }
    </style>
</head>
<body class="bg-background-dark text-slate-100 font-display antialiased min-h-screen flex flex-col bg-subtle-radial">

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
            <a class="hover:text-primary transition-colors" href="features.html">Features</a>
            <a class="hover:text-primary transition-colors" href="solutions.html">Solutions</a>
            <a class="hover:text-primary transition-colors" href="analytics.html">Analytics</a>
            <a class="hover:text-primary transition-colors" href="faq.html">Support</a>
        </nav>
        <a href="register.html" class="bg-primary text-white text-sm font-bold px-7 py-3 rounded-xl hover:scale-105 transition-all shadow-lg shadow-primary/30">Register</a>
    </div>
</header>

<main class="flex-grow flex items-center justify-center py-16 px-6">
    <div class="w-full max-w-5xl">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">

            <!-- LOGIN FORM -->
            <div class="glass-card rounded-[3rem] overflow-hidden shadow-2xl">
                <div class="p-10 lg:p-14">
                    <div class="mb-10">
                        <div class="inline-flex items-center justify-center w-14 h-14 bg-primary/10 text-primary rounded-2xl mb-6 border border-primary/20">
                            <span class="material-symbols-outlined text-2xl">lock_open</span>
                        </div>
                        <h2 class="text-3xl font-black text-white tracking-tight mb-2">Welcome back</h2>
                        <p class="text-slate-400 font-medium">Sign in to your Parkiyo account to continue.</p>
                    </div>

                    <form id="loginForm" novalidate class="space-y-5">
                        <div id="formMsg" class="hidden px-4 py-4 rounded-2xl text-[13px] font-bold flex items-start gap-3">
                            <span id="formMsgIcon" class="material-symbols-outlined text-base mt-[1px]">info</span>
                            <p id="formMsgText"></p>
                        </div>

                        <div>
                            <label class="block text-sm font-bold text-slate-200 ml-1 mb-2">Email Address</label>
                            <input type="email" id="email" class="w-full h-14 rounded-2xl px-5 input-glass font-medium" placeholder="you@parkiyo.com" autocomplete="email"/>
                        </div>

                        <div>
                            <label class="block text-sm font-bold text-slate-200 ml-1 mb-2">Password</label>
                            <div class="relative">
                                <input type="password" id="password" class="w-full h-14 rounded-2xl px-5 pr-14 input-glass font-medium" placeholder="Your password" autocomplete="current-password"/>
                                <button type="button" onclick="togglePw()" class="absolute right-5 top-1/2 -translate-y-1/2 text-slate-500 hover:text-white transition-colors">
                                    <span id="eyeIcon" class="material-symbols-outlined text-xl">visibility</span>
                                </button>
                            </div>
                        </div>

                        <div class="flex items-center justify-between pt-1">
                            <label class="flex items-center gap-3 cursor-pointer">
                                <input type="checkbox" id="rememberMe" class="w-4 h-4 rounded accent-primary"/>
                                <span class="text-sm text-slate-400 font-medium">Remember me</span>
                            </label>
                            <a href="forgot-password.html" class="text-sm font-bold text-primary hover:brightness-125 transition-all">Forgot password?</a>
                        </div>

                        <div class="pt-2">
                            <button type="submit" class="w-full h-16 bg-primary text-white font-black rounded-2xl shadow-xl shadow-primary/20 hover:scale-[1.02] active:scale-[0.98] transition-all flex items-center justify-center gap-3 text-base">
                                Sign In
                                <span class="material-symbols-outlined">arrow_forward</span>
                            </button>
                        </div>
                    </form>

                    <div class="mt-8 pt-8 border-t border-white/5 text-center">
                        <p class="text-slate-500 text-sm font-medium">Don't have an account? <a href="register.html" class="text-primary font-black hover:brightness-125 transition-all">Create one</a></p>
                    </div>
                </div>
            </div>

            <!-- QR LOGIN PANEL -->
            <div class="glass-card rounded-[3rem] overflow-hidden flex flex-col">
                <div class="p-10 lg:p-14 flex flex-col flex-1">
                    <div class="mb-8">
                        <div class="inline-flex items-center justify-center w-14 h-14 bg-white/5 text-slate-300 rounded-2xl mb-6 border border-white/10">
                            <span class="material-symbols-outlined text-2xl">qr_code_scanner</span>
                        </div>
                        <h2 class="text-2xl font-black text-white tracking-tight mb-2">QR Code Login</h2>
                        <p class="text-slate-400 text-sm font-medium leading-relaxed">Scan this code from your connected mobile device to login instantly without typing credentials.</p>
                    </div>

                    <!-- QR Visual -->
                    <div class="flex-1 flex flex-col items-center justify-center">
                        <div class="relative w-48 h-48 mx-auto mb-8">
                            <div class="absolute inset-0 rounded-3xl bg-white/5 border border-white/10"></div>
                            <div class="absolute inset-4 grid grid-cols-7 grid-rows-7 gap-1 qr-pulse">
                                <!-- QR code decoration -->
                                <div class="col-span-3 row-span-3 border-2 border-primary rounded-lg bg-primary/20"></div>
                                <div class="col-span-1"></div>
                                <div class="col-span-3 row-span-3 border-2 border-primary rounded-lg bg-primary/20"></div>
                                <div class="col-span-7 row-span-1 flex gap-1">
                                    <div class="flex-1 bg-white/20 rounded-sm"></div>
                                    <div class="flex-1"></div>
                                    <div class="flex-1 bg-white/20 rounded-sm"></div>
                                    <div class="flex-1 bg-white/20 rounded-sm"></div>
                                    <div class="flex-1"></div>
                                    <div class="flex-1 bg-white/20 rounded-sm"></div>
                                    <div class="flex-1"></div>
                                </div>
                                <div class="col-span-3 row-span-3 border-2 border-primary rounded-lg bg-primary/20"></div>
                                <div class="col-span-4 row-span-3 grid grid-cols-4 grid-rows-3 gap-1">
                                    <div class="bg-white/20 rounded-sm"></div><div></div><div class="bg-white/20 rounded-sm"></div><div class="bg-white/20 rounded-sm"></div>
                                    <div></div><div class="bg-white/20 rounded-sm"></div><div></div><div></div>
                                    <div class="bg-white/20 rounded-sm"></div><div class="bg-white/20 rounded-sm"></div><div></div><div class="bg-white/20 rounded-sm"></div>
                                </div>
                            </div>
                            <div class="absolute inset-0 flex items-center justify-center">
                                <div class="h-12 w-12 rounded-xl bg-primary shadow-[0_0_20px_rgba(31,104,249,0.5)] flex items-center justify-center">
                                    <span class="material-symbols-outlined text-white text-xl">local_parking</span>
                                </div>
                            </div>
                        </div>

                        <div class="text-center mb-8">
                            <p class="text-xs font-black uppercase tracking-widest text-slate-500 mb-1">Code refreshes in</p>
                            <p id="countdown" class="text-2xl font-black text-primary">2:58</p>
                        </div>

                        <div class="w-full space-y-3">
                            <div class="flex items-center gap-4 p-4 rounded-xl bg-white/[0.02] border border-white/5">
                                <span class="h-7 w-7 rounded-full bg-primary/10 text-primary text-xs font-black flex items-center justify-center shrink-0">1</span>
                                <span class="text-slate-400 text-xs font-medium">Open the Parkiyo app on your phone</span>
                            </div>
                            <div class="flex items-center gap-4 p-4 rounded-xl bg-white/[0.02] border border-white/5">
                                <span class="h-7 w-7 rounded-full bg-primary/10 text-primary text-xs font-black flex items-center justify-center shrink-0">2</span>
                                <span class="text-slate-400 text-xs font-medium">Tap "Scan to Login" in the app menu</span>
                            </div>
                            <div class="flex items-center gap-4 p-4 rounded-xl bg-white/[0.02] border border-white/5">
                                <span class="h-7 w-7 rounded-full bg-primary/10 text-primary text-xs font-black flex items-center justify-center shrink-0">3</span>
                                <span class="text-slate-400 text-xs font-medium">Point your camera at the code above</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    // QR countdown
    let secs = 178;
    const countdown = document.getElementById('countdown');
    setInterval(() => {
        secs--;
        if (secs <= 0) secs = 180;
        const m = Math.floor(secs / 60), s = secs % 60;
        countdown.textContent = `${m}:${s.toString().padStart(2,'0')}`;
    }, 1000);

    // Toggle pw
    function togglePw() {
        const pw = document.getElementById('password');
        const icon = document.getElementById('eyeIcon');
        if (pw.type === 'password') { pw.type = 'text'; icon.textContent = 'visibility_off'; }
        else { pw.type = 'password'; icon.textContent = 'visibility'; }
    }

    // Form logic
    const form = document.getElementById('loginForm');
    const formMsg = document.getElementById('formMsg');
    const formMsgText = document.getElementById('formMsgText');
    const formMsgIcon = document.getElementById('formMsgIcon');

    function showMsg(type, text) {
        formMsg.className = `px-4 py-4 rounded-2xl text-[13px] font-bold flex items-start gap-3 msg-visible ${type === 'error' ? 'notify-error text-rose-400' : 'notify-success text-emerald-400'}`;
        formMsgIcon.textContent = type === 'error' ? 'error' : 'check_circle';
        formMsgText.textContent = text;
    }

    form.addEventListener('submit', e => {
        e.preventDefault();
        const email = document.getElementById('email').value.trim();
        const pw = document.getElementById('password').value;
        if (!email || !pw) { showMsg('error', 'Please enter your email and password.'); return; }
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) { showMsg('error', 'Enter a valid email address.'); return; }

        // Demo role routing
        if (email === 'admin@parkiyo.com' && pw === 'admin123') {
            showMsg('success', 'Login successful. Redirecting to admin dashboard...');
            setTimeout(() => window.location.href = 'dashboard-admin.html', 1200);
        } else if (pw.length >= 6) {
            showMsg('success', 'Login successful. Redirecting...');
            setTimeout(() => window.location.href = 'dashboard-user.html', 1200);
        } else {
            showMsg('error', 'Invalid credentials. Please check your email and password.');
        }
    });
</script>
</body>
</html>
