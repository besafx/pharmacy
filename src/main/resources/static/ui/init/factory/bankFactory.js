app.factory("BankService",
    ['$http', '$log', function ($http, $log) {
        return {
            update: function (bank) {
                return $http.put("/api/bank/update", bank).then(function (response) {
                    return response.data;
                });
            },
            get: function () {
                return $http.get("/api/bank/get").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);