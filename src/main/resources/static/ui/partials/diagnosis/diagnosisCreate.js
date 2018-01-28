app.controller('diagnosisCreateCtrl', ['DiagnosisService', 'DrugService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'order',
    function (DiagnosisService, DrugService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, order) {

        $scope.diagnosis = {};
        $scope.diagnosis.order = order;

        $timeout(function () {
            $scope.refreshDrugs();
        }, 800);

        $scope.refreshDrugs = function () {
            DrugService.findAllDrugUnitsCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.submit = function () {
            DiagnosisService.create($scope.diagnosis).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);