app.controller('doctorCreateUpdateCtrl', ['TeamService' ,'DoctorService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'doctor',
        function (TeamService, DoctorService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, doctor) {

            $timeout(function () {
                TeamService.findAllCombo().then(function (data) {
                    $scope.teams = data;
                });
            }, 2000);

            $scope.doctor = doctor;

            $scope.title = title;

            $scope.action = action;

            $scope.submit = function () {
                switch ($scope.action) {
                    case 'create' :
                        DoctorService.create($scope.doctor).then(function (data) {
                            $uibModalInstance.close(data);
                        });
                        break;
                    case 'update' :
                        DoctorService.update($scope.doctor).then(function (data) {
                            $scope.doctor = data;
                        });
                        break;
                }
            };

            $scope.cancel = function () {
                $uibModalInstance.dismiss('cancel');
            };

        }]);