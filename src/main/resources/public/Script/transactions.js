var currentIban = null;
var currentAccount = null;

function SetIban() {
    this.currentIban = getUrlParameter("iban");
    this.currentAccount = GetAccount(currentIban);
}

function GetTransactionsFormatEmployee() {
    username = document.getElementById("username").value
    dateStart = document.getElementById("startdate").value
    dateEnd = document.getElementById("enddate").value
    limit = null;

    if (!username) {
        limit = 100;
        username = null
    }
    if (!dateStart) {
        dateStart = null
    }
    if (!dateEnd) {
        dateEnd = null
    }

    $.ajax({
        type: "GET",
        url: baseRequestURL + "/Transactions",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        data: {
            "username": username,
            "dateFrom": dateStart,
            "dateTo": dateEnd,
            "limit": limit
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (results) {
            console.log(results)
            output = "";
            $.each(results, function () {
                output += '\
                <object>\
                <p class="date">' + GetDateTime(this.timestamp) + '</p>\
                <p class="sender">FROM:</p>\
                <p class="iban">' + this.accountFrom + '</p><br>\
                <p class="sender">TO:</p>\
                <p class="iban">' + this.accountTo + '</p>\
                <p class="currency">' + GetCurrency(this.accountFrom) + '</p>\
                <p class="amount">' + this.amount.toFixed(2) + '</p>\
                <p class="char"></p>\
                <br>\
                <hr>\
                </object>'
            });
            SetListOfTransactions(output)
        },
        error: function () {
            alert("Could not load all transactions!")
        }
    });
}

function GetCurrency(iban) {
    currency = null;
    $.ajax({
        type: "GET",
        url: baseRequestURL + "/Accounts/iban/" + iban,
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function (result) {
            currency = result.currency
        }
    });
    return currency
}

function SetListOfTransactions(output) {
    $("#transactions").html(output)
}

function GetTransactions() {
    $.ajax({
        type: "GET",
        url: baseRequestURL + "/Accounts/" + GetAccount(currentIban).id + "/Transactions",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (results) {
            output = "";
            $.each(results, function (i, transaction) {
                output += GetTransaction(transaction);
            });
            SetListOfTransactions(output)
        },
        error: function () {
            alert("Could not load all transactions!")
        }
    });
}

function GetTransaction(transaction) {
    if (transaction.accountFrom == currentIban) {

        return '<div class="transaction-row">\
                    <p class="date-tag">' + GetDateTime(transaction.timestamp) + '</p>\
                    <p class="transaction-from-to">TO: </p>\
                    <p class="transaction-iban">' + transaction.accountTo + '</p>\
                    <p class="transaction-operator">- </p>\
                    <p class="transaction-amount">' + transaction.amount.toFixed(2) + '</p>\
                    <p class="currency-tag">' + GetCurrency(transaction.accountFrom) + '</p>\
                    <div class="hr-line">\
                </div>';
    } else {
        return '<div class="transaction-row">\
                    <p class="date-tag">' + GetDateTime(transaction.timestamp) + '</p>\
                    <p class="transaction-from-to">FROM: </p>\
                    <p class="transaction-iban">' + transaction.accountFrom + '</p>\
                    <p class="transaction-operator">+ </p>\
                    <p class="transaction-amount">' + transaction.amount.toFixed(2) + '</p>\
                    <p class="currency-tag">' + GetCurrency(transaction.accountFrom) + '</p>\
                    <div class="hr-line">\
                </div>';
    }
}

function GetTransactionCustommer(transaction) {
    if (transaction.accountFrom == currentIban) {
        return '<div class="transaction-row"><p class="date-tag">' + GetDate(transaction) + '</p><p class="transaction-from-to">TO: </p><p class="transaction-iban">' + transaction.accountTo + '</p><p class="transaction-operator">- </p><p class="transaction-amount">€ ' + transaction.amount.toFixed(2) + '</p><p class="currency-tag">EUR</p><div class="hr-line"></div></div>';
    } else {
        return '<div class="transaction-row"><p class="date-tag">' + GetDate(transaction) + '</p><p class="transaction-from-to">FROM: </p><p class="transaction-iban">' + transaction.accountFrom + '</p><p class="transaction-operator">+ </p><p class="transaction-amount">€ ' + transaction.amount.toFixed(2) + '</p><p class="currency-tag">EUR</p><div class="hr-line"></div></div>';
    }
}

function GetTransactionType(accountTo, currentAccount) {
    if (accountTo.userId == currentAccount.userId) {
        if (GetAccount(accountTo).type == "Savings" && currentAccount.type == "Current") {
            return "Deposit";
        }
        if (GetAccount(accountTo).type == "Current" && currentAccount.type == "Savings") {
            return "Withdrawal";
        }
    }
    return "Payment";
}

function CreateTransaction() {
    if (currentAccount == null) {
        currentAccount = GetAccount(document.getElementById("ibanFrom").value)
        var accountFrom = currentAccount.iban;
    } else {
        var accountFrom = currentAccount.iban;
    }
    var accountTo = document.getElementById("ibanTo").value;
    var description = document.getElementById("description").value;
    var amount = document.getElementById("amount").value;

    $.ajax({
        type: "POST",
        url: baseRequestURL + "/Transactions",
        data: JSON.stringify({
            accountFrom: accountFrom,
            accountTo: accountTo,
            amount: amount,
            description: description,
            userPerformingId: GetCurrentUserId(),
            transactionType: GetTransactionType(document.getElementById("ibanTo").value, currentAccount)
        }),
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function (jqXHR) {
            switch (jqXHR.status) {
                case 201:
                    alert(jqXHR.responseText);
                    location.reload();
                    break;
                default:
                    console.log(jqXHR);
                    if (jqXHR.responseJSON.message != null) {
                        alert(jqXHR.responseJSON.message);
                    } else {
                        alert(jqXHR.responseText);
                    }
            }
        }
    });
}

function GetTransactionByCustomerAccountId() {
    var allTransactions = $('#accounts-box');

    $.ajax({
        type: 'GET',
        url: baseRequestURL + '/Accounts/' + GetAccount(currentIban).id + '/Transactions',
        headers: {
            "session": sessionStorage.getItem("session")
        },
        success: function (transactions) {
            $.each(transactions, function (i, transaction) {
                allTransactions.append(GetTransactionCustommer(transaction));
            });
        }
    });
}

// Get the modal
var modal = document.getElementById('createNewTransaction');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}