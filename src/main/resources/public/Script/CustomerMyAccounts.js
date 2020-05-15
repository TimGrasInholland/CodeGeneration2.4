
function GetAccountsByUserId() {
    var accounts = null;
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/Users/'+GetCurrentUserId()+'/Accounts',
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function(results) {
            console.log(results)
            currentsOutput = "";
            savingsOutput = "";

            $.each(results, function (i, account) {
                if (account.type == "Current") {
                    currentsOutput += 
                    '<div class="account">\
                        <p class="iban">'+ account.iban +'</p>\
                        <p class="price">'+ account.balance.balance.toFixed(2) +'</p>\
                        <p class="currency">EUR</p>\
                         <img src="Images/arrow_right.png" class="arrow-right-accounts">\
                    </div>'
                } else{
                    savingsOutput +=
                    '<div class="account">\
                        <p class="iban">'+ account.iban +'</p>\
                        <p class="price">'+ account.balance.balance.toFixed(2) +'</p>\
                        <p class="currency">EUR</p>\
                         <img src="Images/arrow_right.png" class="arrow-right-accounts">\
                    </div>'
                }
            });
            SetListOfAccounts(currentsOutput, savingsOutput)
        },
        error: function(error) {
            console.log(error);     
        }
    });

}


function SetListOfAccounts(currentsOutput, savingsOutput){
    $("#currentsAccounts").html(currentsOutput)
    $("#savingsAccounts").html(savingsOutput)
}


// <div class="currents">
//       <p id="currents-header">Currents</p>
//       <div class="account">
//         <p class="iban">NLxxINHO0xxxxxxxxx</p>
//         <p class="price">100.00</p>
//         <p class="currency">EUR</p>
//         <img src="Images/arrow_right.png" class="arrow-right-accounts">
//       </div>
//       <div class="account">
//         <p class="iban">NLxxINHO0xxxxxxxxx</p>
//         <p class="price">100.00</p>
//         <p class="currency">EUR</p>
//         <img src="Images/arrow_right.png" class="arrow-right-accounts">
//       </div>
//     </div>
//     <div class="savings">
//       <p id="currents-header">Savings</p>
//       <div class="account">
//         <p class="iban">NLxxINHO0xxxxxxxxx</p>
//         <p class="price">200.00</p>
//         <p class="currency">EUR</p>
//         <img src="Images/arrow_right.png" class="arrow-right-accounts">
//       </div>
//     </div>