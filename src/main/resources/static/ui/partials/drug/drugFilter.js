app.controller('drugFilterCtrl', ['DrugCategoryService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (DrugCategoryService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $timeout(function () {
            DrugCategoryService.findAllCombo().then(function (data) {
                $scope.drugCategories = data;
            });
        }, 2000);

        $scope.submit = function () {
            $uibModalInstance.close($scope.buffer);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);