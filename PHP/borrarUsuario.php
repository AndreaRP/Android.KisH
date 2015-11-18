<?php
    $login = $_POST['campoLogin']; 
    $con = mysqli_connect("*******","**********","**********", "***********"); 
// Check connection
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
    exit();
}
$login = mysqli_real_escape_string($con, $login);
//se borran los bichos
$sql = "DELETE FROM BichoCap WHERE usuario_fk = (select usuario_pk from Usuario where login='".$login."')";
    if (mysqli_query($con, $sql)) {
        echo "Bichos eliminados";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
//se borra el usuario
$sql = "DELETE FROM Usuario WHERE login ='".$login."'";
    if (mysqli_query($con, $sql)) {
        echo "Usuario eliminado";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
mysqli_close($con);
?>
