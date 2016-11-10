<?php
    include_once 'Variables.php';
    
    //This query finds if the user exists
    
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $pass);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $username, $city1, $pass);
    
    $response = array();
    $response["success"] = false;
    
    while(mysqli_stmt_fetch($statement)){
        //This is what will be given back to the code
        $response["success"] = true;  
        $response["city"] = $city1;
        $response["username"] = $username;
        $response["password"] = $pass;
    }
    
    echo json_encode($response);
    
    
?>