$(document).ready(function () {
    operationWhenPageIsLoaded()
});

// Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
  'use strict'

  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  var forms = document.querySelectorAll('.needs-validation')

  // Loop over them and prevent submission
  Array.prototype.slice.call(forms)
    .forEach(function (form) {
      form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        }

        form.classList.add('was-validated')
      }, false)
    })
})()

function operationWhenPageIsLoaded(){
	
}

function voteProposal(titleProposalToVote) {
		$.ajax({
        type: 'GET',
        url: 'http://localhost:8070/proposals/all-active/',
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
			if(response.length==0)
			{
				var tag = '<p> there are not active proposal </p>'
			}
			else
			{
				response.forEach(element => {
					switch (element.proposal) {
						if(element.proposal.title==titleProposalToVote)
						var tag = '<p> proposal' + element.proposal.title + ' voted successfully </p>'
						$('#current-status').append(tag)
					}
				});
			}
        },
        error: function () {
            alert("You do not have permissions, you will logout")
			logout()
        }
    });
}

function createProposal() {
	var title = $('#proposal-title').val()
	alert(neighborhood)
    var description = $('#proposal-description').val()
	var expirationDate = $('#proposal-exp-date').val()
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8070/proposals/create/',
		headers: {
		'X-CSRFToken': cookieValue = document.cookie
			.split('; ')
			.find(row => row.startsWith('csrftoken'))
			.split('=')[1]
        },
        dataType: 'json',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            title: title,
            description: description,
			expiration_date: expirationDate
        }),
        xhrFields: {
            withCredentials: true
        },
        success: function (response) {
            location.reload()
        },
        error: function () {
			alert("Something bad happened when tried to add your proposal")
        }
    });
}
