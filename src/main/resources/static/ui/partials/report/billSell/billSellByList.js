app.controller('billSellByListCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance', 'billSells',
    function ($scope, $rootScope, $timeout, $uibModalInstance, billSells) {

        $scope.buffer = {};

        $scope.billSells = billSells;

        $scope.removeOrder = function (index) {
            $scope.billSells.splice(index, 1);
        };

        $scope.submit = function () {
            var ids = [];
            angular.forEach(billSells, function (billSell) {
               ids.push(billSell.id);
            });
            window.open('/report/billSells?ids=' + ids + '&exportType=PDF');
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);