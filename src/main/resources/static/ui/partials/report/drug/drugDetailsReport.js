app.controller('drugDetailsReportCtrl', ['DrugService', '$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function (DrugService, $scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $timeout(function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        }, 600);

        $scope.submit = function () {
            window.open('/report/drug/' + $scope.buffer.drug.id  + '/PDF'
            );
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);