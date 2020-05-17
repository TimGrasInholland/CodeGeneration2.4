function GetCustomerNavBar(){
    return '\
        <ul>\
          <li><p class="group-name"> Groep 3B</p></li>\
          <li style="float:right"><a onclick="logout()">Logout</a></li>\
          <li id="myProfile" style="float:right"><a href="ViewUser.html">My Profile</a></li>\
          <li id="myAccounts" style="float:right"><a href="MyAccounts.html">My Accounts</a></li>\
          <li id="home" style="float:right"><a href="Home.html">Home</a></li>\
        </ul>'
}

function GetEmployeeNavBar(){
    return '\
        <ul>\
            <li><p class="group-name"> Groep 3B</p></li>\
            <li style="float:right"><a onclick="logout()">Logout</a></li>\
            <li id="myProfile" style="float:right"><a href="ViewUser.html">My Profile</a></li>\
            <li id="dashboard" style="float:right"><a href="EmployeeDashboard.html">Dashboard</a></li>\
            <li id="myAccounts" style="float:right"><a href="MyAccounts.html">My Accounts</a></li>\
            <li id="home"style="float:right"><a href="Home.html">Home</a></li>\
        </ul>'
}

function GetUnsetUserNavBar(){
    return '\
        <ul>\
            <li><p class="group-name"> Groep 3B</p></li>\
            <li id="login" style="float:right"><a href="Login.html">Login</a></li>\
            <li id="home"style="float:right"><a href="Home.html">Home</a></li>\
        </ul>'
}

function SetNavBar(active){
    var navbar
    if(sessionStorage.getItem("session") == null){
        navbar = GetUnsetUserNavBar()
    }
    else{
        CheckIfUserIsLoggedIn()

        var role = GetCurrentUserRole()
        if(role == 'Employee'){
            navbar = GetEmployeeNavBar()
        }
        else{
            navbar = GetCustomerNavBar()
        }
    }

    $("nav").html(navbar)
    SetItemActive(active)
}

function SetItemActive(active){
    document.getElementById(active).classList.add("active")
}

function logout() {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/api/Logout",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        complete: function(jqXHR) {
            switch (jqXHR.status) {
                case 200:
                    alert("You have been logged out.");
                    window.location.href = './Login.html';
                    break;
                default:
                    alert("Oops! Something went wrong.");
            }
        }
    });
}

function CheckIfUserIsLoggedIn(){
    var authKey = GetCurrentUserAuthKey()
    if(authKey == null){
        window.location.href = './Login.html';
    }
}

function GetCurrentUserRole(){
    var role;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/SessionToken/"+sessionStorage.getItem("session"),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function(data){
            role = data.role
        }
    });
    return role
}

function GetCurrentUserAuthKey(){
    var key = null;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/SessionToken/"+sessionStorage.getItem("session"),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function(data){
            key = data.authKey
        }
    });
    return key
}

function GetCurrentUserId(){
    var id = null;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/SessionToken/"+sessionStorage.getItem("session"),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function(data){
            id = data.userId
        }
    });
    return id
}

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
};