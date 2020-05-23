function CreateUser(){
    var userId = GetUserId()

        var newUser = JSON.stringify({
            "username": $( "input[name=username]" ).val(),
            "password": $( "input[name=password]" ).val(),
            "firstName": $( "input[name=firstname]" ).val(),
            "prefix": $( "input[name=prefix]" ).val(),
            "lastName": $( "input[name=lastname]" ).val(),
            "email": $( "input[name=email]" ).val(),
            "birthdate": $( "input[name=birthdate]" ).val(),
            "address": $( "input[name=address]" ).val(),
            "postalcode": $( "input[name=postalcode]" ).val(),
            "city": $( "input[name=city]" ).val(),
            "phoneNumber": $( "input[name=phonenumber]" ).val(),
            "type": $( "select[name=type]" ).val(),
            "active": true
        });
    if(userId != null){
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/Users",
            data: newUser,
            headers: {
                "session": sessionStorage.getItem("session")
            },
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            complete: function(jqXHR) {
                switch (jqXHR.status) {
                    case 201:
                        alert("User created!");
                        window.location.href = './RegisterAccount.html';
                        break;
                    default:
                        alert (jqXHR.error);
                        alert("Oops! Something went wrong.");
                }
            }
        });
    }
    else{
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/Users",
            headers:"",
            data: newUser,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            complete: function(jqXHR) {
                switch (jqXHR.status) {
                    case 201:
                        alert("User created!");
                        window.location.href = './RegisterAccount.html';
                        break;
                    default:
                        alert (jqXHR.error);
                        alert("Oops! Something went wrong.");
                }
            }
        });
    }
}

function GetUserId(){
    var userId = null;
    var session = sessionStorage.getItem("session")
    if(session != null){
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/SessionToken/"+session,
            headers: {
                "session": sessionStorage.getItem("session")
            },
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            async: false,
            success: function(data){
                if(data != null){
                    userId = data.userId
                }
            }
        });
        return userId
    }
    return null;
}

$(document).ready(function(){
        if(GetUserId() == null){
            $('#selector').empty();
        }
});