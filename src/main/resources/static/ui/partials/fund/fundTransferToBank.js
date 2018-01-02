app.controller('fundTransferToBankCtrl', ['FundReceiptService', 'FundService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (FundReceiptService, FundService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $scope.fundReceipt = {};

        $scope.fundReceipt.fund = {};

        $timeout(function () {
            FundService.get().then(function (data) {
                $scope.fundReceipt.fund = data;
            });
        }, 600);

        $scope.submit = function () {
            FundReceiptService.transferToBank($scope.fundReceipt).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);