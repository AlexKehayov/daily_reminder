$(document).ready(function () {
    $("#diary-filter-date-from").datepicker(
        $.extend(
            {},
            {
                maxDate: "0",
                changeMonth: true,
                changeYear: true,
                showOtherMonths: true,
                selectOtherMonths: false,
                dateFormat: 'dd.mm.yy',
                onSelect: function (date) {
                    $("#diary-filter-date-to").datepicker("option", "minDate", date);
                }
            }
        ));

    $("#diary-filter-date-to").datepicker(
        $.extend(
            {},
            {
                maxDate: "0",
                changeMonth: true,
                changeYear: true,
                showOtherMonths: true,
                selectOtherMonths: false,
                dateFormat: 'dd.mm.yy',
                onSelect: function (date) {
                    $("#diary-filter-date-from").datepicker("option", "maxDate", date);
                }
            }
        ));
});

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
            searchDiary(null, null, null, "1");
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
}

$(document).on("click", ".delete-diary-entry", function (e) {

    let id = $(this).attr('data-id');

    $.ajax({
        url: '/dailyReminder/diary/deleteEntry',
        type: "post",
        data: {
            id: id
        },
        success: function () {
            searchDiary(null, null, null, "1");
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
});

$(document).on("click", "#diaryRecords-filter-clear", function (e) {
    $('#diary-filter-content').val('');
    $('#diary-filter-date-from').val('');
    $('#diary-filter-date-to').val('');
    searchDiary(null, null, null, "1");
});

$(document).on("click", "#diaryRecords-filter-submit", function (e) {
    let content = $('#diary-filter-content').val();
    let dateFrom = $('#diary-filter-date-from').val();
    let dateTo = $('#diary-filter-date-to').val();
    searchDiary(content, dateFrom, dateTo, "1");
});

$(document).on("click", ".diary-page", function (e) {
    let content = $('#diary-filter-content').val();
    let dateFrom = $('#diary-filter-date-from').val();
    let dateTo = $('#diary-filter-date-to').val();
    let currentPage = $(this).val();
    searchDiary(content, dateFrom, dateTo, currentPage);
});

$(document).on("click", ".diary-next-page", function (e) {
    let content = $('#diary-filter-content').val();
    let dateFrom = $('#diary-filter-date-from').val();
    let dateTo = $('#diary-filter-date-to').val();
    let currentPage = parseInt($('#currentPage').val())+1;
    searchDiary(content, dateFrom, dateTo, currentPage);
});

$(document).on("click", ".diary-prev-page", function (e) {
    let content = $('#diary-filter-content').val();
    let dateFrom = $('#diary-filter-date-from').val();
    let dateTo = $('#diary-filter-date-to').val();
    let currentPage = parseInt($('#currentPage').val())-1;
    searchDiary(content, dateFrom, dateTo, currentPage);
});

function searchDiary(content, dateFrom, dateTo, currentPage){
    $.ajax({
        url: '/dailyReminder/diary/searchDiary',
        type: "post",
        data: {
            content: content,
            dateFrom: dateFrom,
            dateTo: dateTo,
            currentPage: currentPage
        },
        success: function (result) {
            $('#tw-diaryRecords').html(result);
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
}

$(document).on("click", ".diary-view-content", function (e) {
    let content = $(this).attr('data-content');
    let createdDate = $(this).attr('data-createdDate');

    $.ajax({
        url: '/dailyReminder/diary/initContentModal',
        type: "post",
        data: {
            content: content,
            createdDate: createdDate
        },
        success: function (result) {
            $('#modal-view').html(result);
            $('#modal-view').addClass('uk-open').show();
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
});

$(document).on("click", ".diary-view-geo-location", function (e) {
    let lat = $(this).attr('data-lat');
    let lng = $(this).attr('data-lng');
    let createdDate = $(this).attr('data-createdDate');

    $.ajax({
        url: '/dailyReminder/diary/initGeoLocationModal',
        type: "post",
        data: {
            lat: lat,
            lng: lng,
            createdDate: createdDate
        },
        success: function (result) {
            $('#modal-location').html(result);
            $('#modal-location').addClass('uk-open').show();
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
});

function initMap() {
    const uluru = {lat: 25.0, lng: 42.0};
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: uluru,
    });
    const marker = new google.maps.Marker({
        position: uluru,
        map: map,
    });
}