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

        var data = {userName: $('#name').val(), email: $('#email').val()};
        $(getCurrentDomainProducts()).each(function(index, product) {
            data["url"] = product;
            $.post('http://localhost:5000/addShoppingCart', data, function () {
                var updatedProducts = $.grep(getCurrentDomainProducts(), function(value) {
                    return value != product;
                });
                localStorage.setItem(HOST_KEY, JSON.stringify(updatedProducts));
                $(window).trigger('bv:updateSelectedProducts', updatedProducts.length);
            });
        });
        $('div#main').show();
        $('div#schedule').hide();
    });

    /* GENERAL USAGE - THE CODEZZ */
    $(window).trigger('bv:updateSelectedProducts', getCurrentDomainProducts().length);
    $('div#main').delay(1500).slideToggle();
});