(function() {
    'use-strict';

    angular.module('wedding')
        .constant('SERVICE', {
            pageSize: 100,
            contentType: 'application/x-www-form-urlencoded; charset=utf-8',
            applicationJson: 'application/json',
            endpoint: {}
        })
        .factory("Notification", Notification)
        .factory("Utils", Utils);

    Notification.$inject = ['$rootScope'];
    Utils.$inject = ['SERVICE', '$http'];

    function Notification($rootScope) {
        return {
            toast: function(message) {
                $rootScope.addAlert({
                    type: 'info',
                    msg: message
                });
            },
            error: function(message) {
                $rootScope.addAlert({
                    type: 'danger',
                    msg: message
                });
            },
            success: function(message) {
                $rootScope.addAlert({
                    type: 'success',
                    msg: message
                });
            }
        }
    }

    function Utils(SERVICE, $http) {
        return {
            reqConfig: function(method, url, action) {
                var config = {
                    method: method,
                    url: url,
                    headers: { 'Content-Type': SERVICE.contentType }
                };
                if (method === "GET") {
                    config["params"] = { action: action };
                    return config;
                } else {
                    config["data"] = { action: action };
                    return config;
                }
            }
        }
    }

})();