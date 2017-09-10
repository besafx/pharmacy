app.controller('orderFilterCtrl', ['FalconService', 'DoctorService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance',
    function (FalconService, DoctorService, $scope, $rootScope, $timeout, $log, $uibModalInstance) {

        $timeout(function () {
            FalconService.findAllCombo().then(function (data) {
               $scope.falcons = data;
            });
            DoctorService.findAllCombo().then(function (data) {
               $scope.doctors = data;
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