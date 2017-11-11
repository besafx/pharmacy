app.controller('billSellReceiptCreateCtrl', ['BillSellReceiptService', 'DetectionTypeService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billSell',
    function (BillSellReceiptService, DetectionTypeService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, billSell) {

        $scope.billSellReceipt = {};

        $scope.billSellReceipt.billSell = billSell;

        $scope.submit = function () {
            BillSellReceiptService.create($scope.billSellReceipt).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);