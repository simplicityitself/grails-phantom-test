(function () {
    "use strict";

    var system = require("system");
    var url = system.args[1];

    phantom.viewportSize = {width:1024, height:768};

    var page = require("webpage").create();

    var testsPassed = true;

    page.onError = function(msg, trace) {
        console.error("Test run failed with : " + msg);
        phantom.exit(-1);
    }
    page.onConsoleMessage = function(msg) {

        console.log("MSG is " + msg);
        if (msg && (msg.indexOf("##teamcity[testSuiteFinished name='mocha.suite']") !== -1 || msg.indexOf("</testsuite>") != -1)) {
            phantom.exit(testsPassed ? 0 : -1);
        } else if (msg && msg.indexOf("</failure>") != -1) {
            testsPassed = false;
        }
    }

    page.open(url, function(status){
        if (status !== "success") {
            console.log("unable to load");
            phantom.exit(-1);
        } else {
            window.setTimeout(function() {
                phantom.exit();
            }, 300 * 1000)
        }
    });
}());
