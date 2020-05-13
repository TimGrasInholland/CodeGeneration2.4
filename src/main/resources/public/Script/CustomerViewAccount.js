var currentIban = 'NL01INHO6666934694';
var currentAccount = GetAccount(currentIban);

// function ViewAccount(iban) {
//     this.currentAccount = GetAccount(iban);
//     document.getElementById("ibanFrom").innerText = currentAccount.iban;
// }

function SetValues() {
    $("#ibanFrom").val(currentIban);
    $("#currencyTagSmall").text(currentAccount.currency);
    $("#current-iban").text(currentIban);
    $("#account-balance").text(currentAccount.balance.balance.toFixed(2));
    $("#currency-tag").text(currentAccount.currency);
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

$(function () {
    var allTransactions = $('#accounts-box');

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/Users/2/Transactions',
        headers: { "session": "1" },
        success: function(transactions) {
            $.each(transactions, function(i, transaction) {
                allTransactions.append(GetTransaction(transaction));
            });
        }
    });
});

function GetTransaction(transaction) {
    if (transaction.accountFrom == currentIban) {
        return '<div class="transaction-row"><p class="date-tag">'+GetDate(transaction)+'</p><p class="transaction-from-to">TO: </p><p class="transaction-iban">'+transaction.accountTo+'</p><p class="transaction-operator">- </p><p class="transaction-amount">€ '+transaction.amount.toFixed(2)+'</p><p class="currency-tag">EUR</p><div class="hr-line"></div></div>';
    } else {
        return '<div class="transaction-row"><p class="date-tag">'+GetDate(transaction)+'</p><p class="transaction-from-to">FROM: </p><p class="transaction-iban">'+transaction.accountFrom+'</p><p class="transaction-operator">+ </p><p class="transaction-amount">€ '+transaction.amount.toFixed(2)+'</p><p class="currency-tag">EUR</p><div class="hr-line"></div></div>';
    }
}

function GetDate(transaction) {
    var date = new Date(transaction.timestamp);
    var ye = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(date)
    var mo = new Intl.DateTimeFormat('en', { month: 'short' }).format(date)
    var da = new Intl.DateTimeFormat('en', { day: '2-digit' }).format(date)
    date = `${da} ${mo} ${ye}`
    return date;
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

function GetTransactionType(accountTo, currentType) {
    if (GetAccount(accountTo).type == "Savings" && currentType == "Current") {return "Deposit";}
    if (GetAccount(accountTo).type == "Current" && currentType == "Savings") {return "Withdrawal";}
    if (GetAccount(accountTo).type == "Current" && currentType == "Current") {return "Payment";}
}
