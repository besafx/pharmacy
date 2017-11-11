app.controller('billSellFilterCtrl', ['CustomerService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title',
    function (CustomerService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title) {

        $scope.buffer = {};

        $scope.title = title;

        $timeout(function () {
            CustomerService.findAllCombo().then(function (data) {
                $scope.customers = data;
            });
        }, 2000);

        $scope.submit = function () {
            $uibModalInstance.close($scope.buffer);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);