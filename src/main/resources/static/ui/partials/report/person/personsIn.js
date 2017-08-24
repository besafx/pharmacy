app.controller('personsInCtrl', ['$scope', '$rootScope', '$timeout', '$uibModalInstance', 'persons',
    function ($scope, $rootScope, $timeout, $uibModalInstance, persons) {

        $scope.persons = persons;
        $scope.buffer = {};
        $scope.buffer.personsList = persons;

        $scope.submit = function () {
            var listId = [];
            for (var i = 0; i < $scope.buffer.personsList.length; i++) {
                listId[i] = $scope.buffer.personsList[i].id;
            }
            window.open('/report/Persons?'
                + "personsList=" + listId);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }]);