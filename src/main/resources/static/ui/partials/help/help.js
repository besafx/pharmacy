app.controller("helpCtrl", ['$scope', '$timeout', function ($scope, $timeout) {
    $timeout(function () {
        window.componentHandler.upgradeAllRegistered();
    }, 1500);
}]);