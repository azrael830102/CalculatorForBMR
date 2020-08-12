<?php
   $result = file_get_contents('php://input');
   $object=json_decode($result);

   $Id_P = $object->{'Id_P'};
   $LastName=$object->{'LastName'};
   $FirstName=$object->{'FirstName'};
   $Address=$object->{'Address'};
   $City=$object->{'City'};

/* 
 * Following code will create a new person row
 * All person details are read from HTTP Post Request
 */

// array for JSON response
  $response = array();

// check for required fields
if (isset($Id_P) || isset($LastName) || isset($FirstName) || isset($Address) || isset($City)) {


    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    connect();

    // mysql inserting a new row
    $result = mysqli_query($con,"INSERT INTO persons(Id_P,LastName,FirstName,Address,City) VALUES('$Id_P', '$LastName','$FirstName','$Address','$City')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Person successfully created.";
        
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>