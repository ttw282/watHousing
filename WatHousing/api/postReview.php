<?php
 
$db = new mysqli('connection string omitted');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}
        
// Insert listing into Listing

// get params
$listId = $_GET["list_id"];
$rating = $_GET["rating"];
$comments = $_GET["comments"];


$stmt = $db->prepare("INSERT INTO Review(reviewId, listingId, rating, comments) VALUES (NULL, ?, ?, ?)");
$stmt->bind_param('ids', $listId, $rating, $comments);

if (!$stmt->execute()) {
    echo "Execute failed: (" . $stmt->errno . ") " . $stmt->error;
}

printf("%d Row inserted. \n" ,$stmt->affected_rows);

$db->close();
 
?>