app.controller('drugCreateUpdateCtrl', ['DrugService', 'DrugCategoryService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'drug',
        function (DrugService, DrugCategoryService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, drug) {

            $timeout(function () {
                DrugCategoryService.findAllCombo().then(function (data) {
                    $scope.drugCategories = data;
                });
            }, 2000);

            $scope.drug = drug;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        DrugService.create($scope.drug).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        DrugService.update($scope.drug).then(function (data) {
                            $scope.drug = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);