app.controller('bankReceiptCreateCtrl', ['BankReceiptService', 'BankService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'receiptType',
    function (BankReceiptService, BankService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, receiptType) {

        $scope.receiptType = receiptType;

        $scope.bankReceipt = {};

        $scope.bankReceipt.bank = {};

        $timeout(function () {
            BankService.get().then(function (data) {
                $scope.bankReceipt.bank = data;
            });
        }, 600);

        $scope.submit = function () {
            switch ($scope.receiptType){
                case "In":
                    BankReceiptService.createIn($scope.bankReceipt).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case "Out":
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