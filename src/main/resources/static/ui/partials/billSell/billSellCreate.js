app.controller('billSellCreateCtrl', ['DrugService', 'DrugUnitService', 'CustomerService', 'ModalProvider', 'BillSellService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'billSell',
    function (DrugService, DrugUnitService, CustomerService, ModalProvider, BillSellService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, billSell) {

        $timeout(function () {
            $scope.refreshDrugs();
            $scope.refreshCustomers();
            DrugUnitService.findAll().then(function (data) {
                $scope.drugUnits = data;
            })
        }, 2000);

        $scope.billSell = billSell;

        $scope.buffer = {};

        $scope.transactionSellList = [];

        $scope.title = title;

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
            transactionSell.drugUnit = $scope.buffer.drugUnit;
            transactionSell.unitSellCost = $scope.buffer.unitSellCost;
            transactionSell.unitSellCost = $scope.buffer.unitSellCost;
            transactionSell.quantity = $scope.buffer.quantity;
            transactionSell.productionDate = $scope.buffer.productionDate;
            transactionSell.warrantInMonth = $scope.buffer.warrantInMonth;
            transactionSell.note = $scope.buffer.note;
            $scope.transactionSellList.push(transactionSell);
            $scope.buffer = {};
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