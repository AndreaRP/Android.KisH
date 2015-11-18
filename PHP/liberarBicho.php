<?php
$bichos = $_POST['bicho']; 
if (isset($bichos) && $bichos != ""){ 
$servername = "*********";
$username = "***********";
$password = "**********";
$dbname = "***********";
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    exit();
}
$bicho = json_decode($bichos, true);
    $sql = "DELETE from BichoCap 
    WHERE bc_pk=".$bicho['pk'];    
    if (mysqli_query($conn, $sql)) {
        echo "Bicho borrado";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }


}else{
    echo "Error, faltan parametros";
}
mysqli_close($conn);
?>
