<?php
    $con = mysqli_connect("mysql.hostinger.in", "u473873008_jj", "Jeyjey312", "u473873008_dtr");
    
    $city = $_POST["city"];
    $username = $_POST["username"];
    $pass = $_POST["password"];

    /*Does this query in the database
        mysqli_query(link to server, "query")
        Then fetch array gets the row that fits this query
        then isset checks if there is a value there or not
        if there is = true, there isn't = false
        */
    
    $patternPass = '/^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])[A-Za-z0-9]{7,23}[^\W\s]$/';
    $patternUserN = '/^(?=.*[A-Za-z])[A-Za-z0-9_-]{3,15}[^\W\s]$/';
?>