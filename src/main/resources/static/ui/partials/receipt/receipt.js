app.controller("receiptCtrl", ['ModalProvider', '$scope', '$rootScope', '$state', '$timeout',
    function (ModalProvider, $scope, $rootScope, $state, $timeout) {

        $scope.selected = {};

        $scope.receipts = [];

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.receipts, function (receipt) {
                    if (object.id == receipt.id) {
                        $scope.selected = receipt;
                        return receipt.isSelected = true;
                    } else {
                        return receipt.isSelected = false;
                    }
                });
            }
        };

        $scope.delete = function (receipt) {
            if (receipt) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند فعلاً؟", "error", "fa-trash", function () {
                    ReceiptService.remove(receipt.id).then(function () {
                        var index = $scope.receipts.indexOf(receipt);
                        $scope.receipts.splice(index, 1);
                        $scope.setSelected($scope.receipts[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند فعلاً؟", "error", "fa-trash", function () {
                ReceiptService.remove($scope.selected.id).then(function () {
                    var index = $scope.receipts.indexOf($scope.selected);
                    $scope.receipts.splice(index, 1);
                    $scope.setSelected($scope.receipts[0]);
                });
            });
        };

        $scope.newReceipt = function () {

        };

        $scope.rowMenu = [
            {
                html: '<div class="drop-menu">انشاء سند جديد<span class="fa fa-pencil fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_RECEIPT_CREATE']);
                },
                click: function ($itemScope, $event, value) {

                }
            },
            {
                html: '<div class="drop-menu">تعديل بيانات السند<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_RECEIPT_UPDATE']);
                },
                click: function ($itemScope, $event, value) {

                }
            },
            {
                html: '<div class="drop-menu">حذف السند<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_RECEIPT_DELETE']);
                },
                click: function ($itemScope, $event, value) {

                }
            }
        ];

        $timeout(function () {
            $scope.fetchTableData();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);