<?php

    include_once 'Variables.php';   
    $response = array();

    $sqlCheck = "SELECT * FROM user WHERE username = '$username' AND city = '$city'";
    $returnedResult = mysqli_fetch_array(mysqli_query($con,$sqlCheck));
    if(!$con) {
    	$response["response"] = "Server is down";
    	echo json_encode($response);
    }
    else if($username == null || $pass == null || $city == null) {
        $response["response"] = "Fill details";
        echo json_encode($response);
    }
    else if(!isset($returnedResult)) {

        $response["response"] = "Username/City invalid";
        echo json_encode($response);
    }
    else if(!preg_match($patternPass, $pass)) {

        $response["response"] = "Password invalid";
        echo json_encode($response);
    }
    else {
    	/*
    	This query finds correct user(as long as they exist) and they 
    	input the correct values for username and city
    	then it updates their password
    	*/
    	$statement = mysqli_prepare($con, "UPDATE user SET password = ? WHERE username = ? AND city = ?");
    	mysqli_stmt_bind_param($statement, "sss", $pass, $username, $city);
    	mysqli_stmt_execute($statement);

        $response["response"] = "Password has successfully changed!";
        echo json_encode($response);

    }
?>