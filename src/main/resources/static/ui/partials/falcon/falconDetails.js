app.controller('falconDetailsCtrl', ['FalconService', 'FalconService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', '$uibModal', 'falcon',
    function (FalconService, FalconService, OrderService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, $uibModal, falcon) {

        $scope.falcon = falcon;

        $scope.refreshFalcon = function () {
            FalconService.findOne($scope.falcon.id).then(function (data) {
                $scope.falcon = data;
            })
        };

        $scope.refreshOrders = function () {
            OrderService.findByFalcon($scope.falcon.id).then(function (data) {
                $scope.falcon.orders = data;
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            FalconService.findOne($scope.falcon.id).then(function (data) {
                $scope.falcon = data;
                $scope.refreshOrders();
            });
        }, 1500);

    }]);