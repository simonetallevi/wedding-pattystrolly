(function() {
    'use strict';

    angular.module('wedding', ['ngAnimate', 'ngTouch', 'ui.bootstrap',
        'ui.router', 'duScroll', 'hj.gridify', 'infinite-scroll'])

    .config(['$stateProvider', '$urlRouterProvider',
        function($stateProvider, $urlRouterProvider) {

            // Routing
            $stateProvider
                .state('home', {
                    url: "/",
                    templateUrl: "partials/home/home.html",
                    controller: 'HomeCtrl as Home'
                })
                .state('photo', {
                    url: "/photo",
                    templateUrl: "partials/photo/photo.html",
                    controller: 'PhotoCtrl as Photo'
                })
                .state('confirmation', {
                    url: "/confirmation",
                    templateUrl: "partials/confirmation/confirmation.html",
                    controller: 'ConfirmationCtrl as Confirmation'
                });

            $urlRouterProvider.otherwise("/");
        }
    ])

    .directive('backImg', function(){
        return function(scope, element, attrs){
            var url = attrs.backImg;
            element.css({
                'background-image': 'url(' + url +')',
                'background-size' : 'cover',
                'background-position': 'center'
            });
        };
    })

    .directive('setHeight', ['$window', function($window) {
        return {
            link: function(scope, element, attrs) {
                element.css('height', $window.innerHeight + 'px');
            }
        }
    }])

    .directive('googleMap', ['$window', 'MAP', function($window, MAP) {
        var self = this;

        return {
            scope: {
                map: '@',
                marker: '@'
            },
            link: function(scope, element, attrs) {
                var mapOptions = {
                    zoom: 12,
                    center: new google.maps.LatLng(MAP.lat, MAP.long), // New York
                    disableDefaultUI: true,
                    scrollwheel: false,
                    draggable: false,
                    styles: MAP.styles
                };
                scope.map = new google.maps.Map(element[0], mapOptions);

                scope.marker = new google.maps.Marker({
                    map: scope.map,
                    icon: "img/marker.png",
                    draggable: false,
                    animation: google.maps.Animation.DROP,
                    position: { lat: MAP.lat, lng: MAP.long }
                });

                scope.marker.addListener('click', function() {
                    window.open("https://www.google.it/maps/place/Convento+San+Giuseppe/@39.2390316,9.1332176,15z/data=!4m5!3m4!1s0x0:0x6bd66e237029c20!8m2!3d39.2390316!4d9.1332176", '_blank');
                });
            },
            controller: function() {}
        };
    }])

    .run(['$rootScope', '$log', '$timeout',
        function($rootScope, $log, $timeout) {
            $rootScope.initialized = true;
            $rootScope.showSpinner = false;
        }
    ])
})();