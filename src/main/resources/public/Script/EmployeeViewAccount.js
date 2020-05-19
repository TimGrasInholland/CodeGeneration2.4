var currentIban = "NL01INHO6666134694";
// Via een get doen?

function GetTransactions(){
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/Accounts/"+GetAccount(currentIban).id+"/Transactions",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(results){
            output = "";
            $.each(results, function (i, transaction) {
                output += GetTransaction(transaction);
            });
            SetListOfTransactions(output)
        },
        error: function(){
            alert("Could not load all transactions!")
        }
    });
}

function GetTransaction(transaction) {
    if (transaction.accountFrom == currentIban) {

        return '<div class="transaction-row">\
                    <p class="date-tag">'+ GetDateTime(transaction.timestamp) +'</p>\
                    <p class="transaction-from-to">TO: </p>\
                    <p class="transaction-iban">'+ transaction.accountTo +'</p>\
                    <p class="transaction-operator">- </p>\
                    <p class="transaction-amount">'+ transaction.amount.toFixed(2) +'</p>\
                    <p class="currency-tag">'+ GetCurrency(transaction.accountFrom) +'</p>\
                    <div class="hr-line">\
                </div>';
    } else {
        return '<div class="transaction-row">\
                    <p class="date-tag">'+ GetDateTime(transaction.timestamp) +'</p>\
                    <p class="transaction-from-to">FROM: </p>\
                    <p class="transaction-iban">'+ transaction.accountFrom +'</p>\
                    <p class="transaction-operator">+ </p>\
                    <p class="transaction-amount">'+ transaction.amount.toFixed(2) +'</p>\
                    <p class="currency-tag">'+ GetCurrency(transaction.accountFrom) +'</p>\
                    <div class="hr-line">\
                </div>';
    }
}

function GetDateTime(timestamp){
    const date = new Date(timestamp)
    const ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(date)
    const mo = new Intl.DateTimeFormat('en', { month: 'short' }).format(date)
    const da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date)
    return `${da} ${mo} ${ye}`
}

function GetCurrency(iban){
    currency = null;
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

function GetHeader(){
    var header = 
    '<h2 id="current-iban">'+currentIban+'</h2>\
    <h2 id="account-balance">€ '+GetAccount(currentIban).balance.balance+'</h2>\
    <p class="currency-tag">'+GetAccount(currentIban).currency+'</p>';
    
    $("#account-header").html(header);
}

function GetAccount(iban) {
    var account = null;
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/Accounts/iban/'+iban,
        headers: { "session": "1" },
        async: false,
        success: function(result) {
            account = result;
        },
        error: function(error) {
            console.log(error);
            
        }
    });
    return account;
}

function DisableAccount(){
    var account = GetAccount(currentIban);
    var json = JSON.stringify({
            id: account.id,
            userId: account.userId,
            type: account.type,
            currency: account.currency,
            balance: account.balance,
            active: false,
            iban: account.iban
        })
    console.log(json);

    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/api/Accounts",
        data: json,
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json;charset=UTF-8",
        complete: function(jqXHR) {
            switch (jqXHR.status) {
                case 200:
                    alert("Account has been disabled");
                    window.location.href = './#jessedeel.html';
                    break;
                default:
                    alert("Oops! Something went wrong.");
            }
        }
    });

}
