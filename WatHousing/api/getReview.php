<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
$stmt = $db->prepare("SELECT * FROM Review");

$stmt->execute();

$stmt->store_result();

$stmt->bind_result($reviewId, $listingId, $rating, $comments);

$data = array();
while ($stmt->fetch()) {
	$data[] = array("reviewId"=>$reviewId, "listingId"=>$listingId, "rating"=>$rating, "comments"=>$comments);
}

$stmt->free_result();
$stmt->close();

echo json_encode($data);


$db->close();
 
?>