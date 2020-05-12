function Login(){
    var username = document.getElementById("form-username").value;
    var password = document.getElementById("form-password").value;

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/Login",
        data: { username: username, password: password },
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        success: function(result) {
            console.log(result)
            sessionStorage.setItem("session", result)
            window.location.href = './MyAccounts.html'
        },
        error: function(){
            alert("Invalid username/password!")
        }
    });
}