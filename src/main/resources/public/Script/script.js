$(function () {
    var allTransactions = $('#accounts-box');

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/Users/2/Transactions',
        headers: { "session": "1" },
        success: function(transactions) {
            $.each(transactions, function(i, transaction) {
                console.log(transaction);
                allTransactions.append(getTransactions(transaction));
            });
        }
    });
});

function getTransactions(transaction) {
    return '<div class="transaction-row"><p class="date-tag">'+transaction.timestamp+'</p><p class="transaction-from-to">FROM: </p><p class="transaction-iban">'+transaction.accountFrom+'</p><p class="transaction-operator">+ </p><p class="transaction-amount">€ '+transaction.amount+'</p><p class="currency-tag">EUR</p><div class="hr-line"></div></div>';
}






// $.ajax({
//     type: "GET",
//     url: "http://localhost:8080/api/Users/2/Transactions",
//     headers: {
//         "session": "1"
//     },
//     statusCode: {
//         200: handle200(data),
//         201: handle201,
//         404: handle404
//     }
// });

// function handle200() {
//     console.log("OK");
    
// }