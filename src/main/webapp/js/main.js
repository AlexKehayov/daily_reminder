$(document).on("click", "#save-diary-entry", function (e) {

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            saveDiaryEntry(position.coords.latitude, position.coords.longitude);
        });
    } else {
        saveDiaryEntry(null, null);
    }

});

function saveDiaryEntry(lat, lng){
    let content = $('#diary-content').val();

    $.ajax({
        url: '/dailyReminder/diary/saveEntry',
        type: "post",
        data: {
            content: content,
            lat: lat,
            lng: lng
        },
        success: function (result) {
            var util = UIkit.util;
            var accordionEl = util.$('ul[uk-accordion]');
            UIkit.accordion(accordionEl).toggle();
            $('#diary-content-container').html(result);
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
}