<?php
include 'PasswordHash.php';

if (isset($_POST['campoLogin']) && $_POST['campoLogin'] != "" && isset($_POST['campoPass']) && $_POST['campoPass'] != ""){ 
     	$login = $_POST['campoLogin']; 
 	$pass = $_POST['campoPass'];
$servername = "sql100.byethost6.com";
$username = "b6_16108246";
$password = "thebeatles1";
$dbname = "b6_16108246_KishBD";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$pass = mysqli_real_escape_string($conn, $pass);
$login = mysqli_real_escape_string($conn, $login);
//hasheamos la pass
$passHash=create_hash($pass);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    exit();
}
#Se comprueba si existe ya ese usuario
$usuario = mysqli_query($conn, "SELECT COUNT(`login`) FROM Usuario where login='".$login."'");
$result = mysqli_fetch_row($usuario);
#Se comprueba que no exista ya el usuario
if ($result[0]==0) {
    $sql = "INSERT INTO Usuario (login, pwd) VALUES ('".$login."', '".$passHash."')";
    if (mysqli_query($conn, $sql)) {
        echo "Usuario dado de alta correctamente";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}else {
    echo "Usuario ya existente";
}
}else{
    echo "Error, faltan parametros";
}
mysqli_close($conn);
?>