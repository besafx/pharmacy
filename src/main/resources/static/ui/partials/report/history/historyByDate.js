app.controller('historyByDateCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $scope.submit = function () {
            window.open('/report/history/' + $scope.buffer.dateFrom.getTime() + '/' + $scope.buffer.dateTo.getTime() + '/PDF');
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);