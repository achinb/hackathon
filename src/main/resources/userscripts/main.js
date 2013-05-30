$(document).ready(function() {

    var HOST_KEY = '__$BV_ASSISTANT';

    if (location.href.indexOf('ugc.bazaarvoice') > 0) {
        return;
    }

    var shouldInitialize = false;

    // Find if the current page has a IFRAME with
    // a .bazaarvoice.com src

    $('script').each(function(index, script) {
        var source = $(script).prop('src');

        if (source.indexOf('bvapi.js') > 0) {
            shouldInitialize = true;
            return;
        }
    });

    if(!shouldInitialize) return;

    var cssTxt = GM_getResourceText('stylesheet');
    GM_addStyle(cssTxt);

    var injectableHtml = GM_getResourceText('html');
    $('body').prepend(injectableHtml);

    $(window).on('bv:updateSelectedProducts', function(event, newCount) {
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

    $('ul.assistant-options li').click(function() {
        var products = getCurrentDomainProducts();
        var newProducts;

        if (products.length == 0) {
            newProducts = [location.href];
        } else {
            products.push(location.href);
            newProducts = products;
        }

        localStorage.setItem(HOST_KEY, JSON.stringify(newProducts));
        $(window).trigger('bv:updateSelectedProducts', newProducts.length);
    });

    /* GENERAL USAGE - THE CODEZZ */
    $(window).trigger('bv:updateSelectedProducts', getCurrentDomainProducts().length);
});