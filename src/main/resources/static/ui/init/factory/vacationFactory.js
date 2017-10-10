app.factory("VacationService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/vacation/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/vacation/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (vacation) {
                return $http.post("/api/vacation/create", vacation).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/vacation/delete/" + id);
            },
            update: function (vacation) {
                return $http.put("/api/vacation/update", vacation).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);