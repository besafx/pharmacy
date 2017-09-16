app.controller('billSellCreateCtrl', ['TransactionBuyService', 'DrugService', 'DrugUnitService', 'CustomerService', 'ModalProvider', 'BillSellService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'billSell',
    function (TransactionBuyService, DrugService, DrugUnitService, CustomerService, ModalProvider, BillSellService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, billSell) {

        $timeout(function () {
            $scope.refreshDrugs();
            $scope.refreshCustomers();
        }, 2000);

        $scope.billSell = billSell;

        $scope.buffer = {};

        $scope.buffer.drug = {};

        $scope.transactionSellList = [];

        $scope.title = title;

        $scope.buffer.drug.transactionBuys = [];

        $scope.selectedTransactionBuy = {};

        $scope.setSelectedTransactionBuy = function (object) {
            if (object) {
                angular.forEach($scope.buffer.drug.transactionBuys, function (transactionBuy) {
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
            DrugUnitService.getRelatedPrices(
                $scope.selectedTransactionBuy.drugUnit.id,
                $scope.selectedTransactionBuy.quantity,
                $scope.selectedTransactionBuy.drugUnit.factor,
                $scope.selectedTransactionBuy.unitBuyCost,
                $scope.selectedTransactionBuy.unitSellCost
            )
                .then(function (data) {
                    $scope.relatedPrices = data;
                });
        };

        $scope.newCustomer = function () {
            ModalProvider.openCustomerCreateModel().result.then(function (data) {
                $scope.customers.splice(0, 0, data);
            }, function () {
                console.info('CustomerCreateModel Closed.');
            });
        };

        $scope.refreshDrugs = function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.refreshTransactionBuyByDrug = function () {
            TransactionBuyService.findByDrug($scope.buffer.drug.id).then(function (data) {
                $scope.buffer.drug.transactionBuys = data
            });
        };

        $scope.refreshCustomers = function () {
            CustomerService.findAllCombo().then(function (data) {
                $scope.customers = data;
            });
        };

        $scope.calculateCostSum = function () {
            $scope.totalCost = 0;
            if ($scope.transactionSellList) {
                for (var i = 0; i < $scope.transactionSellList.length; i++) {
                    var transactionSell = $scope.transactionSellList[i];
                    $scope.totalCost = $scope.totalCost + (transactionSell.unitSellCost * transactionSell.quantity);
                }
            }
        };

        $scope.addTransactionSellToList = function () {
            //Add To Table
            var transactionSell = {};
            transactionSell.drug = $scope.buffer.drug;
            transactionSell.drugUnit = $scope.buffer.related.obj1;
            transactionSell.unitSellCost = $scope.buffer.related.obj3;
            transactionSell.quantity = $scope.buffer.quantity;
            transactionSell.note = $scope.buffer.note;
            $scope.transactionSellList.push(transactionSell);
            $scope.buffer = {};
            $scope.relatedPrices = {};
            $scope.calculateCostSum();
        };

        $scope.removeTransactionSellFromList = function (index) {
            $scope.transactionSellList.splice(index, 1);
            $scope.calculateCostSum();
        };

        $scope.submit = function () {
            $scope.billSell.transactionSells = $scope.transactionSellList;
            BillSellService.create($scope.billSell).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);