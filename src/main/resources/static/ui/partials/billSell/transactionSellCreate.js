app.controller('transactionSellCreateCtrl', ['DrugService', 'DrugUnitService', 'BillSellService', 'TransactionSellService', 'TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'billSell',
    function (DrugService, DrugUnitService, BillSellService, TransactionSellService, TransactionBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, billSell) {

        $timeout(function () {
            $scope.refreshDrugs();
        }, 2000);

        $scope.transactionSell = {};

        $scope.transactionSell.billSell = billSell;

        $scope.buffer = {};

        $scope.title = title;

        $scope.selectedTransactionBuy = {};

        $scope.transactionBuyCalculation = function () {
            DrugUnitService.getRelatedPrices($scope.selectedTransactionBuy.id).then(function (data) {
                $scope.relatedPrices = data;
            });
        };

        $scope.setSelectedTransactionBuy = function (object) {
            if (object) {
                angular.forEach($scope.transactionSell.drug.transactionBuys, function (transactionBuy) {
                    if (object.id == transactionBuy.id) {
                        $scope.selectedTransactionBuy = transactionBuy;
                        $scope.transactionBuyCalculation();
                        return transactionBuy.isSelected = true;
                    } else {
                        return transactionBuy.isSelected = false;
                    }
                });
            }
        };

        $scope.transactionBuyCalculation = function () {
            DrugUnitService.getRelatedPrices($scope.selectedTransactionBuy.id).then(function (data) {
                $scope.relatedPrices = data;
            });
        };

        $scope.refreshTransactionBuyByDrug = function () {
            TransactionBuyService.findByDrug($scope.transactionSell.drug.id).then(function (data) {
                $scope.transactionSell.drug.transactionBuys = data
            });
        };

        $scope.refreshBillSell = function () {
            BillSellService.findOne(billSell.id).then(function (data) {
                $scope.transactionSell.billSell = data;
            });
        };

        $scope.refreshDrugs = function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.submit = function () {
            $scope.transactionSell.transactionBuy = $scope.selectedTransactionBuy;
            $scope.transactionSell.drugUnit = $scope.buffer.related.obj1;
            $scope.transactionSell.unitSellCost = $scope.buffer.related.obj3;
            TransactionSellService.create($scope.transactionSell).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);