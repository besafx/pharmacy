app.controller('customerDetailsReportCtrl', ['CustomerService', '$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function (CustomerService, $scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $timeout(function () {
            CustomerService.findAllCombo().then(function (data) {
                $scope.customers = data;
            });
        }, 2000);

        $scope.submit = function () {
            window.open('/report/customer/details/' + $scope.buffer.customer.id + '?exportType=' + 'PDF'
            );
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);