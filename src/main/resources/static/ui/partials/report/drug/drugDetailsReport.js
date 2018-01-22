app.controller('drugDetailsReportCtrl', ['DrugService', '$scope', '$rootScope', '$timeout', '$uibModalInstance',
    function (DrugService, $scope, $rootScope, $timeout, $uibModalInstance) {

        $scope.buffer = {};

        $scope.drugList = [];

        $timeout(function () {
            DrugService.findAllCombo().then(function (data) {
                $scope.drugs = data;
            });
        }, 600);

        $scope.submit = function () {
            var ids = [];
            angular.forEach($scope.buffer.drugList, function (drug) {
                ids.push(drug.id);
            });
            window.open('/report/drugs/details?ids=' + ids + "&exportType=PDF");
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);