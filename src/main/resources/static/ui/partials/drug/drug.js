app.controller("drugCtrl", ['DrugService', 'DrugUnitService', 'TransactionBuyService', 'DrugCategoryService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal',
    function (DrugService, DrugUnitService, TransactionBuyService, DrugCategoryService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal) {

        $scope.selected = {};

        $scope.selectedTransactionBuy = {};

        $scope.buffer = {};

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.drugs, function (drug) {
                    if (object.id == drug.id) {
                        $scope.selected = drug;
                        $scope.drugCalculation();
                        return drug.isSelected = true;
                    } else {
                        return drug.isSelected = false;
                    }
                });
            }
        };

        $scope.setSelectedTransactionBuy = function (object) {
            if (object) {
                angular.forEach($scope.selected.transactionBuys, function (transactionBuy) {
                    if (object.id == transactionBuy.id) {
                        $scope.selectedTransactionBuy = transactionBuy;
                        $scope.transactionBuyCalculation();
                        return transactionBuy.isSelected = true;
                    } else {
                        return transactionBuy.isSelected = false;
                    }
                });
            }
        };

        $scope.openFilter = function () {
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

            modalInstance.result.then(function (buffer) {
                var search = [];

                //
                if (buffer.codeFrom) {
                    search.push('codeFrom=');
                    search.push(buffer.codeFrom);
                    search.push('&');
                }
                if (buffer.codeTo) {
                    search.push('codeTo=');
                    search.push(buffer.codeTo);
                    search.push('&');
                }
                //
                if (buffer.nameArabic) {
                    search.push('nameArabic=');
                    search.push(buffer.nameArabic);
                    search.push('&');
                }
                if (buffer.nameEnglish) {
                    search.push('nameEnglish=');
                    search.push(buffer.nameEnglish);
                    search.push('&');
                }
                //
                if (buffer.medicalNameArabic) {
                    search.push('medicalNameArabic=');
                    search.push(buffer.medicalNameArabic);
                    search.push('&');
                }
                if (buffer.medicalNameEnglish) {
                    search.push('medicalNameEnglish=');
                    search.push(buffer.medicalNameEnglish);
                    search.push('&');
                }
                //
                if (buffer.drugCategoryList) {
                    var drugCategories = [];
                    for (var i = 0; i < buffer.drugCategoryList.length; i++) {
                        drugCategories.push(buffer.drugCategoryList[i].id);
                    }
                    search.push('drugCategories=');
                    search.push(drugCategories);
                    search.push('&');
                }
                //
                DrugService.filter(search.join("")).then(function (data) {
                    $scope.drugs = data;
                    $scope.setSelected(data[0]);
                });
            }, function () {
            });
        };

        $scope.delete = function (drug) {
            if (drug) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الدواء فعلاً؟", "error", "fa-trash", function () {
                    DrugService.remove(drug.id).then(function () {
                        var index = $scope.drugs.indexOf(drug);
                        $scope.drugs.splice(index, 1);
                        $scope.setSelected($scope.drugs[0]);
                    });
                });
                return;
            }

            $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الدواء فعلاً؟", "error", "fa-trash", function () {
                DrugService.remove($scope.selected.id).then(function () {
                    var index = $scope.drugs.indexOf($scope.selected);
                    $scope.drugs.splice(index, 1);
                    $scope.setSelected($scope.drugs[0]);
                });
            });
        };

        $scope.deleteTransactionBuy = function (transactionBuy) {
            if (transactionBuy) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                    TransactionBuyService.remove(transactionBuy.id).then(function () {
                        var index = $scope.selected.transactionBuys.indexOf(transactionBuy);
                        $scope.selected.transactionBuys.splice(index, 1);
                        $scope.setSelected($scope.selected.transactionBuys[0]);
                        $scope.drugCalculation();
                    });
                });

            }
        };

        $scope.refreshDrugCategories = function () {
            DrugCategoryService.findAll().then(function (data) {
                $scope.categories = data;
            });
        };

        $scope.refreshTransactionBuyByDrug = function () {
            if ($scope.selected) {
                TransactionBuyService.findByDrug($scope.selected.id).then(function (data) {
                    $scope.selected.transactionBuys = data
                });
            }
        };

        $scope.newDrug = function () {
            ModalProvider.openDrugCreateModel().result.then(function (data) {
                $scope.drugs.splice(0, 0, data);
            }, function () {
                console.info('DrugCreateModel Closed.');
            });
        };

        $scope.newDrugCategory = function () {
            ModalProvider.openDrugCategoryCreateModel().result.then(function (data) {

            }, function () {
                console.info('DrugCategoryCreateModel Closed.');
            });
        };

        $scope.newTransactionBuy = function () {
            ModalProvider.openDrugTransactionBuyCreateModel($scope.selected).result.then(function (data) {
                $scope.selected.transactionBuys.splice(0, 0, data);
            }, function () {
                console.info('DrugTransactionBuyCreateModel Closed.');
            });
        };

        $scope.drugCalculation = function () {
            DrugService.getTransactionBuysSum($scope.selected.id).then(function (data) {
                $scope.totalBuyCost = data;
            });
        };

        $scope.transactionBuyCalculation = function () {
            DrugUnitService.getRelatedPrices($scope.selectedTransactionBuy.id).then(function (data) {
                $scope.relatedPrices = data;
            })
        };

        $scope.printList = function () {
            var ids = [];
            angular.forEach($scope.drugs, function (data) {
                ids.push(data.id);
            });
            window.open('/report/drugs?ids=' + ids + "&exportType=PDF");
        };

        $scope.rowMenu = [
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
                    $scope.delete($itemScope.drug);
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
                    $scope.printList();
                }
            }
        ];

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
            DrugService.findAll().then(function (data) {
                $scope.drugs = data;
            });
        }, 1500);

    }]);