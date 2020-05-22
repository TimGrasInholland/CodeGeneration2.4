var currentUser;
var isOwner;

function LoadInfo() {
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
                case 201:
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
                case 201:
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