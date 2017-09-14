app.controller('depositCreateCtrl', ['BankService', 'DepositService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'bank',
    function (BankService, DepositService, $scope, $rootScope, $timeout, $log, $uibModalInstance, bank) {

        $scope.bank = bank;

        $scope.deposit = {};

        $scope.deposit.bank = bank;

        $scope.refreshBank = function () {
            BankService.findOne(bank.id).then(function (data) {
                $scope.deposit.bank = data;
            })
        };

        $scope.submit = function () {
            DepositService.create($scope.deposit).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);