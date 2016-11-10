<?php
    include_once 'Variables.php';
    
    $sqlCheck = "SELECT * FROM user WHERE username = '$username'";
    $returnedResult = mysqli_fetch_array(mysqli_query($con,$sqlCheck));
    $response = array();
    if (!$con) {

        $response["response"] = "Server is Down!";
        echo json_encode($response);
    }
    else if($username == null || $pass == null || $city == null) {
        $response["response"] = "Fill details";
        echo json_encode($response);
    }
    else if(isset($returnedResult)) {

        $response["response"] = "Username exists";
        echo json_encode($response);
    }
    else if(!preg_match($patternPass, $pass) || !preg_match($patternUserN, $username)) {

        $response["response"] = "Password/Username invalid";
        echo json_encode($response);
    }
    else {

        $statement = mysqli_prepare($con, "INSERT INTO user (username,password,city) VALUES (?,?,?)");
        mysqli_stmt_bind_param($statement, "sss", $username, $pass, $city);
        mysqli_stmt_execute($statement);

        $response["response"] = "Successfully Registered";
        echo json_encode($response);

    }

        
?>