var app = angular.module('Application',
    [
        'ui.router',
        'ngAnimate',
        'toggle-switch',
        'jcs-autoValidate',
        'fm',
        'ui.select',
        'ngSanitize',
        'counter',
        'FBAngular',
        'ui.bootstrap',
        'angularCSS',
        'smart-table',
        'lrDragNDrop',
        'nya.bootstrap.select',
        'localytics.directives',
        'angularFileUpload',
        'ngLoadingSpinner',
        'ngStomp',
        'luegg.directives',
        'monospaced.elastic',
        'pageslide-directive',
        'ui.bootstrap.contextMenu',
        'kdate',
        'ui.sortable',
        'timer',
        'chart.js'
    ]);

app.factory('errorInterceptor', ['$q', '$rootScope', '$location', '$log',
    function ($q, $rootScope, $location, $log) {
        return {
            request: function (config) {
                return config || $q.when(config);
            },
            requestError: function (request) {
                return $q.reject(request);
            },
            response: function (response) {
                return response || $q.when(response);
            },
            responseError: function (response) {
                if (response && response.status === 404) {
                }
                if (response && response.status >= 500) {
                    $rootScope.showTechnicalNotify("الدعم الفني", response.data.message, "error", "fa-ban");
                }
                return $q.reject(response);
            }
        };
    }]);

app.config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$cssProvider', 'ChartJsProvider', '$httpProvider',
    function ($stateProvider, $urlRouterProvider, $locationProvider, $cssProvider, ChartJsProvider, $httpProvider) {

        ChartJsProvider.setOptions({colors: ['#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360']});

        $urlRouterProvider.otherwise("/menu");

        $locationProvider.html5Mode(true);

        $httpProvider.interceptors.push('errorInterceptor');

        /**************************************************************
         *                                                            *
         * Home State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("home", {
            url: "/home",
            css: [
                '/ui/css/mdl-style.css',
                '/ui/css/theme-black.css'
            ],
            views: {
                '': {
                    templateUrl: "/ui/partials/home/home.html",
                    controller: "homeCtrl"
                }
            }
        });

        /**************************************************************
         *                                                            *
         * Menu State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("menu", {
            url: "/menu",
            css: [
                '/ui/css/mdl-style.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/menu/menu.html",
            controller: "menuCtrl"
        });

        /**************************************************************
         *                                                            *
         * Company State                                              *
         *                                                            *
         *************************************************************/
        $stateProvider.state("company", {
            url: "/company",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/company/company.html",
            controller: "companyCtrl"
        });

        /**************************************************************
         *                                                            *
         * Customer State                                             *
         *                                                            *
         *************************************************************/
        $stateProvider.state("customer", {
            url: "/customer",
            templateUrl: "/ui/partials/customer/customer.html",
            controller: "customerCtrl",
            controllerAs: "customerCtrl"
        });

        $stateProvider.state("customer.list", {
            url: "/list",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@customer':{
                    templateUrl: "/ui/partials/customer/list/list.html"
                },
                'options@customer':{
                    templateUrl: "/ui/partials/customer/list/options.html"
                }
            }
        });

        $stateProvider.state("customer.details", {
            url: "/details",
            css: [
                '/ui/css/mdl-style-indigo-pink.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@customer':{
                    templateUrl: "/ui/partials/customer/details/details.html"
                },
                'options@customer':{
                    templateUrl: "/ui/partials/customer/details/options.html"
                }
            }
        });

        /**************************************************************
         *                                                            *
         * Supplier State                                             *
         *                                                            *
         *************************************************************/
        $stateProvider.state("supplier", {
            url: "/supplier",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/supplier/supplier.html",
            controller: "supplierCtrl"
        });

        /**************************************************************
         *                                                            *
         * Bank State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("bank", {
            url: "/bank",
            css: [
                '/ui/css/mdl-style-grey-deep_orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/bank/bank.html",
            controller: "bankCtrl"
        });

        /**************************************************************
         *                                                            *
         * Falcon State                                               *
         *                                                            *
         *************************************************************/
        $stateProvider.state("falcon", {
            url: "/falcon",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/falcon/falcon.html",
            controller: "falconCtrl"
        });

        /**************************************************************
         *                                                            *
         * Doctor State                                               *
         *                                                            *
         *************************************************************/
        $stateProvider.state("doctor", {
            url: "/doctor",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/doctor/doctor.html",
            controller: "doctorCtrl"
        });

        /**************************************************************
         *                                                            *
         * Employee State                                             *
         *                                                            *
         *************************************************************/
        $stateProvider.state("employee", {
            url: "/employee",
            templateUrl: "/ui/partials/employee/employee.html",
            controller: "employeeCtrl",
            controllerAs: "employeeCtrl"
        });

        $stateProvider.state("employee.list", {
            url: "/list",
            css: [
                '/ui/css/mdl-style-indigo-pink.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@employee':{
                    templateUrl: "/ui/partials/employee/list/list.html"
                },
                'options@employee':{
                    templateUrl: "/ui/partials/employee/list/listOptions.html"
                }
            }
        });

        $stateProvider.state("employee.vacationType", {
            url: "/vacationType",
            css: [
                '/ui/css/mdl-style-light_green-lime.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@employee':{
                    templateUrl: "/ui/partials/employee/vacationType/vacationType.html"
                },
                'options@employee':{
                    templateUrl: "/ui/partials/employee/vacationType/vacationTypeOptions.html"
                }
            }
        });

        $stateProvider.state("employee.vacation", {
            url: "/vacation",
            css: [
                '/ui/css/mdl-style-lime-orange.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@employee':{
                    templateUrl: "/ui/partials/employee/vacation/vacation.html"
                },
                'options@employee':{
                    templateUrl: "/ui/partials/employee/vacation/vacationOptions.html"
                }
            }
        });

        $stateProvider.state("employee.deductionType", {
            url: "/deductionType",
            css: [
                '/ui/css/mdl-style-purple-pink.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@employee':{
                    templateUrl: "/ui/partials/employee/deductionType/deductionType.html"
                },
                'options@employee':{
                    templateUrl: "/ui/partials/employee/deductionType/deductionTypeOptions.html"
                }
            }
        });

        $stateProvider.state("employee.deduction", {
            url: "/deduction",
            css: [
                '/ui/css/mdl-style-red-deep_orange.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@employee':{
                    templateUrl: "/ui/partials/employee/deduction/deduction.html"
                },
                'options@employee':{
                    templateUrl: "/ui/partials/employee/deduction/deductionOptions.html"
                }
            }
        });

        $stateProvider.state("employee.salary", {
            url: "/salary",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@employee':{
                    templateUrl: "/ui/partials/employee/salary/salary.html"
                },
                'options@employee':{
                    templateUrl: "/ui/partials/employee/salary/salaryOptions.html"
                }
            }
        });

        /**************************************************************
         *                                                            *
         * Detection Type State                                       *
         *                                                            *
         *************************************************************/
        $stateProvider.state("detectionType", {
            url: "/detectionType",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/detectionType/detectionType.html",
            controller: "detectionTypeCtrl"
        });

        /**************************************************************
         *                                                            *
         * Order State                                                *
         *                                                            *
         *************************************************************/
        $stateProvider.state("order", {
            url: "/order",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/order/order.html",
            controller: "orderCtrl"
        });

        /**************************************************************
         *                                                            *
         * Diagnosis State                                            *
         *                                                            *
         *************************************************************/
        $stateProvider.state("diagnosis", {
            url: "/diagnosis",
            css: [
                '/ui/css/mdl-style-lime-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/diagnosis/diagnosis.html",
            controller: "diagnosisCtrl"
        });

        /**************************************************************
         *                                                            *
         * Drug State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("drug", {
            url: "/drug",
            css: [
                '/ui/css/mdl-style-red-deep_orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/drug/drug.html",
            controller: "drugCtrl"
        });

        /**************************************************************
         *                                                            *
         * Receipt State                                              *
         *                                                            *
         *************************************************************/
        $stateProvider.state("receipt", {
            url: "/receipt",
            templateUrl: "/ui/partials/receipt/receipt.html",
            controller: "receiptCtrl",
            controllerAs: "receiptCtrl"
        });

        $stateProvider.state("receipt.in", {
            url: "/in",
            css: [
                '/ui/css/mdl-style-indigo-pink.css',
                '/ui/css/mdl-ext.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@receipt':{
                    templateUrl: "/ui/partials/receipt/in/in.html"
                },
                'options@receipt':{
                    templateUrl: "/ui/partials/receipt/in/inOptions.html"
                }
            }
        });

        $stateProvider.state("receipt.out", {
            url: "/out",
            css: [
                '/ui/css/mdl-style-light_green-lime.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@receipt':{
                    templateUrl: "/ui/partials/receipt/out/out.html"
                },
                'options@receipt':{
                    templateUrl: "/ui/partials/receipt/out/outOptions.html"
                }
            }
        });

        $stateProvider.state("receipt.term", {
            url: "/term",
            css: [
                '/ui/css/mdl-style-lime-orange.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@receipt':{
                    templateUrl: "/ui/partials/receipt/term/term.html"
                },
                'options@receipt':{
                    templateUrl: "/ui/partials/receipt/term/termOptions.html"
                }
            }
        });

        /**************************************************************
         *                                                            *
         * BillBuy State                                              *
         *                                                            *
         *************************************************************/
        $stateProvider.state("billBuy", {
            url: "/billBuy",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/billBuy/billBuy.html",
            controller: "billBuyCtrl"
        });

        /**************************************************************
         *                                                            *
         * BillSell State                                             *
         *                                                            *
         *************************************************************/
        $stateProvider.state("billSell", {
            url: "/billSell",
            templateUrl: "/ui/partials/billSell/billSell.html",
            controller: "billSellCtrl",
            controllerAs: "billSellCtrl"
        });

        $stateProvider.state("billSell.insideSales", {
            url: "/insideSales",
            css: [
                '/ui/css/mdl-style-indigo-pink.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@billSell':{
                    templateUrl: "/ui/partials/billSell/insideSales/insideSales.html"
                },
                'options@billSell':{
                    templateUrl: "/ui/partials/billSell/insideSales/insideSalesOptions.html"
                }
            }
        });

        $stateProvider.state("billSell.outsideSales", {
            url: "/outsideSales",
            css: [
                '/ui/css/mdl-style-light_green-lime.css',
                '/ui/css/theme-black.css'
            ],
            views:{
                'body@billSell':{
                    templateUrl: "/ui/partials/billSell/outsideSales/outsideSales.html"
                },
                'options@billSell':{
                    templateUrl: "/ui/partials/billSell/outsideSales/outsideSalesOptions.html"
                }
            }
        });

        /**************************************************************
         *                                                            *
         * Team State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("team", {
            url: "/team",
            css: [
                '/ui/css/mdl-style-red-deep_orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/team/team.html",
            controller: "teamCtrl"
        });

        /**************************************************************
         *                                                            *
         * Help State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("help", {
            url: "/help",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/help/help.html",
            controller: "helpCtrl"
        });

        /**************************************************************
         *                                                            *
         * Profile State                                              *
         *                                                            *
         *************************************************************/
        $stateProvider.state("profile", {
            url: "/profile",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/profile/profile.html",
            controller: "profileCtrl"
        });

        /**************************************************************
         *                                                            *
         * About State                                                *
         *                                                            *
         *************************************************************/
        $stateProvider.state("about", {
            url: "/about",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/about/about.html",
            controller: "aboutCtrl"
        });

        /**************************************************************
         *                                                            *
         * Report State                                               *
         *                                                            *
         *************************************************************/
        $stateProvider.state("report", {
            url: "/report",
            css: [
                '/ui/css/mdl-style-green-orange.css',
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/report/report.html",
            controller: "reportCtrl"
        });
    }
]);


