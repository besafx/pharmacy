app.factory("BillSellReceiptService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/billSellReceipt/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBillSell: function (billSellId) {
                return $http.get("/api/billSellReceipt/findByBillSell/" + billSellId).then(function (response) {
                    return response.data;
                });
            },
            create: function (billSell) {
                return $http.post("/api/billSellReceipt/create", billSell).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billSellReceipt/delete/" + id);
            },
            filterInside: function (search) {
                return $http.get("/api/billSellReceipt/filterInside?" + search).then(function (response) {
                    return response.data;
                });
            },
            filterOutside: function (search) {
                return $http.get("/api/billSellReceipt/filterOutside?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);