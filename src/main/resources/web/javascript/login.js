// Example starter JavaScript for disabling form submissions if there are invalid fields
(function() {
  'use strict';
  window.addEventListener('load', function() {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName('needs-validation');
    // Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function(form) {
      form.addEventListener('submit', function(event) {
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      }, false);
    });
  }, false);
})();

function loginUser() {
    var username = $('#inputID-User').val()
    var password = $('#inputPassword-User').val()
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8070/users/login/',
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            username: username,
            password: password
        }),
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
            location.replace("proposal.html")
        },
        error: function () {
        }
    });
}

function loginStaff() {
    var username = $('#inputID-Staff').val()
    var password = $('#inputPassword-Staff').val()
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8070/users/login/',
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            username: username,
            password: password
        }),
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
            location.replace("staff.html")
        },
        error: function () {
        }
    });
}