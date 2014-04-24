function checkRegistration() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/registrating', true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if(xhr.status == 200) {
                if (xhr.responseText.indexOf("You're successfully registered!") > -1) {
                    $('#message').text("You're successfully registered!");
                    $('#regForm').hide();
                    $('#auth').show();
                    clearInterval(interval);
                } else if (xhr.responseText.indexOf("Error! This login is occupied.") > -1) {
                    $('#message').text("Error! This login is occupied.");
                    clearInterval(interval);
                } else if (xhr.responseText != "") {
                    console.log(xhr.responseText);
                    clearInterval(interval);
                }
            }
        }
    };
    xhr.send(null);
}

function checkRegistrationLoop() {
    interval = setInterval(checkRegistration, 5000);
}
