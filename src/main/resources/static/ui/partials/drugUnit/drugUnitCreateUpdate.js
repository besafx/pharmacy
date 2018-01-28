app.controller('drugUnitCreateUpdateCtrl', [
    'DrugUnitService',
    '$scope',
    '$rootScope',
    '$timeout',
    '$log',
    '$uibModalInstance',
    'title',
    'action',
    'drugUnit',
    function (DrugUnitService,
              $scope,
              $rootScope,
              $timeout,
              $log,
              $uibModalInstance,
              title,
              action,
              drugUnit) {

        $scope.drugUnit = drugUnit;

        $scope.title = title;

        $scope.action = action;

        $scope.submit = function () {
            switch ($scope.action) {
                case 'create' :
                    DrugUnitService.create($scope.drugUnit).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    DrugUnitService.update($scope.drugUnit).then(function (data) {
                        $scope.drugUnit = data;
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);