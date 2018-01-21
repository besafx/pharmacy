app.controller('billBuyCreateCtrl', [
    'DrugService',
    'DrugUnitService',
    'SupplierService',
    'ModalProvider',
    'BillBuyService',
    '$scope',
    '$rootScope',
    '$timeout',
    '$log',
    '$uibModalInstance',
    '$uibModal',
    'title',
    'billBuy',
    function (
        DrugService,
        DrugUnitService,
        SupplierService,
        ModalProvider,
        BillBuyService,
        $scope,
        $rootScope,
        $timeout,
        $log,
        $uibModalInstance,
        $uibModal,
        title,
        billBuy) {

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

        $scope.totalCost = 0;

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

        $scope.openDrugChooser = function () {
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/ui/partials/billBuy/billBuyCreateAddItem.html',
                controller: 'billBuyCreateAddItemCtrl',
                scope: $scope,
                backdrop: 'static',
                keyboard: false
            });

            modalInstance.result.then(function (transactionBuy) {
                $scope.transactionBuyList.push(transactionBuy);
                $scope.calculateCostSum();
            }, function () {
            });
        };

        $scope.removeTransactionBuyFromList = function (index) {
            $scope.transactionBuyList.splice(index, 1);
            $scope.calculateCostSum();
        };

        $scope.submit = function () {
            $scope.billBuy.transactionBuys = $scope.transactionBuyList;
            //Ignore Creating Receipt In case Of Selecting Later Options For Payment Method
            if($scope.billBuy.receipt.paymentMethod!=='Later'){
                var billBuyReceipts = [];
                var billBuyReceipt = {};
                billBuyReceipt.receipt = $scope.billBuy.receipt;
                billBuyReceipts.push(billBuyReceipt);
                $scope.billBuy.billBuyReceipts = billBuyReceipts;
            }
            BillBuyService.create($scope.billBuy).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };



    }]);