<%--Reference: https://gist.github.com/bMinaise/7329874
logo reference: http://www.clker.com/clipart-globe-white-and-blue.html
--%>
<html>
<%@include file="../templates/header.html"%>
<link rel="stylesheet" type="text/css" href="/stylesheets/login.css"
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center login-title" style="font-size: 40px; color: blue;">SafeGuarder</h1>
            <div class="account-wall">
                <img class="profile-img" src="../resources/img/safeguarder_logo.png"
                     alt="">
                <form class="form-signin" action="/controlpanel/authenticate" method="POST">
                    <input type="text" class="form-control" placeholder="Email" name="email" required autofocus>
                    <input type="password" class="form-control" placeholder="Password" name="password" required>
                    <button class="btn btn-lg btn-primary btn-block" type="submit">
                        Sign in
                    </button>
                    <%--<label class="checkbox pull-left">--%>
                        <%--<input type="checkbox" value="remember-me">--%>
                        <%--Remember me--%>
                    <%--</label>--%>
                    <%--<a href="#" class="pull-right need-help">Need help? </a><span class="clearfix"></span>--%>
                </form>
            </div>
            <%--<a href="#" class="text-center new-account">Create an account </a>--%>
        </div>
    </div>
</div>
</body>

</html>