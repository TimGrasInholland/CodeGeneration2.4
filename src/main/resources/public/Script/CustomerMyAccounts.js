
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
                    '<a href="CustomerViewAccount.html?iban='+account.iban+'" class="clickAccount">\
                        <div class="account">\
                            <p class="iban">'+ account.iban +'</p>\
                            <p class="price">'+ account.balance.balance.toFixed(2) +'</p>\
                            <p class="currency">EUR</p>\
                            <img src="Images/arrow_right.png" class="arrow-right-accounts">\
                        </div>\
                    </a>'
                } else{
                    savingsOutput +=
                    '<a href="CustomerViewAccount.html?iban='+account.iban+'" class="clickAccount">\
                        <div class="account">\
                            <p class="iban">'+ account.iban +'</p>\
                            <p class="price">'+ account.balance.balance.toFixed(2) +'</p>\
                            <p class="currency">EUR</p>\
                            <img src="Images/arrow_right.png" class="arrow-right-accounts">\
                        </div>\
                    </a>'
                }
            });

            if (currentsOutput == "") {
                currentsOutput += 
                '<div class="noAccount">\
                    <p class="noAccountText">You do not have any currents accounts</p>\
                </div>'
            }

            if (savingsOutput == "") {
                savingsOutput += 
                '<div class="noAccount">\
                    <p class="noAccountText">You do not have any savings accounts</p>\
                </div>'
            }
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
