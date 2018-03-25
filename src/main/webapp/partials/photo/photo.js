(function() {
    'use strict';

    angular.module('wedding')
        .controller('PhotoCtrl', PhotoCtrl);

    PhotoCtrl.$inject = ['$log', '$rootScope', '$scope', 'SERVICE', 'Utils', '$http', '$timeout', '$window'];

    function PhotoCtrl($log, $rootScope, $scope, SERVICE, Utils, $http, $timeout, $window) {
        var self = this;

        self.hasMore = true;
        self.loading = false;
        self.currentPage = 1;
        self.collection = [];
        self.getPerRow = function() {
            return $window.innerWidth > 1000 ? 3 : 2;
        };

        self.loadNext = function(){
            if(!self.loading && self.hasMore){
                console.log("LOAD - NEXT " + self.currentPage);
                self.loadImg();
            }
        }

        self.loadImg = function(){
            self.loading = true;
            var req = Utils.reqConfig("GET", 'https://api.flickr.com/services/rest/');
            req.params['method'] = 'flickr.photos.search';
            req.params['user_id'] = '160363174@N07';
            req.params['api_key'] = '3592844f07dede57633c527de3e2ac3d';
            req.params['format'] = 'json';
            req.params['nojsoncallback'] = '1';
            req.params['extras'] = 'url_l';
            req.params['per_page'] = 50;
            req.params['page'] = self.currentPage;

            $http(req)
                .then(function(resp){
                    console.log(resp);
                    if(!resp.data.photos
                        || !resp.data.photos.photo
                        || resp.data.photos.photo.length == 0){
                        console.log("No photo!");
                        self.hasMore = false;
                    }else{
                        var photos = resp.data.photos.photo;
                        photos.forEach(function(p){
                           self.collection.push({
                               ratio: (p.width_l / p.height_l),
                               color: '#' + ('000000' + Math.floor(Math.random() * 16777215).toString(16)).slice(-6),
                               src: p.url_l
                           });
                        });
                    }

                    self.currentPage++
                    $timeout(function(){
                        self.loading = false;
                    }, 1000);
                }, function(error){
                    console.log(error);
                    $timeout(function(){
                        self.loading = false;
                    }, 1000);
                });
        }

        self.init = function() {
            $window.scrollTo(0, 0);
            $rootScope.navBarShrink(true);
            self.hasMore = true;
            self.loading = false;
            self.currentPage = 1;
            self.collection = [];
            self.loadImg();
        };
    }
})();