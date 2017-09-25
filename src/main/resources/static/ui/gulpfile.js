var gulp = require('gulp');
var uglify = require('gulp-uglify');
var concat = require('gulp-concat');
var cleanCSS = require('gulp-clean-css');
var strip = require('gulp-strip-comments');
var replace = require('gulp-replace');


gulp.task('css', function () {

    gulp.src([
        './css/animate.css',
        './css/animation.css',
        './css/bootstrap.css',
        './css/fonts.css',
        './css/md-icons.css',
        './css/angular-toggle-switch-bootstrap-3.css',
        './css/highlight.css',
        './css/chat.css',
        './bootstrap-select/css/nya-bs-select.css',
        './css/font-awesome-animation.css',
        './kdate/css/jquery.calendars.picker.css',
        './chosen/chosen.css'
        // './css/mdl-style.css',
        // './css/theme-black.css',
        // './css/style.css'
    ])
        .pipe(replace('/*!', '/*'))
        .pipe(concat('app.css'))
        .pipe(cleanCSS({specialComments : 'all'}))
        .pipe(gulp.dest('./'));

});

gulp.task('scripts', function () {

    gulp.src([

        './js/material.js',
        './js/fontawesome.js',
        './js/jquery.js',

        './kdate/js/jquery.calendars.js',
        './kdate/js/jquery.calendars-ar.js',
        './kdate/js/jquery.calendars-ar-EG.js',
        './kdate/js/jquery.plugin.js',
        './kdate/js/jquery.calendars.picker.js',
        './kdate/js/jquery.calendars.picker-ar.js',
        './kdate/js/jquery.calendars.picker-ar-EG.js',
        './kdate/js/jquery.calendars.picker.lang.js',
        './kdate/js/jquery.calendars.ummalqura.js',
        './kdate/js/jquery.calendars.ummalqura-ar.js',
        './kdate/js/jquery.calendars.plus.js',

        './js/jquery-ui.js',
        './js/angular.js',
        './js/angular-locale_ar.js',
        './js/angular-sanitize.js',
        './js/angular-ui-router.js',
        './js/angular-animate.js',
        './js/angular-touch.js',

        './angular-spinner/spin.js',
        './angular-spinner/angular-spinner.js',
        './angular-spinner/angular-loading-spinner.js',

        './js/ui-bootstrap.js',
        './js/ui-bootstrap-tpls.js',

        './sockjs/sockjs.js',
        './stomp-websocket/lib/stomp.js',
        './ng-stomp/ng-stomp.js',
        './kdate/kdate.module.js',
        './kdate/kdate.filter.js',
        './kdate/kdate.picker.js',
        './js/underscore.js',
        './js/lrDragNDrop.js',
        './js/contextMenu.js',
        './js/lrStickyHeader.js',
        './js/smart-table.js',
        './js/stStickyHeader.js',
        './js/angular-pageslide-directive.js',
        './js/elastic.js',
        './js/scrollglue.js',
        './ng-upload/angular-file-upload.js',
        './bootstrap-select/js/nya-bs-select.js',
        './js/angular-css.js',
        './js/angular-timer-all.js',
        './full-screen/angular-fullscreen.js',
        './animate-counter/angular-counter.js',
        './js/angular-focusmanager.js',
        './js/jcs-auto-validate.js',
        './js/angular-toggle-switch.js',
        './js/chosen.jquery.js',
        './chosen/angular-chosen.js',
        './js/sortable.js',
        './js/FileSaver.js',
        './js/jquery.noty.packaged.js',

        './init/config/config.js',
        './init/factory/companyFactory.js',
        './init/factory/customerFactory.js',
        './init/factory/supplierFactory.js',
        './init/factory/detectionTypeFactory.js',
        './init/factory/diagnosisAttachFactory.js',
        './init/factory/diagnosisFactory.js',
        './init/factory/doctorFactory.js',
        './init/factory/drugFactory.js',
        './init/factory/drugUnitFactory.js',
        './init/factory/drugCategoryFactory.js',
        './init/factory/employeeFactory.js',
        './init/factory/falconFactory.js',
        './init/factory/orderAttachFactory.js',
        './init/factory/orderDetectionTypeAttachFactory.js',
        './init/factory/orderDetectionTypeFactory.js',
        './init/factory/orderFactory.js',
        './init/factory/personFactory.js',
        './init/factory/teamFactory.js',
        './init/factory/billBuyFactory.js',
        './init/factory/transactionBuyFactory.js',
        './init/factory/billSellDetectionFactory.js',
        './init/factory/transactionSellDetectionFactory.js',
        './init/factory/billSellFactory.js',
        './init/factory/transactionSellFactory.js',
        './init/factory/bankFactory.js',
        './init/factory/depositFactory.js',
        './init/factory/withdrawFactory.js',

        './init/service/service.js',
        './init/directive/directive.js',
        './init/filter/filter.js',
        './init/run/run.js',

        './partials/home/home.js',
        './partials/menu/menu.js',
        './partials/company/company.js',
        './partials/team/team.js',
        './partials/team/teamCreateUpdate.js',
        './partials/customer/customer.js',
        './partials/customer/customerDetails.js',
        './partials/customer/customerCreateUpdate.js',
        './partials/customer/customerFalconCreateUpdate.js',
        './partials/supplier/supplier.js',
        './partials/supplier/supplierCreateUpdate.js',
        './partials/bank/bank.js',
        './partials/bank/bankCreateUpdate.js',
        './partials/bank/depositCreate.js',
        './partials/bank/withdrawCreate.js',
        './partials/doctor/doctor.js',
        './partials/doctor/doctorCreateUpdate.js',
        './partials/employee/employee.js',
        './partials/employee/employeeCreateUpdate.js',
        './partials/detectionType/detectionType.js',
        './partials/detectionType/detectionTypeCreateUpdate.js',
        './partials/detectionType/detectionTypeHeavyWork.js',
        './partials/drug/drug.js',
        './partials/drug/drugFilter.js',
        './partials/drug/drugCreateUpdate.js',
        './partials/drug/drugDetails.js',
        './partials/drug/drugHeavyWork.js',
        './partials/drug/drugCategoryCreateUpdate.js',
        './partials/drug/drugCategoryHeavyWork.js',
        './partials/drug/drugTransactionBuyCreate.js',
        './partials/billBuy/billBuy.js',
        './partials/billBuy/billBuyFilter.js',
        './partials/billBuy/billBuyCreate.js',
        './partials/billBuy/billBuyHeadCreate.js',
        './partials/billBuy/transactionBuyCreate.js',
        './partials/billBuy/updatePrices.js',

        './partials/billSell/billSell.js',
        './partials/billSell/billSellFilter.js',
        './partials/billSell/billSellCreate.js',
        './partials/billSell/billSellHeadCreate.js',
        './partials/billSell/transactionSellCreate.js',

        './partials/falcon/falcon.js',
        './partials/falcon/falconCreateUpdate.js',
        './partials/order/order.js',
        './partials/order/orderCreate.js',
        './partials/order/orderDetectionTypeCreate.js',
        './partials/order/orderFilter.js',
        './partials/order/orderAttachUpload.js',

        './partials/diagnosis/diagnosis.js',
        './partials/diagnosis/diagnosisCreate.js',
        './partials/diagnosis/diagnosisFilter.js',
        './partials/diagnosis/diagnosisDetails.js',
        './partials/diagnosis/diagnosisAttachUpload.js',
        './partials/diagnosis/orderDetectionTypeAttachUpload.js',

        './partials/report/person/personsIn.js',
        './partials/report/order/orderByDate.js',
        './partials/report/order/orderByList.js',
        './partials/report/order/orderDetailsByList.js',
        './partials/report/order/orderDetailsByDate.js',

        './partials/help/help.js',
        './partials/profile/profile.js',
        './partials/about/about.js'

    ])
        .pipe(strip())
        .pipe(concat('app.js'))
        .pipe(uglify())
        .pipe(gulp.dest('./'))

});

gulp.task('default', ['css', 'scripts']);