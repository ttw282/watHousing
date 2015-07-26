<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}

if (!($res = $db->query("SELECT * FROM User"))) {
    echo "Fetch failed: (" . $db->errno . ") " . $db->error;
}

while ($row = $res->fetch_assoc()) {
    var_dump($row);
}

$db->close();
 
?>