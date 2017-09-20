app.controller('orderDetailsByListCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance', 'orders',
    function ($scope, $rootScope, $timeout, $uibModalInstance, orders) {

        $scope.buffer = {};

        $scope.orders = orders;

        $scope.removeOrder = function (index) {
            $scope.orders.splice(index, 1);
        };

        $scope.submit = function () {
            var ids = [];
            angular.forEach(orders, function (order) {
               ids.push(order.id);
            });
            window.open('/report/ordersDetails?ids=' + ids + '&exportType=PDF');
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);