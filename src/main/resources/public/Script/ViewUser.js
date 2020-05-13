var currentUser;

function LoadInfo() {
    currentUser = GetUser();
    $("#username").val(currentUser.username);
    $("#password").val(currentUser.password);
    $("#firstname").val(currentUser.firstName);
    $("#prefix").val(currentUser.prefix);
    $("#lastname").val(currentUser.lastName);
    $("#email").val(currentUser.email);
    $("#birthdate").val(currentUser.birthdate);
    $("#address").val(currentUser.address);
    $("#city").val(currentUser.city);
    $("#phonenumber").val(currentUser.phoneNumber);
}

function GetButtons() {
    if (currentUser.type == "Employee") {
        return '\
        <input type="button" class="userBtn" id="disableUserBtn" value="DISABLE USER" name="disableUserBtn">\
        <input type="button" class="userBtn" id="updateUserBtn" value="UPDATE USER" name="updateUserBtn"></input>';
    } else {
        return '<input type="button" class="userBtn" id="updateUserBtn" value="UPDATE USER" name="updateUserBtn"></input>';
    }
}

function GetUser() {
    // if login with Adrie538, goes to "success", but result is empty/null? no error given
    var user = null;    
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/Users/'+ GetCurrentUserId(),
        headers: { "session": "1" },
        async: false,
        success: function(result) {
            user = result;
        },
        error: function(error) {
            console.log(error);
        }
    });
    return user;
}