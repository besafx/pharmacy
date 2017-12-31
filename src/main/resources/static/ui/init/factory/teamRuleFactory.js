app.factory("TeamRuleService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/teamRule/findAll").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);