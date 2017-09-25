app.factory("DiagnosisAttachService",
    ['$http', '$log', function ($http, $log) {
        return {
            upload: function (diagnosis, fileName, mimeType, description, file) {
                var fd = new FormData();
                fd.append('file', file);
                return $http.post("/api/diagnosisAttach/upload?diagnosisId=" + diagnosis.id + "&fileName=" + fileName + "&mimeType=" + mimeType + "&description=" + description + "&remote=true",
                    fd, {transformRequest: angular.identity, headers: {'Content-Type': undefined}}).then(function (response) {
                    return response.data;
                });
            },
            remove: function (diagnosisAttach) {
                return $http.delete("/api/diagnosisAttach/delete/" + diagnosisAttach.id).then(function (response) {
                    return response.data;
                });
            },
            removeWhatever: function (diagnosisAttach) {
                return $http.delete("/api/diagnosisAttach/deleteWhatever/" + diagnosisAttach.id);
            },
            findByDaignosis: function (diagnosis) {
                return $http.get("/api/diagnosisAttach/findByDaignosis/" + diagnosis.id).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);