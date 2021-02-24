function logout() {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8070/users/logout/',
        headers: {
            'X-CSRFToken': cookieValue = document.cookie
                .split('; ')
                .find(row => row.startsWith('csrftoken'))
                .split('=')[1]
        },
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
			location.replace("login.html")
        },
        error: function () {
            alert('Error logging out')
        }
    });
}