app.controller('orderDetectionTypeCreateCtrl', ['OrderDetectionTypeService', 'DetectionTypeService', 'ModalProvider', '$uibModal', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'order',
    function (OrderDetectionTypeService, DetectionTypeService, ModalProvider, $uibModal, $scope, $rootScope, $timeout, $log, $uibModalInstance, order) {

        $timeout(function () {
            $scope.refreshDetectionTypes();
        }, 2000);

        $scope.orderDetectionType = {};

        $scope.orderDetectionType.order = order;

        $scope.refreshDetectionTypes = function () {
            DetectionTypeService.findAllCombo().then(function (data) {
                $scope.detectionTypes = data;
            });
        };

        $scope.newDetectionType = function () {
            ModalProvider.openDetectionTypeCreateModel().result.then(function (data) {
                $scope.detectionTypes.splice(0, 0, data);
                $scope.buffer.selectedDetectionType = data;
            }, function () {
                console.info('DetectionTypeCreateModel Closed.');
            });
        };

        $scope.submit = function () {
            OrderDetectionTypeService.create($scope.orderDetectionType).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);