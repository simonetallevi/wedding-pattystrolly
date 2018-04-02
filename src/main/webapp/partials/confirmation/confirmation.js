(function() {
    'use strict';

    angular.module('wedding')
        .controller('ConfirmationCtrl', ConfirmationCtrl);

    ConfirmationCtrl.$inject = ['$log', '$rootScope', '$timeout'];

    function HomeCtrl($log, $rootScope, $timeout) {
        var self = this;

        self.init = function() {
            $rootScope.navBarShrink(false);
        };
    }
})();