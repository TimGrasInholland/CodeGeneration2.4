function logout() {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/api/Logout",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        complete: function(jqXHR) {
            switch (jqXHR.status) {
                case 200:
                    alert("You have been logged out.");
                    window.location.href = './Login.html';
                    break;
                default:
                    alert("Oops! Something went wrong.");
            }
        }
    });
    
}