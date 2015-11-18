<?php
$login = $_POST['campoLogin']; 
$codigoBicho = $_POST['codigoBicho']; 
$codigoPunto = $_POST['codigoPunto']; 
$nombre = $_POST['nombre']; 
if (isset($login) && $login != "" && isset($codigoBicho) && $codigoBicho != 0 && isset($codigoPunto) && $codigoPunto != 0 && isset($nombre) && $nombre != ""){ 
$servername = "sql100.byethost6.com";
$username = "b6_16108246";
$password = "thebeatles1";
$dbname = "b6_16108246_KishBD";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$codigoBicho = mysqli_real_escape_string($conn, $codigoBicho);
$login = mysqli_real_escape_string($conn, $login);
$codigoPunto = mysqli_real_escape_string($conn, $codigoPunto);
$nombre = mysqli_real_escape_string($conn, $nombre);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    exit();
}
    $sql = "INSERT BichoCap (bicho_fk, punto_fk, usuario_fk, nombre) 
    select '".$codigoBicho."', '".$codigoPunto."', usuario_pk, '".$nombre."' from Usuario where login ='".$login."'";
    if (mysqli_query($conn, $sql)) {
        echo "Bicho insertado correctamente";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}else{
    echo "Error, faltan parametros";
}
mysqli_close($conn);
?>