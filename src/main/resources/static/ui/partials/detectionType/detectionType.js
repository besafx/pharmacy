app.controller("detectionTypeCtrl", ['DetectionTypeService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (DetectionTypeService, ModalProvider, $scope, $rootScope, $state, $timeout) {



        $timeout(function () {
            $scope.fetchTableData();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);