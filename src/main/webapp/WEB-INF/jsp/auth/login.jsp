<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Parkiyo - Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f7fb;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .login-box {
            background: white;
            padding: 30px;
            width: 350px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #1f3c88;
        }

        label {
            display: block;
            margin-top: 12px;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-sizing: border-box;
        }

        .actions {
            margin-top: 18px;
        }

        button {
            width: 100%;
            padding: 10px;
            background: #1f3c88;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
        }

        button:hover {
            background: #17306d;
        }

        .links {
            margin-top: 15px;
            text-align: center;
        }

        .links a {
            display: block;
            margin-top: 8px;
            text-decoration: none;
            color: #1f3c88;
            font-size: 14px;
        }

        .links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="login-box">
    <h2>Parkiyo Login</h2>

    <form action="#" method="post">
        <label>Email or Username</label>
        <input type="text" name="username" placeholder="Enter your email or username">

        <label>Password</label>
        <input type="password" name="password" placeholder="Enter your password">

        <div class="actions">
            <button type="submit">Login</button>
        </div>
    </form>

    <div class="links">
        <a href="/register">Register New Account</a>
        <a href="/forgot-password">Forgot Password?</a>
    </div>
</div>
</body>
</html>