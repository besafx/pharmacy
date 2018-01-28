app.controller('falconDetailsCtrl', ['FalconService', 'OrderService', 'ModalProvider', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', '$uibModal', 'falcon',
    function (FalconService, OrderService, ModalProvider, $scope, $rootScope, $timeout, $log, $uibModalInstance, $uibModal, falcon) {

        $scope.falcon = falcon;

        $scope.refreshFalcon = function () {
            FalconService.findOneDetails($scope.falcon.id).then(function (data) {
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
            $scope.refreshFalcon();
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);