app.factory("BillSellService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/billSell/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/billSell/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/billSell/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (billSell) {
                return $http.post("/api/billSell/create", billSell).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billSell/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/billSell/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
            pay: function (id) {
                return $http.get("/api/billSell/pay/" + id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);