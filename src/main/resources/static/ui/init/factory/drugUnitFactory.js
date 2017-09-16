app.factory("DrugUnitService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/drugUnit/findAll").then(function (response) {
                    return response.data;
                });
            },
            getRelated: function (id) {
                return $http.get("/api/drugUnit/getRelated/" + id).then(function (response) {
                    return response.data;
                });
            },
            getRelatedPrices: function (id, defaultQuantity, defaultFactor, defaultBuyCost, defaultSellCost) {
                return $http.get("/api/drugUnit/getRelatedPrices/" + id + "/" + defaultQuantity + "/" + defaultFactor + "/" + defaultBuyCost + "/" + defaultSellCost).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);