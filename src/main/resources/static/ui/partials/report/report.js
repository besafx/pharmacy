app.controller("reportCtrl", ['DrugService', '$rootScope', '$scope', '$timeout',
    function (DrugService, $rootScope, $scope, $timeout) {

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);


    }]);