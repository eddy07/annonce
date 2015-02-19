'use strict';

angular.module('newsappApp')
    .controller('SaveAuthorController', function ($scope, SaveAuthorResource) {
    
    $scope.create = function(){
        SaveAuthorResource.save($scope.author.firstName, $scope.author.lastName, $scope.author.pseudo, $scope.author.password, $scope.author.email)
        .success(function(data){
            SaveAuthorResource.saveImage(data.id,$scope.author.picture)
            .success(function(data){
                console.log("end with success");
            })
            .error(function(error){
                console.log("error: request stoped");
            });
        })
        .error(function(error){
            console.log("end with error");
    });
});