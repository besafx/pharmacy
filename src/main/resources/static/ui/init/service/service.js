app.service('ModalProvider', ['$uibModal', '$log', '$rootScope', function ($uibModal, $log, $rootScope) {

    /**************************************************************
     *                                                            *
     * Customer Model                                             *
     *                                                            *
     *************************************************************/
    this.openCustomerCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerCreateUpdate.html',
            controller: 'customerCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء حساب عميل جديد' : 'New Customer';
                },
                action: function () {
                    return 'create';
                },
                customer: function () {
                    return {};
                }
            }
        });
    };

    this.openCustomerUpdateModel = function (customer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerCreateUpdate.html',
            controller: 'customerCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل حساب عميل' : 'Update Customer Information';
                },
                action: function () {
                    return 'update';
                },
                customer: function () {
                    return customer;
                }
            }
        });
    };

    this.openCustomerDetailsModel = function (customer) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerDetails.html',
            controller: 'customerDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                customer: function () {
                    return customer;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Supplier Model                                             *
     *                                                            *
     *************************************************************/
    this.openSupplierCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierCreateUpdate.html',
            controller: 'supplierCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء حساب مورد جديد' : 'New Supplier';
                },
                action: function () {
                    return 'create';
                },
                supplier: function () {
                    return {};
                }
            }
        });
    };

    this.openSupplierUpdateModel = function (supplier) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierCreateUpdate.html',
            controller: 'supplierCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل حساب مورد' : 'Update Supplier Information';
                },
                action: function () {
                    return 'update';
                },
                supplier: function () {
                    return supplier;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Bank Model                                                 *
     *                                                            *
     *************************************************************/
    this.openBankCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/bankCreateUpdate.html',
            controller: 'bankCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء حساب بنك جديد' : 'New Bank';
                },
                action: function () {
                    return 'create';
                },
                bank: function () {
                    return {};
                }
            }
        });
    };

    this.openBankUpdateModel = function (bank) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/bankCreateUpdate.html',
            controller: 'bankCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل حساب بنك' : 'Update Bank Information';
                },
                action: function () {
                    return 'update';
                },
                bank: function () {
                    return bank;
                }
            }
        });
    };

    this.openDepositCreateModel = function (bank) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/depositCreate.html',
            controller: 'depositCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                bank: function () {
                    return bank;
                }
            }
        });
    };

    this.openWithdrawCreateModel = function (bank) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/bank/withdrawCreate.html',
            controller: 'withdrawCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                bank: function () {
                    return bank;
                }
            }
        });
    };


    /**************************************************************
     *                                                            *
     * Doctor Model                                               *
     *                                                            *
     *************************************************************/
    this.openDoctorCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/doctor/doctorCreateUpdate.html',
            controller: 'doctorCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء حساب طبيب جديد' : 'New Doctor';
                },
                action: function () {
                    return 'create';
                },
                doctor: function () {
                    return {};
                }
            }
        });
    };

    this.openDoctorUpdateModel = function (doctor) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/doctor/doctorCreateUpdate.html',
            controller: 'doctorCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل حساب طبيب' : 'Update Doctor Information';
                },
                action: function () {
                    return 'update';
                },
                doctor: function () {
                    return doctor;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Employee Model                                             *
     *                                                            *
     *************************************************************/
    this.openEmployeeCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/employee/employeeCreateUpdate.html',
            controller: 'employeeCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء حساب موظف جديد' : 'New Employee';
                },
                action: function () {
                    return 'create';
                },
                employee: function () {
                    return {};
                }
            }
        });
    };

    this.openEmployeeUpdateModel = function (employee) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/employee/employeeCreateUpdate.html',
            controller: 'employeeCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل حساب موظف' : 'Update Employee Information';
                },
                action: function () {
                    return 'update';
                },
                employee: function () {
                    return employee;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * DetectionType Model                                        *
     *                                                            *
     *************************************************************/
    this.openDetectionTypeCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/detectionType/detectionTypeCreateUpdate.html',
            controller: 'detectionTypeCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء نوع فحص جديد' : 'New Detection Type';
                },
                action: function () {
                    return 'create';
                },
                detectionType: function () {
                    return {};
                }
            }
        });
    };

    this.openDetectionTypeUpdateModel = function (detectionType) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/detectionType/detectionTypeCreateUpdate.html',
            controller: 'detectionTypeCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل نوع فحص' : 'Update Detection Type Information';
                },
                action: function () {
                    return 'update';
                },
                detectionType: function () {
                    return detectionType;
                }
            }
        });
    };

    this.openDetectionTypeHeavyWorkModel = function () {
        $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/detectionType/detectionTypeHeavyWork.html',
            controller: 'detectionTypeHeavyWorkCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * Order Model                                                *
     *                                                            *
     *************************************************************/
    this.openOrderCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/order/orderCreate.html',
            controller: 'orderCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء طلب جديد' : 'New Order';
                },
                order: function () {
                    return {};
                }
            }
        });
    };

    this.openOrderDetectionTypeCreateModel = function (order) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/order/orderDetectionTypeCreate.html',
            controller: 'orderDetectionTypeCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                order: function () {
                    return order;
                }
            }
        });
    };

    this.openReportOrderByListModel = function (orders) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: "/ui/partials/report/order/orderByList.html",
            controller: "orderByListCtrl",
            backdrop: 'static',
            keyboard: false,
            resolve: {
                orders: function () {
                    return orders;
                }
            }
        });
    };

    this.openReportOrderDetailsByListModel = function (orders) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: "/ui/partials/report/order/orderDetailsByList.html",
            controller: "orderDetailsByListCtrl",
            backdrop: 'static',
            keyboard: false,
            resolve: {
                orders: function () {
                    return orders;
                }
            }
        });
    };

    this.openReportOrderByDateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: "/ui/partials/report/order/orderByDate.html",
            controller: "orderByDateCtrl",
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openReportOrderDetailsByDateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: "/ui/partials/report/order/orderDetailsByDate.html",
            controller: "orderDetailsByDateCtrl",
            backdrop: 'static',
            keyboard: false
        });
    };

    /**************************************************************
     *                                                            *
     * Diagnosis Model                                            *
     *                                                            *
     *************************************************************/
    this.openDiagnosisCreateModel = function (order) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/diagnosis/diagnosisCreate.html',
            controller: 'diagnosisCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                order: function () {
                    return order;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Drug Model                                                 *
     *                                                            *
     *************************************************************/
    this.openDrugCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugCreateUpdate.html',
            controller: 'drugCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء دواء جديد' : 'New Drug';
                },
                action: function () {
                    return 'create';
                },
                drug: function () {
                    return {};
                }
            }
        });
    };

    this.openDrugUpdateModel = function (drug) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugCreateUpdate.html',
            controller: 'drugCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل بيانات دواء' : 'Update Drug Information';
                },
                action: function () {
                    return 'update';
                },
                drug: function () {
                    return drug;
                }
            }
        });
    };

    this.openDrugDetailsModel = function (drug) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugDetails.html',
            controller: 'drugDetailsCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                drug: function () {
                    return drug;
                }
            }
        });
    };

    this.openDrugHeavyWorkModel = function () {
        $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugHeavyWork.html',
            controller: 'drugHeavyWorkCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openDrugCategoryCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugCategoryCreateUpdate.html',
            controller: 'drugCategoryCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء تصنيف دواء جديد' : 'New Drug Category';
                },
                action: function () {
                    return 'create';
                },
                drugCategory: function () {
                    return {};
                }
            }
        });
    };

    this.openDrugCategoryHeavyWorkModel = function () {
        $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugCategoryHeavyWork.html',
            controller: 'drugCategoryHeavyWorkCtrl',
            backdrop: 'static',
            keyboard: false
        });
    };

    this.openDrugTransactionBuyCreateModel = function (drug) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/drug/drugTransactionBuyCreate.html',
            controller: 'drugTransactionBuyCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                drug: function () {
                    return drug;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * BillBuy Model                                              *
     *                                                            *
     *************************************************************/
    this.openBillBuyCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billBuy/billBuyCreate.html',
            controller: 'billBuyCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء فاتورة شراء جديدة' : 'New Bill Buy';
                },
                billBuy: function () {
                    return {};
                }
            }
        });
    };

    this.openBillBuyHeadCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billBuy/billBuyHeadCreate.html',
            controller: 'billBuyHeadCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg'
        });
    };

    this.openTransactionBuyCreateModel = function (billBuy) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billBuy/transactionBuyCreate.html',
            controller: 'transactionBuyCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'حركة شراء جديدة' : 'New Transaction Buy';
                },
                billBuy: function () {
                    return billBuy;
                }
            }
        });
    };

    this.openUpdatePricesModel = function (transactionBuy) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billBuy/updatePrices.html',
            controller: 'updatePricesCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                transactionBuy: function () {
                    return transactionBuy;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * BillSell Model                                             *
     *                                                            *
     *************************************************************/
    this.openBillSellCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSell/billSellCreate.html',
            controller: 'billSellCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء فاتورة بيع جديدة' : 'New Bill Sell';
                },
                billSell: function () {
                    return {};
                }
            }
        });
    };

    this.openBillSellHeadCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSell/billSellHeadCreate.html',
            controller: 'billSellHeadCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg'
        });
    };

    this.openTransactionSellCreateModel = function (billSell) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/billSell/transactionSellCreate.html',
            controller: 'transactionSellCreateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'حركة بيع جديدة' : 'New Transaction Sell';
                },
                billSell: function () {
                    return billSell;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Falcon Model                                               *
     *                                                            *
     *************************************************************/
    this.openFalconCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/falcon/falconCreateUpdate.html',
            controller: 'falconCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء حساب صقر جديد' : 'New Falcon Account';
                },
                action: function () {
                    return 'create';
                },
                falcon: function () {
                    return {};
                }
            }
        });
    };

    this.openFalconUpdateModel = function (falcon) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/falcon/falconCreateUpdate.html',
            controller: 'falconCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل حساب الصقر' : 'Update Falcon Account Information';
                },
                action: function () {
                    return 'update';
                },
                falcon: function () {
                    return falcon;
                }
            }
        });
    };

    /**************************************************************
     *                                                            *
     * Team Model                                                 *
     *                                                            *
     *************************************************************/
    this.openTeamCreateModel = function () {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/team/teamCreateUpdate.html',
            controller: 'teamCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'انشاء مجموعة جديدة' : 'New Team';
                },
                action: function () {
                    return 'create';
                },
                team: function () {
                    return undefined;
                }
            }
        });
    };

    this.openTeamUpdateModel = function (team) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/team/teamCreateUpdate.html',
            controller: 'teamCreateUpdateCtrl',
            backdrop: 'static',
            keyboard: false,
            size: 'lg',
            resolve: {
                title: function () {
                    return $rootScope.lang === 'AR' ? 'تعديل بيانات مجموعة' : 'Update Team';
                },
                action: function () {
                    return 'update';
                },
                team: function () {
                    return team;
                }
            }
        });
    };

    this.openPersonsReportModel = function (persons) {
        return $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/report/person/personsIn.html',
            controller: 'personsInCtrl',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                persons: function () {
                    return persons;
                }
            }
        });
    };

}]);

app.service('NotificationProvider', ['$http', function ($http) {

    this.notifyOne = function (code, title, message, type, receiver) {
        $http.post("/notifyOne?"
            + 'code=' + code
            + '&'
            + 'title=' + title
            + '&'
            + 'message=' + message
            + '&'
            + 'type=' + type
            + '&'
            + 'receiver=' + receiver);
    };
    this.notifyAll = function (code, title, message, type) {
        $http.post("/notifyAll?"
            + 'code=' + code
            + '&'
            + 'title=' + title
            + '&'
            + 'message=' + message
            + '&'
            + 'type=' + type
        );
    };
    this.notifyAllExceptMe = function (code, title, message, type) {
        $http.post("/notifyAllExceptMe?"
            + 'code=' + code
            + '&'
            + 'title=' + title
            + '&'
            + 'message=' + message
            + '&'
            + 'type=' + type
        );
    };

}]);

