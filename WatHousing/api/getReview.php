<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
// Insert listing into Listing

// get params
$listId = $_GET["listId"];
// contact

// api/postListing.php?addr={}&contact={}&name={}&pcode={}&rent={}
if($listId != NULL) {
	$stmt = $db->prepare("SELECT * FROM Review WHERE listingId = ?");
	$stmt->bind_param('i', $listId);
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
}


$db->close();
 
?>