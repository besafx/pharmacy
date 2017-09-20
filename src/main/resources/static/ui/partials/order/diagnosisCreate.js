app.controller('diagnosisCreateCtrl', ['DiagnosisService', 'DrugService', 'DrugUnitService', 'OrderDetectionTypeService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'orderDetectionType',
    function (DiagnosisService, DrugService, DrugUnitService, OrderDetectionTypeService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, orderDetectionType) {

        $scope.diagnosis = {};

        $scope.buffer = {};

        $timeout(function () {
            $scope.refreshDrugs();
        }, 2000);

        $scope.refreshDrugs = function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.getRelatedPricesByDrug = function (drug) {
            DrugUnitService.getRelatedPricesByDrug(drug.id).then(function (data) {
                $scope.relatedPrices = data;
            });
        };

        OrderDetectionTypeService.findOne(orderDetectionType.id).then(function (data) {
            $scope.diagnosis.orderDetectionType = data;
        });

        $scope.submit = function () {
            $scope.diagnosis.drugUnit = $scope.buffer.related.obj1;
            DiagnosisService.create($scope.diagnosis).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);