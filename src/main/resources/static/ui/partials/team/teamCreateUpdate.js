app.controller('teamCreateUpdateCtrl', ['TeamService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'team',
    function (TeamService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, team) {

        $scope.roles = [
            {
                id: 1,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات الشركة' : 'Update Company Information',
                value: 'ROLE_COMPANY_UPDATE',
                selected: false
            },
            {
                id: 2,
                name: $rootScope.lang === 'AR' ? 'إنشاء حسابات العملاء' : 'Create Customers',
                value: 'ROLE_CUSTOMER_CREATE',
                selected: false
            },
            {
                id: 3,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات حسابات العملاء' : 'Update Customers Information',
                value: 'ROLE_CUSTOMER_UPDATE',
                selected: false
            },
            {
                id: 4,
                name: $rootScope.lang === 'AR' ? 'حذف حسابات العملاء' : 'Delete Customers',
                value: 'ROLE_CUSTOMER_DELETE',
                selected: false
            },
            {
                id: 5,
                name: $rootScope.lang === 'AR' ? 'تفعيل حسابات العملاء' : 'Enable Customers',
                value: 'ROLE_CUSTOMER_ENABLE',
                selected: false
            },
            {
                id: 6,
                name: $rootScope.lang === 'AR' ? 'تعطيل حسابات العملاء' : 'Disable Customers',
                value: 'ROLE_CUSTOMER_DISABLE',
                selected: false
            },
            {
                id: 7,
                name: $rootScope.lang === 'AR' ? 'إنشاء حسابات الاطباء' : 'Create Doctors',
                value: 'ROLE_DOCTOR_CREATE',
                selected: false
            },
            {
                id: 8,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات حسابات الاطباء' : 'Update Doctors Information',
                value: 'ROLE_DOCTOR_UPDATE',
                selected: false
            },
            {
                id: 9,
                name: $rootScope.lang === 'AR' ? 'حذف حسابات الاطباء' : 'Delete Doctors',
                value: 'ROLE_DOCTOR_DELETE',
                selected: false
            },
            {
                id: 10,
                name: $rootScope.lang === 'AR' ? 'تفعيل حسابات الاطباء' : 'Enable Doctors',
                value: 'ROLE_DOCTOR_ENABLE',
                selected: false
            },
            {
                id: 11,
                name: $rootScope.lang === 'AR' ? 'تعطيل حسابات الاطباء' : 'Disable Doctors',
                value: 'ROLE_DOCTOR_DISABLE',
                selected: false
            },
            {
                id: 12,
                name: $rootScope.lang === 'AR' ? 'إنشاء حسابات الموظفون' : 'Create Employees',
                value: 'ROLE_EMPLOYEE_CREATE',
                selected: false
            },
            {
                id: 13,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات حسابات الموظفون' : 'Update Employees Information',
                value: 'ROLE_EMPLOYEE_UPDATE',
                selected: false
            },
            {
                id: 14,
                name: $rootScope.lang === 'AR' ? 'حذف حسابات الموظفون' : 'Delete Employees',
                value: 'ROLE_EMPLOYEE_DELETE',
                selected: false
            },
            {
                id: 15,
                name: $rootScope.lang === 'AR' ? 'تفعيل حسابات الموظفون' : 'Enable Employees',
                value: 'ROLE_EMPLOYEE_ENABLE',
                selected: false
            },
            {
                id: 16,
                name: $rootScope.lang === 'AR' ? 'تعطيل حسابات الموظفون' : 'Disable Employees',
                value: 'ROLE_EMPLOYEE_DISABLE',
                selected: false
            },
            {
                id: 17,
                name: $rootScope.lang === 'AR' ? 'إنشاء أنواع الفحوصات' : 'Create Detection Types',
                value: 'ROLE_DETECTION_TYPE_CREATE',
                selected: false
            },
            {
                id: 18,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات أنواع الفحوصات' : 'Update Detection Types Information',
                value: 'ROLE_DETECTION_TYPE_UPDATE',
                selected: false
            },
            {
                id: 19,
                name: $rootScope.lang === 'AR' ? 'حذف أنواع الفحوصات' : 'Delete Detection Types',
                value: 'ROLE_DETECTION_TYPE_DELETE',
                selected: false
            },
            {
                id: 20,
                name: $rootScope.lang === 'AR' ? 'إنشاء طلبات الفحص' : 'Create Orders',
                value: 'ROLE_ORDER_CREATE',
                selected: false
            },
            {
                id: 21,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات طلبات الفحص' : 'Update Orders Information',
                value: 'ROLE_ORDER_UPDATE',
                selected: false
            },
            {
                id: 22,
                name: $rootScope.lang === 'AR' ? 'حذف طلبات الفحص' : 'Delete Orders',
                value: 'ROLE_ORDER_DELETE',
                selected: false
            },
            {
                id: 23,
                name: $rootScope.lang === 'AR' ? 'رفع مستندات طلبات الفحص' : 'Create Order Attachments',
                value: 'ROLE_ORDER_ATTACH_CREATE',
                selected: false
            },
            {
                id: 24,
                name: $rootScope.lang === 'AR' ? 'حذف مستندات طلبات الفحص' : 'Delete Order Attachments',
                value: 'ROLE_ORDER_ATTACH_DELETE',
                selected: false
            },
            {
                id: 25,
                name: $rootScope.lang === 'AR' ? 'إنشاء حسابات الصقور' : 'Create Falcon Account',
                value: 'ROLE_FALCON_CREATE',
                selected: false
            },
            {
                id: 26,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات حسابات الصقور' : 'Update Falcon Account Information',
                value: 'ROLE_FALCON_UPDATE',
                selected: false
            },
            {
                id: 27,
                name: $rootScope.lang === 'AR' ? 'حذف حسابات الصقور' : 'Delete Falcon Account',
                value: 'ROLE_FALCON_DELETE',
                selected: false
            },
            {
                id: 28,
                name: $rootScope.lang === 'AR' ? 'إنشاء الدواء' : 'Create Medicine',
                value: 'ROLE_DRUG_CREATE',
                selected: false
            },
            {
                id: 29,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات الدواء' : 'Update Medicine Information',
                value: 'ROLE_DRUG_UPDATE',
                selected: false
            },
            {
                id: 30,
                name: $rootScope.lang === 'AR' ? 'حذف الدواء' : 'Delete Medicine',
                value: 'ROLE_DRUG_DELETE',
                selected: false
            },
            {
                id: 31,
                name: $rootScope.lang === 'AR' ? 'إنشاء اصناف الدواء' : 'Create Medicine Category',
                value: 'ROLE_DRUG_CATEGORY_CREATE',
                selected: false
            },
            {
                id: 32,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات اصناف الدواء' : 'Update Medicine Category Information',
                value: 'ROLE_DRUG_CATEGORY_UPDATE',
                selected: false
            },
            {
                id: 33,
                name: $rootScope.lang === 'AR' ? 'حذف اصناف الدواء' : 'Delete Medicine Category',
                value: 'ROLE_DRUG_CATEGORY_DELETE',
                selected: false
            },
            {
                id: 34,
                name: $rootScope.lang === 'AR' ? 'إنشاء فواتير شراء الأدوية' : 'Create Drugs Bill Buy',
                value: 'ROLE_BILL_BUY_CREATE',
                selected: false
            },
            {
                id: 35,
                name: $rootScope.lang === 'AR' ? 'حذف فواتير شراء الأدوية' : 'Delete Drugs Bill Buy',
                value: 'ROLE_BILL_BUY_DELETE',
                selected: false
            },
            {
                id: 36,
                name: $rootScope.lang === 'AR' ? 'إنشاء فواتير بيع الأدوية' : 'Create Drugs Bill Sell',
                value: 'ROLE_BILL_SELL_CREATE',
                selected: false
            },
            {
                id: 37,
                name: $rootScope.lang === 'AR' ? 'حذف فواتير بيع الأدوية' : 'Delete Drugs Bill Sell',
                value: 'ROLE_BILL_SELL_DELETE',
                selected: false
            },
            {
                id: 38,
                name: $rootScope.lang === 'AR' ? 'إنشاء الصلاحيات' : 'Create Privileges',
                value: 'ROLE_TEAM_CREATE',
                selected: false
            },
            {
                id: 39,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات الصلاحيات' : 'Update Privileges',
                value: 'ROLE_TEAM_UPDATE',
                selected: false
            },
            {
                id: 40,
                name: $rootScope.lang === 'AR' ? 'حذف الصلاحيات' : 'Delete Privileges',
                value: 'ROLE_TEAM_DELETE',
                selected: false
            },
            {
                id: 41,
                name: $rootScope.lang === 'AR' ? 'إنشاء حسابات الموردين' : 'Create Supplier',
                value: 'ROLE_SUPPLIER_CREATE',
                selected: false
            },
            {
                id: 42,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات حسابات الموردين' : 'Update Supplier Information',
                value: 'ROLE_SUPPLIER_UPDATE',
                selected: false
            },
            {
                id: 43,
                name: $rootScope.lang === 'AR' ? 'حذف حسابات الموردين' : 'Delete Supplier',
                value: 'ROLE_SUPPLIER_DELETE',
                selected: false
            },
            {
                id: 44,
                name: $rootScope.lang === 'AR' ? 'تفعيل حسابات الموردين' : 'Enable Supplier',
                value: 'ROLE_SUPPLIER_ENABLE',
                selected: false
            },
            {
                id: 45,
                name: $rootScope.lang === 'AR' ? 'تعطيل حسابات الموردين' : 'Disable Supplier',
                value: 'ROLE_SUPPLIER_DISABLE',
                selected: false
            },
            {
                id: 46,
                name: $rootScope.lang === 'AR' ? 'إنشاء حسابات البنوك' : 'Create Bank Account',
                value: 'ROLE_BANK_CREATE',
                selected: false
            },
            {
                id: 47,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات حسابات البنوك' : 'Update Bank Account Information',
                value: 'ROLE_BANK_UPDATE',
                selected: false
            },
            {
                id: 48,
                name: $rootScope.lang === 'AR' ? 'حذف حسابات البنوك' : 'Delete Bank Account',
                value: 'ROLE_BANK_DELETE',
                selected: false
            },
            {
                id: 49,
                name: $rootScope.lang === 'AR' ? 'إنشاء الإيداعات' : 'Create Deposits',
                value: 'ROLE_DEPOSIT_CREATE',
                selected: false
            },
            {
                id: 50,
                name: $rootScope.lang === 'AR' ? 'إنشاء السحبيات' : 'Create Withdraws',
                value: 'ROLE_WITHDRAW_CREATE',
                selected: false
            },
            {
                id: 51,
                name: $rootScope.lang === 'AR' ? 'إنشاء الوصفات الطبية' : 'Create Prescription',
                value: 'ROLE_DIAGNOSIS_CREATE',
                selected: false
            },
            {
                id: 52,
                name: $rootScope.lang === 'AR' ? 'حذف الوصفات الطبية' : 'Delete Prescription',
                value: 'ROLE_DIAGNOSIS_DELETE',
                selected: false
            },
            {
                id: 53,
                name: $rootScope.lang === 'AR' ? 'تعديل اسعار الاصناف' : 'Update Prices',
                value: 'ROLE_DRUG_PRICE_UPDATE',
                selected: false
            },
            {
                id: 57,
                name: $rootScope.lang === 'AR' ? 'رفع مستندات الخدمات الطبية' : 'Create Detections Attachments',
                value: 'ROLE_ORDER_DETECTION_TYPE_ATTACH_CREATE',
                selected: false
            },
            {
                id: 58,
                name: $rootScope.lang === 'AR' ? 'حذف مستندات الخدمات الطبية' : 'Delete Detections Attachments',
                value: 'ROLE_ORDER_DETECTION_TYPE_ATTACH_DELETE',
                selected: false
            },
            {
                id: 59,
                name: $rootScope.lang === 'AR' ? 'تشخيص طلبات الفحص' : 'Diagnosing Orders',
                value: 'ROLE_ORDER_SAVE_NOTE',
                selected: false
            },
            {
                id: 60,
                name: $rootScope.lang === 'AR' ? 'تشخيص خدمات الفحص' : 'Diagnosing Detections',
                value: 'ROLE_ORDER_DETECTION_TYPE_SAVE_CASE',
                selected: false
            },
            {
                id: 61,
                name: $rootScope.lang === 'AR' ? 'تعديل كميات الاصناف' : 'Update Quantity',
                value: 'ROLE_DRUG_QUANTITY_UPDATE',
                selected: false
            },
            {
                id: 62,
                name: $rootScope.lang === 'AR' ? 'تسديد فواتير البيع' : 'Pay For Bill Sales',
                value: 'ROLE_BILL_SELL_PAY',
                selected: false
            },
            {
                id: 63,
                name: $rootScope.lang === 'AR' ? 'تسديد مستحقات طلبات الفحص' : 'Pay For Order Costs',
                value: 'ROLE_ORDER_PAY',
                selected: false
            },
            {
                id: 64,
                name: $rootScope.lang === 'AR' ? 'إنشاء بنود الاجازات' : 'Create Vacation Terms',
                value: 'ROLE_VACATION_TYPE_CREATE',
                selected: false
            },
            {
                id: 65,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات بنود الاجازات' : 'Update Vacation Terms Information',
                value: 'ROLE_VACATION_TYPE_UPDATE',
                selected: false
            },
            {
                id: 66,
                name: $rootScope.lang === 'AR' ? 'حذف بنود الاجازات' : 'Delete Vacation Terms',
                value: 'ROLE_VACATION_TYPE_DELETE',
                selected: false
            },
            {
                id: 67,
                name: $rootScope.lang === 'AR' ? 'إنشاء الاجازات' : 'Create Vacation',
                value: 'ROLE_VACATION_CREATE',
                selected: false
            },
            {
                id: 68,
                name: $rootScope.lang === 'AR' ? 'تعديل بيانات الاجازات' : 'Update Vacation Information',
                value: 'ROLE_VACATION_UPDATE',
                selected: false
            },
            {
                id: 69,
                name: $rootScope.lang === 'AR' ? 'حذف الاجازات' : 'Delete Vacation',
                value: 'ROLE_VACATION_DELETE',
                selected: false
            }
        ];


        if (team) {
            $scope.team = team;
            if (typeof team.authorities === 'string') {
                $scope.team.authorities = team.authorities.split(',');
            }
            //
            angular.forEach($scope.team.authorities, function (auth) {
                angular.forEach($scope.roles, function (role) {
                    if (role.value === auth) {
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
                if (role.selected) {
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