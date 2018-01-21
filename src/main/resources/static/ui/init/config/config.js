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
         * Menu State                                                 *
         *                                                            *
         *************************************************************/
        $stateProvider.state("menu", {
            url: "/menu",
            css: [
                '/ui/css/theme-black.css'
            ],
            templateUrl: "/ui/partials/menu/menu.html",
            controller: "menuCtrl"
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


