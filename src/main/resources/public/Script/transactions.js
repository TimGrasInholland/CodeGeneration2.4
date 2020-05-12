function GetTransactions(){
    username = document.getElementById("username").value
    dateStart = document.getElementById("startdate").value
    dateEnd = document.getElementById("enddate").value
    
    if(!username){
        username = null
    }
    if(!dateStart){
        dateStart = null
    }
    if(!dateEnd){
        dateEnd = null
    }

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/Transactions",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        data: {
            "username": username,
            "dateFrom": dateStart,
            "dateTo": dateEnd
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        statusCode: {
            200: function(results){
                console.log(results)
                output = "";
                $.each(results, function () {
                    output += '\
                    <object>\
                    <p class="date">'+ GetDateTime(this.timestamp) +'</p>\
                    <p class="sender">FROM:</p>\
                    <p class="iban">'+ this.accountFrom +'</p><br>\
                    <p class="sender">TO:</p>\
                    <p class="iban">'+ this.accountTo +'</p>\
                    <p class="currency">'+ GetCurrency(this.accountFrom) +'</p>\
                    <p class="amount">'+ this.amount.toFixed(2) +'</p>\
                    <p class="char">TODO</p>\
                    <br>\
                    <hr>\
                    </object>'
                });
                SetListOfTransactions(output)
            }
        }
    });
}

function GetDateTime(timestamp){
    const date = new Date(timestamp)
    const ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(date)
    const mo = new Intl.DateTimeFormat('en', { month: 'short' }).format(date)
    const da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date)
    return `${da} ${mo} ${ye}`
}

function GetCurrency(iban){
    currency = false;
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/Accounts/iban/"+iban,
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function (result){
            currency = result.currency
        }
    });
    return currency
}

function SetListOfTransactions(output){
    $("#transactions").html(output)
}

function SetDates(){
    var dateFrom = new Date()
    dateFrom.setDate(dateFrom.getDate() - 7)
    dateStart = document.getElementById("startdate").value = dateFrom.toISOString().substr(0, 10)
    var dateTo = new Date()
    dateEnd = document.getElementById("enddate").value = dateTo.toISOString().substr(0, 10)
}
