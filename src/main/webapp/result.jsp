<%@page import="java.time.ZoneId"%>
<%@page import="com.dsam.appengine.shared.model.Candidate" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="com.dsam.appengine.shared.model.VotingPeriod" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="https://code.highcharts.com/highcharts.js"></script>

    <title>Election Results</title>

    <style type="text/css">
        /* Show the dropdown menu (use JS to add this class to the .dropdown-content container when the user clicks on the dropdown button) */
        .show {
            display: block;
        }
    </style>
</head>
<body>
<div class="container shadow rounded mt-3 p-3">
    <%
        List<VotingPeriod> votingPeriods = (List<VotingPeriod>) request.getAttribute("VotingPeriod");
        boolean isAfterElection = false;
        if (votingPeriods.size() != 0 && LocalDateTime.now(ZoneId.of("Europe/Berlin")).isAfter(votingPeriods.get(0).getEnd())) {
            isAfterElection = true;
        }
        int totalVoters = (Integer) (request.getAttribute("voters"));
        int totalVotesCast = (Integer) (request.getAttribute("votescast"));
        Double percentageOfVotes = (Double) (request.getAttribute("percentage")); %>
    <h1 class="display-4">Election Results</h1>
    <div id="table" class="table-editable">
        <p><a href="/" class="btn btn-outline-primary mr-3"><i style="vertical-align: top;"
                                                               class="material-icons">arrow_back</i></a>
        </p>
        <%if (isAfterElection) {%>
        <table class="table table-sm table-responsive-md table-striped text-center">
            <thead class="">
            <tr>
                <th class="text-center">Number of Voters</th>
                <th class="text-center">Total Votes Cast</th>
                <th class="text-center">Percentage of Voting</th>
                <th></th>
            </tr>
            </thead>
            <tbody>

            <tr>
                <td>
                    <%= totalVoters %>
                </td>
                <td>
                    <%= totalVotesCast %>
                </td>
                <td>
                    <%= percentageOfVotes %>
                </td>
            </tr>
            </tbody>
        </table>

        <h4><b>List of Candidates</b></h4>
        <% List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidates"); %>
        <table class="table table-sm table-responsive-md table-striped text-center">
            <thead class="">
            <tr>
                <th class="text-center">First Name</th>
                <th class="text-center">Surname</th>
                <th class="text-center">Faculty</th>
                <th class="text-center">Votes</th>
            </tr>
            </thead>
            <tbody>
            <% for (Candidate candidate : candidates) { %>
            <tr>
                <td>
                    <%= candidate.getFirstName() %>
                </td>
                <td>
                    <%= candidate.getSurName() %>
                </td>
                <td>
                    <%= candidate.getFaculty() %>
                </td>
                <td>
                    <%= candidate.getVotes() %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <div id="piechart"></div>
        <%} else if (votingPeriods.size() == 0) {%>
        <div class="alert alert-info" role="alert">The election dates are not announced yet, please come back later!
        </div>
        <%} else if (votingPeriods.size() != 0 && LocalDateTime.now(ZoneId.of("Europe/Berlin")).isBefore(votingPeriods.get(0).getStart())) { %>
        <div class="alert alert-info" role="alert">The election is not started yet, please come back later!
        </div>
        <%} else {%>
        <div class="alert alert-info" role="alert">The election is not over yet, please come back later!
        </div>
        <%}%>
    </div>
</div>


<script type="text/javascript">
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
    Highcharts.chart('piechart', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'Voting results'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        accessibility: {
            point: {
                valueSuffix: '%'
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                }
            }
        },
        series: [{
            name: 'Candidate',
            colorByPoint: true,
            data: [
                <% List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidates");
                for (Candidate candidate : candidates) { %>
                {
                    name: '<%= candidate.getSurName()%> , <%= candidate.getFirstName()%>',
                    y: <%= candidate.getVotes()%>
                },
                <%}%>]
        }]
    });
</script>

</body>
</html>
