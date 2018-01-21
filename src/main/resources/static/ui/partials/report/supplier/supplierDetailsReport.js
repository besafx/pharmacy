app.controller('supplierDetailsReportCtrl', ['SupplierService', '$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function (SupplierService, $scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $timeout(function () {
            SupplierService.findAllCombo().then(function (data) {
                $scope.suppliers = data;
            });
        }, 2000);

        $scope.submit = function () {
            window.open('/report/supplier/details/' + $scope.buffer.supplier.id + '?exportType=' + 'PDF'
            );
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);