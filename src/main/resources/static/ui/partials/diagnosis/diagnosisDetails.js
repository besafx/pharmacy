app.controller('diagnosisDetailsCtrl', ['DiagnosisService' ,'OrderDetectionTypeService', 'ModalProvider' ,'$scope', '$uibModalInstance', '$rootScope', '$timeout', '$log', 'orderDetectionType',
    function (DiagnosisService,OrderDetectionTypeService, ModalProvider, $scope, $uibModalInstance, $rootScope, $timeout, $log, orderDetectionType) {

        OrderDetectionTypeService.findOne(orderDetectionType.id).then(function (data) {
            $scope.orderDetectionType = data;
        });

        $scope.newDiagnosis = function () {
            ModalProvider.openDiagnosisCreateModel($scope.orderDetectionType).result.then(function (data) {
                $scope.orderDetectionType.diagnoses.splice(0, 0, data);
            }, function () {
                console.info('DiagnosisCreateModel Closed.');
            });
        };

        $scope.deleteDiagnosis = function (diagnoses) {
            $rootScope.showConfirmNotify("العيادة الطبية", "هل تود حذف الوصفة الطبية فعلاً؟", "error", "fa-trash", function () {
                DiagnosisService.remove(diagnoses.id).then(function () {
                    var index = $scope.orderDetectionType.diagnoses.indexOf(diagnoses);
                    $scope.orderDetectionType.diagnoses.splice(index, 1);
                });
            });
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);