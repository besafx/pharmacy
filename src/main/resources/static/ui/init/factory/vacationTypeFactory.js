app.factory("VacationTypeService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/vacationType/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/vacationType/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (vacationType) {
                return $http.post("/api/vacationType/create", vacationType).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/vacationType/delete/" + id);
            },
            update: function (vacationType) {
                return $http.put("/api/vacationType/update", vacationType).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);