function CreateAccount(){
    var userId = GetUserId()

    if(userId != null){

        var type = document.getElementById("type")
        type = type.options[type.selectedIndex].value
        var currency = document.getElementById("currency")
        currency = currency.options[currency.selectedIndex].value

        var marker = JSON.stringify({
            "userId": userId,
            "type": type,
            "currency": currency
        })

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/Accounts",
            data: marker,
            headers: {
                "session": sessionStorage.getItem("session")
            },
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            complete: function(jqXHR) {
                switch (jqXHR.status) {
                    case 201:
                        alert("Account created!");
                        window.location.href = './MyAccounts.html';
                        break;
                    default:
                        alert("Oops! Something went wrong.");
                }
            }
        });
    }
    else{
        alert("You are not logged in!")
        window.location.href = './Login.html';
    }
}

function GetUserId(){
    var userId = null;
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
            userId = data.userId
        }
    });
    return userId
}