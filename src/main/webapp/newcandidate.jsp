<!DOCTYPE html>
<%@page import="java.util.List" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Add Candidate</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>

</head>
<body>
<div class="container shadow rounded mt-3 p-3">
    <h1 class="display-4">New Candidate</h1>
    <span class="table-add float-right mb-3 mr-2"><a href="#!" class="text-success"><i
            class="fas fa-plus fa-2x" aria-hidden="true"></i></a></span>

    <form role="form" action="/admin/candidates" class="mt-3" method="post">
        <div class="form-group">
            <label for="fname">First name</label>
            <input name="fname" id="fname" type="text" class="form-control" placeholder="Enter candidate first name" required>
        </div>
        <div class="form-group">
            <label for="sname">Surname</label>
            <input name="sname" id="sname" type="text" class="form-control"
                   placeholder="Enter candidate surname" required>
        </div>
        <div class="form-group">
            <label for="faculty">Faculty</label>
            <input name="faculty" id="faculty" type="text" class="form-control" placeholder="Enter candidate faculty"
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
