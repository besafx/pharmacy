app.controller('outsideSalesDetailsCtrl', ['BillSellService', 'BillSellReceiptService', 'TransactionSellService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$uibModalInstance', '$uibModal', 'billSell',
    function (BillSellService, BillSellReceiptService, TransactionSellService, ModalProvider, $scope, $rootScope, $timeout, $uibModalInstance, $uibModal, billSell) {

        $scope.billSell = billSell;

        $scope.refreshBillSell = function () {
            BillSellService.findOne(billSell.id).then(function (data) {
                $scope.billSell = data;
                $scope.refreshTransactionSells();
                $scope.refreshBillSellReceipts();
            });
        };

        $scope.refreshTransactionSells = function () {
            TransactionSellService.findByBillSell($scope.billSell.id).then(function (data) {
                return $scope.billSell.transactionSells = data;
            });
        };

        $scope.refreshBillSellReceipts = function () {
            BillSellReceiptService.findByBillSell($scope.billSell.id).then(function (data) {
                return $scope.billSell.billSellReceipts = data;
            });
        };

        $scope.newBillSellReceipt = function () {
            ModalProvider.openBillSellReceiptCreateModel($scope.billSell).result.then(function (data) {
                $scope.billSell.billSellReceipts = data.billSellReceipts;
                $scope.billSell.paid = data.paid;
                $scope.billSell.remain = data.remain;
            }, function () {});
        };

        $scope.newTransactionSell = function () {
            ModalProvider.openTransactionSellCreateModel($scope.billSell).result.then(function (data) {
                return $scope.billSell.transactionSells.splice(0, 0, data);
            }, function () {});
        };

        $scope.deleteTransactionSell = function (transactionSell) {
            $rootScope.showConfirmNotify("المبيعات", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                TransactionSellService.remove(transactionSell.id).then(function () {
                    $scope.refreshBillSell();
                });
            });

        };

        $scope.deleteBillSellReceipt = function (billSellReceipt) {
            $rootScope.showConfirmNotify("المبيعات", "هل تود حذف الدفعة فعلاً؟", "error", "fa-trash", function () {
                BillSellReceiptService.remove(billSellReceipt.id).then(function () {
                    $scope.refreshBillSell();
                });
            });

        };

        $scope.updatePrices = function (transactionSell) {
            $rootScope.showConfirmNotify("المبيعات", "هل تود تعديل أسعار الطلبية فعلاً؟", "warning", "fa-edit", function () {
                ModalProvider.openUpdatePricesModel(transactionSell).result.then(function (data) {
                    return transactionSell = data;
                });
            });
        };

        $scope.updateQuantity = function (transactionSell) {
            if (transactionSell) {
                $rootScope.showConfirmNotify("المبيعات", "هل تعديل كمية الطلبية فعلاً؟", "warning", "fa-edit", function () {
                    ModalProvider.openUpdateQuantityModel(transactionSell).result.then(function (data) {
                        return transactionSell = data;
                    });
                });
            }
        };

        $scope.transactionSellRowMenu = [
            {
                html: '<div class="drop-menu">حذف<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_DELETE_ITEM']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteTransactionSell($itemScope.transactionSell);
                }
            },
            {
                html: '<div class="drop-menu">تعديل الأسعار<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_PRICE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.updatePrices($itemScope.transactionSell);
                }
            },
            {
                html: '<div class="drop-menu">تعديل الكمية<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_QUANTITY_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.updateQuantity($itemScope.transactionSell);
                }
            }
        ];

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshBillSell();
        }, 500);

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 300);

    }]);