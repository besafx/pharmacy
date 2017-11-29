app.controller("receiptCtrl", ['ReceiptService', '$scope', '$rootScope', '$state', '$timeout',
    function (ReceiptService, $scope, $rootScope, $state, $timeout) {

        var vm = this;

        /**************************************************************
         *                                                            *
         * Receipts In                                                *
         *                                                            *
         *************************************************************/
        vm.selected = {};

        vm.buffer = {};

        vm.receipts = [];

        vm.setSelected = function (object) {
            if (object) {
                angular.forEach(vm.receipts, function (receipt) {
                    if (object.id == receipt.id) {
                        vm.selected = receipt;
                        return receipt.isSelected = true;
                    } else {
                        return receipt.isSelected = false;
                    }
                });
            }
        };

        vm.search = function () {

            var search = [];

            if (vm.buffer.code) {
                search.push('code=');
                search.push(vm.buffer.code);
                search.push('&');
            }
            //
            if (vm.buffer.amountFrom) {
                search.push('amountFrom=');
                search.push(vm.buffer.amountFrom);
                search.push('&');
            }
            if (vm.buffer.amountTo) {
                search.push('amountTo=');
                search.push(vm.buffer.amountTo);
                search.push('&');
            }
            //
            if (vm.buffer.dateFrom) {
                search.push('dateFrom=');
                search.push(vm.buffer.dateFrom.getTime());
                search.push('&');
            }
            if (vm.buffer.dateTo) {
                search.push('dateTo=');
                search.push(vm.buffer.dateTo.getTime());
                search.push('&');
            }
            //
            if (vm.buffer.lastUpdateFrom) {
                search.push('lastUpdateFrom=');
                search.push(vm.buffer.lastUpdateFrom.getTime());
                search.push('&');
            }
            if (vm.buffer.lastUpdateTo) {
                search.push('lastUpdateTo=');
                search.push(vm.buffer.lastUpdateTo.getTime());
                search.push('&');
            }
            //
            if (vm.buffer.paymentMethodList) {
                var paymentMethods = [];
                for (var i = 0; i < vm.buffer.paymentMethodList.length; i++) {
                    paymentMethods.push(vm.buffer.paymentMethodList[i]);
                }
                search.push('paymentMethods=');
                search.push(paymentMethods);
                search.push('&');
            }
            //
            ReceiptService.filter(search.join("")).then(function (data) {
                vm.receipts = data;
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.clear = function () {
            vm.buffer = {};
        };

        vm.delete = function (receipt) {
            if (receipt) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند فعلاً؟", "error", "fa-trash", function () {
                    ReceiptService.remove(receipt.id).then(function () {
                        var index = vm.receipts.indexOf(receipt);
                        vm.receipts.splice(index, 1);
                        vm.setSelected(vm.receipts[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند فعلاً؟", "error", "fa-trash", function () {
                ReceiptService.remove(vm.selected.id).then(function () {
                    var index = vm.receipts.indexOf(vm.selected);
                    vm.receipts.splice(index, 1);
                    vm.setSelected(vm.receipts[0]);
                });
            });
        };

        vm.newReceipt = function () {

        };

        vm.rowMenu = [
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
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);