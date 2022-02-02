<html>
    <head>
        <title>Homework 5</title>
    </head>
    <body>
        <h1>The name ${name} is already in use</h1>

        <p>Please enter another name and password</p>

        <form action="/register" method="post">
            <table>
                <tr><td>User Name:</td>
                    <td><input type="text" name="username" /></td></tr>
                <tr><td>Password:</td>
                    <td><input type="password" name="password" /></td>
                    <td><input type="submit" value = "Register" /></td></tr>
            </table>
        </form>

    </body>
</html>
