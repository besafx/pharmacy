app.controller('transactionSellCreateCtrl', ['DrugService', 'DrugUnitService', 'BillSellService', 'TransactionSellService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billSell',
    function (DrugService, DrugUnitService, BillSellService, TransactionSellService, TransactionBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance, billSell) {

        $timeout(function () {
            $scope.refreshDrugs();
        }, 600);

        $scope.transactionSell = {};

        $scope.transactionSell.billSell = billSell;

        $scope.transactionSell.transactionBuy = {};

        $scope.buffer = {};

        $scope.transactionBuyCalculation = function () {
            DrugUnitService.getRelatedPrices($scope.transactionSell.transactionBuy.id).then(function (data) {
                $scope.relatedPrices = data;
            });
        };

        $scope.transactionBuyCalculation = function () {
            DrugUnitService.getRelatedPrices($scope.transactionSell.transactionBuy.id).then(function (data) {
                $scope.relatedPrices = data;
            });
        };

        $scope.refreshTransactionBuy = function () {
            TransactionBuyService.findByDrug($scope.transactionSell.drug.id).then(function (data) {
                $scope.transactionSell.drug.transactionBuys = data
            });
        };

        $scope.refreshDrugs = function () {
            DrugService.findAllDrugUnitsCombo().then(function (drugs) {
                $scope.drugs = drugs;
            });
        };

        $scope.submit = function () {
            $scope.transactionSell.drugUnit = $scope.buffer.related.obj1;
            $scope.transactionSell.unitCost = $scope.buffer.related.obj3;
            TransactionSellService.create($scope.transactionSell).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);