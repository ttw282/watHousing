<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}


// postalCode
$_pcode = substr($_GET["pcode"], 0, 3);
$_pcode .= '%';

$stmt = $db->prepare("SELECT * FROM Listing WHERE postalCode LIKE ?");
$stmt->bind_param('s', $_pcode);

if (!$stmt->execute()) {
    echo "CALL failed: (" . $db->errno . ") " . $db->error;
}

$stmt->store_result();

$stmt->bind_result($id, $pcode, $addr, $name, $contact, $rent);

$data = array();
while ($stmt->fetch()) {
	$data[] = array("postalCode"=>$pcode, "address"=>$addr, "name"=>$name, "contact"=>$contact, "rent"=>$rent);
}

$stmt->free_result();
$stmt->close();

echo json_encode($data);

$db->close();
 
?>