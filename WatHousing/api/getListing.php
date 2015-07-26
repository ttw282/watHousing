<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}

if (!($res = $db->query("SELECT * FROM Listing"))) {
    echo "Fetch failed: (" . $db->errno . ") " . $db->error;
} else {
	$rArray = array();

	while ($row = $res->fetch_assoc()) {
		$rArray[] = $row;
	    //var_dump($row);
	}

	echo json_encode($rArray);
}


// $stmt->bind_result($addr, $contact, $name, $code, $rent);

$db->close();
 
?>