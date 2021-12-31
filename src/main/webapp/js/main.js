//COMMON

function initMapCommon(){

    $('#mapCommon').show();

    var diaryRecords = $('#diaryRecordsJson').val();
    let arr = JSON.parse(diaryRecords)

    var map = new ol.Map({
        target: 'mapCommon',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
            })
        ],
        view: new ol.View({
            center: ol.proj.fromLonLat([arr[0].geoLng, arr[0].geoLat]),
            zoom: 7
        })
    });

    for( var i=0; i<arr.length; i++){

        var mar = new ol.Feature({
            geometry: new ol.geom.Point(ol.proj.fromLonLat([arr[i].geoLng, arr[i].geoLat]))
        })
        var iconBlue = new ol.style.Style({
            text: new ol.style.Text({
                text: arr[i].id.toString(),
                scale: 1.2,
                fill: new ol.style.Fill({
                    color: "#fff"
                }),
                stroke: new ol.style.Stroke({
                    color: "0",
                    width: 3
                })
            })
        });
        mar.setStyle(iconBlue);

        var layer = new ol.layer.Vector({
            source: new ol.source.Vector({
                features: [
                    mar
                ]
            })
        });
        map.addLayer(layer);
    }

    map.updateSize();
}

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
                firstDay: 1,
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
                firstDay: 1,
                onSelect: function (date) {
                    $("#diary-filter-date-from").datepicker("option", "maxDate", date);
                }
            }
        ));

    //Organizer filters
    $("#organizer-filter-from-date").datepicker(
        $.extend(
            {},
            {
                changeMonth: true,
                changeYear: true,
                showOtherMonths: true,
                selectOtherMonths: false,
                dateFormat: 'dd.mm.yy',
                firstDay: 1,
                onSelect: function (date) {
                    $("#organizer-filter-to-date").datepicker("option", "minDate", date);
                }
            }
        ));

    $("#organizer-filter-to-date").datepicker(
        $.extend(
            {},
            {
                changeMonth: true,
                changeYear: true,
                showOtherMonths: true,
                selectOtherMonths: false,
                dateFormat: 'dd.mm.yy',
                firstDay: 1,
                onSelect: function (date) {
                    $("#organizer-filter-from-date").datepicker("option", "maxDate", date);
                }
            }
        ));

});

//DIARY
$(document).on("click", "#save-diary-entry", function (e) {

    let val = $("#diary-is-not-current-location").is(":checked");

    if(!val){
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                saveDiaryEntry(position.coords.latitude, position.coords.longitude, null);
            });
        } else {
            saveDiaryEntry(null, null, null);
        }
    } else {
        let lat = $("#diary-geo-lat").val();
        let lng = $("#diary-geo-lng").val();
        let place = $("#diary-geo-place").val();
        saveDiaryEntry(lat, lng, place);
    }

});

function saveDiaryEntry(lat, lng, place) {
    let content = $('#diary-content').val();

    $.ajax({
        url: '/dailyReminder/diary/saveEntry',
        type: "post",
        data: {
            content: content,
            lat: lat,
            lng: lng,
            place: place
        },
        success: function (result) {
            $('#diary-content-container').html(result);
            if ($('#errors').val() == null || $('#errors').val() == ''){
                UIkit.accordion(UIkit.util.$('ul[uk-accordion]')).toggle();
            }
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
    let currentPage = parseInt($('#currentPage').val()) + 1;
    searchDiary(content, dateFrom, dateTo, currentPage);
});

$(document).on("click", ".diary-prev-page", function (e) {
    let content = $('#diary-filter-content').val();
    let dateFrom = $('#diary-filter-date-from').val();
    let dateTo = $('#diary-filter-date-to').val();
    let currentPage = parseInt($('#currentPage').val()) - 1;
    searchDiary(content, dateFrom, dateTo, currentPage);
});

function searchDiary(content, dateFrom, dateTo, currentPage) {
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
            initMapCommon();
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

//ORGANIZER
$(document).on("click", "#save-organizer-entry", function (e) {
    saveOrganizerEntry();
});

function saveOrganizerEntry() {
    let title = $('#organizer-title').val();
    let content = $('#organizer-content').val();
    let isFixedDate = $('#organizer-is-fixed-date').is(":checked");
    let fixedDate = $('#organizer-fixed-date').val();
    let fromDate = $('#organizer-from-date').val();
    let toDate = $('#organizer-to-date').val();
    let isFixedTime = $('#organizer-is-fixed-time').is(":checked");
    let fixedTime = $('#organizer-fixed-time').val();
    let fromTime = $('#organizer-from-time').val();
    let toTime = $('#organizer-to-time').val();
    let geoLat = $('#organizer-geo-lat').val();
    let geoLng = $('#organizer-geo-lng').val();
    let geoPlace = $('#organizer-geo-place').val();

    $.ajax({
        url: '/dailyReminder/organizer/saveEntry',
        type: "post",
        data: {
            title: title,
            content: content,
            isFixedDate: isFixedDate,
            fixedDate: fixedDate,
            fromDate: fromDate,
            toDate: toDate,
            isFixedTime: isFixedTime,
            fixedTime: fixedTime,
            fromTime: fromTime,
            toTime: toTime,
            geoLat: geoLat,
            geoLng: geoLng,
            geoPlace: geoPlace
        },
        success: function (result) {
            $('#organizer-content-container').html(result);
            if ($('#errors').val() == null || $('#errors').val() == ''){
                UIkit.accordion(UIkit.util.$('ul[uk-accordion]')).toggle();
            }
            searchOrganizer(null, null, null, null, "1");
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
}

$(document).on("click", ".delete-organizer-entry", function (e) {

    let id = $(this).attr('data-id');

    $.ajax({
        url: '/dailyReminder/organizer/deleteEntry',
        type: "post",
        data: {
            id: id
        },
        success: function () {
            searchOrganizer(null, null, null, null, "1");
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
});

$(document).on("click", "#organizerRecords-filter-clear", function (e) {
    $('#organizer-filter-content').val('');
    $('#organizer-filter-title').val('');
    $('#organizer-filter-from-date').val('');
    $('#organizer-filter-to-date').val('');
    searchOrganizer(null, null, null, null, "1");
});

$(document).on("click", "#organizerRecords-filter-submit", function (e) {
    let title = $('#organizer-filter-title').val();
    let content = $('#organizer-filter-content').val();
    let fromDate = $('#organizer-filter-from-date').val();
    let toDate = $('#organizer-filter-to-date').val();
    searchOrganizer(title, content, fromDate, toDate, "1");
});

$(document).on("click", ".organizer-page", function (e) {
    let title = $('#organizer-filter-title').val();
    let content = $('#organizer-filter-content').val();
    let fromDate = $('#organizer-filter-from-date').val();
    let toDate = $('#organizer-filter-to-date').val();
    let currentPage = $(this).val();
    searchOrganizer(title, content, fromDate, toDate, currentPage);
});

$(document).on("click", ".organizer-next-page", function (e) {
    let title = $('#organizer-filter-title').val();
    let content = $('#organizer-filter-content').val();
    let fromDate = $('#organizer-filter-from-date').val();
    let toDate = $('#organizer-filter-to-date').val();
    let currentPage = parseInt($('#currentPage').val()) + 1;
    searchOrganizer(title, content, fromDate, toDate, currentPage);
});

$(document).on("click", ".organizer-prev-page", function (e) {
    let title = $('#organizer-filter-title').val();
    let content = $('#organizer-filter-content').val();
    let fromDate = $('#organizer-filter-from-date').val();
    let toDate = $('#organizer-filter-to-date').val();
    let currentPage = parseInt($('#currentPage').val()) - 1;
    searchOrganizer(title, content, fromDate, toDate, currentPage);
});

function searchOrganizer(title, content, fromDate, toDate, currentPage) {
    $.ajax({
        url: '/dailyReminder/organizer/searchOrganizer',
        type: "post",
        data: {
            title: title,
            content: content,
            fromDate: fromDate,
            toDate: toDate,
            currentPage: currentPage
        },
        success: function (result) {
            $('#tw-organizerRecords').html(result);
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
}

$(document).on("click", ".organizer-view-content", function (e) {
    let id = $(this).attr('data-id');

    $.ajax({
        url: '/dailyReminder/organizer/initContentModal',
        type: "post",
        data: {
            id: id
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

$(document).on("click", ".organizer-view-geo-location", function (e) {
    let id = $(this).attr('data-id');

    $.ajax({
        url: '/dailyReminder/organizer/initGeoLocationModal',
        type: "post",
        data: {
            id: id
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

$(document).on("change", "#organizer-is-fixed-date", function (e) {
    let val = $(this).is(":checked");
    if(val){
        $('#organizer-fixed-date').prop('disabled', false).val('');
        $('#organizer-from-date').prop('disabled', true).val('');
        $('#organizer-to-date').prop('disabled', true).val('');
    } else {
        $('#organizer-fixed-date').prop('disabled', true).val('');
        $('#organizer-from-date').prop('disabled', false).val('');
        $('#organizer-to-date').prop('disabled', false).val('');
    }
});

$(document).on("change", "#organizer-is-fixed-time", function (e) {
    let val = $(this).is(":checked");
    if(val){
        $('#organizer-fixed-time').prop('disabled', false).val('');
        $('#organizer-from-time').prop('disabled', true).val('');
        $('#organizer-to-time').prop('disabled', true).val('');
    } else {
        $('#organizer-fixed-time').prop('disabled', true).val('');
        $('#organizer-from-time').prop('disabled', false).val('');
        $('#organizer-to-time').prop('disabled', false).val('');
    }
});

$(document).on("input", ".geo-place", function (e) {
    let name = $(this).val();
    $.ajax({
        url: '/dailyReminder/organizer/locationsAutoComplete',
        type: "post",
        data: {
            name: name
        },
        success: function (result) {
            $('#location-dropdown-wrapper').html(result);
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
});

$(document).on("click", ".place-option", function (e) {
    let locationName = $(this).attr('data-locationName');
    let lat = $(this).attr('data-lat');
    let lng = $(this).attr('data-lng');
    $('.geo-place').val(locationName);
    $('.geo-lat').val(lat);
    $('.geo-lng').val(lng);
});

//ADMIN
$(document).on("click", "#save-user-entry", function (e) {
    let username = $(this).attr('data-username');
    let diaryRead = $('#diary-read').is(":checked");
    let diaryWrite = $('#diary-write').is(":checked");
    let organizerRead = $('#organizer-read').is(":checked");
    let organizerWrite = $('#organizer-write').is(":checked");
    let isEnabled = $('#user-enabled').is(":checked");
    saveUserEntry(username, diaryRead, diaryWrite, organizerRead, organizerWrite, isEnabled);
});

function saveUserEntry(username, diaryRead, diaryWrite, organizerRead, organizerWrite, isEnabled) {

    $.ajax({
        url: '/dailyReminder/admin/saveEntry',
        type: "post",
        data: {
            username: username,
            diaryRead: diaryRead,
            diaryWrite: diaryWrite,
            organizerRead: organizerRead,
            organizerWrite: organizerWrite,
            isEnabled: isEnabled
        },
        success: function () {
            $('.uk-modal-close-default').click();
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
}

$(document).on("click", ".delete-user-entry", function (e) {

    let username = $(this).attr('data-username');

    $.ajax({
        url: '/dailyReminder/admin/deleteEntry',
        type: "post",
        data: {
            username: username
        },
        success: function () {
            searchUsers(null, null, null, null, "1");
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
});

$(document).on("click", "#users-filter-clear", function (e) {
    $('#user-filter-username').val('');
    $('#user-filter-first-name').val('');
    $('#user-filter-last-name').val('');
    $('#user-filter-email').val('');
    searchUsers(null, null, null, null, "1");
});

$(document).on("click", "#users-filter-submit", function (e) {
    let username = $('#user-filter-username').val();
    let firstName = $('#user-filter-first-name').val();
    let lastName = $('#user-filter-last-name').val();
    let email = $('#user-filter-email').val();
    searchUsers(username, firstName, lastName, email, "1");
});

$(document).on("click", ".users-page", function (e) {
    let username = $('#user-filter-username').val();
    let firstName = $('#user-filter-first-name').val();
    let lastName = $('#user-filter-last-name').val();
    let email = $('#user-filter-email').val();
    let currentPage = $(this).val();
    searchUsers(username, firstName, lastName, email, currentPage);
});

$(document).on("click", ".users-next-page", function (e) {
    let username = $('#user-filter-username').val();
    let firstName = $('#user-filter-first-name').val();
    let lastName = $('#user-filter-last-name').val();
    let email = $('#user-filter-email').val();
    let currentPage = parseInt($('#currentPage').val()) + 1;
    searchUsers(username, firstName, lastName, email, currentPage);
});

$(document).on("click", ".users-prev-page", function (e) {
    let username = $('#user-filter-username').val();
    let firstName = $('#user-filter-first-name').val();
    let lastName = $('#user-filter-last-name').val();
    let email = $('#user-filter-email').val();
    let currentPage = parseInt($('#currentPage').val()) - 1;
    searchUsers(username, firstName, lastName, email, currentPage);
});

function searchUsers(username, firstName, lastName, email, currentPage) {
    $.ajax({
        url: '/dailyReminder/admin/searchUsers',
        type: "post",
        data: {
            username: username,
            firstName: firstName,
            lastName: lastName,
            email: email,
            currentPage: currentPage
        },
        success: function (result) {
            $('#tw-users').html(result);
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
}

$(document).on("click", ".user-configure", function (e) {
    let username = $(this).attr('data-username');
    $.ajax({
        url: '/dailyReminder/admin/initConfigureModal',
        type: "post",
        data: {
            username: username
        },
        success: function (result) {
            $('#modal-configure').html(result);
            $('#modal-configure').addClass('uk-open').show();
        },
        error: function () {
            alert("An unexpected error occurred... Please try again.")
        }
    });
});

$(document).on("change", "#diary-is-not-current-location", function (e) {
    let val = $(this).is(":checked");
    if(!val){
        $('#diary-geo-place').prop('disabled', true).val('');
        $('#diary-geo-lat').val('');
        $('#diary-geo-lng').val('');
    } else {
        $('#diary-geo-place').prop('disabled', false).val('');
    }
});
