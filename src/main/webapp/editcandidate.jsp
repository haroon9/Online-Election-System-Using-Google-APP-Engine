<!DOCTYPE html>
<%@page import="com.dsam.appengine.shared.model.Candidate"%>
<%@page import="java.util.List" %>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Edit Candidate</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>

</head>
<body>
<div class="container shadow rounded mt-3 p-3">
    <% Candidate candidate = (Candidate) request.getAttribute("candidate"); %>
    <h1 class="display-4">Edit Candidate</h1>
    <span class="table-add float-right mb-3 mr-2"><a href="#!" class="text-success"><i class="fas fa-plus fa-2x"
                                                                                       aria-hidden="true"></i></a></span>
    <form role="form" action="/admin/edit" method="post">
        <div class="form-group">
            <label for="fname">First name</label>
            <input name="fname" id="fname" type="text" class="form-control" value="<%= candidate.getFirstName() %>" required>
            <input type="hidden" name="key" value="<%= candidate.getKey().getId() %>" id="key">
        </div>
        <div class="form-group">
            <label for="surname">Surname</label>
            <input name="surname" id="surname" type="text" class="form-control"
                   value="<%= candidate.getSurName() %>" required>
        </div>
        <div class="form-group">
            <label for="faculty">Faculty</label>
            <input name="faculty" id="faculty" type="text" class="form-control" value="<%= candidate.getFaculty()%>"
                   required>
        </div>
        <button type="submit" class="btn btn-primary">Save</button>
        <a href="/" class="btn btn-outline-secondary">Cancel</a>
    </form>
</div>

<script type="text/javascript">
</script>
</body>
</html>