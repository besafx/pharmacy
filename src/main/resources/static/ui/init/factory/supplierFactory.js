app.factory("SupplierService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/supplier/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/supplier/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/supplier/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (supplier) {
                return $http.post("/api/supplier/create", supplier).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/supplier/delete/" + id);
            },
            update: function (supplier) {
                return $http.put("/api/supplier/update", supplier).then(function (response) {
                    return response.data;
                });
            },
            enable: function (supplier) {
                return $http.get("/api/supplier/enable/" + supplier.id).then(function (response) {
                    return response.data;
                });
            },
            disable: function (supplier) {
                return $http.get("/api/supplier/disable/" + supplier.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);