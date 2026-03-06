<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Parkiyo - Register</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f7fb;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .box {
            background: white;
            padding: 30px;
            width: 420px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        }

        h2 {
            text-align: center;
            color: #1f3c88;
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-top: 12px;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            margin-top: 18px;
            padding: 10px;
            background: #1f3c88;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
        }

        .error {
            color: red;
            margin-top: 10px;
            text-align: center;
        }

        a {
            display: block;
            text-align: center;
            margin-top: 15px;
            text-decoration: none;
            color: #1f3c88;
        }
    </style>
</head>
<body>
<div class="box">
    <h2>Create Account</h2>

    <form action="/register" method="post">
        <label>First Name</label>
        <input type="text" name="firstName" placeholder="Enter first name" required>

        <label>Last Name</label>
        <input type="text" name="lastName" placeholder="Enter last name" required>

        <label>Email</label>
        <input type="email" name="email" placeholder="Enter email" required>

        <label>Phone Number</label>
        <input type="text" name="phone" placeholder="Enter phone number" required>

        <label>Password</label>
        <input type="password" name="password" placeholder="Enter password" required>

        <label>Gender</label>
        <select name="gender">
            <option value="">Select gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
        </select>

        <button type="submit">Register</button>
    </form>

    <p class="error">${error}</p>

    <a href="/">Back to Login</a>
</div>
</body>
</html>