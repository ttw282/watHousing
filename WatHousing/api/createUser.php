<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
// Insert listing into Listing

// get params
$username = $_POST["username"];
$password = $_POST["password"];
$role = $_POST["role"];

$salt = substr(str_shuffle(MD5(microtime())), 0, 128);

$stmt = $db->prepare("CALL createUser(?, ?, ?, ?)");

$stmt->bind_param('ssss', $username, $password, $salt, $role);

if (!$stmt->execute()) {
    echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
}

printf("%d Row inserted. \n" ,$stmt->affected_rows);

$db->close();
 
?>