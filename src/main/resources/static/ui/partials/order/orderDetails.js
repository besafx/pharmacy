app.controller('orderDetailsCtrl', ['OrderDetectionTypeService', 'DetectionTypeService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'order',
    function (OrderDetectionTypeService, DetectionTypeService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, order) {

        $scope.order = order;

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);