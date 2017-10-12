app.factory("DeductionTypeService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/deductionType/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/deductionType/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (deductionType) {
                return $http.post("/api/deductionType/create", deductionType).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/deductionType/delete/" + id);
            },
            update: function (deductionType) {
                return $http.put("/api/deductionType/update", deductionType).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);