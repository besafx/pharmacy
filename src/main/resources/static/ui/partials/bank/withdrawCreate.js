app.controller('withdrawCreateCtrl', ['BankService', 'WithdrawService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'bank',
    function (BankService, WithdrawService, $scope, $rootScope, $timeout, $log, $uibModalInstance, bank) {

        $scope.bank = bank;

        $scope.withdraw = {};

        $scope.withdraw.bank = bank;

        $scope.refreshBank = function () {
            BankService.findOne(bank.id).then(function (data) {
                $scope.withdraw.bank = data;
            })
        };

        $scope.submit = function () {
            WithdrawService.create($scope.withdraw).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);