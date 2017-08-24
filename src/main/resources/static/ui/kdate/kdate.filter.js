/* http://mkhodary.com
 kdatepicker.js
 Written by Mohamed Elkhodary - December 2015.
 Available under the MIT license.
 Please attribute the author if you use it. */

(function () {
    'use strict';

    angular.module('kdate').filter('kdate', kdate);

    kdate.$inject = ['$filter'];

    function kdate($filter) {
        return function (input, dateformat, language, calendarType) {
            if (!input)
                return;

            if (calendarType == "ummalqura") {
                var monthsArbic = "المحرّم_صفر_ربيع الأول_ربيع الثاني_جمادى الاول_جمادى الآخر_رجب_شعبان_رمضان_شوّال_ذو القعدة_ذو الحجة";
                var weekArabic = "الأحد_الإثنين_الثلاثاء_الأربعاء_الخميس_الجمعة_السبت";
                var monthsEnglish = "Al-Muharram_Safar_Rabi' al-awwal_Rabi' Al-Thani_Jumada Al-Awwal_Jumada Al-Thani_Rajab_Sha'aban_Ramadan_Shawwal_Dhu al-Qi'dah_Dhu al-Hijjah";
                var weekEnglish = "Sunday_Monday_Tuesday_Wednesday_Thursday_Friday_Saturday";
                var monthsArr;
                var weekArr;

                if (language === 'ar') {
                    monthsArr = monthsArbic.split('_');
                    weekArr = weekArabic.split('_');
                } else if (language === 'en') {
                    monthsArr = monthsEnglish.split('_');
                    weekArr = weekEnglish.split('_');
                }

                //date conversion
                var date = new Date(input);
                var dayOfWeek = date.getDay();
                var jdate = $.calendars.instance().newDate(
                    parseInt(date.getFullYear(), 10),
                    parseInt(date.getMonth() + 1, 10),
                    parseInt(date.getDate(), 10)).toJD();

                // Get date result that will be dispalyed.
                var dateResult = $.calendars.instance("ummalqura").fromJD(jdate);
                var result = new Date(dateResult);
                result.setHours(date.getHours());
                result.setMinutes(date.getMinutes());
                result.setSeconds(date.getSeconds());
                result.setMilliseconds(date.getMilliseconds());

                var calendar = $.calendars.instance('ummalqura');
                return calendar.formatDate(dateformat, dateResult);

            } else {
                return $filter('date')(input, dateformat);
            }
        }
    }
})();