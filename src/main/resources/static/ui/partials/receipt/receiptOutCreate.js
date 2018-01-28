app.controller('receiptOutCreateCtrl', ['FundReceiptService', 'BankReceiptService', 'FundService', 'BankService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (FundReceiptService, BankReceiptService, FundService, BankService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.receipt = {};

        $scope.receipt.paymentFrom = "Fund";

        $scope.fundReceipt = {};

        $scope.bankReceipt = {};

        $scope.fundReceipt.fund = {};

        $scope.bankReceipt.bank = {};

        $scope.getFund = function () {
            FundService.get().then(function (data) {
                $scope.fundReceipt.fund = data;
            });
        };

        $scope.getBank = function () {
            BankService.get().then(function (data) {
                $scope.bankReceipt.bank = data;
            });
        };

        $scope.onChangePaymentFrom = function () {
            switch ($scope.receipt.paymentFrom) {
                case "Fund":
                    $scope.getFund();
                    break;
                case "Bank":
                    $scope.getBank();
                    break;
            }
        };

        $timeout(function () {
            $scope.onChangePaymentFrom();
        }, 600);

        $scope.submit = function () {
            switch ($scope.receipt.paymentFrom){
                case "Fund":
                    $scope.fundReceipt.receipt = $scope.receipt;
                    FundReceiptService.createOut($scope.fundReceipt).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case "Bank":
                    $scope.bankReceipt.receipt = $scope.receipt;
                    BankReceiptService.createOut($scope.bankReceipt).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);