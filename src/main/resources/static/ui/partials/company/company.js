app.controller("companyCtrl", ['CompanyService', 'PersonService', 'ModalProvider', '$scope', '$rootScope', '$log', '$http', '$state', '$timeout', '$location', '$anchorScroll',
    function (CompanyService, PersonService, ModalProvider, $scope, $rootScope, $log, $http, $state, $timeout, $location, $anchorScroll) {

        $scope.selected = {};

        $scope.buffer = {};

        $scope.buffer.personsList = [];

        $scope.options = {};

        $scope.submit1 = function () {
            CompanyService.update($scope.selected).then(function (data) {
                $scope.selected = data;
            });
        };

        //////////////////////////File Manager///////////////////////////////////
        $scope.uploadFile = function () {
            document.getElementById('uploader').click();
        };

        $scope.uploadCompanyLogo = function (files) {
            CompanyService.uploadCompanyLogo(files[0]).then(function (data) {
                $scope.selected.logo = data;
            });
        };
        //////////////////////////File Manager///////////////////////////////////

        $timeout(function () {
            CompanyService.findAll().then(function (data) {
                $scope.selected = data[0];
                $scope.options = JSON.parse(data[0].options);
            });
            $location.hash('companyMenu');
            $anchorScroll();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);