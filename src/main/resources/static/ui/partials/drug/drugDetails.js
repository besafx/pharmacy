app.controller('drugDetailsCtrl', ['DrugService', 'DrugUnitService', 'TransactionBuyService', 'TransactionSellService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', '$uibModal', 'drug',
    function (DrugService, DrugUnitService, TransactionBuyService, TransactionSellService,  ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, $uibModal, drug) {

        $scope.drug = drug;

        $scope.refreshDrug = function () {
            DrugService.findOne($scope.drug.id).then(function (data) {
                $scope.drug = data;
                $scope.refreshDrugUnitsByDrug();
                $scope.refreshTransactionBuyByDrug();
                $scope.refreshTransactionSellByDrug();
            })
        };

        $scope.refreshDrugUnitsByDrug = function () {
            DrugUnitService.findByDrug($scope.drug.id).then(function (data) {
                return $scope.drug.drugUnits = data;
            });
        };

        $scope.newDrugUnit = function () {
            ModalProvider.openDrugUnitCreateModel($scope.drug).result.then(function (data) {
                return $scope.drug.drugUnits.splice(0, 0, data);
            }, function () {});
        };

        $scope.deleteDrugUnit = function (drugUnit) {
            $rootScope.showConfirmNotify("الدواء", "هل تود حذف الوحدة فعلا ًوكل ما يتعلق بها من حسابات هامة؟", "error", "fa-trash", function () {
                DrugUnitService.remove(drugUnit.id).then(function () {
                    var index = $scope.drug.drugUnits.indexOf(drugUnit);
                    return $scope.drug.drugUnits.splice(index, 1);
                });
            });
        };

        $scope.refreshTransactionBuyByDrug = function () {
            TransactionBuyService.findByDrug($scope.drug.id).then(function (data) {
                return $scope.drug.transactionBuys = data;
            });
        };

        $scope.newTransactionBuy = function () {
            ModalProvider.openDrugTransactionBuyCreateModel($scope.drug).result.then(function (data) {
                return $scope.drug.transactionBuys.splice(0, 0, data);
            }, function () {});
        };

        $scope.updatePrices = function (transactionBuy) {
            $rootScope.showConfirmNotify("الدواء", "هل تعديل أسعار الطلبية فعلاً؟", "warning", "fa-edit", function () {
                ModalProvider.openUpdatePricesModel(transactionBuy).result.then(function (data) {
                    return transactionBuy = data;
                });
            });
        };

        $scope.updateQuantity = function (transactionBuy) {
            $rootScope.showConfirmNotify("الدواء", "هل تعديل كمية الطلبية فعلاً؟", "warning", "fa-edit", function () {
                ModalProvider.openUpdateQuantityModel(transactionBuy).result.then(function (data) {
                    return transactionBuy = data;
                });
            });
        };

        $scope.deleteTransactionBuy = function (transactionBuy) {
            $rootScope.showConfirmNotify("الدواء", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                TransactionBuyService.remove(transactionBuy.id).then(function () {
                    var index = $scope.drug.transactionBuys.indexOf(transactionBuy);
                    return $scope.drug.transactionBuys.splice(index, 1);
                });
            });
        };

        $scope.refreshTransactionSellByDrug = function () {
            TransactionSellService.findByDrug($scope.drug.id).then(function (data) {
                return $scope.drug.transactionSells = data;
            });
        };

        $scope.deleteTransactionSell = function (transactionSell) {
            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                TransactionSellService.remove(transactionSell.id).then(function () {
                    var index = $scope.drug.transactionSells.indexOf(transactionSell);
                    return $scope.drug.transactionSells.splice(index, 1);
                });
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshDrug();
        }, 500);

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 300);

    }]);