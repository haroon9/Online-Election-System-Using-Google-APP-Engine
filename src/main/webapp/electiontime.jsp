<%@page import="java.time.ZoneId" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="com.dsam.appengine.shared.model.VotingPeriod" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Election Date and Time</title>

    <!-- Bootstrap -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.15.1/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/js/bootstrap-datetimepicker.min.js"></script>
 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.7.14/css/bootstrap-datetimepicker.min.css">
</head>
<body>
<div class="container shadow rounded mt-3 p-3">
    <h1 class="display-4">Set Election Date and Time</h1>
    <%
        List<VotingPeriod> votingPeriods = (List<VotingPeriod>) request.getAttribute("VotingPeriod");
        String startDate = "";
        String endDate = "";
        if (votingPeriods.size() != 0) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            startDate = format.format(Date.from(votingPeriods.get(0).getStart().atZone(ZoneId.of("Europe/Berlin")).toInstant()));
            endDate = format.format(Date.from(votingPeriods.get(0).getEnd().atZone(ZoneId.of("Europe/Berlin")).toInstant()));
        } else {
            startDate = "Start Date";
            endDate = "End Date";
        }
        if (votingPeriods.size() != 0) {%>
    <div class="alert alert-warning" role="alert">By selecting a new election period, the old one gets deleted!</div>
    <%}%>
    <form role="form" action="/admin/setdate" class="mt-3" method="post">
        <div class="form-group">
            <div class='input-group date' id='datetimepicker1'>
                <input type='text' class="form-control" name="start" <%= votingPeriods.size() != 0 ? "readonly" : "" %>
                       placeholder=<%= startDate %> required>
                <span class="input-group-addon">
	            <span class="glyphicon glyphicon-calendar"></span>
	          </span>
            </div>
        </div>
        <div class="form-group">
            <label for="end">End Date</label>
            <div class='input-group date' id='datetimepicker2'>
                <input type='text' class="form-control" name="end" <%= votingPeriods.size() != 0 ? "readonly" : "" %>
                       placeholder=<%= endDate%> required>
                <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
            </div>
        </div>

        <button type="submit" class="btn btn-primary">Save</button>
        <button type="button" class="edit btn btn-primary"  <%= votingPeriods.size() == 0 ? "disabled" : "" %>>Edit
        </button>
        <a href="/" role="button" class="btn btn-outline-secondary">Cancel</a>
    </form>
</div>

<script type="text/javascript">
    $(function () {
        $('#datetimepicker1').datetimepicker({
            minDate: moment()
        });
    });

    $(function () {
        $('#datetimepicker2').datetimepicker();
    });

    $(".edit").click(function () {
        $("input").removeAttr("readonly");
        //$(".saveTiming").removeClass("hidden");
        //$(".cancel").removeClass("hidden");
    });

</script>
</body>
</html>
