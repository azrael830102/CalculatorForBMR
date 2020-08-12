<?php
    require_once __DIR__ . '/db_config.php';
    
    $conn = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASSWORD) or die(mysqli_connect_error());

    $sql_query="SELECT SCHEMA_NAME
                FROM INFORMATION_SCHEMA.SCHEMATA
                WHERE SCHEMA_NAME = '$DB_DATABASE'";

    $result = $conn->query($sql_query);
    if(!mysqli_num_rows ($result)){  
        $sql_create_db="CREATE DATABASE $DB_DATABASE";
        $conn->query($sql_create_db);
    }

    mysqli_select_db ($conn,$DB_DATABASE);

    $sql_query = "SELECT * 
                  FROM information_schema.tables
                  WHERE table_schema = '$DB_DATABASE' 
                  AND table_name = '$tb_bmr_cscalculator'";
    $result = $conn->query($sql_query);
    if(!mysqli_num_rows ($result)){
        $sql_create_members = "CREATE TABLE $tb_bmr_cscalculator (
                                $col_id varchar(200),
                                $col_name varchar(50),
                                $col_gender varchar(10),
                                $col_age varchar(3),
                                $col_height varchar(3),
                                $col_weight varchar(3),
                                $col_bmr_value varchar(10),
                                $col_bmi_value varchar(3)
                                )";
        
        $result = $conn->query($sql_create_members);
    }
    $conn ->close();
?>
