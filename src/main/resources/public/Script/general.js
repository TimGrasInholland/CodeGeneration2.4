//Set API Server
var baseRequestURL = /*"https://inholland-bank-api.herokuapp.com/api"*/ "http://localhost:8080/api"

function SetNavBar(active) {
    var navbar
    if(sessionStorage.getItem("session") != null){
        sessionToken = GetCurrentSessionToken();
        if (sessionToken != null) {
            if(GetCurrentUserRole() == 'Employee'){
                navbar = GetEmployeeNavBar()
            } else{
                navbar = GetCustomerNavBar()
            }
        } else {
            navbar = GetUnsetUserNavBar()
            if (active != 'login' && active != 'home' && active != 'unset') {
                alert('This user account has been accessed on another browser, you are now being logged out.')
                window.location.href = './Login.html'
            }
        }
    }
    else{
        navbar = GetUnsetUserNavBar()
    }

    //Set navbar in HTML file
    $("nav").html(navbar)
    //Set active item in navbar (BOLD)
    SetItemActive(active)
}

function GetCustomerNavBar() {
    return '\
        <ul>\
          <li><p class="group-name"> Groep 3B</p></li>\
          <li style="float:right"><a class="logout" type="button" onclick="logout()">Logout</a></li>\
          <li id="myProfile" style="float:right"><a href="ViewUser.html">My Profile</a></li>\
          <li id="myAccounts" style="float:right"><a href="MyAccounts.html">My Accounts</a></li>\
        </ul>'
}

function GetEmployeeNavBar() {
    return '\
        <ul>\
            <li><p class="group-name"> Groep 3B</p></li>\
            <li style="float:right"><a class="logout" type="button" onclick="logout()">Logout</a></li>\
            <li id="myProfile" style="float:right"><a href="ViewUser.html">My Profile</a></li>\
            <li id="dashboard" style="float:right"><a href="EmployeeDashboard.html">Dashboard</a></li>\
            <li id="myAccounts" style="float:right"><a href="MyAccounts.html">My Accounts</a></li>\
        </ul>'
}

function GetUnsetUserNavBar() {
    return '\
        <ul>\
            <li><p class="group-name"> Groep 3B</p></li>\
            <li id="login" style="float:right"><a href="Login.html">Login</a></li>\
            <li id="home"style="float:right"><a href="Home.html">Home</a></li>\
        </ul>'
}

function SetItemActive(active) {
    if (document.getElementById(active)) {
        document.getElementById(active).classList.add("active")
    }
}

function CheckIfUserIsLoggedIn() {
    var authKey = GetCurrentUserAuthKey()
    if (authKey == null) {
        window.location.href = './Login.html';
    }
}

function GetCurrentSessionToken() {
    var sessionToken;
    $.ajax({
        type: "GET",
        url: baseRequestURL + "/SessionToken/" + sessionStorage.getItem("session"),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function (data) {
            sessionToken = data
        }
    });
    return sessionToken
}

function GetCurrentUserRole() {
    return GetCurrentSessionToken().role
}

function GetCurrentUserAuthKey() {
    return GetCurrentSessionToken().authKey
}

function GetCurrentUserId() {
    var sessionToken = GetCurrentSessionToken();
    if (sessionToken != null) {
        return sessionToken.userId;
    } else {
        return null;
    }
}

function logout() {
    $.ajax({
        type: "DELETE",
        url: baseRequestURL + "/Logout",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        complete: function (jqXHR) {
            switch (jqXHR.status) {
                case 200:
                    alert("You have been logged out.");
                    sessionStorage.removeItem("session")
                    window.location.href = './Login.html';
                    break;
                default:
                    alert(jqXHR.responseText);
            }
        }
    });
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

function GetDate(transaction) {
    var date = new Date(transaction.timestamp);
    var ye = new Intl.DateTimeFormat('en', {year: 'numeric'}).format(date)
    var mo = new Intl.DateTimeFormat('en', {month: 'short'}).format(date)
    var da = new Intl.DateTimeFormat('en', {day: '2-digit'}).format(date)
    date = `${da} ${mo} ${ye}`
    return date;
}

function GetDateTime(timestamp) {
    const date = new Date(timestamp)
    const ye = new Intl.DateTimeFormat('en', {year: 'numeric'}).format(date)
    const mo = new Intl.DateTimeFormat('en', {month: 'short'}).format(date)
    const da = new Intl.DateTimeFormat('en', {day: '2-digit'}).format(date)
    return `${da} ${mo} ${ye}`
}

function GetBirthDate(birthdate) {
    const date = new Date(birthdate)
    const ye = new Intl.DateTimeFormat('en', {year: 'numeric'}).format(date)
    const mo = new Intl.DateTimeFormat('en', {month: '2-digit'}).format(date)
    const da = new Intl.DateTimeFormat('en', {day: '2-digit'}).format(date)
    return `${ye}-${mo}-${da}`
}

function SetDefaultDates() {
    var dateFrom = new Date()
    dateFrom.setDate(dateFrom.getDate() - 7)
    dateStart = document.getElementById("startdate").value = dateFrom.toISOString().substr(0, 10)
    var dateTo = new Date()
    dateEnd = document.getElementById("enddate").value = dateTo.toISOString().substr(0, 10)
}
