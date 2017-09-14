app.factory("WithdrawService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/withdraw/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/withdraw/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBank: function (id) {
                return $http.get("/api/withdraw/findByBank/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (withdraw) {
                return $http.post("/api/withdraw/create", withdraw).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);