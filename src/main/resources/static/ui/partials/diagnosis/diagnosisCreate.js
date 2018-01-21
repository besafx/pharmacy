app.controller('diagnosisCreateCtrl', ['DiagnosisService', 'DrugService', 'DrugUnitService', 'OrderService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'order',
    function (DiagnosisService, DrugService, DrugUnitService, OrderService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, order) {

        $scope.buffer = {};
        $scope.diagnosis = {};
        $scope.diagnoses = [];
        $scope.orders = [];
        $scope.order = order;

        $timeout(function () {
            $scope.refreshDrugs();
            $scope.refreshOrders();
        }, 800);

        $scope.refreshDrugs = function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.refreshOrders = function () {
            OrderService.findAllCombo().then(function (data) {
                $scope.orders = data;
            });
        };

        $scope.refreshOrder = function (order) {
            OrderService.findOne(order.id).then(function (data) {
                return $scope.order = data;
            });
        };

        $scope.getRelatedPricesByDrug = function (drug) {
            DrugUnitService.getRelatedPricesByDrug(drug.id).then(function (data) {
                $scope.relatedPrices = data;
            });
        };

        $scope.addDiagnosisToList = function () {
            //Add To Table
            $scope.diagnosis.drugUnit = $scope.buffer.related.obj1;
            $scope.diagnosis.order = $scope.order;
            $scope.diagnoses.push($scope.diagnosis);
            $scope.diagnosis = {};
            $scope.buffer = {};
        };

        $scope.removeDiagnosisFromList = function (index) {
            $scope.diagnoses.splice(index, 1);
        };

        $scope.submit = function () {
            var diagnosisWrapper = {};
            diagnosisWrapper.diagnoses = $scope.diagnoses;
            DiagnosisService.createAll(diagnosisWrapper).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);