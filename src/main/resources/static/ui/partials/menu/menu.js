function menuCtrl($scope,
                  $rootScope,
                  $state,
                  $timeout,
                  $uibModal,
                  ModalProvider,
                  CompanyService,
                  CustomerService,
                  FalconService,
                  DoctorService,
                  DetectionTypeService,
                  OrderService,
                  DiagnosisService,
                  OrderDetectionTypeService,
                  OrderAttachService,
                  OrderReceiptService,
                  DrugService,
                  DrugUnitService,
                  BillBuyService,
                  BillSellService,
                  TransactionBuyService,
                  TransactionSellService,
                  DrugCategoryService,
                  SupplierService,
                  TeamService,
                  FundService,
                  FundReceiptService,
                  BankService,
                  BankReceiptService,
                  PersonService) {

    /**************************************************************************************************************
     *                                                                                                            *
     * General                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $timeout(function () {
        CompanyService.get().then(function (data) {
            $scope.selectedCompany = data;
        });
        window.componentHandler.upgradeAllRegistered();
    }, 600);
    $scope.$watch('toggleState', function (newValue, oldValue) {
        switch (newValue) {
            case 'menu': {
                $scope.pageTitle = 'القائمة';
                $scope.MDLIcon = 'widgets';
                break;
            }
            case 'company': {
                $scope.pageTitle = 'الشركة';
                $scope.MDLIcon = 'store';
                break;
            }
            case 'customer': {
                $scope.pageTitle = 'العملاء';
                $scope.MDLIcon = 'account_circle';
                break;
            }
            case 'falcon': {
                $scope.pageTitle = 'الصقور';
                $scope.MDLIcon = 'adb';
                break;
            }
            case 'doctor': {
                $scope.pageTitle = 'الاطباء';
                $scope.MDLIcon = 'local_hospital';
                break;
            }
            case 'detectionType': {
                $scope.pageTitle = 'أنواع الفحوصات';
                $scope.MDLIcon = 'spa';
                break;
            }
            case 'order': {
                $scope.pageTitle = 'طلبات الفحص';
                $scope.MDLIcon = 'burst_mode';
                break;
            }
            case 'diagnosis': {
                $scope.pageTitle = 'نتائج الفحص';
                $scope.MDLIcon = 'assignment';
                break;
            }
            case 'drug': {
                $scope.pageTitle = 'الدواء';
                $scope.MDLIcon = 'add_shopping_cart';
                break;
            }
            case 'supplier': {
                $scope.pageTitle = 'الموردين';
                $scope.MDLIcon = 'store';
                break;
            }
            case 'billBuy': {
                $scope.pageTitle = 'فواتير الشراء';
                $scope.MDLIcon = 'shopping_cart';
                break;
            }
            case 'insideSales': {
                $scope.pageTitle = 'المبيعات الداخلية';
                $scope.MDLIcon = 'shopping_cart';
                break;
            }
            case 'outsideSales': {
                $scope.pageTitle = 'المبيعات الخارجية';
                $scope.MDLIcon = 'shopping_cart';
                break;
            }
            case 'employee': {
                $scope.pageTitle = 'الموظفين';
                $scope.MDLIcon = 'person_pin';
                break;
            }
            case 'team': {
                $scope.pageTitle = 'الصلاحيات';
                $scope.MDLIcon = 'security';
                break;
            }
            case 'person': {
                $scope.pageTitle = 'المستخدمين';
                $scope.MDLIcon = 'lock';
                break;
            }
            case 'fund': {
                $scope.pageTitle = 'الصندوق';
                $scope.MDLIcon = 'monetization_on';
                break;
            }
            case 'bank': {
                $scope.pageTitle = 'البنك';
                $scope.MDLIcon = 'account_balance';
                break;
            }
            case 'profile': {
                $scope.pageTitle = 'الملف الشخصي';
                $scope.MDLIcon = 'account_circle';
                break;
            }
            case 'help': {
                $scope.pageTitle = 'المساعدة';
                $scope.MDLIcon = 'help';
                break;
            }
            case 'about': {
                $scope.pageTitle = 'عن البرنامج';
                $scope.MDLIcon = 'info';
                break;
            }
            case 'report': {
                $scope.pageTitle = 'التقارير';
                $scope.MDLIcon = 'print';
                break;
            }
        }
        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 500);
    }, true);
    $scope.toggleState = 'menu';
    $scope.openStateMenu = function () {
        $scope.toggleState = 'menu';
    };
    $scope.openStateCompany = function () {
        $scope.toggleState = 'company';
    };
    $scope.openStateCustomer = function () {
        $scope.toggleState = 'customer';
    };
    $scope.openStateFalcon = function () {
        $scope.toggleState = 'falcon';
    };
    $scope.openStateDoctor = function () {
        $scope.toggleState = 'doctor';
    };
    $scope.openStateDetectionType = function () {
        $scope.toggleState = 'detectionType';
    };
    $scope.openStateOrder = function () {
        $scope.toggleState = 'order';
        $scope.findOrdersByToday();
    };
    $scope.openStateDiagnosis = function () {
        $scope.toggleState = 'diagnosis';
        $scope.findOrdersByToday();
    };
    $scope.openStateDrug = function () {
        $scope.toggleState = 'drug';
    };
    $scope.openStateSupplier = function () {
        $scope.toggleState = 'supplier';
    };
    $scope.openStateBillBuy = function () {
        $scope.toggleState = 'billBuy';
    };
    $scope.openStateInsideSales = function () {
        $scope.toggleState = 'insideSales';
    };
    $scope.openStateOutsideSales = function () {
        $scope.toggleState = 'outsideSales';
    };
    $scope.openStateEmployee = function () {
        $scope.toggleState = 'employee';
        $timeout(function () {
            $scope.fetchEmployeeTableData();
        }, 500);
    };
    $scope.openStateTeam = function () {
        $scope.toggleState = 'team';
        $timeout(function () {
            $scope.fetchTeamTableData();
        }, 500);
    };
    $scope.openStatePerson = function () {
        $scope.toggleState = 'person';
        $timeout(function () {
            $scope.fetchActivePersonTableData();
        }, 500);
    };
    $scope.openStateFund = function () {
        $scope.toggleState = 'fund';
    };
    $scope.openStateBank = function () {
        $scope.toggleState = 'bank';
    };
    $scope.openStateProfile = function () {
        $scope.toggleState = 'profile';
    };
    $scope.openStateHelp = function () {
        $scope.toggleState = 'help';
    };
    $scope.openStateAbout = function () {
        $scope.toggleState = 'about';
    };
    $scope.openStateReport = function () {
        $scope.toggleState = 'report';
    };

    /**************************************************************************************************************
     *                                                                                                            *
     * Company                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.selectedCompany = {};
    $scope.submitCompany = function () {
        CompanyService.update($scope.selectedCompany).then(function (data) {
            $scope.selectedCompany = data;
        });
    };
    $scope.browseCompanyLogo = function () {
        document.getElementById('uploader-company').click();
    };
    $scope.uploadCompanyLogo = function (files) {
        CompanyService.uploadCompanyLogo(files[0]).then(function (data) {
            $scope.selectedCompany.logo = data;
        });
    };

    /**************************************************************************************************************
     *                                                                                                            *
     * Customer                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.customers = [];
    $scope.paramCustomer = {};
    $scope.findAllCustomers = function () {
        CustomerService.findAllInfo().then(function (data) {
            $scope.customers = data;
        });
    };
    $scope.filterCustomer = function () {
        var search = [];
        if ($scope.paramCustomer.code) {
            search.push('code=');
            search.push($scope.paramCustomer.code);
            search.push('&');
        }
        if ($scope.paramCustomer.name) {
            search.push('name=');
            search.push($scope.paramCustomer.name);
            search.push('&');
        }
        if ($scope.paramCustomer.mobile) {
            search.push('mobile=');
            search.push($scope.paramCustomer.mobile);
            search.push('&');
        }
        if ($scope.paramCustomer.identityNumber) {
            search.push('identityNumber=');
            search.push($scope.paramCustomer.identityNumber);
            search.push('&');
        }
        if ($scope.paramCustomer.email) {
            search.push('email=');
            search.push($scope.paramCustomer.email);
            search.push('&');
        }
        CustomerService.filter(search.join("")).then(function (data) {
            $scope.customers = data;
        });
    };
    $scope.openFilterCustomer = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerFilter.html',
            controller: 'customerFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramCustomer) {

            $scope.paramCustomer = paramCustomer;

            $scope.filterCustomer();

        }, function () {
        });
    };
    $scope.deleteCustomer = function (customer) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف العميل وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
            CustomerService.remove(customer.id).then(function () {
                var index = $scope.customers.indexOf(customer);
                $scope.customers.splice(index, 1);
            });
        });
    };
    $scope.newCustomer = function () {
        ModalProvider.openCustomerCreateModel().result.then(function (data) {
            $scope.customers.splice(0, 0, data);
            $scope.newFalcon(data);
        }, function () {
        });
    };
    $scope.newFalcon = function (customer) {
        $rootScope.showConfirmNotify("العمليات على قواعد البيانات", "هل تود ربط حساب صقر جديد بالعميل؟", "information", "fa-database", function () {
            ModalProvider.openFalconCreateByCustomerModel(customer).result.then(function (data) {
                return customer.falcons.push(data);
            }, function () {
            });
        });
    };
    $scope.enableCustomer = function (customer) {
        CustomerService.enable(customer).then(function (data) {
            return customer.enabled = data.enabled;
        });
    };
    $scope.disableCustomer = function (customer) {
        CustomerService.disable(customer).then(function (data) {
            return customer.enabled = data.enabled;
        });
    };
    $scope.rowMenuCustomer = [
        {
            html: '<div class="drop-menu">انشاء عميل جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newCustomer();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات العميل<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openCustomerUpdateModel($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">تفعيل العميل<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_ENABLE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.enableCustomer($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">تعطيل العميل<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DISABLE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.disableCustomer($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">حذف العميل<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteCustomer($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openCustomerDetailsModel($itemScope.customer);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Falcon                                                                                                     *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.falcons = [];
    $scope.paramFalcon = {};
    $scope.findAllFalcons = function () {
        FalconService.findAll().then(function (data) {
            $scope.falcons = data;
        });
    };
    $scope.filterFalcon = function () {
        var search = [];
        //
        if ($scope.paramFalcon.customerName) {
            search.push('customerName=');
            search.push($scope.paramFalcon.customerName);
            search.push('&');
        }
        if ($scope.paramFalcon.customerMobile) {
            search.push('customerMobile=');
            search.push($scope.paramFalcon.customerMobile);
            search.push('&');
        }
        if ($scope.paramFalcon.customerIdentityNumber) {
            search.push('customerIdentityNumber=');
            search.push($scope.paramFalcon.customerIdentityNumber);
            search.push('&');
        }
        //
        if ($scope.paramFalcon.falconCode) {
            search.push('falconCode=');
            search.push($scope.paramFalcon.falconCode);
            search.push('&');
        }
        if ($scope.paramFalcon.falconType) {
            search.push('falconType=');
            search.push($scope.paramFalcon.falconType);
            search.push('&');
        }
        if ($scope.paramFalcon.weightTo) {
            search.push('weightTo=');
            search.push($scope.paramFalcon.weightTo);
            search.push('&');
        }
        if ($scope.paramFalcon.weightFrom) {
            search.push('weightFrom=');
            search.push($scope.paramFalcon.weightFrom);
            search.push('&');
        }
        //
        FalconService.filter(search.join("")).then(function (data) {
            $scope.falcons = data;
        });

    };
    $scope.openFilterFalcon = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/falcon/falconFilter.html',
            controller: 'falconFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramFalcon) {

            $scope.paramFalcon = paramFalcon;

            $scope.filterFalcon();

        }, function () {
        });
    };
    $scope.deleteFalcon = function (falcon) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الحساب فعلاً؟", "error", "fa-trash", function () {
            FalconService.remove(falcon.id).then(function () {
                var index = $scope.falcons.indexOf(falcon);
                $scope.falcons.splice(index, 1);
            });
        });
    };
    $scope.newFalcon = function () {
        ModalProvider.openFalconCreateModel().result.then(function (data) {
            $scope.falcons.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.enableFalcon = function (falcon) {
        FalconService.enable(falcon).then(function (data) {
            return falcon.enabled = data.enabled;
        });
    };
    $scope.disableFalcon = function (falcon) {
        FalconService.disable(falcon).then(function (data) {
            return falcon.enabled = data.enabled;
        });
    };
    $scope.printFalcon = function (falcon) {
        var ids = [];
        ids.push(falcon.id);
        window.open('/report/falcons?ids=' + ids + '&exportType=PDF');
    };
    $scope.printFalcons = function () {
        var ids = [];
        angular.forEach($scope.falcons, function (falcon) {
            ids.push(falcon.id);
        });
        window.open('/report/falcons?ids=' + ids + '&exportType=PDF');
    };
    $scope.rowMenuFalcon = [
        {
            html: '<div class="drop-menu">انشاء حساب جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FALCON_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newFalcon();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات الحساب<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FALCON_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openFalconUpdateModel($itemScope.falcon);
            }
        },
        {
            html: '<div class="drop-menu">حذف الحساب<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_FALCON_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteFalcon($itemScope.falcon);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openFalconDetailsModel($itemScope.falcon);
            }
        },
        {
            html: '<div class="drop-menu">تقرير مختصر<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printFalcon();
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Doctor                                                                                                     *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.doctors = [];
    $scope.findAllDoctors = function () {
        DoctorService.findAll().then(function (data) {
            $scope.doctors = data;
        });
    };
    $scope.deleteDoctor = function (doctor) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطبيب فعلاً؟", "error", "fa-trash", function () {
            DoctorService.remove(doctor.id).then(function () {
                var index = $scope.doctors.indexOf(doctor);
                $scope.doctors.splice(index, 1);
            });
        });
    };
    $scope.newDoctor = function () {
        ModalProvider.openDoctorCreateModel().result.then(function (data) {
            $scope.doctors.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.enable = function () {
        DoctorService.enable($scope.selected).then(function (data) {

        });
    };
    $scope.disable = function () {
        DoctorService.disable($scope.selected).then(function (data) {

        });
    };
    $scope.rowMenuDoctor = [
        {
            html: '<div class="drop-menu">انشاء طبيب جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DOCTOR_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newDoctor();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات الطبيب<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DOCTOR_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openDoctorUpdateModel($itemScope.doctor);
            }
        },
        {
            html: '<div class="drop-menu">حذف الطبيب<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DOCTOR_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteDoctor($itemScope.doctor);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * DetectionType                                                                                              *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.detectionTypes = [];
    $scope.findAllDetectionTypes = function () {
        DetectionTypeService.findAll().then(function (data) {
            $scope.detectionTypes = data;
        });
    };
    $scope.deleteDetectionType = function (detectionType) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف النوع فعلاً؟", "error", "fa-trash", function () {
            DetectionTypeService.remove(detectionType.id).then(function () {
                var index = $scope.detectionTypes.indexOf(detectionType);
                $scope.detectionTypes.splice(index, 1);
            });
        });
    };
    $scope.newDetectionType = function () {
        ModalProvider.openDetectionTypeCreateModel().result.then(function (data) {
            $scope.detectionTypes.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.rowMenuDetectionType = [
        {
            html: '<div class="drop-menu">انشاء نوع جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DETECTION_TYPE_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newDetectionType();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات النوع<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DETECTION_TYPE_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openDetectionTypeUpdateModel($itemScope.detectionType);
            }
        },
        {
            html: '<div class="drop-menu">حذف النوع<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DETECTION_TYPE_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteDetectionType($itemScope.detectionType);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Order                                                                                                      *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.itemsOrder = [];
    $scope.itemsOrder.push(
        {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'}
    );
    $scope.orders = [];
    $scope.paramOrder = {};
    $scope.findOrdersByToday = function () {
        OrderService.findByToday().then(function (data) {
            $scope.orders = data;
            $scope.itemsOrder = [];
            $scope.itemsOrder.push(
                {
                    'id': 1,
                    'type': 'title',
                    'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'
                },
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات اليوم' : 'Orders For Today'}
            );
        });
    };
    $scope.findOrdersByWeek = function () {
        OrderService.findByWeek().then(function (data) {
            $scope.orders = data;
            $scope.itemsOrder = [];
            $scope.itemsOrder.push(
                {
                    'id': 1,
                    'type': 'title',
                    'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'
                },
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الاسبوع' : 'Orders For Week'}
            );
        });
    };
    $scope.findOrdersByMonth = function () {
        OrderService.findByMonth().then(function (data) {
            $scope.orders = data;
            $scope.itemsOrder = [];
            $scope.itemsOrder.push(
                {
                    'id': 1,
                    'type': 'title',
                    'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'
                },
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات الشهر' : 'Orders For Month'}
            );
        });
    };
    $scope.findOrdersByYear = function () {
        OrderService.findByYear().then(function (data) {
            $scope.orders = data;
            $scope.itemsOrder = [];
            $scope.itemsOrder.push(
                {
                    'id': 1,
                    'type': 'title',
                    'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'
                },
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'طلبات العام' : 'Orders For Year'}
            );
        });
    };
    $scope.openFilterOrder = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/order/orderFilter.html',
            controller: 'orderFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false,
            size: 'lg'
        });

        modalInstance.result.then(function (paramOrder) {
            var search = [];

            if (paramOrder.codeFrom) {
                search.push('codeFrom=');
                search.push(paramOrder.codeFrom);
                search.push('&');
            }
            if (paramOrder.codeTo) {
                search.push('codeTo=');
                search.push(paramOrder.codeTo);
                search.push('&');
            }
            //
            if (paramOrder.paymentMethodList) {
                var paymentMethods = [];
                for (var i = 0; i < paramOrder.paymentMethodList.length; i++) {
                    paymentMethods.push(paramOrder.paymentMethodList[i]);
                }
                search.push('paymentMethods=');
                search.push(paymentMethods);
                search.push('&');
            }
            //
            if (paramOrder.dateTo) {
                search.push('dateTo=');
                search.push(paramOrder.dateTo.getTime());
                search.push('&');
            }
            if (paramOrder.dateFrom) {
                search.push('dateFrom=');
                search.push(paramOrder.dateFrom.getTime());
                search.push('&');
            }
            //
            if (paramOrder.customerName) {
                search.push('customerName=');
                search.push(paramOrder.customerName);
                search.push('&');
            }
            if (paramOrder.customerMobile) {
                search.push('customerMobile=');
                search.push(paramOrder.customerMobile);
                search.push('&');
            }
            if (paramOrder.customerIdentityNumber) {
                search.push('customerIdentityNumber=');
                search.push(paramOrder.customerIdentityNumber);
                search.push('&');
            }
            //
            if (paramOrder.falconCode) {
                search.push('falconCode=');
                search.push(paramOrder.falconCode);
                search.push('&');
            }
            if (paramOrder.falconType) {
                search.push('falconType=');
                search.push(paramOrder.falconType);
                search.push('&');
            }
            if (paramOrder.weightTo) {
                search.push('weightTo=');
                search.push(paramOrder.weightTo);
                search.push('&');
            }
            if (paramOrder.weightFrom) {
                search.push('weightFrom=');
                search.push(paramOrder.weightFrom);
                search.push('&');
            }
            //
            OrderService.filter(search.join("")).then(function (data) {
                $scope.orders = data;
                $scope.itemsOrder = [];
                $scope.itemsOrder.push(
                    {
                        'id': 1,
                        'type': 'title',
                        'name': $rootScope.lang === 'AR' ? 'طلبات الفحص' : 'Detection Orders'
                    },
                    {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filter'}
                );
            });
        }, function () {
        });
    };
    $scope.deleteOrder = function (order) {
        $rootScope.showConfirmNotify("الإستقبال", "هل تود حذف الطلب فعلاً؟", "error", "fa-trash", function () {
            OrderService.remove(order.id).then(function () {
                var index = $scope.orders.indexOf(order);
                $scope.orders.splice(index, 1);
            });
        });
    };
    $scope.newOrder = function () {
        ModalProvider.openOrderCreateModel().result.then(function (data) {
            $scope.orders.splice(0, 0, data);
            $rootScope.showConfirmNotify("الإستقبال", "هل تود طباعة الطلب ؟", "notification", "fa-info", function () {
                $scope.printPending(data);
            });
        }, function () {
        });
    };
    $scope.newOrderReceipt = function (order) {
        ModalProvider.openOrderReceiptCreateModel(order).result.then(function (data) {
            OrderService.findPrices(order.id).then(function (newOrder) {
                order.netCost = newOrder.netCost;
                order.paid = newOrder.paid;
                order.remain = newOrder.remain;
            });
            return order.orderReceipts.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.printPending = function (order) {
        window.open('/report/order/pending/' + order.id + '/PDF');
    };
    $scope.printDiagnosed = function (order) {
        window.open('/report/order/diagnosed/' + order.id + '/PDF');
    };
    $scope.printOrdersSummaryByList = function () {
        var ids = [];
        angular.forEach($scope.orders, function (order) {
            ids.push(order.id);
        });
        window.open('/report/orders/summary/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.printOrdersDetailsByList = function () {
        var ids = [];
        angular.forEach($scope.orders, function (order) {
            ids.push(order.id);
        });
        window.open('/report/orders/details/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.rowMenuOrder = [
        {
            html: '<div class="drop-menu">انشاء طلب جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newOrder();
            }
        },
        {
            html: '<div class="drop-menu">حذف الطلب<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteOrder($itemScope.order);
            }
        },
        {
            html: '<div class="drop-menu">علاج جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DIAGNOSIS_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newDiagnosis($itemScope.order);
            }
        },
        {
            html: '<div class="drop-menu">تسديد دفعة<span class="fa fa-money fa-lg"></span></div>',
            enabled: function ($itemScope) {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_ORDER_RECEIPT_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newOrderReceipt($itemScope.order);
            }
        },
        {
            html: '<div class="drop-menu">طباعة طلب الفحص<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printPending($itemScope.order);
            }
        },
        {
            html: '<div class="drop-menu">طباعة التشخيص<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printDiagnosed($itemScope.order);
            }
        },
        {
            html: '<div class="drop-menu">طباعة تقرير مختصر<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openReportOrderByListModel($scope.orders);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openOrderDetailsModel($itemScope.order);
            }
        }
    ];
    //////////////////////////Area Chart///////////////////////////////////
    $scope.series = ['عدد الطلبات'];
    $scope.labels = [];
    $scope.data = [[]];
    $scope.datasetOverride = [{yAxisID: 'y-axis-1'}];
    $scope.options = {
        scales: {
            yAxes: [
                {
                    id: 'y-axis-1',
                    type: 'linear',
                    display: true,
                    position: 'left'
                }
            ]
        }
    };
    $scope.findOrderQuantityByDay = function () {
        $scope.labels = [];
        $scope.data = [[]];
        OrderService.findQuantityByDay().then(function (data) {
            angular.forEach(data, function (wrapper) {
                $scope.labels.push(wrapper.obj1);
                $scope.data[0].push(wrapper.obj2);
            });
        });
    };
    $scope.findOrderQuantityByMonth = function () {
        $scope.labels = [];
        $scope.data = [[]];
        OrderService.findQuantityByMonth().then(function (data) {
            angular.forEach(data, function (wrapper) {
                $scope.labels.push(wrapper.obj1);
                $scope.data[0].push(wrapper.obj2);
            });
        });
    };
    //////////////////////////Area Chart///////////////////////////////////

    /**************************************************************************************************************
     *                                                                                                            *
     * Diagnosis                                                                                                  *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.newDiagnosis = function (order) {
        ModalProvider.openDiagnosisCreateModel(order).result.then(function (data) {
            if (order.diagnoses) {
                Array.prototype.splice.apply(order.diagnoses, [1, 0].concat(data));
            }
        }, function () {
        });
    };

    /**************************************************************************************************************
     *                                                                                                            *
     * Drug                                                                                                       *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.drugs = [];
    $scope.clearParamDrug = function () {
        $scope.paramDrug = {};
        $scope.paramDrug.drugCategoryList = [];
    };
    $scope.clearParamDrug();
    $scope.filterDrugs = function () {
        var search = [];
        //
        if ($scope.paramDrug.codeFrom) {
            search.push('codeFrom=');
            search.push($scope.paramDrug.codeFrom);
            search.push('&');
        }
        if ($scope.paramDrug.codeTo) {
            search.push('codeTo=');
            search.push($scope.paramDrug.codeTo);
            search.push('&');
        }
        //
        if ($scope.paramDrug.nameArabic) {
            search.push('nameArabic=');
            search.push($scope.paramDrug.nameArabic);
            search.push('&');
        }
        if ($scope.paramDrug.nameEnglish) {
            search.push('nameEnglish=');
            search.push($scope.paramDrug.nameEnglish);
            search.push('&');
        }
        //
        if ($scope.paramDrug.medicalNameArabic) {
            search.push('medicalNameArabic=');
            search.push($scope.paramDrug.medicalNameArabic);
            search.push('&');
        }
        if ($scope.paramDrug.medicalNameEnglish) {
            search.push('medicalNameEnglish=');
            search.push($scope.paramDrug.medicalNameEnglish);
            search.push('&');
        }
        //
        if ($scope.paramDrug.drugCategoryList.length > 0) {
            var drugCategories = [];
            for (var i = 0; i < $scope.paramDrug.drugCategoryList.length; i++) {
                drugCategories.push($scope.paramDrug.drugCategoryList[i].id);
            }
            if (drugCategories.length > 0) {
                search.push('drugCategories=');
                search.push(drugCategories);
                search.push('&');
            }
        }
        //
        DrugService.filter(search.join("")).then(function (data) {
            $scope.drugs = data;
        });
    };
    $scope.openFilterDrug = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugFilter.html',
            controller: 'drugFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramDrug) {
            $scope.paramDrug = paramDrug;
            $scope.filterDrugs();
        }, function () {
        });
    };
    $scope.newDrug = function () {
        ModalProvider.openDrugCreateModel().result.then(function (data) {
            $scope.drugs.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.deleteDrug = function (drug) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الدواء فعلاً؟", "error", "fa-trash", function () {
            DrugService.remove(drug.id).then(function () {
                var index = $scope.drugs.indexOf(drug);
                $scope.drugs.splice(index, 1);
            });
        });
    };
    $scope.refreshDrugCategories = function () {
        DrugCategoryService.findAll().then(function (data) {
            $scope.categories = data;
        });
    };
    $scope.newDrugCategory = function () {
        ModalProvider.openDrugCategoryCreateModel().result.then(function (data) {
        }, function () {
        });
    };
    $scope.printDrug = function (drug) {
        window.open('/report/drug/' + drug.id + '/PDF');
    };
    $scope.printDrugList = function () {
        var ids = [];
        angular.forEach($scope.drugs, function (drug) {
            ids.push(drug.id);
        });
        window.open('/report/drugs?ids=' + ids + "&exportType=PDF");
    };
    $scope.rowMenuDrug = [
        {
            html: '<div class="drop-menu">انشاء دواء جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newDrug();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات الدواء<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openDrugUpdateModel($itemScope.drug);
            }
        },
        {
            html: '<div class="drop-menu">حذف الدواء<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteDrug($itemScope.drug);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openDrugDetailsModel($itemScope.drug);
            }
        },
        {
            html: '<div class="drop-menu">طباعة الكل<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printDrugList();
            }
        },
        {
            html: '<div class="drop-menu">تقرير مختصر<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printDrug($itemScope.drug);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Supplier                                                                                                   *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.suppliers = [];
    $scope.fetchSupplierTableData = function () {
        SupplierService.findAll().then(function (data) {
            $scope.suppliers = data;
        });
    };
    $scope.deleteSupplier = function (supplier) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المورد وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
            SupplierService.remove(supplier.id).then(function () {
                var index = $scope.suppliers.indexOf(supplier);
                $scope.suppliers.splice(index, 1);
            });
        });
    };
    $scope.newSupplier = function () {
        ModalProvider.openSupplierCreateModel().result.then(function (data) {
            $scope.suppliers.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.enableSupplier = function (supplier) {
        SupplierService.enable(supplier).then(function (data) {

        });
    };
    $scope.disableSupplier = function () {
        SupplierService.disable(supplier).then(function (data) {

        });
    };
    $scope.rowMenuSupplier = [
        {
            html: '<div class="drop-menu">انشاء مورد جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newSupplier();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات المورد<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openSupplierUpdateModel($itemScope.supplier);
            }
        },
        {
            html: '<div class="drop-menu">حذف المورد<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteSupplier($itemScope.supplier);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * BillBuy                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.billBuys = [];
    $scope.paramBillBuy = {};
    $scope.paramBillBuy.suppliersList = [];
    $scope.findAllBillBuy = function () {
        BillBuyService.findAll().then(function (data) {
            $scope.billBuys = data;
            $scope.subTitle = $rootScope.lang === 'AR' ? 'كل الفواتير' : 'All Bills';
        });
    };
    $scope.findBillBuyByToday = function () {
        BillBuyService.findByToday().then(function (data) {
            $scope.billBuys = data;
            $scope.subTitle = $rootScope.lang === 'AR' ? 'فواتير اليوم' : 'Today Bills';
        });
    };
    $scope.findBillBuyByWeek = function () {
        BillBuyService.findByWeek().then(function (data) {
            $scope.billBuys = data;
            $scope.subTitle = $rootScope.lang === 'AR' ? 'فواتير الاسبوع' : 'Week Bills';
        });
    };
    $scope.findBillBuyByMonth = function () {
        BillBuyService.findByMonth().then(function (data) {
            $scope.billBuys = data;
            $scope.subTitle = $rootScope.lang === 'AR' ? 'فواتير الشهر' : 'Month Bills';
        });
    };
    $scope.findBillBuyByYear = function () {
        BillBuyService.findByYear().then(function (data) {
            $scope.billBuys = data;
            $scope.subTitle = $rootScope.lang === 'AR' ? 'فواتير العام' : 'Year Bills';
        });
    };
    $scope.filterBillBuy = function () {
        var search = [];

        if ($scope.paramBillBuy.codeFrom) {
            search.push('codeFrom=');
            search.push($scope.paramBillBuy.codeFrom);
            search.push('&');
        }
        if ($scope.paramBillBuy.codeTo) {
            search.push('codeTo=');
            search.push($scope.paramBillBuy.codeTo);
            search.push('&');
        }
        //
        if ($scope.paramBillBuy.dateTo) {
            search.push('dateTo=');
            search.push($scope.paramBillBuy.dateTo.getTime());
            search.push('&');
        }
        if ($scope.paramBillBuy.dateFrom) {
            search.push('dateFrom=');
            search.push($scope.paramBillBuy.dateFrom.getTime());
            search.push('&');
        }
        //
        if ($scope.paramBillBuy.suppliersList) {
            var suppliers = [];
            for (var i = 0; i < $scope.paramBillBuy.suppliersList.length; i++) {
                suppliers.push($scope.paramBillBuy.suppliersList[i].id);
            }
            if (suppliers.length > 0) {
                search.push('suppliers=');
                search.push(suppliers);
                search.push('&');
            }
        }
        //
        BillBuyService.filter(search.join("")).then(function (data) {
            $scope.billBuys = data;
            $scope.subTitle = $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filtered Data';
        });
    };
    $scope.openFilterBillBuy = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billBuy/billBuyFilter.html',
            controller: 'billBuyFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramBillBuy) {

            $scope.paramBillBuy = paramBillBuy;

            $scope.filterBillBuy();

        }, function () {
        });

    };
    $scope.newBillBuy = function () {
        ModalProvider.openBillBuyCreateModel().result.then(function (data) {
            $rootScope.showConfirmNotify("المشتريات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                $scope.printBillBuy(data);
            });
            $scope.billBuys.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.newBillBuyReceipt = function (billBuy) {
        ModalProvider.openBillBuyReceiptCreateModel(billBuy).result.then(function (data) {
            var index = $scope.billBuys.indexOf(billBuy);
            $scope.billBuys[index].paid = data.paid;
            $scope.billBuys[index].remain = data.remain;
        }, function () {
        });
    };
    $scope.newTransactionBuy = function (billBuy) {
        ModalProvider.openTransactionBuyCreateModel(billBuy).result.then(function (data) {
            if (billBuy.transactionBuys) {
                return billBuy.transactionBuys.splice(0, 0, data);
            }
        }, function () {
        });
    };
    $scope.deleteBillBuy = function (billBuy) {
        $rootScope.showConfirmNotify("المخازن", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
            BillBuyService.remove(billBuy.id).then(function () {
                var index = $scope.billBuys.indexOf(billBuy);
                $scope.billBuys.splice(index, 1);
            });
        });
    };
    $scope.printBillBuysByList = function () {
        var ids = [];
        angular.forEach($scope.billBuys, function (billBuy) {
            ids.push(billBuy.id);
        });
        window.open('/report/billBuy/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.printBillBuysDetailsByList = function () {
        var ids = [];
        angular.forEach($scope.billBuys, function (billBuy) {
            ids.push(billBuy.id);
        });
        window.open('/report/billBuy/details/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.printBillBuy = function (billBuy) {
        window.open('/report/billBuy/' + billBuy.id + '/PDF');
    };
    $scope.rowMenuBillBuy = [
        {
            html: '<div class="drop-menu">انشاء فاتورة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newBillBuy();
            }
        },
        {
            html: '<div class="drop-menu">حذف الفاتورة<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteBillBuy($itemScope.billBuy);
            }
        },
        {
            html: '<div class="drop-menu">تسديد دفعة<span class="fa fa-money fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_RECEIPT_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newBillBuyReceipt($itemScope.billBuy);
            }
        },
        {
            html: '<div class="drop-menu">اضافة صنف<span class="fa fa-plus-square fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_ADD_ITEM']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newTransactionBuy($itemScope.billBuy);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openBillBuyDetailsModel($itemScope.billBuy);
            }
        },
        {
            html: '<div class="drop-menu">طباعة الفاتورة<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printBillBuy($itemScope.billBuy);
            }
        }
    ];

    /**************************************************************
     *                                                            *
     * Bill Sell                                                  *
     *                                                            *
     *************************************************************/
    $scope.newTransactionSell = function (billSell) {
        ModalProvider.openTransactionSellCreateModel(billSell).result.then(function (data) {
            return billSell.transactionSells.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.newBillSellReceipt = function (billSell) {
        ModalProvider.openBillSellReceiptCreateModel(billSell).result.then(function (data) {
            BillSellService.findPrices(billSell.id).then(function (newBillSell) {
                billSell.net = newBillSell.net;
                billSell.paid = newBillSell.paid;
                billSell.remain = newBillSell.remain;
            });
            if (billSell.billSellReceipts) {
                return billSell.billSellReceipts.splice(0, 0, data);
            }

        }, function () {
        });
    };

    /**************************************************************************************************************
     *                                                                                                            *
     * InsideSales                                                                                                *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.insideBillSells = [];
    $scope.paramInsideSales = {};
    $scope.openInsideSalesFilter = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSell/insideSalesFilter.html',
            controller: 'insideSalesFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'البحث فى المبيعات الداخلية' : 'Searching For Inside Sales';
                }
            }
        });

        modalInstance.result.then(function (paramInsideSales) {
            $scope.paramInsideSales = paramInsideSales;
            $scope.refreshInsideSalesTable();
        }, function () {
        });
    };
    $scope.refreshInsideSalesTable = function () {
        var search = [];
        //
        if ($scope.paramInsideSales.codeFrom) {
            search.push('codeFrom=');
            search.push($scope.paramInsideSales.codeFrom);
            search.push('&');
        }
        if ($scope.paramInsideSales.codeTo) {
            search.push('codeTo=');
            search.push($scope.paramInsideSales.codeTo);
            search.push('&');
        }
        //
        if ($scope.paramInsideSales.dateTo) {
            search.push('dateTo=');
            search.push($scope.paramInsideSales.dateTo.getTime());
            search.push('&');
        }
        if ($scope.paramInsideSales.dateFrom) {
            search.push('dateFrom=');
            search.push($scope.paramInsideSales.dateFrom.getTime());
            search.push('&');
        }
        //
        if ($scope.paramInsideSales.orderCodeFrom) {
            search.push('orderCodeFrom=');
            search.push($scope.paramInsideSales.orderCodeFrom);
            search.push('&');
        }
        if ($scope.paramInsideSales.orderCodeTo) {
            search.push('orderCodeTo=');
            search.push($scope.paramInsideSales.orderCodeTo);
            search.push('&');
        }
        //
        if ($scope.paramInsideSales.orderFalconCode) {
            search.push('orderFalconCode=');
            search.push($scope.paramInsideSales.orderFalconCode);
            search.push('&');
        }
        if ($scope.paramInsideSales.orderCustomerName) {
            search.push('orderCustomerName=');
            search.push($scope.paramInsideSales.orderCustomerName);
            search.push('&');
        }
        BillSellService.filterInside(search.join("")).then(function (data) {

            $scope.insideBillSells = data;

            $scope.subTitle_insideSales = $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filtered Data';

        });
    };
    $scope.findInsideSalesByToday = function () {
        BillSellService.findInsideSalesByToday().then(function (data) {

            $scope.insideBillSells = data;

            $scope.subTitle_insideSales = $rootScope.lang === 'AR' ? 'فواتير اليوم' : 'Today Bills';

        });
    };
    $scope.findInsideSalesByWeek = function () {
        BillSellService.findInsideSalesByWeek().then(function (data) {

            $scope.insideBillSells = data;

            $scope.subTitle_insideSales = $rootScope.lang === 'AR' ? 'فواتير الاسبوع' : 'Week Bills';

        });
    };
    $scope.findInsideSalesByMonth = function () {
        BillSellService.findInsideSalesByMonth().then(function (data) {

            $scope.insideBillSells = data;

            $scope.subTitle_insideSales = $rootScope.lang === 'AR' ? 'فواتير الشهر' : 'Month Bills';

        });
    };
    $scope.findInsideSalesByYear = function () {
        BillSellService.findInsideSalesByYear().then(function (data) {

            $scope.insideBillSells = data;

            $scope.subTitle_insideSales = $rootScope.lang === 'AR' ? 'فواتير العام' : 'Year Bills';

        });
    };
    $scope.newInsideSales = function () {
        ModalProvider.openInsideSalesCreateModel().result.then(function (data) {
            $rootScope.showConfirmNotify("المبيعات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                $scope.printInsideBillPurchase(data);
            });
            $scope.insideBillSells.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.deleteInsideBillSell = function (billSell) {
        $rootScope.showConfirmNotify("المبيعات الداخلية", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
            BillSellService.remove(billSell.id).then(function () {
                var index = $scope.insideBillSells.indexOf(billSell);
                $scope.insideBillSells.splice(index, 1);
            });
        });
    };
    $scope.printInsideSalesByList = function () {
        var ids = [];
        angular.forEach($scope.insideBillSells, function (billSell) {
            ids.push(billSell.id);
        });
        window.open('/report/insideSales/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.printInsideSalesDetailsByList = function () {
        var ids = [];
        angular.forEach($scope.insideBillSells, function (billSell) {
            ids.push(billSell.id);
        });
        window.open('/report/insideSales/details/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.printInsideBillPurchase = function (billSell) {
        window.open('/report/insideBillPurchase/' + billSell.id + '/PDF');
    };
    $scope.rowMenuInsideSales = [
        {
            html: '<div class="drop-menu">انشاء فاتورة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newInsideSales();
            }
        },
        {
            html: '<div class="drop-menu">حذف الفاتورة<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteInsideBillSell($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">تسديد دفعة<span class="fa fa-money fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_RECEIPT_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newBillSellReceipt($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">اضافة صنف<span class="fa fa-plus-square fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_ADD_ITEM']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newTransactionSell($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openInsideSalesDetailsModel($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">طباعة الفاتورة<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printInsideBillPurchase($itemScope.billSell);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * OutsideSales                                                                                               *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.outsideBillSells = [];
    $scope.paramOutsideSales = {};
    $scope.openOutsideSalesFilter = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSell/outsideSalesFilter.html',
            controller: 'outsideSalesFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'البحث فى المبيعات الخارجية' : 'Searching For Outside Sales';
                }
            }
        });

        modalInstance.result.then(function (paramOutsideSales) {
            $scope.paramOutsideSales = paramOutsideSales;
            $scope.refreshOutsideSalesTable();
        }, function () {
        });
    };
    $scope.refreshOutsideSalesTable = function () {
        var search = [];
        //
        if ($scope.paramOutsideSales.codeFrom) {
            search.push('codeFrom=');
            search.push($scope.paramOutsideSales.codeFrom);
            search.push('&');
        }
        if ($scope.paramOutsideSales.codeTo) {
            search.push('codeTo=');
            search.push($scope.paramOutsideSales.codeTo);
            search.push('&');
        }
        //
        if ($scope.paramOutsideSales.dateTo) {
            search.push('dateTo=');
            search.push($scope.paramOutsideSales.dateTo.getTime());
            search.push('&');
        }
        if ($scope.paramOutsideSales.dateFrom) {
            search.push('dateFrom=');
            search.push($scope.paramOutsideSales.dateFrom.getTime());
            search.push('&');
        }
        BillSellService.filterOutside(search.join("")).then(function (data) {

            $scope.outsideBillSells = data;

            $scope.subTitle_outsideSales = $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Filtered Data';

        });
    };
    $scope.findOutsideSalesByToday = function () {
        BillSellService.findOutsideSalesByToday().then(function (data) {

            $scope.outsideBillSells = data;

            $scope.subTitle_outsideSales = $rootScope.lang === 'AR' ? 'فواتير اليوم' : 'Today Bills';

        });
    };
    $scope.findOutsideSalesByWeek = function () {
        BillSellService.findOutsideSalesByWeek().then(function (data) {

            $scope.outsideBillSells = data;

            $scope.subTitle_outsideSales = $rootScope.lang === 'AR' ? 'فواتير الاسبوع' : 'Week Bills';

        });
    };
    $scope.findOutsideSalesByMonth = function () {
        BillSellService.findOutsideSalesByMonth().then(function (data) {

            $scope.outsideBillSells = data;

            $scope.subTitle_outsideSales = $rootScope.lang === 'AR' ? 'فواتير الشهر' : 'Month Bills';

        });
    };
    $scope.findOutsideSalesByYear = function () {
        BillSellService.findOutsideSalesByYear().then(function (data) {

            $scope.outsideBillSells = data;

            $scope.subTitle_outsideSales = $rootScope.lang === 'AR' ? 'فواتير العام' : 'Year Bills';

        });
    };
    $scope.newOutsideSales = function () {
        ModalProvider.openOutsideSalesCreateModel().result.then(function (data) {
            $rootScope.showConfirmNotify("المبيعات", "هل تود طباعة الفاتورة ؟", "notification", "fa-info", function () {
                $scope.printOutsideBillPurchase(data);
            });
            $scope.outsideBillSells.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.deleteOutsideBillSell = function (billSell) {
        $rootScope.showConfirmNotify("المبيعات", "هل تود حذف الفاتورة فعلاً؟", "error", "fa-trash", function () {
            BillSellService.remove(billSell.id).then(function () {
                var index = $scope.outsideBillSells.indexOf(billSell);
                $scope.outsideBillSells.splice(index, 1);
            });
        });
    };
    $scope.printOutsideSalesByList = function () {
        var ids = [];
        angular.forEach($scope.outsideBillSells, function (billSell) {
            ids.push(billSell.id);
        });
        window.open('/report/outsideSales/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.printOutsideSalesDetailsByList = function () {
        var ids = [];
        angular.forEach($scope.outsideBillSells, function (billSell) {
            ids.push(billSell.id);
        });
        window.open('/report/outsideSales/details/list?ids=' + ids + '&exportType=PDF');
    };
    $scope.printOutsideBillPurchase = function (billSell) {
        window.open('/report/outsideBillPurchase/' + billSell.id + '/PDF');
    };
    $scope.rowMenuOutsideSales = [
        {
            html: '<div class="drop-menu">انشاء فاتورة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newOutsideSales();
            }
        },
        {
            html: '<div class="drop-menu">حذف الفاتورة<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteOutsideBillSell($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">تسديد دفعة<span class="fa fa-money fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_RECEIPT_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newBillSellReceipt($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">اضافة صنف<span class="fa fa-plus-square fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_SELL_ADD_ITEM']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newTransactionSell($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openOutsideSalesDetailsModel($itemScope.billSell);
            }
        },
        {
            html: '<div class="drop-menu">طباعة الفاتورة<span class="fa fa-print fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                $scope.printOutsideBillPurchase($itemScope.billSell);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Team                                                                                                       *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.teams = [];
    $scope.fetchTeamTableData = function () {
        TeamService.findAll().then(function (data) {
            $scope.teams = data;
        });
    };
    $scope.newTeam = function () {
        ModalProvider.openTeamCreateModel().result.then(function (data) {
            $scope.teams.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.deleteTeam = function (team) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المجموعة فعلاً؟", "error", "fa-trash", function () {
            TeamService.remove(team.id).then(function () {
                var index = $scope.teams.indexOf(team);
                $scope.teams.splice(index, 1);
            });
        });
    };
    $scope.rowMenuTeam = [
        {
            html: '<div class="drop-menu">انشاء مجموعة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newTeam();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات المجموعة<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openTeamUpdateModel($itemScope.team);
            }
        },
        {
            html: '<div class="drop-menu">حذف المجموعة<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteTeam($itemScope.team);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Fund                                                                                                       *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.selectedFund = {};
    $scope.paramFundReceipt = {};
    $scope.refreshFund = function () {
        FundService.get().then(function (data) {
            $scope.selectedFund = data;
        });
    };
    $scope.transferToBank = function () {
        ModalProvider.openFundReceiptInCreateModel().result.then(function (data) {
            $scope.receiptsIn.splice(0, 0, data);
            $scope.selectedFund.balance += data.receipt.amountNumber;
            $scope.totalAmountIn = 0;
            angular.forEach($scope.receiptsIn, function (fundReceipt) {
                $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
            });
        }, function () {
        });
    };

    /**************************************************************
     *                                                            *
     * Fund Receipt In                                            *
     *                                                            *
     *************************************************************/
    $scope.receiptsIn = [];
    $scope.fundReceiptItemsIn = [];
    $scope.fundReceiptItemsIn.push(
        {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
        {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'}
    );
    $scope.filterIn = function () {

        var search = [];

        //
        if ($scope.paramFundReceipt.receiptCodeFrom) {
            search.push('receiptCodeFrom=');
            search.push($scope.paramFundReceipt.receiptCodeFrom);
            search.push('&');
        }
        if ($scope.paramFundReceipt.receiptCodeTo) {
            search.push('receiptCodeTo=');
            search.push($scope.paramFundReceipt.receiptCodeTo);
            search.push('&');
        }
        //
        if ($scope.paramFundReceipt.receiptDateFrom) {
            search.push('receiptDateFrom=');
            search.push($scope.paramFundReceipt.receiptDateFrom.getTime());
            search.push('&');
        }
        if ($scope.paramFundReceipt.receiptDateTo) {
            search.push('receiptDateTo=');
            search.push($scope.paramFundReceipt.receiptDateTo.getTime());
            search.push('&');
        }
        //
        $scope.paramFundReceipt.receiptType = 'In';
        search.push('receiptType=');
        search.push($scope.paramFundReceipt.receiptType);
        search.push('&');
        //

        FundReceiptService.filter(search.join("")).then(function (data) {
            $scope.receiptsIn = data;
            $scope.totalAmountIn = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsIn = [];
            $scope.fundReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Custom Filters'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.openFilterInModal = function () {
        $scope.paramFundReceipt.receiptType = 'In';
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/fund/fundReceiptFilter.html',
            controller: 'fundReceiptFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramFundReceipts) {
            $scope.paramFundReceipt = paramFundReceipts;
            $scope.filterIn();
        }, function () {
        });
    };
    $scope.findFundReceiptsInByToday = function () {
        FundReceiptService.findByTodayIn().then(function (data) {
            $scope.receiptsIn = data;
            $scope.totalAmountIn = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsIn = [];
            $scope.fundReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد اليوم' : 'For Today'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findFundReceiptsInByWeek = function () {
        FundReceiptService.findByWeekIn().then(function (data) {
            $scope.receiptsIn = data;
            $scope.totalAmountIn = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsIn = [];
            $scope.fundReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد الاسبوع' : 'For Week'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findFundReceiptsInByMonth = function () {
        FundReceiptService.findByMonthIn().then(function (data) {
            $scope.receiptsIn = data;
            $scope.totalAmountIn = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsIn = [];
            $scope.fundReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد الشهر' : 'For Month'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findFundReceiptsInByYear = function () {
        FundReceiptService.findByYearIn().then(function (data) {
            $scope.receiptsIn = data;
            $scope.totalAmountIn = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsIn = [];
            $scope.fundReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Fund Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'ايراد العام' : 'For Year'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.deleteFundReceiptIn = function (fundReceipt) {
        if (fundReceipt) {
            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                FundReceiptService.remove(fundReceipt.id).then(function () {
                    var index = $scope.receiptsIn.indexOf(fundReceipt);
                    $scope.receiptsIn.splice(index, 1);
                    $scope.totalAmountIn = 0;
                    angular.forEach($scope.receiptsIn, function (fundReceipt) {
                        $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
                    });
                });
            });
            return;
        }

        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
            FundReceiptService.remove($scope.selectedReceiptIn.id).then(function () {
                var index = $scope.receiptsIn.indexOf($scope.selectedReceiptIn);
                $scope.receiptsIn.splice(index, 1);
                $scope.totalAmountIn = 0;
                angular.forEach($scope.receiptsIn, function (fundReceipt) {
                    $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
                });
            });
        });
    };
    $scope.newFundReceiptIn = function () {
        ModalProvider.openFundReceiptInCreateModel().result.then(function (data) {
            $scope.receiptsIn.splice(0, 0, data);
            $scope.selectedFund.balance += data.receipt.amountNumber;
            $scope.totalAmountIn = 0;
            angular.forEach($scope.receiptsIn, function (fundReceipt) {
                $scope.totalAmountIn += fundReceipt.receipt.amountNumber;
            });
        }, function () {
            console.info('FundReceiptCreateModel Closed.');
        });
    };

    /**************************************************************
     *                                                            *
     * Fund Receipt Out                                           *
     *                                                            *
     *************************************************************/
    $scope.receiptsOut = [];
    $scope.fundReceiptItemsOut = [];
    $scope.fundReceiptItemsOut.push(
        {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
        {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'}
    );
    $scope.filterOut = function () {

        var search = [];

        //
        if ($scope.paramFundReceipt.receiptCodeFrom) {
            search.push('receiptCodeFrom=');
            search.push($scope.paramFundReceipt.receiptCodeFrom);
            search.push('&');
        }
        if ($scope.paramFundReceipt.receiptCodeTo) {
            search.push('receiptCodeTo=');
            search.push($scope.paramFundReceipt.receiptCodeTo);
            search.push('&');
        }
        //
        if ($scope.paramFundReceipt.receiptDateFrom) {
            search.push('receiptDateFrom=');
            search.push($scope.paramFundReceipt.receiptDateFrom.getTime());
            search.push('&');
        }
        if ($scope.paramFundReceipt.receiptDateTo) {
            search.push('receiptDateTo=');
            search.push($scope.paramFundReceipt.receiptDateTo.getTime());
            search.push('&');
        }
        //
        $scope.paramFundReceipt.receiptType = 'Out';
        search.push('receiptType=');
        search.push($scope.paramFundReceipt.receiptType);
        search.push('&');
        //

        FundReceiptService.filter(search.join("")).then(function (data) {
            $scope.receiptsOut = data;
            $scope.totalAmountOut = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsOut = [];
            $scope.fundReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Custom Filters'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.openFilterOutModal = function () {
        $scope.paramFundReceipt.receiptType = 'Out';
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/fund/fundReceiptFilter.html',
            controller: 'fundReceiptFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramFundReceipt) {
            $scope.paramFundReceipt = paramFundReceipt;
            $scope.filterOut();
        }, function () {
        });
    };
    $scope.findFundReceiptsOutByToday = function () {
        FundReceiptService.findByTodayOut().then(function (data) {
            $scope.receiptsOut = data;
            $scope.totalAmountOut = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsOut = [];
            $scope.fundReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف اليوم' : 'For Today'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findFundReceiptsOutByWeek = function () {
        FundReceiptService.findByWeekOut().then(function (data) {
            $scope.receiptsOut = data;
            $scope.totalAmountOut = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsOut = [];
            $scope.fundReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف الاسبوع' : 'For Week'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findFundReceiptsOutByMonth = function () {
        FundReceiptService.findByMonthOut().then(function (data) {
            $scope.receiptsOut = data;
            $scope.totalAmountOut = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsOut = [];
            $scope.fundReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف الشهر' : 'For Month'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findFundReceiptsOutByYear = function () {
        FundReceiptService.findByYearOut().then(function (data) {
            $scope.receiptsOut = data;
            $scope.totalAmountOut = 0;
            angular.forEach(data, function (fundReceipt) {
                $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
            });
            $scope.fundReceiptItemsOut = [];
            $scope.fundReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'الصندوق' : 'Fund'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Fund Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'مصاريف العام' : 'For Year'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.deleteFundReceiptOut = function (fundReceipt) {
        if (fundReceipt) {
            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
                FundReceiptService.remove(fundReceipt.id).then(function () {
                    var index = $scope.receiptsOut.indexOf(fundReceipt);
                    $scope.receiptsOut.splice(index, 1);
                    $scope.totalAmountOut = 0;
                    angular.forEach($scope.receiptsOut, function (fundReceipt) {
                        $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
                    });
                });
            });
            return;
        }

        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
            FundReceiptService.remove($scope.selectedOut.id).then(function () {
                var index = $scope.receiptsOut.indexOf($scope.selectedOut);
                $scope.receiptsOut.splice(index, 1);
                $scope.totalAmountOut = 0;
                angular.forEach($scope.receiptsOut, function (fundReceipt) {
                    $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
                });
            });
        });
    };
    $scope.newFundReceiptOut = function () {
        ModalProvider.openFundReceiptOutCreateModel().result.then(function (data) {
            $scope.receiptsOut.splice(0, 0, data);
            $scope.selectedFund.balance -= data.receipt.amountNumber;
            $scope.totalAmountOut = 0;
            angular.forEach($scope.receiptsOut, function (fundReceipt) {
                $scope.totalAmountOut += fundReceipt.receipt.amountNumber;
            });
        }, function () {
            console.info('FundReceiptCreateModel Closed.');
        });
    };

    /**************************************************************
     *                                                            *
     * Bank                                                       *
     *                                                            *
     *************************************************************/
    $scope.selectedBank = {};
    $scope.paramBankReceipt = {};
    $scope.refreshBank = function () {
        BankService.get().then(function (data) {
            $scope.selectedBank = data;
        });
    };
    $scope.submitBankUpdate = function () {
        BankService.update($scope.selectedBank).then(function (data) {
            $scope.selectedBank = data;
        });
    };

    /**************************************************************
     *                                                            *
     * Bank Receipt In                                            *
     *                                                            *
     *************************************************************/
    $scope.bankReceiptsIn = [];
    $scope.bankReceiptItemsIn = [];
    $scope.bankReceiptItemsIn.push(
        {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
        {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'}
    );
    $scope.filterBankReceiptIn = function () {

        var search = [];

        //
        if ($scope.paramBankReceipt.receiptCodeFrom) {
            search.push('receiptCodeFrom=');
            search.push($scope.paramBankReceipt.receiptCodeFrom);
            search.push('&');
        }
        if ($scope.paramBankReceipt.receiptCodeTo) {
            search.push('receiptCodeTo=');
            search.push($scope.paramBankReceipt.receiptCodeTo);
            search.push('&');
        }
        //
        if ($scope.paramBankReceipt.receiptDateFrom) {
            search.push('receiptDateFrom=');
            search.push($scope.paramBankReceipt.receiptDateFrom.getTime());
            search.push('&');
        }
        if ($scope.paramBankReceipt.receiptDateTo) {
            search.push('receiptDateTo=');
            search.push($scope.paramBankReceipt.receiptDateTo.getTime());
            search.push('&');
        }
        //
        $scope.paramBankReceipt.receiptType = 'In';
        search.push('receiptType=');
        search.push($scope.paramBankReceipt.receiptType);
        search.push('&');
        //

        BankReceiptService.filter(search.join("")).then(function (data) {
            $scope.bankReceiptsIn = data;
            $scope.bankTotalAmountIn = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountIn+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsIn = [];
            $scope.bankReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Custom Filters'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.openBankFilterInModal = function () {
        $scope.paramBankReceipt.receiptType = 'Out';
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/bankReceiptFilter.html',
            controller: 'bankReceiptFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramBankReceipt) {
            $scope.paramBankReceipt = paramBankReceipt;
            $scope.filterBankReceiptIn();
        }, function () {});
    };
    $scope.findBankReceiptsInByToday = function () {
        BankReceiptService.findByTodayIn().then(function (data) {
            $scope.bankReceiptsIn = data;
            $scope.bankTotalAmountIn = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountIn+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsIn = [];
            $scope.bankReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل اليوم' : 'For Today'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findBankReceiptsInByWeek = function () {
        BankReceiptService.findByWeekIn().then(function (data) {
            $scope.bankReceiptsIn = data;
            $scope.bankTotalAmountIn = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountIn+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsIn = [];
            $scope.bankReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل الاسبوع' : 'For Week'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findBankReceiptsInByMonth = function () {
        BankReceiptService.findByMonthIn().then(function (data) {
            $scope.bankReceiptsIn = data;
            $scope.bankTotalAmountIn = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountIn+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsIn = [];
            $scope.bankReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل الشهر' : 'For Month'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findBankReceiptsInByYear = function () {
        BankReceiptService.findByYearIn().then(function (data) {
            $scope.bankReceiptsIn = data;
            $scope.bankTotalAmountIn = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountIn+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsIn = [];
            $scope.bankReceiptItemsIn.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات القبض' : 'Bank Cash In'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'تحصيل العام' : 'For Year'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.deleteBankReceiptIn = function (bankReceipt) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
            BankReceiptService.remove(bankReceipt.id).then(function () {
                var index = $scope.bankReceiptsIn.indexOf(bankReceipt);
                $scope.bankReceiptsIn.splice(index, 1);
                $scope.bankTotalAmountIn = 0;
                angular.forEach($scope.bankReceiptsIn, function (bankReceipt) {
                    $scope.bankTotalAmountIn+=bankReceipt.receipt.amountNumber;
                });
            });
        });
    };
    $scope.newBankReceiptIn = function () {
        ModalProvider.openBankReceiptInCreateModel().result.then(function (data) {
            $scope.bankReceiptsIn.splice(0, 0, data);
            $scope.selectedBank.balance+=data.receipt.amountNumber;
            $scope.bankTotalAmountIn = 0;
            angular.forEach($scope.bankReceiptsIn, function (bankReceipt) {
                $scope.bankTotalAmountIn+=bankReceipt.receipt.amountNumber;
            });
        }, function () {
            console.info('BankReceiptCreateModel Closed.');
        });
    };

    /**************************************************************
     *                                                            *
     * Bank Receipt Out                                           *
     *                                                            *
     *************************************************************/
    $scope.banakReceiptsOut = [];
    $scope.bankReceiptItemsOut = [];
    $scope.bankReceiptItemsOut.push(
        {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
        {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'}
    );
    $scope.filterBankReceiptOut = function () {

        var search = [];

        //
        if ($scope.paramBankReceipt.receiptCodeFrom) {
            search.push('receiptCodeFrom=');
            search.push($scope.paramBankReceipt.receiptCodeFrom);
            search.push('&');
        }
        if ($scope.paramBankReceipt.receiptCodeTo) {
            search.push('receiptCodeTo=');
            search.push($scope.paramBankReceipt.receiptCodeTo);
            search.push('&');
        }
        //
        if ($scope.paramBankReceipt.receiptDateFrom) {
            search.push('receiptDateFrom=');
            search.push($scope.paramBankReceipt.receiptDateFrom.getTime());
            search.push('&');
        }
        if ($scope.paramBankReceipt.receiptDateTo) {
            search.push('receiptDateTo=');
            search.push($scope.paramBankReceipt.receiptDateTo.getTime());
            search.push('&');
        }
        //
        $scope.paramBankReceipt.receiptType = 'Out';
        search.push('receiptType=');
        search.push($scope.paramBankReceipt.receiptType);
        search.push('&');
        //

        BankReceiptService.filter(search.join("")).then(function (data) {
            $scope.banakReceiptsOut = data;
            $scope.bankTotalAmountOut = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountOut+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsOut = [];
            $scope.bankReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'بحث مخصص' : 'Custom Filters'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.openBankFilterOutModal = function () {
        $scope.paramBankReceipt.receiptType = 'Out';
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/bankReceiptFilter.html',
            controller: 'bankReceiptFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false,
            size:'lg'
        });

        modalInstance.result.then(function (paramBankReceipt) {
            $scope.paramBankReceipt = paramBankReceipt;
            $scope.filterBankReceiptOut();
        }, function () {});
    };
    $scope.findBankReceiptsOutByToday = function () {
        BankReceiptService.findByTodayOut().then(function (data) {
            $scope.banakReceiptsOut = data;
            $scope.bankTotalAmountOut = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountOut+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsOut = [];
            $scope.bankReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات اليوم' : 'For Today'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findBankReceiptsOutByWeek = function () {
        BankReceiptService.findByWeekOut().then(function (data) {
            $scope.banakReceiptsOut = data;
            $scope.bankTotalAmountOut = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountOut+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsOut = [];
            $scope.bankReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات الاسبوع' : 'For Week'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findBankReceiptsOutByMonth = function () {
        BankReceiptService.findByMonthOut().then(function (data) {
            $scope.banakReceiptsOut = data;
            $scope.bankTotalAmountOut = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountOut+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsOut = [];
            $scope.bankReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات الشهر' : 'For Month'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.findBankReceiptsOutByYear = function () {
        BankReceiptService.findByYearOut().then(function (data) {
            $scope.banakReceiptsOut = data;
            $scope.bankTotalAmountOut = 0;
            angular.forEach(data, function (bankReceipt) {
                $scope.bankTotalAmountOut+=bankReceipt.receipt.amountNumber;
            });
            $scope.bankReceiptItemsOut = [];
            $scope.bankReceiptItemsOut.push(
                {'id': 1, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'البنك' : 'Bank'},
                {'id': 2, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'سندات الصرف' : 'Bank Cash Out'},
                {'id': 3, 'type': 'title', 'name': $rootScope.lang === 'AR' ? 'دفعات العام' : 'For Year'}
            );
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.deleteBankReceiptOut = function (bankReceipt) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف السند وكل ما يتعلق به من حسابات فعلاً؟", "error", "fa-trash", function () {
            BankReceiptService.remove(bankReceipt.id).then(function () {
                var index = $scope.banakReceiptsOut.indexOf(bankReceipt);
                $scope.banakReceiptsOut.splice(index, 1);
                $scope.bankTotalAmountOut = 0;
                angular.forEach($scope.banakReceiptsOut, function (bankReceipt) {
                    $scope.bankTotalAmountOut+=bankReceipt.receipt.amountNumber;
                });
            });
        });
    };
    $scope.newBankReceiptOut = function () {
        ModalProvider.openBankReceiptOutCreateModel().result.then(function (data) {
            $scope.banakReceiptsOut.splice(0, 0, data);
            $scope.selectedBank.balance-=data.receipt.amountNumber;
            $scope.bankTotalAmountOut = 0;
            angular.forEach($scope.banakReceiptsOut, function (bankReceipt) {
                $scope.bankTotalAmountOut+=bankReceipt.receipt.amountNumber;
            });
        }, function () {
            console.info('BankReceiptCreateModel Closed.');
        });
    };

    /**************************************************************************************************************
     *                                                                                                            *
     * Profile                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.submitProfile = function () {
        PersonService.update($scope.me).then(function (data) {
            $scope.me = data;
        });
    };
    $scope.browseProfilePhoto = function () {
        document.getElementById('uploader-profile').click();
    };
    $scope.uploadPersonPhoto = function (files) {
        PersonService.uploadPersonPhoto(files[0]).then(function (data) {
            $scope.me.photo = data;
        });
    };

}
menuCtrl.$inject = [
    '$scope',
    '$rootScope',
    '$state',
    '$timeout',
    '$uibModal',
    'ModalProvider',
    'CompanyService',
    'CustomerService',
    'FalconService',
    'DoctorService',
    'DetectionTypeService',
    'OrderService',
    'DiagnosisService',
    'OrderDetectionTypeService',
    'OrderAttachService',
    'OrderReceiptService',
    'DrugService',
    'DrugUnitService',
    'BillBuyService',
    'BillSellService',
    'TransactionBuyService',
    'TransactionSellService',
    'DrugCategoryService',
    'SupplierService',
    'TeamService',
    'FundService',
    'FundReceiptService',
    'BankService',
    'BankReceiptService',
    'PersonService'
];

app.controller("menuCtrl", menuCtrl);