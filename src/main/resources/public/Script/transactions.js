var currentIban = "NL01INHO6666134694";
// Via een get doen!

function GetTransactionsFormatEmployee(){
    username = document.getElementById("username").value
    dateStart = document.getElementById("startdate").value
    dateEnd = document.getElementById("enddate").value
    console.log(dateStart);
    
    
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
        success: function(results){
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
                <p class="char"></p>\
                <br>\
                <hr>\
                </object>'
            });
            SetListOfTransactions(output)
        },
        error: function(){
            alert("Could not load all transactions!")
        }
    });
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

function GetTransactionCustommer(transaction) {
    if (transaction.accountFrom == currentIban) {
        return '<div class="transaction-row"><p class="date-tag">'+GetDate(transaction)+'</p><p class="transaction-from-to">TO: </p><p class="transaction-iban">'+transaction.accountTo+'</p><p class="transaction-operator">- </p><p class="transaction-amount">€ '+transaction.amount.toFixed(2)+'</p><p class="currency-tag">EUR</p><div class="hr-line"></div></div>';
    } else {
        return '<div class="transaction-row"><p class="date-tag">'+GetDate(transaction)+'</p><p class="transaction-from-to">FROM: </p><p class="transaction-iban">'+transaction.accountFrom+'</p><p class="transaction-operator">+ </p><p class="transaction-amount">€ '+transaction.amount.toFixed(2)+'</p><p class="currency-tag">EUR</p><div class="hr-line"></div></div>';
    }
}

function GetTransactionType(accountTo, currentType) {
    if (GetAccount(accountTo).type == "Savings" && currentType == "Current") {return "Deposit";}
    if (GetAccount(accountTo).type == "Current" && currentType == "Savings") {return "Withdrawal";}
    if (GetAccount(accountTo).type == "Current" && currentType == "Current") {return "Payment";}
}

function CreateTransaction() {
    var accountTo = document.getElementById("ibanTo").value;
    var description = document.getElementById("description").value;
    var amount = document.getElementById("amount").value;

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/Transactions",
        data: JSON.stringify({
            accountFrom: currentIban,
            accountTo: accountTo,
            amount: amount,
            description: description,
            userPerformingId: GetCurrentUserId(),
            transactionType: GetTransactionType(document.getElementById("ibanTo").value, currentAccount.type)
        }),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function(jqXHR) {
            switch (jqXHR.status) {
                case 201:
                    alert("Transaction Successful.");
                    location.reload();
                    break;
                default:
                    alert("Oops! Something went wrong.");
            }
        }
    });
}

function GetTransactionByCustomerAccountId() {
    var allTransactions = $('#accounts-box');

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/Accounts/'+ GetAccount(currentIban).id +'/Transactions',
        headers: {
            "session": sessionStorage.getItem("session")
        },
        success: function(transactions) {
            $.each(transactions, function(i, transaction) {
                allTransactions.append(GetTransactionCustommer(transaction));
            });
        }
    });
};