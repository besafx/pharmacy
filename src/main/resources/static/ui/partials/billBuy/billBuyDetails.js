app.controller('billBuyDetailsCtrl', ['BillBuyService', 'BillBuyReceiptService', 'TransactionBuyService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$uibModalInstance', '$uibModal', 'billBuy',
    function (BillBuyService, BillBuyReceiptService, TransactionBuyService, ModalProvider, $scope, $rootScope, $timeout, $uibModalInstance, $uibModal, billBuy) {

        $scope.billBuy = billBuy;

        $scope.refreshBillBuy = function () {
            BillBuyService.findOne(billBuy.id).then(function (data) {
                $scope.billBuy = data;
            });
        };

        $scope.refreshTransactionBuyByBill = function () {
            TransactionBuyService.findByBillBuy($scope.billBuy.id).then(function (data) {
                return $scope.billBuy.transactionBuys = data;
            });
        };

        $scope.refreshBillBuyReceipts = function () {
            BillBuyReceiptService.findByBillBuy($scope.billBuy.id).then(function (data) {
                return $scope.billBuy.billBuyReceipts = data;
            });
        };

        $scope.newBillBuyReceipt = function () {
            ModalProvider.openBillBuyReceiptCreateModel($scope.billBuy).result.then(function (data) {
                $scope.billBuy.billBuyReceipts = data.billBuyReceipts;
                $scope.billBuy.paid = data.paid;
                $scope.billBuy.remain = data.remain;
            }, function () {});
        };

        $scope.newTransactionBuy = function () {
            ModalProvider.openTransactionBuyCreateModel($scope.billBuy).result.then(function (data) {
                return $scope.billBuy.transactionBuys.splice(0, 0, data);
            }, function () {});
        };

        $scope.deleteTransactionBuy = function (transactionBuy) {
            $rootScope.showConfirmNotify("المشتريات", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                TransactionBuyService.remove(transactionBuy.id).then(function () {
                    $scope.refreshBillBuy();
                });
            });

        };

        $scope.deleteBillBuyReceipt = function (billBuyReceipt) {
            $rootScope.showConfirmNotify("المشتريات", "هل تود حذف الدفعة فعلاً؟", "error", "fa-trash", function () {
                BillBuyReceiptService.remove(billBuyReceipt.id).then(function () {
                    $scope.refreshBillBuy();
                });
            });

        };

        $scope.updatePrices = function (transactionBuy) {
            $rootScope.showConfirmNotify("المشتريات", "هل تود تعديل أسعار الطلبية فعلاً؟", "warning", "fa-edit", function () {
                ModalProvider.openUpdatePricesModel(transactionBuy).result.then(function (data) {
                    return transactionBuy = data;
                });
            });
        };

        $scope.updateQuantity = function (transactionBuy) {
            if (transactionBuy) {
                $rootScope.showConfirmNotify("المشتريات", "هل تعديل كمية الطلبية فعلاً؟", "warning", "fa-edit", function () {
                    ModalProvider.openUpdateQuantityModel(transactionBuy).result.then(function (data) {
                        return transactionBuy = data;
                    });
                });
            }
        };

        $scope.transactionBuyRowMenu = [
            {
                html: '<div class="drop-menu">حذف<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_DELETE_ITEM']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteTransactionBuy($itemScope.transactionBuy);
                }
            },
            {
                html: '<div class="drop-menu">تعديل الأسعار<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_PRICE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.updatePrices($itemScope.transactionBuy);
                }
            },
            {
                html: '<div class="drop-menu">تعديل الكمية<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_QUANTITY_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.updateQuantity($itemScope.transactionBuy);
                }
            }
        ];

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshBillBuy();
        }, 500);

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 300);

    }]);