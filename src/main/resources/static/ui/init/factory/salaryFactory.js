app.factory("SalaryService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/salary/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/salary/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (salary) {
                return $http.post("/api/salary/create", salary).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/salary/delete/" + id);
            },
            update: function (salary) {
                return $http.put("/api/salary/update", salary).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);