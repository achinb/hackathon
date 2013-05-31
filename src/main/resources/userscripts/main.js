function notify(emailAddress) {
    var havePermission = window.webkitNotifications.checkPermission();
    if (havePermission == 0) {
        // 0 is PERMISSION_ALLOWED
        var notification = window.webkitNotifications.createNotification(
                'http://summit.bazaarvoice.com/wp-content/themes/summit/library/images/bazaarvoice-logo-green.png',
                'Shopping Assistant',
                'Thanks! We\'ll email you a reminder for your appointment to: ' + emailAddress
        );

        notification.onclick = function () {
            notification.close();
        }
        notification.show();
    } else {
        window.webkitNotifications.requestPermission();
    }
}

$(document).ready(function () {

    var HOST_KEY = '__$BV_ASSISTANT';

    if (location.href.indexOf('ugc.bazaarvoice') > 0) {
        return;
    }

    var shouldInitialize = false;

    // Find if the current page has a IFRAME with
    // a .bazaarvoice.com src

    $('script').each(function (index, script) {
        var source = $(script).prop('src');

        if (source.indexOf('bvapi.js') > 0) {
            shouldInitialize = true;
            return;
        }
    });

    if (!shouldInitialize) return;

    var cssTxt = GM_getResourceText('stylesheet');
    GM_addStyle(cssTxt);

    var injectableHtml = GM_getResourceText('html');
    $('body').prepend(injectableHtml);

    $(window).on('bv:updateSelectedProducts', function (event, newCount) {
        $('#counter').text(newCount);
    });

    function getCurrentDomainProducts() {
        var existingAsString = localStorage.getItem(HOST_KEY);
        if (existingAsString == null) {
            return [];
        } else {
            return JSON.parse(existingAsString);
        }
    }

    $('ul.assistant-options li.view').click(function () {
        var products = getCurrentDomainProducts();
        var currentProduct = location.href;

        if ($.inArray(currentProduct, products) == -1) {
            products.push(currentProduct);
            localStorage.setItem(HOST_KEY, JSON.stringify(products));
            $(window).trigger('bv:updateSelectedProducts', products.length);
        }
    });

    $('ul.assistant-options li.cancel').click(function () {
        $('div#main').slideToggle();
    });

    $('.total_feedback_window .total-minimize').on('click', function () {
        $('div#main').slideToggle();
    });

    $('a.schedule').on('click', function () {
        $('div#main').hide();
        $('div#schedule').show();
    });

    $("#schedule-btn").click(function (evt) {
        evt.preventDefault();

        var data = {userName: $('#name').val(), email: $('#email').val(), urls: getCurrentDomainProducts()};
        /*$.post('http://localhost:5000/addShoppingCart', data, function () {
            localStorage.setItem(HOST_KEY, []);
            $(window).trigger('bv:updateSelectedProducts', getCurrentDomainProducts().length);
        });*/

        $.ajax({
            url: 'http://localhost:5000/addShoppingCart',
            type: "POST",
            data: data,
            success: function () {
                localStorage.setItem(HOST_KEY, "[]");
                $(window).trigger('bv:updateSelectedProducts', getCurrentDomainProducts().length);

                $('div#main').show();
                $('div#schedule').hide();
                $('div#main').slideToggle(900, function() {
                    notify($('#email').val());
                });
            },
            crossDomain: true
        });
    });

    /* GENERAL USAGE - THE CODEZZ */
    $(window).trigger('bv:updateSelectedProducts', getCurrentDomainProducts().length);
    $('div#main').delay(1500).slideToggle();
});