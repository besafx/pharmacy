app.controller('billBuyReceiptCreateCtrl', ['BillBuyReceiptService', 'BillBuyService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'billBuy',
    function (BillBuyReceiptService, BillBuyService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, billBuy) {

        $scope.billBuyReceipt = {};

        $scope.billBuyReceipt.billBuy = billBuy;

        $scope.submit = function () {
            BillBuyReceiptService.create($scope.billBuyReceipt).then(function (data) {
                $scope.billBuyReceipt = data;
                BillBuyService.findOne(billBuy.id).then(function (data) {
                    $uibModalInstance.close(data);
                });
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);