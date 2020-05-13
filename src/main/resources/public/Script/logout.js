function logout() {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/api/Logout",
        headers: {
            "session": sessionStorage.getItem("session")
        },
        success: function(result) {
            confirm(result);
            window.location.href = './Login.html';
        },
        error: function(result) {
            confirm(result.responseText);
            window.location.href = './Login.html';
            
        }
    });
    
}