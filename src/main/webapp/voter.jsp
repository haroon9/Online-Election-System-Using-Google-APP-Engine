<%@page import="java.time.ZoneId"%>
<%@page import="com.dsam.appengine.shared.model.Voter" %>
<%@page import="com.dsam.appengine.shared.model.VotingPeriod" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.List" %>
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

    <title>Manage Voters</title>

    <style type="text/css">
        /* Show the dropdown menu (use JS to add this class to the .dropdown-content container when the user clicks on the dropdown button) */
        .show {
            display: block;
        }
    </style>
</head>
<body>
<div class="container shadow rounded mt-3 p-3">
    <% List<Voter> voters = (List<Voter>) request.getAttribute("voters"); %>
    <h1 class="display-4">Overview of voters</h1>
    <div id="table" class="table-editable">
        <p><a href="/" class="btn btn-outline-primary mr-3"><i style="vertical-align: top;"
                                                               class="material-icons">arrow_back</i></a>
        </p>
        <%
        List<VotingPeriod> votingPeriods = (List<VotingPeriod>) request.getAttribute("VotingPeriod");
        String disabledText = "";
        boolean isAddable = true;
        if (votingPeriods.size() != 0) {
        	if(votingPeriods.get(0).getStart().isBefore(LocalDateTime.now(ZoneId.of("Europe/Berlin")))){
        		disabledText = " disabled";
                isAddable = false;	
        	}
        }%>
        <% if (!voters.isEmpty() && votingPeriods.size() != 0) {
			if(votingPeriods.get(0).getEnd().isAfter(LocalDateTime.now(ZoneId.of("Europe/Berlin")))) { %>
        		<p><a href="/admin/notify" class="btn btn-primary">Notify Voters</a></p>
        	<%}%>
        <%}%>
        <table class="table table-sm table-responsive-md table-striped text-center">
            <thead class="">
            <tr>
                <th class="text-center">Email</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <% for (Voter voter : voters) {
            %>
            <tr>
                <td><%= voter.getEmail() %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <%if (isAddable) {%>
        <form role="form" action="/admin/upload" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="exampleFormControlFile1"><b>Upload Email adresses of Voters</b></label>
                <input type="file" name="file" class="form-control-file" id="exampleFormControlFile1">
                <br>
                <input type="submit" style="" class="btn btn-primary" value="Upload">
                <a href="/" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
        <%} else {%>
        <div class="alert alert-info" role="alert">Voters can only be added and edited before the election!
        </div>
        <%}%>
    </div>
</div>


<script type="text/javascript">
    $(document).ready(function () {
        $('[data-toggle="tooltip"]').tooltip();
    });
</script>

</body>
</html>
