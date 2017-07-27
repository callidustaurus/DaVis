
var myApp = angular.module("davis", []);



myApp.controller("dataController", function ($scope, $http) {
    $scope.sortInDescendingOrder = true;
    $scope.rowToSort='buchungsTag';

    $http.get("http://localhost:9000/data").
        then(function (response) {
            $scope.csvData = response.data
    })


    $scope.comparison = function (property, value, comparator) {
        return function(item){
            if(value == undefined) return true;
            switch(comparator) {
                case 'greaterThen': return item[property] > value;
                case 'smallerThen': return item[property] < value;
                case 'equal': return item[property] = value;
            }
        }
    }
});
