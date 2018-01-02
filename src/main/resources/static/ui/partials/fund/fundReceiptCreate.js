app.controller('fundReceiptCreateCtrl', ['FundReceiptService', 'FundService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'receiptType',
    function (FundReceiptService, FundService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, receiptType) {

        $scope.receiptType = receiptType;

        $scope.fundReceipt = {};

        $scope.fundReceipt.fund = {};

        $timeout(function () {
            FundService.get().then(function (data) {
                $scope.fundReceipt.fund = data;
            });
        }, 600);

        $scope.submit = function () {
            switch ($scope.receiptType){
                case "In":
                    FundReceiptService.createIn($scope.fundReceipt).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case "Out":
                    FundReceiptService.createOut($scope.fundReceipt).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);