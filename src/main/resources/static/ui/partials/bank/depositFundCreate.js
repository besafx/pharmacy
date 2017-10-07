app.controller('depositFundCreateCtrl', ['BankService', 'DepositService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'date', 'totalFund',
    function (BankService, DepositService, $scope, $rootScope, $timeout, $log, $uibModalInstance, date, totalFund) {

        $scope.deposit = {};

        $scope.deposit.date = date;

        $scope.deposit.amount = totalFund;

        $scope.deposit.depositMethod = 'FromFund';

        $timeout(function () {
            BankService.findAllCombo().then(function (data) {
                $scope.banks = data;
            })
        }, 2000);

        $scope.submit = function () {
            DepositService.create($scope.deposit).then(function (data) {
                $uibModalInstance.close(data);
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);