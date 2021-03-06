'use strict';

angular.module('newsappApp')
    .factory('News', function ($resource) {
        return $resource('api/newss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.publicationDate = new Date(data.publicationDate);
                    return data;
                }
            }
        });
    });
