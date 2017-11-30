app.controller("receiptCtrl", ['ReceiptService', 'OrderReceiptService', '$scope', '$rootScope', '$state', '$timeout',
    function (ReceiptService, OrderReceiptService, $scope, $rootScope, $state, $timeout) {

        var vm = this;

        /**************************************************************
         *                                                            *
         * Receipts In                                                *
         *                                                            *
         *************************************************************/
        vm.buffer = {};

        vm.param1 = {};

        vm.param2 = {};

        vm.receipts = [];

        vm.orderReceipts = [];

        vm.search1 = function () {

            var search = [];

            if (vm.param1.code) {
                search.push('code=');
                search.push(vm.param1.code);
                search.push('&');
            }
            //
            if (vm.param1.amountFrom) {
                search.push('amountFrom=');
                search.push(vm.param1.amountFrom);
                search.push('&');
            }
            if (vm.param1.amountTo) {
                search.push('amountTo=');
                search.push(vm.param1.amountTo);
                search.push('&');
            }
            //
            if (vm.param1.dateFrom) {
                search.push('dateFrom=');
                search.push(vm.param1.dateFrom.getTime());
                search.push('&');
            }
            if (vm.param1.dateTo) {
                search.push('dateTo=');
                search.push(vm.param1.dateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param1.lastUpdateFrom) {
                search.push('lastUpdateFrom=');
                search.push(vm.param1.lastUpdateFrom.getTime());
                search.push('&');
            }
            if (vm.param1.lastUpdateTo) {
                search.push('lastUpdateTo=');
                search.push(vm.param1.lastUpdateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param1.paymentMethodList) {
                var paymentMethods = [];
                for (var i = 0; i < vm.param1.paymentMethodList.length; i++) {
                    paymentMethods.push(vm.buffer.paymentMethodList[i]);
                }
                search.push('paymentMethods=');
                search.push(paymentMethods);
                search.push('&');
            }
            //
            ReceiptService.filter(search.join("")).then(function (data) {
                vm.receipts = data;
                vm.totalAmount1 = 0;
                angular.forEach(data, function (receipt) {
                    vm.totalAmount1+=receipt.amountNumber;
                });
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        vm.search2 = function () {

            var search = [];

            //Order Keys
            if (vm.param2.orderCodeFrom) {
                search.push('orderCodeFrom=');
                search.push(vm.param2.orderCodeFrom);
                search.push('&');
            }
            if (vm.param2.orderCodeTo) {
                search.push('orderCodeTo=');
                search.push(vm.param2.orderCodeTo);
                search.push('&');
            }
            //
            if (vm.param2.orderCustomerName) {
                search.push('orderCustomerName=');
                search.push(vm.param2.orderCustomerName);
                search.push('&');
            }
            if (vm.param2.orderCustomerMobile) {
                search.push('orderCustomerMobile=');
                search.push(vm.param2.orderCustomerMobile);
                search.push('&');
            }
            //
            if (vm.param2.orderFalconCode) {
                search.push('orderFalconCode=');
                search.push(vm.param2.orderFalconCode);
                search.push('&');
            }
            if (vm.param2.orderFalconType) {
                search.push('orderFalconType=');
                search.push(vm.param2.orderFalconType);
                search.push('&');
            }
            //
            if (vm.param2.orderDateFrom) {
                search.push('orderDateFrom=');
                search.push(vm.param2.orderDateFrom.getTime());
                search.push('&');
            }
            if (vm.param2.orderDateTo) {
                search.push('orderDateTo=');
                search.push(vm.param2.orderDateTo.getTime());
                search.push('&');
            }
            //

            //Receipt Keys
            if (vm.param2.code) {
                search.push('receiptCode=');
                search.push(vm.param2.code);
                search.push('&');
            }
            //
            if (vm.param2.amountFrom) {
                search.push('receiptAmountFrom=');
                search.push(vm.param2.amountFrom);
                search.push('&');
            }
            if (vm.param2.amountTo) {
                search.push('receiptAmountTo=');
                search.push(vm.param2.amountTo);
                search.push('&');
            }
            //
            if (vm.param2.dateFrom) {
                search.push('receiptDateFrom=');
                search.push(vm.param2.dateFrom.getTime());
                search.push('&');
            }
            if (vm.param2.dateTo) {
                search.push('receiptDateTo=');
                search.push(vm.param2.dateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param2.lastUpdateFrom) {
                search.push('receiptLastUpdateFrom=');
                search.push(vm.param2.lastUpdateFrom.getTime());
                search.push('&');
            }
            if (vm.param2.lastUpdateTo) {
                search.push('receiptLastUpdateTo=');
                search.push(vm.param2.lastUpdateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param2.paymentMethodList) {
                var paymentMethods = [];
                for (var i = 0; i < vm.param2.paymentMethodList.length; i++) {
                    paymentMethods.push(vm.param2.paymentMethodList[i]);
                }
                search.push('receiptPaymentMethods=');
                search.push(paymentMethods);
                search.push('&');
            }
            //
            OrderReceiptService.filter(search.join("")).then(function (data) {
                vm.orderReceipts = data;
                vm.totalAmount2 = 0;
                angular.forEach(data, function (orderReceipt) {
                    vm.totalAmount2+=orderReceipt.receipt.amountNumber;
                });
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);