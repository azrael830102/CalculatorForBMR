<?php

// array for JSON response
$response = array();

// include db connect class
include(__DIR__.'/db_connect.php');

$sql_query = "SELECT * FROM $tb_bmr_calculator";
echo $sql_query."<br>";
$result = $connect->query($sql_query);
print_r($result);

// check for empty result
if (mysqli_num_rows ($result)) {
    // looping through all results
    // products node
    $response["records"] = array();

    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $info = array();
        $info[$col_id] = $row[$col_id];
        $info[$col_name] = $row[$col_name];
        $info[$col_gender] = $row[$col_gender];
        $info[$col_age] = $row[$col_age];
        $info[$col_height] = $row[$col_height];
        $info[$col_weight] = $row[$col_weight];
        $info[$col_bmr_value] = $row[$col_bmr_value];
        $info[$col_bmi_value] = $row[$col_bmi_value];
        print_r($info);
        // push single product into final response array
        array_push($response["records"], $info);
    }
    print_r($response["records"]);
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No products found";

    // echo no users JSON
    echo json_encode($response);
}
 $connect-> close();
?>