app.controller('teamCreateUpdateCtrl', ['TeamService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'team',
    function (TeamService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, team) {

        $scope.roles = [
            {id: 1, name: $rootScope.lang==='AR' ? 'تعديل بيانات الشركة' : 'Update Company Information', value: 'ROLE_COMPANY_UPDATE', selected: false},
            {id: 2, name: $rootScope.lang==='AR' ? 'إنشاء حسابات العملاء' : 'Create Customers' , value: 'ROLE_CUSTOMER_CREATE', selected: false},
            {id: 3, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات العملاء' : 'Update Customers Information' , value: 'ROLE_CUSTOMER_UPDATE', selected: false},
            {id: 4, name: $rootScope.lang==='AR' ? 'حذف حسابات العملاء' : 'Delete Customers' , value: 'ROLE_CUSTOMER_DELETE', selected: false},
            {id: 5, name: $rootScope.lang==='AR' ? 'إنشاء حسابات الاطباء' : 'Create Doctors' , value: 'ROLE_DOCTOR_CREATE', selected: false},
            {id: 6, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات الاطباء' : 'Update Doctors Information' , value: 'ROLE_DOCTOR_UPDATE', selected: false},
            {id: 7, name: $rootScope.lang==='AR' ? 'حذف حسابات الاطباء' : 'Delete Doctors' , value: 'ROLE_DOCTOR_DELETE', selected: false},
            {id: 8, name: $rootScope.lang==='AR' ? 'إنشاء حسابات الموظفون' : 'Create Employees' , value: 'ROLE_EMPLOYEE_CREATE', selected: false},
            {id: 9, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات الموظفون' : 'Update Employees Information' , value: 'ROLE_EMPLOYEE_UPDATE', selected: false},
            {id: 10, name: $rootScope.lang==='AR' ? 'حذف حسابات الموظفون' : 'Delete Employees' , value: 'ROLE_EMPLOYEE_DELETE', selected: false},
            {id: 11, name: $rootScope.lang==='AR' ? 'إنشاء الصلاحيات' : 'Create Privileges' , value: 'ROLE_TEAM_CREATE', selected: false},
            {id: 12, name: $rootScope.lang==='AR' ? 'تعديل بيانات الصلاحيات' : 'Update Privileges' , value: 'ROLE_TEAM_UPDATE', selected: false},
            {id: 13, name: $rootScope.lang==='AR' ? 'حذف الصلاحيات' : 'Delete Privileges' , value: 'ROLE_TEAM_DELETE', selected: false}
        ];


        if (team) {
            $scope.team = team;
            if(typeof team.authorities === 'string'){
                $scope.team.authorities = team.authorities.split(',');
            }
            //
            angular.forEach($scope.team.authorities, function (auth) {
                angular.forEach($scope.roles, function (role) {
                    if(role.value === auth){
                        return role.selected = true;
                    }
                })
            });
        } else {
            $scope.team = {};
        }

        $scope.title = title;

        $scope.action = action;

        $scope.submit = function () {
            $scope.team.authorities = [];
            angular.forEach($scope.roles, function (role) {
                if(role.selected){
                    $scope.team.authorities.push(role.value);
                }
            });
            $scope.team.authorities = $scope.team.authorities.join();
            switch ($scope.action) {
                case 'create' :
                    TeamService.create($scope.team).then(function (data) {
                        $scope.team = {};
                        $scope.from.$setPristine();
                    });
                    break;
                case 'update' :
                    TeamService.update($scope.team).then(function (data) {
                        $scope.team = data;
                        $scope.team.authorities = team.authorities.split(',');
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);