app.factory("DrugUnitService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/drugUnit/findAll").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);