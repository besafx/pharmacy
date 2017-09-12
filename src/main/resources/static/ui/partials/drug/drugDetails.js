app.controller('drugDetailsCtrl', ['DrugService', 'FalconService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', '$uibModal', 'drug',
    function (DrugService, FalconService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, $uibModal, drug) {

        $scope.drug = drug;

        $scope.refreshDrug = function () {
            DrugService.findOne($scope.drug.id).then(function (data) {
                $scope.drug = data;
            })
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);