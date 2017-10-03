app.controller('updateQuantityCtrl', ['TransactionBuyService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'transactionBuy',
    function (TransactionBuyService,$scope, $rootScope, $timeout, $log, $uibModalInstance, transactionBuy) {

        $scope.transactionBuy = transactionBuy;

        $scope.submit = function () {
            TransactionBuyService.updateQuantity($scope.transactionBuy.id, $scope.transactionBuy.quantity).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);