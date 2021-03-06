<?php
    include(__DIR__.'/db_connect.php');

   
    $id = $_POST[$col_id];
    $name = $_POST[$col_name];
    $gender = $_POST[$col_gender];
    $age = $_POST[$col_age];
    $height = $_POST[$col_height];
    $weight = $_POST[$col_weight];
    $bmr_value = $_POST[$col_bmr_value];
    $bmi_value = $_POST[$col_bmi_value];


    // array for JSON response
    $response = array();
    // check for required fields
    if (isset($id) || isset($name) || isset($gender) || isset($age) || isset($height)
       || isset($weight)|| isset($bmr_value)|| isset($bmi_value)) {
        // include db connect class
        include(__DIR__.'/db_connect.php');

        // mysql inserting a new row
         $sql_insert="INSERT INTO $tb_bmr_calculator 
                    ($col_id, $col_name, $col_gender, $col_age, 
                     $col_height, $col_weight, $col_bmr_value, $col_bmi_value)
                    VALUES ('$id', '$name', '$gender',$age , '$height', 
                            '$weight', '$bmr_value',$bmi_value )";
        
        $result = $connect->query($sql_insert);
        // check if row inserted or not
        if ($result) {
            // successfully inserted into database
            $response["msg"] = "Record successfully created.";

            // echoing JSON response
            echo json_encode($response);
        } else {
            // failed to insert row
            $response["msg"] = "An error occurred.";

            // echoing JSON response
            echo json_encode($response);
        }
    } else {
        // required field is missing
        $response["msg"] = "Required field(s) is missing";
        // echoing JSON response
        echo json_encode($response);
    }
        $connect-> close();
?>