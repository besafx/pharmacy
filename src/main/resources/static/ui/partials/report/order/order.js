app.controller('orderInCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function ($scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $scope.submit = function () {
            var listId = [];
            for (var i = 0; i < $scope.buffer.orderList.length; i++) {
                listId[i] = $scope.buffer.orderList[i].id;
            }
            window.open('/report/order/' + order.id + '/' + $scope.buffer.exportType);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);