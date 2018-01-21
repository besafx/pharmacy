app.factory("BillBuyReceiptService",
    ['$http', '$log', function ($http, $log) {
        return {
            findOne: function (id) {
                return $http.get("/api/billBuyReceipt/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            findByBillBuy: function (billBuyId) {
                return $http.get("/api/billBuyReceipt/findByBillBuy/" + billBuyId).then(function (response) {
                    return response.data;
                });
            },
            create: function (billBuy) {
                return $http.post("/api/billBuyReceipt/create", billBuy).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/billBuyReceipt/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/billBuyReceipt/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);