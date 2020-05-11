// var xhttp = new XMLHttpRequest();
// xhttp.onreadystatechange = function() {
//     if (this.status == 200) {

//         console.log(this.response);
//     }
// };
// xhttp.open("GET", "http://localhost:8080/api/Accounts", true);
// xhttp.setRequestHeader("session", "1");
// xhttp.send();

var markers = JSON.stringify({
    "username": "Inholasdadwawad",
    "password": "Welcome567?waddaw",
    "firstName": "Bankwadadw",
    "prefix": "awdwa",
    "lastName": "Inholwadadwland",
    "email": "bank@dwddwaadwwadwnd-bank.nl",
    "birthdate": "2019-01-01",
    "address": "Arnold straat 33",
    "postalcode": "1354PK",
    "city": "Haarlem",
    "phoneNumber": "0638313905",
    "type": "Customer",
    "active": true
});

$.ajax({
    type: "POST",
    url: "http://localhost:8080/api/Users",
    data: markers,
    headers: {
        "session": "1"
    },
    contentType: "application/json; charset=utf-8",
    dataType: "json",
    // created: function(data) {
    //     console.log(data);
    //     console.log("wodjaoiwdj");
        
    // },
    // success: function(data){
    //     console.log(data);
    //     console.log("askjaiosjijoadw");
        
    // },
    // failure: function(errMsg) {
    //     alert(errMsg);
    // },
    statusCode: {
        200: handle200,
        201: handle201,
        404: handle404
    }
});

function handle200() {
    console.log("200");
    
}

function handle201() {
    console.log("201");
    
}

function handle404() {
    console.log("404");
    
}
