app.controller('teamCreateUpdateCtrl', ['TeamService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'team',
    function (TeamService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, team) {

        $scope.roles = [
            {id: 1, name: $rootScope.lang==='AR' ? 'تعديل بيانات الشركة' : 'Update Company Information', value: 'ROLE_COMPANY_UPDATE', selected: false},
            {id: 2, name: $rootScope.lang==='AR' ? 'إنشاء المناطق' : 'Create Regions', value: 'ROLE_REGION_CREATE', selected: false},
            {id: 3, name: $rootScope.lang==='AR' ? 'تعديل بيانات المناطق' : 'Update Regions Information', value: 'ROLE_REGION_UPDATE', selected: false},
            {id: 4, name: $rootScope.lang==='AR' ? 'حذف المناطق' : 'Delete Regions' , value: 'ROLE_REGION_DELETE', selected: false},
            {id: 5, name: $rootScope.lang==='AR' ? 'إنشاء الفروع' : 'Create Branches' , value: 'ROLE_BRANCH_CREATE', selected: false},
            {id: 6, name: $rootScope.lang==='AR' ? 'تعديل بيانات الفروع' : 'Update Branches Information' , value: 'ROLE_BRANCH_UPDATE', selected: false},
            {id: 7, name: $rootScope.lang==='AR' ? 'حذف الفروع' : 'Delete Branches' , value: 'ROLE_BRANCH_DELETE', selected: false},
            {id: 8, name: $rootScope.lang==='AR' ? 'إنشاء الاقسام' : 'Create Departments' , value: 'ROLE_DEPARTMENT_CREATE', selected: false},
            {id: 9, name: $rootScope.lang==='AR' ? 'تعديل بيانات الاقسام' : 'Update Departments Information' , value: 'ROLE_DEPARTMENT_UPDATE', selected: false},
            {id: 10, name: $rootScope.lang==='AR' ? 'حذف الاقسام' : 'Delete Departments' , value: 'ROLE_DEPARTMENT_DELETE', selected: false},
            {id: 11, name: $rootScope.lang==='AR' ? 'إنشاء حسابات الموظفون' : 'Create Employees' , value: 'ROLE_EMPLOYEE_CREATE', selected: false},
            {id: 12, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات الموظفون' : 'Update Employees Information' , value: 'ROLE_EMPLOYEE_UPDATE', selected: false},
            {id: 13, name: $rootScope.lang==='AR' ? 'حذف حسابات الموظفون' : 'Delete Employees' , value: 'ROLE_EMPLOYEE_DELETE', selected: false},
            {id: 14, name: $rootScope.lang==='AR' ? 'إنشاء المستخدمون' : 'Create Users Accounts' , value: 'ROLE_PERSON_CREATE', selected: false},
            {id: 15, name: $rootScope.lang==='AR' ? 'تعديل المستخدمون' : 'Update Users Information' , value: 'ROLE_PERSON_UPDATE', selected: false},
            {id: 16, name: $rootScope.lang==='AR' ? 'حذف المستخدمون' : 'Delete Users' , value: 'ROLE_PERSON_DELETE', selected: false},
            {id: 17, name: $rootScope.lang==='AR' ? 'إنشاء المهام' : 'Create Tasks' , value: 'ROLE_TASK_CREATE', selected: false},
            {id: 18, name: $rootScope.lang==='AR' ? 'تعديل المهام' : 'Update Tasks Information' , value: 'ROLE_TASK_UPDATE', selected: false},
            {id: 19, name: $rootScope.lang==='AR' ? 'حذف المهام' : 'Delete Tasks' , value: 'ROLE_TASK_DELETE', selected: false},
            {id: 20, name: $rootScope.lang==='AR' ? 'التعليق على المهام' : 'Create Comments' , value: 'ROLE_TASK_OPERATION_CREATE', selected: false},
            {id: 21, name: $rootScope.lang==='AR' ? 'إنشاء الصلاحيات' : 'Create Privileges' , value: 'ROLE_TEAM_CREATE', selected: false},
            {id: 22, name: $rootScope.lang==='AR' ? 'تعديل بيانات الصلاحيات' : 'Update Privileges' , value: 'ROLE_TEAM_UPDATE', selected: false},
            {id: 23, name: $rootScope.lang==='AR' ? 'حذف الصلاحيات' : 'Delete Privileges' , value: 'ROLE_TEAM_DELETE', selected: false}
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