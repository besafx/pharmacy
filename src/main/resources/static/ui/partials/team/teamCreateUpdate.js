app.controller('teamCreateUpdateCtrl', ['TeamService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'team',
    function (TeamService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, team) {

        $scope.roles = [
            {id: 1, name: $rootScope.lang==='AR' ? 'تعديل بيانات الشركة' : 'Update Company Information', value: 'ROLE_COMPANY_UPDATE', selected: false},
            {id: 2, name: $rootScope.lang==='AR' ? 'إنشاء حسابات العملاء' : 'Create Customers' , value: 'ROLE_CUSTOMER_CREATE', selected: false},
            {id: 3, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات العملاء' : 'Update Customers Information' , value: 'ROLE_CUSTOMER_UPDATE', selected: false},
            {id: 4, name: $rootScope.lang==='AR' ? 'حذف حسابات العملاء' : 'Delete Customers' , value: 'ROLE_CUSTOMER_DELETE', selected: false},
            {id: 5, name: $rootScope.lang==='AR' ? 'تفعيل حسابات العملاء' : 'Enable Customers' , value: 'ROLE_CUSTOMER_ENABLE', selected: false},
            {id: 6, name: $rootScope.lang==='AR' ? 'تعطيل حسابات العملاء' : 'Disable Customers' , value: 'ROLE_CUSTOMER_DISABLE', selected: false},
            {id: 7, name: $rootScope.lang==='AR' ? 'إنشاء حسابات الاطباء' : 'Create Doctors' , value: 'ROLE_DOCTOR_CREATE', selected: false},
            {id: 8, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات الاطباء' : 'Update Doctors Information' , value: 'ROLE_DOCTOR_UPDATE', selected: false},
            {id: 9, name: $rootScope.lang==='AR' ? 'حذف حسابات الاطباء' : 'Delete Doctors' , value: 'ROLE_DOCTOR_DELETE', selected: false},
            {id: 10, name: $rootScope.lang==='AR' ? 'تفعيل حسابات الاطباء' : 'Enable Doctors' , value: 'ROLE_DOCTOR_ENABLE', selected: false},
            {id: 11, name: $rootScope.lang==='AR' ? 'تعطيل حسابات الاطباء' : 'Disable Doctors' , value: 'ROLE_DOCTOR_DISABLE', selected: false},
            {id: 12, name: $rootScope.lang==='AR' ? 'إنشاء حسابات الموظفون' : 'Create Employees' , value: 'ROLE_EMPLOYEE_CREATE', selected: false},
            {id: 13, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات الموظفون' : 'Update Employees Information' , value: 'ROLE_EMPLOYEE_UPDATE', selected: false},
            {id: 14, name: $rootScope.lang==='AR' ? 'حذف حسابات الموظفون' : 'Delete Employees' , value: 'ROLE_EMPLOYEE_DELETE', selected: false},
            {id: 15, name: $rootScope.lang==='AR' ? 'تفعيل حسابات الموظفون' : 'Enable Employees' , value: 'ROLE_EMPLOYEE_ENABLE', selected: false},
            {id: 16, name: $rootScope.lang==='AR' ? 'تعطيل حسابات الموظفون' : 'Disable Employees' , value: 'ROLE_EMPLOYEE_DISABLE', selected: false},
            {id: 17, name: $rootScope.lang==='AR' ? 'إنشاء أنواع الفحوصات' : 'Create Detection Types' , value: 'ROLE_DETECTION_TYPE_CREATE', selected: false},
            {id: 18, name: $rootScope.lang==='AR' ? 'تعديل بيانات أنواع الفحوصات' : 'Update Detection Types Information' , value: 'ROLE_DETECTION_TYPE_UPDATE', selected: false},
            {id: 19, name: $rootScope.lang==='AR' ? 'حذف أنواع الفحوصات' : 'Delete Detection Types' , value: 'ROLE_DETECTION_TYPE_DELETE', selected: false},
            {id: 17, name: $rootScope.lang==='AR' ? 'إنشاء فواتير الفحوصات' : 'Create Detection Bills' , value: 'ROLE_BILL_SELL_DETECTION_CREATE', selected: false},
            {id: 19, name: $rootScope.lang==='AR' ? 'حذف فواتير الفحوصات' : 'Delete Detection Bills' , value: 'ROLE_BILL_SELL_DETECTION_DELETE', selected: false},
            {id: 20, name: $rootScope.lang==='AR' ? 'إنشاء طلبات الفحص' : 'Create Orders' , value: 'ROLE_ORDER_CREATE', selected: false},
            {id: 21, name: $rootScope.lang==='AR' ? 'تعديل بيانات طلبات الفحص' : 'Update Orders Information' , value: 'ROLE_ORDER_UPDATE', selected: false},
            {id: 22, name: $rootScope.lang==='AR' ? 'حذف طلبات الفحص' : 'Delete Orders' , value: 'ROLE_ORDER_DELETE', selected: false},
            {id: 23, name: $rootScope.lang==='AR' ? 'رفع مستندات طلبات الفحص' : 'Create Order Attachments' , value: 'ROLE_ORDER_ATTACH_CREATE', selected: false},
            {id: 24, name: $rootScope.lang==='AR' ? 'حذف مستندات طلبات الفحص' : 'Delete Order Attachments' , value: 'ROLE_ORDER_ATTACH_DELETE', selected: false},
            {id: 25, name: $rootScope.lang==='AR' ? 'إنشاء حسابات الصقور' : 'Create Falcon Account' , value: 'ROLE_FALCON_CREATE', selected: false},
            {id: 26, name: $rootScope.lang==='AR' ? 'تعديل بيانات حسابات الصقور' : 'Update Falcon Account Information' , value: 'ROLE_FALCON_UPDATE', selected: false},
            {id: 27, name: $rootScope.lang==='AR' ? 'حذف حسابات الصقور' : 'Delete Falcon Account' , value: 'ROLE_FALCON_DELETE', selected: false},
            {id: 28, name: $rootScope.lang==='AR' ? 'إنشاء الدواء' : 'Create Medicine' , value: 'ROLE_DRUG_CREATE', selected: false},
            {id: 29, name: $rootScope.lang==='AR' ? 'تعديل بيانات الدواء' : 'Update Medicine Information' , value: 'ROLE_DRUG_UPDATE', selected: false},
            {id: 30, name: $rootScope.lang==='AR' ? 'حذف الدواء' : 'Delete Medicine' , value: 'ROLE_DRUG_DELETE', selected: false},
            {id: 31, name: $rootScope.lang==='AR' ? 'إنشاء اصناف الدواء' : 'Create Medicine Category' , value: 'ROLE_DRUG_CATEGORY_CREATE', selected: false},
            {id: 32, name: $rootScope.lang==='AR' ? 'تعديل بيانات اصناف الدواء' : 'Update Medicine Category Information' , value: 'ROLE_DRUG_CATEGORY_UPDATE', selected: false},
            {id: 33, name: $rootScope.lang==='AR' ? 'حذف اصناف الدواء' : 'Delete Medicine Category' , value: 'ROLE_DRUG_CATEGORY_DELETE', selected: false},
            {id: 34, name: $rootScope.lang==='AR' ? 'إنشاء الصلاحيات' : 'Create Privileges' , value: 'ROLE_TEAM_CREATE', selected: false},
            {id: 35, name: $rootScope.lang==='AR' ? 'تعديل بيانات الصلاحيات' : 'Update Privileges' , value: 'ROLE_TEAM_UPDATE', selected: false},
            {id: 36, name: $rootScope.lang==='AR' ? 'حذف الصلاحيات' : 'Delete Privileges' , value: 'ROLE_TEAM_DELETE', selected: false}
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
                        $uibModalInstance.close(data);
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