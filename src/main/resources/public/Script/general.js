function GetCustomerNavBar(){
    return '\
        <ul>\
          <li><p class="group-name"> Groep 3B</p></li>\
          <li style="float:right"><a onclick="logout()">Logout</a></li>\
          <li id="myProfile" style="float:right"><a href="ViewUser.html">My Profile</a></li>\
          <li id="myAccounts" style="float:right"><a href="MyAccounts.html">My Accounts</a></li>\
          <li id="home" style="float:right"><a href="#home">Home</a></li>\
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
            <li id="home"style="float:right"><a href="#home">Home</a></li>\
        </ul>'
}

function SetNavBar(active){
    CheckIfUserIsLoggedIn()

    var role = GetCurrentUserRole()
    var navbar
    if(role == 'Customer'){
        navbar = GetCustomerNavBar()
    }
    else if(role == 'Employee'){
        navbar = GetEmployeeNavBar()
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