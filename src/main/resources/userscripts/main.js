$(document).ready(function() {

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
});