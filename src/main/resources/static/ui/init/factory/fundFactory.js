app.factory("FundService",
    ['$http', '$log', function ($http, $log) {
        return {
            update: function (bank) {
                return $http.put("/api/fund/update", bank).then(function (response) {
                    return response.data;
                });
            },
            get: function () {
                return $http.get("/api/fund/get").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);