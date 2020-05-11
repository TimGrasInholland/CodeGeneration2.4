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
        statusCode: {
            200: function(result){
                console.log("logged in" + result);
                sessionStorage.setItem("session", result);
                alert("[TEST] API Token saved as session: "+sessionStorage.getItem("session"))
                window.location.href = './Employee_Transactions.html';
            }
        }
    });
}