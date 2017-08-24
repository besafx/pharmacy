app.controller("aboutCtrl", ['$rootScope', '$scope', '$timeout', '$log',
    function ($rootScope, $scope, $timeout, $log) {
        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);
    }]);