<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}

// address
$_addr = $_GET["addr"];
// postalCode
$_pcode = $_GET["pcode"];
// rent
$_rent = $_GET["rent"];

$stmt = $db->prepare("SELECT * FROM Listing");
//AND address LIKE ? AND rent <= ?
// $stmt->bind_param('ssi', $pcode, $addr, $rent);
// $stmt->bind_param('s', $pcode);

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

// while ($row = $res->fetch_assoc()) {
// 	$rArray[] = $row;
// }

echo json_encode($data);

$db->close();
 
?>