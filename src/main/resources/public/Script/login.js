function Login(){
    var username = document.getElementById("form-username").value;
    var password = document.getElementById("form-password").value;

    $.ajax({
        type: "POST",
        url: baseRequestURL+"/Login",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: { 
            username: username, 
            password: password 
        },
        complete: function(result) {
            switch (result.status) {
                case 200:
                    console.log(result)
                    sessionStorage.setItem("session", result.responseText)
                    if(GetCurrentUserRole() == "Customer")
                        window.location.href = './MyAccounts.html'
                    else{
                        window.location.href = './EmployeeDashboard.html'
                    }
                    break;
                default:
                    alert(result.responseText);
            }
        }
    });
}