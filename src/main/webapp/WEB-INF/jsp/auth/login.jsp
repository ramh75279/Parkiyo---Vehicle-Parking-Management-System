<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Parkiyo - Login</title>
</head>
<body>
<h1>Parkiyo Login</h1>

<form action="/login" method="post">
    <label>Email</label><br>
    <input type="email" name="email" required><br><br>

    <label>Password</label><br>
    <input type="password" name="password" required><br><br>

    <button type="submit">Login</button>
</form>

<p style="color:red;">${error}</p>

<a href="/register">Register New Account</a><br>
<a href="/forgot-password">Forgot Password?</a>
</body>
</html>