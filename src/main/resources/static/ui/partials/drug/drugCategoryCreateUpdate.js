app.controller('drugCategoryCreateUpdateCtrl', ['DrugCategoryService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'drugCategory',
        function (DrugCategoryService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, drugCategory) {

            $scope.drugCategory = drugCategory;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        DrugCategoryService.create($scope.drugCategory).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        DrugCategoryService.update($scope.drugCategory).then(function (data) {
                            $scope.drugCategory = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);