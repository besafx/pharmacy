app.factory("OrderService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/order/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/order/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/order/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByCustomer: function (customerId) {
                return $http.get("/api/order/findByCustomer/" + customerId).then(function (response) {
                    return response.data;
                });
            },
            findByFalcon: function (falconId) {
                return $http.get("/api/order/findByFalcon/" + falconId).then(function (response) {
                    return response.data;
                });
            },
            findByFalconAndCodeNot: function (falconId, code) {
                return $http.get("/api/order/findByFalconAndCodeNot/" + falconId + "/" + code).then(function (response) {
                    return response.data;
                });
            },
            findByFalconCustomerAndCodeNot: function (customerId, code) {
                return $http.get("/api/order/findByFalconCustomerAndCodeNot/" + customerId + "/" + code).then(function (response) {
                    return response.data;
                });
            },
            create: function (order) {
                return $http.post("/api/order/create", order).then(function (response) {
                    return response.data;
                });
            },
            saveNote: function (order, note) {
                return $http.get("/api/order/saveNote/" + order.id + "/" + note).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/order/delete/" + id);
            },
            findQuantityByDay: function () {
                return $http.get("/api/order/findQuantityByDay").then(function (response) {
                    return response.data;
                });
            },
            findQuantityByMonth: function () {
                return $http.get("/api/order/findQuantityByMonth").then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/order/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
            findByToday: function () {
                return $http.get("/api/order/findByToday").then(function (response) {
                    return response.data;
                });
            },
            findByWeek: function () {
                return $http.get("/api/order/findByWeek").then(function (response) {
                    return response.data;
                });
            },
            findByMonth: function () {
                return $http.get("/api/order/findByMonth").then(function (response) {
                    return response.data;
                });
            },
            findByYear: function () {
                return $http.get("/api/order/findByYear").then(function (response) {
                    return response.data;
                });
            },
            pay: function (id) {
                return $http.get("/api/order/pay/" + id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);