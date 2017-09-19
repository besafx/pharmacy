app.controller('orderByDateCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $scope.submit = function () {
            window.open('/report/ordersByDate?'
                + 'exportType=' + 'PDF'
                + '&dateFrom=' + $scope.buffer.dateFrom.getTime()
                + '&dateTo=' + $scope.buffer.dateTo.getTime()
            );
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);