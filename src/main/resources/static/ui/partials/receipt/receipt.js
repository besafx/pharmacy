app.controller("receiptCtrl", ['ReceiptService', 'OrderReceiptService', 'OrderService', 'BillSellReceiptService', 'BillSellService', '$scope', '$rootScope', '$state', '$timeout',
    function (ReceiptService, OrderReceiptService, OrderService, BillSellReceiptService, BillSellService, $scope, $rootScope, $state, $timeout) {

        var vm = this;

        /**************************************************************
         *                                                            *
         * All Receipts                                               *
         *                                                            *
         *************************************************************/
        vm.param1 = {};
        vm.receipts = [];
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
        /**************************************************************
         *                                                            *
         * Incomes of Diagnoses                                       *
         *                                                            *
         *************************************************************/
        vm.param2 = {};
        vm.orderReceipts = [];
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
        /**************************************************************
         *                                                            *
         * Debts of Diagnoses                                         *
         *                                                            *
         *************************************************************/
        vm.param3 = {};
        vm.orders = [];
        vm.search3 = function () {

            var search = [];

            //Order Keys
            if (vm.param3.codeFrom) {
                search.push('codeFrom=');
                search.push(vm.param3.codeFrom);
                search.push('&');
            }
            if (vm.param3.codeTo) {
                search.push('codeTo=');
                search.push(vm.param3.codeTo);
                search.push('&');
            }
            //
            if (vm.param3.customerName) {
                search.push('customerName=');
                search.push(vm.param3.customerName);
                search.push('&');
            }
            if (vm.param3.customerMobile) {
                search.push('customerMobile=');
                search.push(vm.param3.customerMobile);
                search.push('&');
            }
            //
            if (vm.param3.falconCode) {
                search.push('falconCode=');
                search.push(vm.param3.falconCode);
                search.push('&');
            }
            if (vm.param3.falconType) {
                search.push('falconType=');
                search.push(vm.param3.falconType);
                search.push('&');
            }
            //
            if (vm.param3.dateFrom) {
                search.push('dateFrom=');
                search.push(vm.param3.dateFrom.getTime());
                search.push('&');
            }
            if (vm.param3.dateTo) {
                search.push('dateTo=');
                search.push(vm.param3.dateTo.getTime());
                search.push('&');
            }
            //
            OrderService.filterDebt(search.join("")).then(function (data) {
                vm.orders = data;
                vm.totalAmount3 = 0;
                angular.forEach(data, function (order) {
                    vm.totalAmount3+=order.remain;
                });
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };
        /**************************************************************
         *                                                            *
         * Incomes of Inside Sales                                    *
         *                                                            *
         *************************************************************/
        vm.param4 = {};
        vm.insideSalesReceipts = [];
        vm.search4 = function () {

            var search = [];

            //BillSell Keys
            if (vm.param4.codeFrom) {
                search.push('codeFrom=');
                search.push(vm.param4.codeFrom);
                search.push('&');
            }
            if (vm.param4.codeTo) {
                search.push('codeTo=');
                search.push(vm.param4.codeTo);
                search.push('&');
            }
            //
            if (vm.param4.dateFrom) {
                search.push('dateFrom=');
                search.push(vm.param4.dateFrom.getTime());
                search.push('&');
            }
            if (vm.param4.dateTo) {
                search.push('dateTo=');
                search.push(vm.param4.dateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param4.orderCodeFrom) {
                search.push('orderCodeFrom=');
                search.push(vm.param4.orderCodeFrom);
                search.push('&');
            }
            if (vm.param4.orderCodeTo) {
                search.push('orderCodeTo=');
                search.push(vm.param4.orderCodeTo);
                search.push('&');
            }
            //
            if (vm.param4.orderCustomerName) {
                search.push('orderCustomerName=');
                search.push(vm.param4.orderCustomerName);
                search.push('&');
            }
            if (vm.param4.orderCustomerMobile) {
                search.push('orderCustomerMobile=');
                search.push(vm.param4.orderCustomerMobile);
                search.push('&');
            }
            //
            if (vm.param4.orderFalconCode) {
                search.push('orderFalconCode=');
                search.push(vm.param4.orderFalconCode);
                search.push('&');
            }
            if (vm.param4.orderFalconType) {
                search.push('orderFalconType=');
                search.push(vm.param4.orderFalconType);
                search.push('&');
            }
            //

            //Receipt Keys
            if (vm.param4.code) {
                search.push('receiptCode=');
                search.push(vm.param4.code);
                search.push('&');
            }
            //
            if (vm.param4.amountFrom) {
                search.push('receiptAmountFrom=');
                search.push(vm.param4.amountFrom);
                search.push('&');
            }
            if (vm.param4.amountTo) {
                search.push('receiptAmountTo=');
                search.push(vm.param4.amountTo);
                search.push('&');
            }
            //
            if (vm.param4.receiptDateFrom) {
                search.push('receiptDateFrom=');
                search.push(vm.param4.dateFrom.getTime());
                search.push('&');
            }
            if (vm.param4.receiptDateTo) {
                search.push('receiptDateTo=');
                search.push(vm.param4.dateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param4.lastUpdateFrom) {
                search.push('receiptLastUpdateFrom=');
                search.push(vm.param4.lastUpdateFrom.getTime());
                search.push('&');
            }
            if (vm.param4.lastUpdateTo) {
                search.push('receiptLastUpdateTo=');
                search.push(vm.param4.lastUpdateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param4.paymentMethodList) {
                var paymentMethods = [];
                for (var i = 0; i < vm.param4.paymentMethodList.length; i++) {
                    paymentMethods.push(vm.param4.paymentMethodList[i]);
                }
                search.push('receiptPaymentMethods=');
                search.push(paymentMethods);
                search.push('&');
            }
            //
            BillSellReceiptService.filterInside(search.join("")).then(function (data) {
                vm.insideSalesReceipts = data;
                vm.totalAmount4 = 0;
                angular.forEach(data, function (orderReceipt) {
                    vm.totalAmount4+=orderReceipt.receipt.amountNumber;
                });
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };
        /**************************************************************
         *                                                            *
         * Debts of Inside Sales                                      *
         *                                                            *
         *************************************************************/
        vm.param5 = {};
        vm.billSells = [];
        vm.search5 = function () {

            var search = [];

            //BillSell Keys
            if (vm.param5.codeFrom) {
                search.push('codeFrom=');
                search.push(vm.param5.codeFrom);
                search.push('&');
            }
            if (vm.param5.codeTo) {
                search.push('codeTo=');
                search.push(vm.param5.codeTo);
                search.push('&');
            }
            //
            if (vm.param5.dateFrom) {
                search.push('dateFrom=');
                search.push(vm.param5.dateFrom.getTime());
                search.push('&');
            }
            if (vm.param5.dateTo) {
                search.push('dateTo=');
                search.push(vm.param5.dateTo.getTime());
                search.push('&');
            }
            //
            if (vm.param5.orderCodeFrom) {
                search.push('orderCodeFrom=');
                search.push(vm.param5.orderCodeFrom);
                search.push('&');
            }
            if (vm.param5.orderCodeTo) {
                search.push('orderCodeTo=');
                search.push(vm.param5.orderCodeTo);
                search.push('&');
            }
            //
            if (vm.param5.orderCustomerName) {
                search.push('orderCustomerName=');
                search.push(vm.param5.orderCustomerName);
                search.push('&');
            }
            if (vm.param5.orderCustomerMobile) {
                search.push('orderCustomerMobile=');
                search.push(vm.param5.orderCustomerMobile);
                search.push('&');
            }
            //
            if (vm.param5.orderFalconCode) {
                search.push('orderFalconCode=');
                search.push(vm.param5.orderFalconCode);
                search.push('&');
            }
            //
            BillSellService.filterInside(search.join("")).then(function (data) {
                vm.billSells = data;
                vm.totalAmount5 = 0;
                angular.forEach(data, function (billSell) {
                    vm.totalAmount5+=billSell.remain;
                });
                $timeout(function () {
                    window.componentHandler.upgradeAllRegistered();
                }, 500);
            });
        };

        /**************************************************************
         *                                                            *
         * Incomes of Outside Sales                                   *
         *                                                            *
         *************************************************************/

        /**************************************************************
         *                                                            *
         * Debts of Outside Sales                                     *
         *                                                            *
         *************************************************************/

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);