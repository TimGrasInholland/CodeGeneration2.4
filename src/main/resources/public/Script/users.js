window.offset = 0;
var nextpage = false;
function GetUsers(){
    var userId = GetUserId()

    if(userId != null){

        var offset = null;
        var limit = null;
        var SeachString = $( "input[id=seaching]" ).val()

        if(SeachString != null && SeachString != ""){
            var header = {
                "session": sessionStorage.getItem("session")
            };
            var data = {
                "offset": 0,
                "limit": 100,
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
                "limit": 100,
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
        $('#seaching').on('keyup paste',username_check);
        GetUsers();
        offset = 0;
});

function username_check(){ 
    GetUsers();
}

function MakeUser(users){
    $("#Users-box").empty();
    $.each(users, function(i) {
        $( "#Users-box" ).append("<a href='ViewUser.html?Id="+users[i].id+"'>"+
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

function next(){
    offset++;
    nextpage = true;
    GetUsers();
    console.log(offset);
}

function back(){
    if(window.offset != 0){
        offset--;
        nextpage = true;
        GetUsers();
    }
    console.log(offset);
}