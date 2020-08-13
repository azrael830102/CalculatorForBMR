<?php
    require_once 'db_config.php';
    
    $conn = mysqli_connect($host, $user, $passwd) or die(mysqli_connect_error());

    $sql_query="SELECT SCHEMA_NAME
                FROM INFORMATION_SCHEMA.SCHEMATA
                WHERE SCHEMA_NAME = '$database'";

    $result = $conn->query($sql_query);
    if(!mysqli_num_rows ($result)){  
        $sql_create_db="CREATE DATABASE $database";
        $conn->query($sql_create_db);
    }

    mysqli_select_db ($conn,$database);

    $sql_query = "SELECT * 
                  FROM information_schema.tables
                  WHERE table_schema = '$database' 
                  AND table_name = '$tb_bmr_calculator'";
    $result = $conn->query($sql_query);
    if(!mysqli_num_rows ($result)){
        $sql_create_members = "CREATE TABLE $tb_bmr_calculator (
                                $col_id varchar(200) NOT NULL UNIQUE,
                                $col_name varchar(50) NOT NULL,
                                $col_gender varchar(10) NOT NULL,
                                $col_age varchar(5) NOT NULL,
                                $col_height varchar(5) NOT NULL,
                                $col_weight varchar(5) NOT NULL,
                                $col_bmr_value varchar(10) NOT NULL,
                                $col_bmi_value varchar(5) NOT NULL
                                )";
        
        $result = $conn->query($sql_create_members);
    }


    $array['msg'] = 'DB init finished'; 
    echo json_encode($array);

    $conn ->close();
?>
