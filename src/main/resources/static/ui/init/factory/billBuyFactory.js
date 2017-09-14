app.factory("BillBuyService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/billBuy/findAll").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/billBuy/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (billBuy) {
                return $http.post("/api/billBuy/create", billBuy).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billBuy/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/billBuy/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);