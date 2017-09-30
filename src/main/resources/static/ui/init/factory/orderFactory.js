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
            }
        };
    }]);