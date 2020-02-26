<%@page import="java.time.ZoneId"%>
<%@page import="com.dsam.appengine.shared.model.Candidate" %>
<%@page import="com.dsam.appengine.shared.model.VotingPeriod" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.LocalDateTime" %>
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

    <title>VOTING :: Electronic Voting System</title>

    <style type="text/css">
        /* Show the dropdown menu (use JS to add this class to the .dropdown-content container when the user clicks on the dropdown button) */
        .show {
            display: block;
        }
    </style>
</head>
<body>
<div class="container shadow rounded mt-3 p-3">

    <h1 class="display-4 text-center mb-3">Voting</h1>
    <%
        List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidatesList");
        List<VotingPeriod> votingPeriods = (List<VotingPeriod>) request.getAttribute("VotingPeriod");
        if (votingPeriods.size() == 0 || votingPeriods.get(0).getStart().isAfter(LocalDateTime.now(ZoneId.of("Europe/Berlin")))) {%>
    <div class="alert alert-info" role="alert">Right now the election is closed, please come back
        later! <%if (votingPeriods.size() != 0 && votingPeriods.get(0).getStart() != null) {%><br/><small
                class="text-muted">Next election is
            starting on
            <%= votingPeriods.get(0).getStart().toString()%>
        </small><%}%></div>
    <%} else if (candidates.size() == 0) { %>
    <div class="alert alert-info" role="alert">Currently there are no candidates available, please come back later!
    </div>
    <% } else if (votingPeriods.size() != 0 && votingPeriods.get(0).getStart().isBefore(LocalDateTime.now(ZoneId.of("Europe/Berlin"))) && votingPeriods.get(0).getEnd().isAfter(LocalDateTime.now(ZoneId.of("Europe/Berlin")))) {%>
    <p>Please enter your vote here!<br/>
        <small class="text-muted">Election is open until <%= votingPeriods.get(0).getEnd().toString()%>
        </small>
    </p>
    <div id="table" class="table-editable">
        <form class="form-inline mt-3" action="/voting" method="post">
            <table class="table table-sm table-responsive-md table-striped text-center">
                <thead class="">
                <tr>
                    <th></th>
                    <th class="text-center">Name</th>
                    <th class="text-center">Faculty</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (Candidate candidate : candidates) {
                %>
                <tr>
                    <td>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="c_key" id="exampleRadios1"
                                   value="<%= candidate.getKey().getId() %>" checked>
                        </div>
                    </td>
                    <td><%= candidate.getSurName() %>, <%= candidate.getFirstName() %>
                    </td>
                    <td>
                        <%= candidate.getFaculty() %>
                    </td>
                </tr>
                <% } %>
                </tbody>
            </table>

            <div class="form-group mx-sm-3 mb-2">
                <button type="submit" class="btn btn-primary">Vote now!</button>
            </div>
            <div class="form-group mx-sm-3 mb-2">
                <input type="text" name="token" class="form-control" id="token" placeholder="Token" required>
            </div>
        </form>
    </div>
    <%} else {%>
    <div class="alert alert-info" role="alert">The election voting period has ended.</div>
    <%} %>
</div>


<script type="text/javascript">
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>

</body>
</html>
