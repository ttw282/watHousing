<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
// Print all tables
$query = "SHOW TABLES";

if(!$result = $db->query($query)){
    die('There was an error running the query [' . $db->error . ']');
}

while($row = $result->fetch_row()){
    echo $row[0];
}

$db->close();
 
?>