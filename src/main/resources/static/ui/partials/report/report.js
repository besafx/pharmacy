app.controller("reportCtrl", ['DrugService', '$rootScope', '$scope', '$timeout', '$location', '$anchorScroll',
    function (DrugService, $rootScope, $scope, $timeout, $location, $anchorScroll) {

    $scope.printDrugsList = function () {
        $rootScope.showConfirmNotify("التقارير", "هل تود طباعة التقرير ؟", "notification", "fa-info", function () {
            DrugService.findAll().then(function (data) {
                var ids = [];
                angular.forEach(data, function (drug) {
                    ids.push(drug.id);
                });
                window.open('/report/drugs?ids=' + ids + "&exportType=PDF");
            });
        });
    };

    $timeout(function () {
        $location.hash('reportMenu');
        $anchorScroll();
        window.componentHandler.upgradeAllRegistered();
    }, 1500);


}]);