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
} else {
	$stmt = $db->prepare("SELECT * FROM Review");
}

$stmt->execute();

$json = array();
$temp = array();

$stmt->bind_result($a, $b, $c, $d);

/*
[
	{
		reviewId: $a,
		listingId: $b,
		rating: $c,
		$comments: $d
	}
]

*/
//$result = $stmt->get_result();
while( $stmt->fetch() ) {
	//echo $row;
	// $temp = $row;
	// echo $d;
	// array_push($json, $temp);
	printf("%d %d %d %s", $a, $b, $c, $d);
}


$db->close();
 
?>