app.controller('supplierCreateUpdateCtrl', ['SupplierService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'supplier',
    function (SupplierService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, supplier) {

        $scope.supplier = supplier;

        $scope.title = title;

        $scope.action = action;

        $scope.submit = function () {
            switch ($scope.action) {
                case 'create' :
                    SupplierService.create($scope.supplier).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    SupplierService.update($scope.supplier).then(function (data) {
                        $scope.supplier = data;
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);