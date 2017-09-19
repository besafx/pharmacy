app.controller('billBuyCreateCtrl', ['DrugService', 'DrugUnitService', 'SupplierService', 'ModalProvider', 'BillBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'billBuy',
    function (DrugService, DrugUnitService, SupplierService, ModalProvider, BillBuyService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, billBuy) {

        $timeout(function () {
            $scope.refreshDrugs();
            $scope.refreshSuppliers();
            DrugUnitService.findAll().then(function (data) {
                $scope.drugUnits = data;
            })
        }, 2000);

        $scope.billBuy = billBuy;

        $scope.buffer = {};

        $scope.transactionBuyList = [];

        $scope.title = title;

        $scope.newSupplier = function () {
            ModalProvider.openSupplierCreateModel().result.then(function (data) {
                $scope.suppliers.splice(0, 0, data);
            }, function () {
                console.info('SupplierCreateModel Closed.');
            });
        };

        $scope.newDrug = function () {
            ModalProvider.openDrugCreateModel().result.then(function (data) {
                $scope.drugs.splice(0, 0, data);
            }, function () {
                console.info('DrugCreateModel Closed.');
            });
        };


        $scope.refreshDrugs = function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        };

        $scope.refreshSuppliers = function () {
            SupplierService.findAllCombo().then(function (data) {
                $scope.suppliers = data;
            });
        };

        $scope.calculateCostSum = function () {
            $scope.totalCost = 0;
            if ($scope.transactionBuyList) {
                for (var i = 0; i < $scope.transactionBuyList.length; i++) {
                    var transactionBuy = $scope.transactionBuyList[i];
                    $scope.totalCost = $scope.totalCost + (transactionBuy.unitBuyCost * transactionBuy.quantity);
                }
            }
        };

        $scope.addTransactionBuyToList = function () {
            //Add To Table
            var transactionBuy = {};
            transactionBuy.drug = $scope.buffer.drug;
            transactionBuy.drugUnit = $scope.buffer.drugUnit;
            transactionBuy.unitBuyCost = $scope.buffer.unitBuyCost;
            transactionBuy.unitSellCost = $scope.buffer.unitSellCost;
            transactionBuy.quantity = $scope.buffer.quantity;
            transactionBuy.productionDate = $scope.buffer.productionDate;
            transactionBuy.warrantInMonth = $scope.buffer.warrantInMonth;
            transactionBuy.note = $scope.buffer.note;
            $scope.transactionBuyList.push(transactionBuy);
            $scope.buffer = {};
            $scope.calculateCostSum();
        };

        $scope.removeTransactionBuyFromList = function (index) {
            $scope.transactionBuyList.splice(index, 1);
            $scope.calculateCostSum();
        };

        $scope.submit = function () {
            $scope.billBuy.transactionBuys = $scope.transactionBuyList;
            BillBuyService.create($scope.billBuy).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);