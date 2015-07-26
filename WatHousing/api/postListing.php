<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
// Insert listing into Listing

// get params
// address
$addr = $_GET["addr"];
// contact
$contact = $_GET["contact"];
// name
$name = $_GET["name"];
// postalCode
$pcode = $_GET["pcode"];
// rent
$rent = $_GET["rent"];

// api/postListing.php?addr={}&contact={}&name={}&pcode={}&rent={}
$stmt = $db->prepare("INSERT INTO Listing(address, contact, name, postalCode, rent) VALUES (?, ?, ?, ?, ?)");

$stmt->bind_param('ssssi', $addr, $contact, $name, $pcode, $rent);

if (!$stmt->execute()) {
    echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
}

printf("%d Row inserted. \n" ,$stmt->affected_rows);

$db->close();
 
?>