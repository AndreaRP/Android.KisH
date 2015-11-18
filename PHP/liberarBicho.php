<?php
$bichos = $_POST['bicho']; 
if (isset($bichos) && $bichos != ""){ 
$servername = "sql100.byethost6.com";
$username = "b6_16108246";
$password = "thebeatles1";
$dbname = "b6_16108246_KishBD";
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
//$login = mysqli_real_escape_string($conn, $login);
//$bichos = mysqli_real_escape_string($conn, $bichos);
// Check connection
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