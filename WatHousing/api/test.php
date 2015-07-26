<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
// Insert listing into Listing

// get params
$username = $_GET["username"];
$password = $_GET["password"];
$role = $_GET["role"];

$salt = substr(str_shuffle(MD5(microtime())), 0, 128);

$stmt = $db->prepare("CALL createUser(?, ?, ?, ?)");

$stmt->bind_param('ssss', $username, $password, $salt, $role);

if (!$stmt->execute()) {
    echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
}

// $db->query("CALL createUser('asdfasfa',  'aeefadsfaf',  'asdfadfa',  '0')");

printf("%d Row inserted. \n" ,$db->affected_rows);

$db->close();
 
?>