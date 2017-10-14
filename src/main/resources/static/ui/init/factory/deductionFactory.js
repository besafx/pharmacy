app.factory("DeductionService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/deduction/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/deduction/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (deduction) {
                return $http.post("/api/deduction/create", deduction).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/deduction/delete/" + id);
            },
            update: function (deduction) {
                return $http.put("/api/deduction/update", deduction).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);