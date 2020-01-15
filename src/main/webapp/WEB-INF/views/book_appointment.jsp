<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 09.12.2019
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="app.entities.enums.City" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Client page</title>

    <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.8.3.js"></script>
    <script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/timepicker/jquery.timepicker.css">
    <script src="${pageContext.request.contextPath}/timepicker/jquery.timepicker.min.js"></script>

    <script>
        $(function() {
            $("#datepicker").datepicker();
        });

        $(function() {
            $("#timepicker").timepicker();
        });
    </script>
</head>

<body>
<div>
    <h1>Welcome, dear Client <c:out value="${client_data.getFirstName()}" />!</h1>
</div>

<div>
    <h2><c:out value="${datepicker_and_timepicker_required}" /></h2>
</div>

<div>
    <div>
        <h2>Please book an appointment</h2>
    </div>

    <form method="post">
        <table>
            <tbody>
            <tr>
                <td>City:</td>
                <td>
                    <label>
                        <select class="selectpicker" id="cities" name="city">
                            <c:forEach items="<%=City.values()%>" var="city">
                                <option value="${city}">${city.getTitle()}</option>
                            </c:forEach>
                        </select><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Date:</td>
                <td>
                    <label>
                        <input name="date" id="datepicker" readonly><br />
                    </label>
                </td>
            </tr>
            <tr>
                <td>Time:</td>
                <td>
                    <label>
                        <input name="time" id="timepicker"><br />
                    </label>
                </td>
            </tr>
            </tbody>
        </table>

        <%-- The script below serves to disable booked dates and time from view --%>
        <script>
            /* Init and onclick-changes section: both for datepicker and timepicker */

            var selectedCity = "${City.MINSK}";
            var selectedDate;
            var disabledDates = [];
            var disabledTimeByCityAndDate = new Map();
            var timeToDisable;

            $("#cities").on("change", function() {
                selectedCity = $(this).val();
                initDisabledDates();
                disabledTimeByCityAndDate.clear();
                initDisabledTimeByCityAndDate();
                timeToDisable = disableTimeByCityAndDate();
                initTimePicker();
                if (disabledDates.includes(deleteYearAndZerosFromDate(selectedDate.toString()))) {
                    $('#datepicker').val('');
                    initTimeDisabling();
                }
            });

            $("#datepicker").on("change", function() {
                selectedDate = $(this).val();
                timeToDisable = disableTimeByCityAndDate();
                initDisabledDates();
                initTimePicker();
                getTimeValue();
            });

            $("#datepicker").datepicker({
                dateFormat: 'yy-mm-dd',
                firstDay: 1,
                minDate: -0,
                maxDate: maxDate,
                showOn: 'button',
                autoSize: true,
                beforeShowDay: disableSpecificDaysAndWeekends
            });

            function initTimePicker() {
                $('#timepicker').timepicker({
                    useSelect: true,
                    timeFormat: 'H:i',
                    minTime: '09:00',
                    maxTime: '16:45',
                    step: '15',
                    disableTimeRanges: timeToDisable
                });
            }

            /* Datepicker section */

            initDisabledDates();

            function initDisabledDates() {
                if (selectedCity === "${City.MINSK}") {
                    disabledDates = prepareDisabledDates("${Arrays.toString(disabled_dates_in_Minsk)}");
                } else if (selectedCity === "${City.GOMEL}") {
                    disabledDates = prepareDisabledDates("${Arrays.toString(disabled_dates_in_Gomel)}");
                } else if (selectedCity === "${City.MOGILEV}") {
                    disabledDates = prepareDisabledDates("${Arrays.toString(disabled_dates_in_Mogilev)}");
                } else if (selectedCity === "${City.BREST}") {
                    disabledDates = prepareDisabledDates("${Arrays.toString(disabled_dates_in_Brest)}");
                } else if (selectedCity === "${City.GRODNO}") {
                    disabledDates = prepareDisabledDates("${Arrays.toString(disabled_dates_in_Grodno)}");
                }
            }

            function prepareDisabledDates(datesArray) {
                var regExp = /\s*,\s*/;
                var splitDates = datesArray.substring(1, datesArray.length - 1).split(regExp);
                var result = [];

                for (var i = 0; i < splitDates.length; i++) {
                    var date = splitDates[i];
                    var modifiedDate = deleteYearAndZerosFromDate(date) ;
                    result.push(modifiedDate);
                }

                result = disableHolidays(result);

                return result;
            }

            function deleteYearAndZerosFromDate(dateToModify) {
                var regExp = /-/;

                var splitDate = dateToModify.split(regExp);
                var month = Number(splitDate[1]);
                var day = Number(splitDate[2]);

                return month + "-" + day;
            }

            function disableHolidays(dates) {
                var result = dates;
                var holidays = ["1-1", "1-7", "3-8", "5-9", "7-3", "11-7", "12-25", "12-31"];

                for (var i = 0; i < holidays.length; i++) {
                    result.push(holidays[i]);
                }

                return result;
            }

            var maxDate = new Date();
            maxDate.setMonth(maxDate.getMonth() + 6);

            function disableSpecificDaysAndWeekends(date) {
                var m = date.getMonth();
                var d = date.getDate();

                for (var i = 0; i < disabledDates.length; i++) {
                    if ($.inArray((m + 1) + '-' + d, disabledDates) !== -1 || new Date() > date) {
                        return [false];
                    }
                }

                var noWeekends = $.datepicker.noWeekends(date);
                return !noWeekends[0] ? noWeekends : [true];
            }

            /* Timepicker section */

            initTimeDisabling();

            function initTimeDisabling() {
                <c:forEach items="${disabled_time_in_Minsk}" var="entry">
                    disabledTimeByCityAndDate.set('${entry.key}', '${Arrays.toString(entry.value)}');
                </c:forEach>
                selectedDate = null;
                timeToDisable = [];
                initTimePicker();
            }

            function getTimeValue() {
                initTimePicker();
                $('#timepicker').val();
            }

            function initDisabledTimeByCityAndDate() {
                if (selectedCity === "${City.MINSK}") {
                    <c:forEach items="${disabled_time_in_Minsk}" var="entry">
                        disabledTimeByCityAndDate.set('${entry.key}', '${Arrays.toString(entry.value)}');
                    </c:forEach>
                } else if (selectedCity === "${City.GOMEL}") {
                    <c:forEach items="${disabled_time_in_Gomel}" var="entry">
                        disabledTimeByCityAndDate.set('${entry.key}', '${Arrays.toString(entry.value)}');
                    </c:forEach>
                } else if (selectedCity === "${City.MOGILEV}") {
                    <c:forEach items="${disabled_time_in_Mogilev}" var="entry">
                        disabledTimeByCityAndDate.set('${entry.key}', '${Arrays.toString(entry.value)}');
                    </c:forEach>
                } else if (selectedCity === "${City.BREST}") {
                    <c:forEach items="${disabled_time_in_Brest}" var="entry">
                        disabledTimeByCityAndDate.set('${entry.key}', '${Arrays.toString(entry.value)}');
                    </c:forEach>
                } else if (selectedCity === "${City.GRODNO}") {
                    <c:forEach items="${disabled_time_in_Grodno}" var="entry">
                        disabledTimeByCityAndDate.set('${entry.key}', '${Arrays.toString(entry.value)}');
                    </c:forEach>
                }
            }

            function disableTimeByCityAndDate() {
                var result = [];
                var regExp = /\s*,\s*/;
                var timeInDb = disabledTimeByCityAndDate.get(selectedDate);
                if (timeInDb === undefined) {
                    return [];
                }
                var splitTime = timeInDb.substring(1, timeInDb.length - 1).split(regExp);
                var timeRange = [];

                for (var i = 0; i < splitTime.length; i++) {
                    var lowerTimeLine = splitTime[i].toString();
                    var upperTimeLine = calcTimeRange(splitTime[i]).toString();
                    timeRange.push(lowerTimeLine, upperTimeLine);
                    result.push(timeRange);
                    timeRange = [];
                }

                return result;
            }

            function calcTimeRange(time) {
                var regExp = /\s*:\s*/;
                var splitTime = time.split(regExp);
                var hours = Number(splitTime[0]);
                var minutes = Number(splitTime[1]);

                if (minutes === 45) {
                    hours = hours + 1;
                    minutes = 0;
                } else {
                    minutes = minutes + 15;
                }

                if (hours < 10 && minutes === 0) {
                    return "0" + hours + ":00";
                } else if (hours >= 10 && minutes === 0) {
                    return hours + ":00";
                } else if (hours < 10) {
                    return "0" + hours + ":" + minutes;
                } else {
                    return hours + ":" + minutes;
                }
            }
        </script>

        <p><button type="reset" onclick="initTimeDisabling()">Reset</button> <button type="submit" onclick="submit()">Submit</button></p>
    </form>
</div>

<div>
    <button onclick="location.href='/logout'">Logout</button><br />
</div>

<c:if test="${request_from_client_page == true}">
    <div>
        <button onclick="location.href='/client-page'">Back to my page</button><br />
    </div>
</c:if>

<div>
    <button onclick="location.href='..'">Back to main</button>
</div>
</body>
</html>