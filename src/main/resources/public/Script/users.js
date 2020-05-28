var currentUser;
var isOwner;
window.offset = 0;
var nextpage = false;

function GetUsers(){
    var userId = GetCurrentUserId()

    if(userId != null){
        var offset = null;
        var limit = null;
        var SeachString = $( "input[id=seaching]" ).val()

        if(SeachString != null && SeachString != ""){
            var header = {
                "session": sessionStorage.getItem("session")
            };
            var data = {
                "offset": window.offset,
                "limit": 10,
                "searchname": SeachString 
            };
        }
        else if(nextpage){
            nextpage = false;
            var header = {
                "session": sessionStorage.getItem("session")
            };
            var data = {
                "offset": window.offset,
                "limit": 10,
                "searchname": SeachString 
            };
        }
        else{
            var header = {
                "session": sessionStorage.getItem("session")
            };
            var data = {
                "searchname": SeachString
            };
        }
        $.ajax({
            type: "Get",
            url: "http://localhost:8080/api/Users",
            data: data,
            headers: header,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            complete: function(jqXHR) {
                switch (jqXHR.status) {
                    case 200:
                        break;
                    default:
                        alert("Oops! Something went wrong.");
                }
            },
            success: function(result){
                MakeUser(result);
            }
        });
    }
    else{
        alert("You are not logged in!")
        window.location.href = './Login.html';
    }
}

$(document).ready(function(){
    //Check if element excists in HTML file
    if(document.getElementById("seaching")){
        $('#seaching').on('keyup paste',username_check);
        GetUsers();
        offset = 0;
    }
});

function username_check(){ 
    GetUsers();
}

function MakeUser(users){
    $("#Users-box").empty();
    $.each(users, function(i) {
        console.log(users[i]);
        $( "#Users-box" ).append("<a href='ViewUser.html?id="+users[i].id+"'>"+
            "<div class='user-box'>"+
            "<i class='arrow right'></i>"+
            "<div class='userName'> "+
            users[i].username+
            "</div>"+
            "<address class='Email'>"+
            users[i].email+
            " </address>"+
            "</div>"+
            "</a>");
    });
    $( "#Users-box" ).append(
        "<div id='back' onclick='back()' class='bottomleft'>"+
        "<i class='arrow left'></i>"+
        "</div>");
    $( "#Users-box" ).append(
        "<div id='next' onclick='next()' class='bottomright'>"+
        "<i class='arrow right'></i>"+
        "</div>");
}

function next(){
    offset++;
    nextpage = true;
    GetUsers();
    console.log(offset)
}; 

function back(){
    if(window.offset != 0){
        offset--;
        nextpage = true;
        GetUsers();
    }
    console.log(offset);
}

function CreateUser(){
    var userId = GetCurrentUserId()
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
                        window.location.href = './EmployeeViewUsers.html';
                        break;
                    default:
                        var obj = jQuery.parseJSON(jqXHR.responseText);
                        $.each(obj, function(key,value) {
                          alert(value.defaultMessage);
                        }); 
                        alert (jqXHR.responseText);
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
                        alert("Your account is created!");
                        window.location.href = './Login.html';
                        break;
                    default:
                        var obj = jQuery.parseJSON(jqXHR.responseText);
                        $.each(obj, function(key,value) {
                            if(key == "errors"){
                                $.each(value, function(key,value) {
                                      alert(value.field + ":" +value.defaultMessage);
                                }); 
                            }
                        }); 
                }
            }
        });
    }
}

function LoadMyProfileInfo() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');

    if (id == null) {
        isOwner = true;
        currentUser = GetUser(GetCurrentUserId());
    } else {
        isOwner = false;
        currentUser = GetUser(id);
    }

    $("#username").val(currentUser.username);
    $("#password").val(currentUser.password);
    $("#firstname").val(currentUser.firstName);
    $("#prefix").val(currentUser.prefix);
    $("#lastname").val(currentUser.lastName);
    $("#email").val(currentUser.email);
    $("#birthdate").val(currentUser.birthdate);
    $("#address").val(currentUser.address);
    $("#city").val(currentUser.city);
    $("#postalcode").val(currentUser.postalcode);
    $("#phonenumber").val(currentUser.phoneNumber);

    if (GetCurrentUserRole() == "Customer" || isOwner) {
        document.getElementById("disableUserBtn").style.visibility = "hidden";
    }
}

function disableUser() {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/api/Users",
        data: JSON.stringify({
            active: false,
            address: currentUser.address,
            birthdate: currentUser.birthdate,
            city: currentUser.city,
            email: currentUser.email,
            firstName: currentUser.firstName,
            id: currentUser.id,
            lastName: currentUser.lastName,
            password: currentUser.password,
            phoneNumber: currentUser.phoneNumber,
            postalcode: currentUser.postalcode,
            prefix: currentUser.prefix,
            type: currentUser.type,
            username: currentUser.username
        }),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function(jqXHR) {
            switch (jqXHR.status) {
                case 200:
                    alert("Update Successful.");
                    location.reload();
                    break;
                default:
                    alert("Oops! Something went wrong.");
            }
        }
    });
}

function updateUser() {
    var address = document.getElementById("address").value;
    var birthdate = document.getElementById("birthdate").value;
    var city = document.getElementById("city").value;
    var email = document.getElementById("email").value;
    var firstName = document.getElementById("firstname").value;
    var id = currentUser.id;
    var lastName = document.getElementById("lastname").value;
    var password = document.getElementById("password").value;
    var phoneNumber = document.getElementById("phonenumber").value;
    var postalcode = document.getElementById("postalcode").value;
    var prefix = document.getElementById("prefix").value;
    var type = currentUser.type;
    var username = document.getElementById("username").value;

    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/api/Users",
        data: JSON.stringify({
            active: true,
            address: address,
            birthdate: birthdate,
            city: city,
            email: email,
            firstName: firstName,
            id: id,
            lastName: lastName,
            password: password,
            phoneNumber: phoneNumber,
            postalcode: postalcode,
            prefix: prefix,
            type: type,
            username: username
        }),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function(jqXHR) {
            switch (jqXHR.status) {
                case 200:
                    alert("Update Successful.");
                    break;
                default:
                    alert("Oops! Something went wrong.");
            }
        }
    });
}

function GetUser(id) {
    var user = null;    
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/Users/'+ id,
        headers: { "session": sessionStorage.getItem("session") },
        async: false,
        success: function(result) {
            user = result;
        },
        error: function(error) {
            alert("Something went wrong! " + error);
        }
    });
    return user;
}

$(document).ready(function(){
    if(document.getElementById("selector") && GetCurrentUserId() == null){
        $('#selector').hide();
    }
});