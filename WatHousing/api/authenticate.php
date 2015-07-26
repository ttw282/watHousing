<?php
 
// get params
$username = $_GET["username"];
$password = $_GET["password"];

if(!$username || !$password) {
	echo "Invalid input!";
	return false;
}

$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
$db->query("SET @valid = ''");
$db->query("SET @role = ''");
$stmt = $db->prepare("CALL authenticate(?, ?, @valid, @role)");
$stmt->bind_param('ss', $username, $password);

if (!$stmt->execute()) {
    echo "CALL failed: (" . $db->errno . ") " . $db->error;
}

if (!($res = $db->query("SELECT @valid as _p_out"))) {
    echo "Fetch failed: (" . $db->errno . ") " . $db->error;
}

$row = $res->fetch_assoc();
echo $row['_p_out'];

$res->free();

if (!($res = $db->query("SELECT @role as _p_role"))) {
    echo "Fetch failed: (" . $db->errno . ") " . $db->error;
}

$row = $res->fetch_assoc();
echo ' ' . $row['_p_role'];


$db->close();
 
?>