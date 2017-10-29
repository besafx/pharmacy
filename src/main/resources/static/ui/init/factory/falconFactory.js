app.factory("FalconService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/falcon/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/falcon/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findAllCodeCombo: function () {
                return $http.get("/api/falcon/findAllCodeCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/falcon/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByCustomer: function (customer) {
                return $http.get("/api/falcon/findByCustomer/" + customer.id).then(function (response) {
                    return response.data;
                });
            },
            create: function (falcon) {
                return $http.post("/api/falcon/create", falcon).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/falcon/delete/" + id);
            },
            update: function (falcon) {
                return $http.put("/api/falcon/update", falcon).then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/falcon/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
        };
    }]);