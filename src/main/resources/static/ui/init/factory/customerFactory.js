app.factory("CustomerService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/customer/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllInfo: function () {
                return $http.get("/api/customer/findAllInfo").then(function (response) {
                    return response.data;
                });
            },
            findPage: function (page, size) {
                return $http.get("/api/customer/findPage" + "?page=" + page + "&size=" + size).then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/customer/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/customer/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (customer) {
                return $http.post("/api/customer/create", customer).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/customer/delete/" + id);
            },
            update: function (customer) {
                return $http.put("/api/customer/update", customer).then(function (response) {
                    return response.data;
                });
            },
            enable: function (customer) {
                return $http.get("/api/customer/enable/" + customer.id).then(function (response) {
                    return response.data;
                });
            },
            disable: function (customer) {
                return $http.get("/api/customer/disable/" + customer.id).then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/customer/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
        };
    }]);