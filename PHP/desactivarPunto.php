<?php


if (isset($_POST['campoPunto']) && $_POST['campoPunto'] != ""){ 
$codigoPunto = $_POST['campoPunto']; 
$servername = "sql100.byethost6.com";
$username = "b6_16108246";
$password = "thebeatles1";
$dbname = "b6_16108246_KishBD";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$codigoPunto = mysqli_real_escape_string($conn, $codigoPunto);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    exit();
}
$query = "update Punto set activo = 0 where punto_pk='".$codigoPunto."'";
    if (mysqli_query($conn, $query)) {
        echo "Punto actualizado";
    } else {
        echo "Error: " . $query . "<br>" . $conn->error;
    }
}else{
    echo "Error, faltan parametros";
}
mysqli_close($conn);
?>