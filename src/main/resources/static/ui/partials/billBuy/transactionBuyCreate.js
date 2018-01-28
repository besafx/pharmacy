app.controller('transactionBuyCreateCtrl', ['DrugService', 'DrugUnitService', 'BillBuyService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billBuy',
    function (DrugService, DrugUnitService, BillBuyService, TransactionBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance, billBuy) {

        $timeout(function () {
            $scope.refreshDrugs();
        }, 600);

        $scope.transactionBuy = {};

        $scope.transactionBuy.billBuy = billBuy;

        $scope.buffer = {};

        $scope.refreshBillBuy = function () {
            BillBuyService.findOne(billBuy.id).then(function (data) {
                $scope.transactionBuy.billBuy = data;
            });
        };

        $scope.refreshDrugs = function () {
            DrugService.findAllDrugUnitsCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.submit = function () {
            TransactionBuyService.create($scope.transactionBuy).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);