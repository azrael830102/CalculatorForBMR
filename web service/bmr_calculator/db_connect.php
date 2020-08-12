<?php
include('db_config.php');

$connect = mysqli_connect($host, $user, $passwd);
mysqli_select_db ($connect,$database);
 
if ($connect->connect_error) {
    die("連線失敗: " . $connect->connect_error);
}
?>
