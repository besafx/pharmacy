app.controller('orderReceiptCreateCtrl', ['OrderReceiptService', 'DetectionTypeService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'order',
    function (OrderReceiptService, DetectionTypeService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, order) {

        $scope.orderReceipt = {};

        $scope.orderReceipt.order = order;

        $scope.submit = function () {
            OrderReceiptService.create($scope.orderReceipt).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);