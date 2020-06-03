function CreateAccount() {
    var userId = GetCurrentUserId()

    var type = document.getElementById("type")
    type = type.options[type.selectedIndex].value
    var currency = document.getElementById("currency")
    currency = currency.options[currency.selectedIndex].value

    var marker = JSON.stringify({
        "userId": userId,
        "type": type,
        "currency": currency
    })

    $.ajax({
        type: "POST",
        url: baseRequestURL + "/Accounts",
        data: marker,
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function (jqXHR) {
            switch (jqXHR.status) {
                case 201:
                    alert("Account created!");
                    window.location.href = './MyAccounts.html';
                    break;
                default:
                    alert("Oops! Something went wrong. Error statuscode: " + jqXHR.status);
            }
        }
    });
}

function CreateAccountByEmployee() {
    var userId = GetUserByUsername(document.getElementById("username").value)[0].id
    var type = document.getElementById("type")
    type = type.options[type.selectedIndex].value
    var currency = document.getElementById("currency")
    currency = currency.options[currency.selectedIndex].value

    var marker = JSON.stringify({
        "userId": userId,
        "type": type,
        "currency": currency
    })

    $.ajax({
        type: "POST",
        url: baseRequestURL + "/Accounts",
        data: marker,
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        complete: function (jqXHR) {
            switch (jqXHR.status) {
                case 201:
                    alert("Account created!");
                    window.location.href = './EmployeeViewAccounts.html';
                    break;
                default:
                    alert("Oops! Something went wrong. Error statuscode: " + jqXHR.status);
            }
        }
    });
}

function GetAccountsByUserId() {
    $.ajax({
        type: 'GET',
        url: baseRequestURL + '/Users/' + GetCurrentUserId() + '/Accounts',
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        async: false,
        success: function (results) {
            console.log(results)
            currentsOutput = "";
            savingsOutput = "";

            $.each(results, function (i, account) {
                if (account.type == "Current") {
                    currentsOutput +=
                        '<a href="CustomerViewAccount.html?iban=' + account.iban + '" class="clickAccount">\
                        <div class="account">\
                            <p class="iban">' + account.iban + '</p>\
                            <p class="price">' + account.balance.balance.toFixed(2) + '</p>\
                            <p class="currency">EUR</p>\
                            <img src="Images/arrow_right.png" class="arrow-right-accounts">\
                        </div>\
                    </a>'
                } else {
                    savingsOutput +=
                        '<a href="CustomerViewAccount.html?iban=' + account.iban + '" class="clickAccount">\
                        <div class="account">\
                            <p class="iban">' + account.iban + '</p>\
                            <p class="price">' + account.balance.balance.toFixed(2) + '</p>\
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
        error: function (error) {
            console.log(error);
        }
    });
}

function SetListOfAccounts(currentsOutput, savingsOutput) {
    $("#currentsAccounts").html(currentsOutput)
    $("#savingsAccounts").html(savingsOutput)
}

function SetValues() {
    $("#ibanFrom").val(currentIban);
    $("#currencyTagSmall").text(currentAccount.currency);
    $("#current-iban").text(currentIban);
    $("#account-balance").text(currentAccount.balance.balance.toFixed(2));
    $("#currency-tag").text(currentAccount.currency);
}

function GetAccount(iban) {
    var account = null;
    $.ajax({
        type: 'GET',
        url: baseRequestURL + '/Accounts/iban/' + iban,
        headers: {"session": sessionStorage.getItem("session")},
        async: false,
        success: function (result) {
            account = result;
        },
        error: function (error) {
            alert("Given IBAN is incorrect. Statuscode: " + error.status);
        }
    });
    return account;
}

function GetAccounts() {
    var SearchString = $("input[id=seaching]").val()
    var header = {
        "session": sessionStorage.getItem("session")
    };

    if (SearchString != null && SearchString != "") {
        var data = {
            "offset": 0,
            "limit": 100,
            "iban": SearchString
        };
    } else {
        data = {
            "iban": SearchString,
        };
    }
    $.ajax({
        type: "GET",
        url: baseRequestURL + "/Accounts",
        data: data,
        headers: header,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            console.log(result)
            MakeUser(result);
        },
        error: function (error) {
            alert("Oops! Something went wrong. Statuscode: " + error.status);
        }
    });
}

$(document).ready(function () {
    if (document.getElementById("seaching")) {
        $('#seaching').on('keyup paste', iban_check);
        GetAccounts();
    }
});

function iban_check() {
    GetAccounts();
}

function MakeUser(account) {
    $("#Accounts-box").empty();
    $.each(account, function (i) {
        $("#Accounts-box").append("<a href='EmployeeViewAccount.html?iban=" + account[i].iban + "'>" +
            "<div class='Account-box'>" +
            "<i class='arrow right'></i>" +
            "<div class='iban'> " +
            account[i].iban +
            "</div>" +
            "<address class='typeSaving'>" +
            account[i].type +
            " </address>" +
            "</div>" +
            "</a>"
        );
    });
}

function DisableAccount() {
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
        url: baseRequestURL + "/Accounts",
        data: json,
        headers: {
            "session": sessionStorage.getItem("session")
        },
        contentType: "application/json;charset=UTF-8",
        complete: function (jqXHR) {
            switch (jqXHR.status) {
                case 200:
                    alert("Account has been disabled");
                    window.location.href = './EmployeeViewAccounts.html';
                    break;
                default:
                    alert("Oops! Something went wrong.");
            }
        }
    });
}

function GetHeader() {
    var header =
        '<h2 id="current-iban">' + currentIban + '</h2>\
    <h2 id="account-balance">€ ' + GetAccount(currentIban).balance.balance + '</h2>\
    <p class="currency-tag">' + GetAccount(currentIban).currency + '</p>';

    $("#account-header").html(header);
}