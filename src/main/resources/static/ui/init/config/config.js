var app = angular.module('Application',
    [
        'ui.router',
        'ngAnimate',
        'toggle-switch',
        'jcs-autoValidate',
        'fm',
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
        'timer'
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

app.config(['$stateProvider', '$urlRouterProvider', '$locationProvider', '$cssProvider', '$httpProvider',
    function ($stateProvider, $urlRouterProvider, $locationProvider, $cssProvider, $httpProvider) {

        $urlRouterProvider.otherwise("/menu");

        $locationProvider.html5Mode(true);

        $httpProvider.interceptors.push('errorInterceptor');

        angular.extend($cssProvider.defaults, {
            container: 'head',
            method: 'append',
            persist: true,
            preload: true,
            bustCache: true
        });

        /**************************************************************
         *                                                            *
         * Home State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("home", {
            url: "/home",
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
            controller: "customerCtrl"
        });

        /**************************************************************
         *                                                            *
         * Supplier State                                             *
         *                                                            *
         *************************************************************/
        $stateProvider.state("supplier", {
            url: "/supplier",
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
            controller: "employeeCtrl"
        });

        /**************************************************************
         *                                                            *
         * Detection Type State                                       *
         *                                                            *
         *************************************************************/
        $stateProvider.state("detectionType", {
            url: "/detectionType",
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
            templateUrl: "/ui/partials/drug/drug.html",
            controller: "drugCtrl"
        });

        /**************************************************************
         *                                                            *
         * BillBuy State                                              *
         *                                                            *
         *************************************************************/
        $stateProvider.state("billBuy", {
            url: "/billBuy",
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
            controller: "billSellCtrl"
        });

        /**************************************************************
         *                                                            *
         * Team State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("team", {
            url: "/team",
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
            templateUrl: "/ui/partials/about/about.html",
            controller: "aboutCtrl"
        });
    }
]);


