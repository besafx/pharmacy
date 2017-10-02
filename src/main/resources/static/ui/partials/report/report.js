app.controller("reportCtrl", ['$scope', '$timeout', function ($scope, $timeout) {
    $timeout(function () {
        window.componentHandler.upgradeAllRegistered();
    }, 1500);
}]);