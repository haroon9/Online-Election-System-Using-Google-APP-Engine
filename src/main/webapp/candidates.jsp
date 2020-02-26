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

    <title>Manage Candidates</title>

    <style type="text/css">
        /* Show the dropdown menu (use JS to add this class to the .dropdown-content container when the user clicks on the dropdown button) */
        .show {
            display: block;
        }
    </style>
</head>
<body>
<div class="container shadow rounded mt-3 p-3">

    <h1 class="display-4">Candidates Overview</h1>
    <div id="table" class="table-editable">
        <p><a href="/" class="btn btn-outline-primary mr-3"><i style="vertical-align: top;"
                                                               class="material-icons">arrow_back</i></a>
                <%
                List<VotingPeriod> votingPeriods = (List<VotingPeriod>) request.getAttribute("VotingPeriod");
                String disabledText="";
                if (votingPeriods.size() != 0 && votingPeriods.get(0).getStart().isBefore(LocalDateTime.now())) {
                    disabledText=" disabled";%>
        <div class="alert alert-info" role="alert">Candidates can only be added and edited before the election!
        </div>
        <%} else {%>
        <a href="/admin/new" data-toggle="tooltip" title="Create new candidate"
           class="float-right btn btn-primary"><i
                style="vertical-align: top;"
                class="material-icons">add</i></a>
        </p>
        <%}%>
        <table class="table table-sm table-responsive-md table-striped text-center">
            <thead class="">
            <tr>
                <th class="text-center">First Name</th>
                <th class="text-center">Surname</th>
                <th class="text-center">Faculty</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Candidate> candidates = (List<Candidate>) request.getAttribute("candidatesList");
                for (Candidate candidate : candidates) { %>
            <tr>
                <td><%= candidate.getFirstName() %>
                </td>
                <td><%= candidate.getSurName() %>
                </td>
                <td>
                    <%= candidate.getFaculty() %>
                </td>
                <td class="text-right">
                    <a id="edit" href="/admin/edit?c_key=<%= candidate.getKey().getId() %>" type="button"
                       class="btn btn-primary btn-rounded btn-sm my-0 <%= disabledText %>">Edit</a>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>


<script type="text/javascript">
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>

</body>
</html>
