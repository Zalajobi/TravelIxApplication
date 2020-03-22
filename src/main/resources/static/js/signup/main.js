$(function(){
	$.validate({
        modules : 'security'
    });
});

//Toggle Password
$(".toggle-password").click(function() {

    $(this).toggleClass("fa-eye fa-eye-slash");
    var input = $($(this).attr("toggle"));
    if (input.attr("type") == "password") {
        input.attr("type", "text");
    } else {
        input.attr("type", "password");
    }
});

// Check whether passwords match and disable submit button if password do not match
function checkPassword() {
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('confirmPassword').value;
    
    if (password == confirmPassword) {
        var enableButton = document.getElementById('submit');
        enableButton.disabled = false;
        document.getElementById('passwordmismatch').style.display = "none";
    } else {
        var disableButton = document.getElementById('submit');
        disableButton.disabled = true;
        document.getElementById('passwordmismatch').style.display = "flex";
    }
    
    if (password.length < 5) {
        var disableButton = document.getElementById('submit');
        disableButton.disabled = true;
        document.getElementById('shortPassword').style.display = "flex";
    } else {
        var disableButton = document.getElementById('submit');
        disableButton.disabled = false;
        document.getElementById('shortPassword').style.display = "none";
    }
}

function emptyField() {
    var user = document.getElementById('userName').value;
    var first = document.getElementById('firstName').value;
    var last = document.getElementById('lastName').value;
    var region = document.getElementById('region').value;

    if (user.length < 3) {
        var disableButton = document.getElementById('submit');
        disableButton.disabled = true;
    } if (first.length < 3) {
        var disableButton = document.getElementById('submit');
        disableButton.disabled = true;
    } if (last.length < 3) {
        var disableButton = document.getElementById('submit');
        disableButton.disabled = true;
    } if (region.length < 3) {
        var disableButton = document.getElementById('submit');
        disableButton.disabled = true;
    } else {
        checkPassword();
    }
    //
    // document.getElementById('idline').innerText ="Hello";
    // document.getElementById('idline').style.display = "none";
}

function Get(url) {
    var HttpReq = new XMLHttpRequest();
    HttpReq.open("GET", url, false);
    HttpReq.send(null);
    return HttpReq.responseText;
}

var countries = JSON.parse(
    Get("https://restcountries.eu/rest/v2/all?fields=name")
);
console.log(countries);

countries.map(function(element, index) {
    countries[index] = element.name;
});

// variables
var input = document.querySelector("input");
var results, countries_to_show = [];
var autocomplete_results = document.getElementById("autocomplete-results");

// functions
function autocomplete(val) {
    var countries_returned = [];

    for (i = 0; i < countries.length; i++) {
        if (val === countries[i].toLowerCase().slice(0, val.length)) {
            countries_returned.push(countries[i]);
        }
    }

    return countries_returned;
}

// events
input.onkeyup = function(e) {
    input_val = this.value.toLowerCase();

    if (input_val.length > 0) {
        autocomplete_results.innerHTML = "";
        countries_to_show = autocomplete(input_val);

        for (i = 0; i < countries_to_show.length; i++) {
            autocomplete_results.innerHTML +=
                "<li id=" +
                countries_to_show[i] +
                ' class="list-item">' +
                countries_to_show[i] +
                "</li>";
        }
        autocomplete_results.style.display = "block";
    } else {
        countries_to_show = [];
        autocomplete_results.innerHTML = "";
    }
};

// Get the element, add a click listener...
document
    .getElementById("autocomplete-results")
    .addEventListener("click", function(e) {
        // e.target is the clicked element!
        // If it was a list item
        if (e.target && e.target.nodeName == "LI") {
            // List item found!  Output the value!
            console.log(e.target.innerHTML);
            input.value = e.target.innerHTML;
            autocomplete_results.innerHTML = null; //empty the value
        }
    });
