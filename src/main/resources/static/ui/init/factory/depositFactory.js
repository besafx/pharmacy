app.factory("DepositService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/deposit/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/deposit/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBank: function (id) {
                return $http.get("/api/deposit/findByBank/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (deposit) {
                return $http.post("/api/deposit/create", deposit).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);