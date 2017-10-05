app.controller("reportCtrl", ['DrugService' ,'$scope', '$timeout', function (DrugService, $scope, $timeout) {
    $timeout(function () {
        window.componentHandler.upgradeAllRegistered();
    }, 1500);

    $scope.printDrugsList = function () {
        DrugService.findAll().then(function (data) {
            var ids = [];
            angular.forEach(data, function (drug) {
                ids.push(drug.id);
            });
            window.open('/report/drugs?ids=' + ids + "&exportType=PDF");
        });
    };


}]);