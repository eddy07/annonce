'use strict';

angular.module('newsappApp')
    .factory('SaveAuthorResource', ['$http',function ($http) {
        var service = {};
        service.saveAuthor = function(firstName,lastName,pseudo,password,email){
             return $http.post('http://localhost:8080/api/authors?firstName='+firstName+'&lastName='+lastName
                               +'&pseudo='+pseudo+'&password='+password+'&email='+email);

         service.saveImage = function(id,picture){
             return $http.post({
                 url: 'localhost:8080/api/picture',
                 datatype: 'multipart/data-form',
                 data: id, picture
             });
        return service;
    }]);
