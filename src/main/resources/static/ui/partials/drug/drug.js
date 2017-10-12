app.controller("drugCtrl", ['DrugService', 'DrugUnitService', 'TransactionBuyService', 'TransactionSellService', 'DrugCategoryService', 'ModalProvider', '$scope', '$rootScope', '$state', '$timeout', '$uibModal', '$location', '$anchorScroll',
    function (DrugService, DrugUnitService, TransactionBuyService, TransactionSellService, DrugCategoryService, ModalProvider, $scope, $rootScope, $state, $timeout, $uibModal, $location, $anchorScroll) {

        $scope.selected = {};

        $scope.selectedTransactionBuy = {};

        $scope.buffer = {};
        $scope.buffer.drugCategoryList =[];

        $scope.setSelected = function (object) {
            if (object) {
                angular.forEach($scope.drugs, function (drug) {
                    if (object.id == drug.id) {
                        $scope.selected = drug;
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
                        $scope.refreshTransactionSellByTransactionBuy();
                        return transactionBuy.isSelected = true;
                    } else {
                        return transactionBuy.isSelected = false;
                    }
                });
            }
        };

        $scope.refreshDrugs = function () {
            var search = [];
            //
            if ($scope.buffer.codeFrom) {
                search.push('codeFrom=');
                search.push($scope.buffer.codeFrom);
                search.push('&');
            }
            if ($scope.buffer.codeTo) {
                search.push('codeTo=');
                search.push($scope.buffer.codeTo);
                search.push('&');
            }
            //
            if ($scope.buffer.nameArabic) {
                search.push('nameArabic=');
                search.push($scope.buffer.nameArabic);
                search.push('&');
            }
            if ($scope.buffer.nameEnglish) {
                search.push('nameEnglish=');
                search.push($scope.buffer.nameEnglish);
                search.push('&');
            }
            //
            if ($scope.buffer.medicalNameArabic) {
                search.push('medicalNameArabic=');
                search.push($scope.buffer.medicalNameArabic);
                search.push('&');
            }
            if ($scope.buffer.medicalNameEnglish) {
                search.push('medicalNameEnglish=');
                search.push($scope.buffer.medicalNameEnglish);
                search.push('&');
            }
            //
            if ($scope.buffer.drugCategoryList.length > 0) {
                var drugCategories = [];
                for (var i = 0; i < $scope.buffer.drugCategoryList.length; i++) {
                    drugCategories.push($scope.buffer.drugCategoryList[i].id);
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
                $scope.buffer = buffer;
                $scope.refreshDrugs();
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
                    $scope.selected.transactionBuys = data;
                    $scope.setSelectedTransactionBuy(data[0]);
                });
            }
        };

        $scope.refreshTransactionSellByTransactionBuy = function () {
            if ($scope.selectedTransactionBuy) {
                TransactionSellService.findByTransactionBuy($scope.selectedTransactionBuy.id).then(function (data) {
                    $scope.selectedTransactionBuy.transactionSells = data;
                })
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

        $scope.deleteTransactionBuy = function (transactionBuy) {
            if (transactionBuy) {
                $rootScope.showConfirmNotify("المخازن", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                    TransactionBuyService.remove(transactionBuy.id).then(function () {
                        var index = $scope.selected.transactionBuys.indexOf(transactionBuy);
                        $scope.selected.transactionBuys.splice(index, 1);
                        $scope.setSelected($scope.selected.transactionBuys[0]);
                    });
                });

            }
        };

        $scope.deleteTransactionSell = function (transactionSell) {
            if (transactionSell) {
                $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف الطلبية فعلاً؟", "error", "fa-trash", function () {
                    TransactionSellService.remove(transactionSell.id).then(function () {
                        var index = $scope.selectedTransactionBuy.transactionSells.indexOf(transactionSell);
                        $scope.selectedTransactionBuy.transactionSells.splice(index, 1);
                        $scope.setSelected($scope.selectedTransactionBuy.transactionSells[0]);
                    });
                });

            }
        };

        $scope.updatePrices = function (transactionBuy) {
            if (transactionBuy) {
                $rootScope.showConfirmNotify("المخازن", "هل تعديل أسعار الطلبية فعلاً؟", "warning", "fa-edit", function () {
                    ModalProvider.openUpdatePricesModel(transactionBuy).result.then(function (data) {
                        return transactionBuy = data;
                    });
                });
            }
        };

        $scope.updateQuantity = function (transactionBuy) {
            if (transactionBuy) {
                $rootScope.showConfirmNotify("المخازن", "هل تعديل كمية الطلبية فعلاً؟", "warning", "fa-edit", function () {
                    ModalProvider.openUpdateQuantityModel(transactionBuy).result.then(function (data) {
                        return transactionBuy = data;
                    });
                });
            }
        };

        $scope.transactionBuyRowMenu = [
            {
                html: '<div class="drop-menu">حذف<span class="fa fa-trash fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_BILL_BUY_DELETE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.deleteTransactionBuy($itemScope.transactionBuy);
                }
            },
            {
                html: '<div class="drop-menu">تعديل الأسعار<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_PRICE_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.updatePrices($itemScope.transactionBuy);
                }
            },
            {
                html: '<div class="drop-menu">تعديل الكمية<span class="fa fa-edit fa-lg"></span></div>',
                enabled: function () {
                    return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_DRUG_QUANTITY_UPDATE']);
                },
                click: function ($itemScope, $event, value) {
                    $scope.updateQuantity($itemScope.transactionBuy);
                }
            }
        ];

        $timeout(function () {
            $scope.refreshDrugs();
            $location.hash('drugMenu');
            $anchorScroll();
            window.componentHandler.upgradeAllRegistered();
        }, 1500);

    }]);