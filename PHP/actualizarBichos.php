<?php

$bichos = $_POST['bichos']; 

if (isset($bichos) && $bichos != ""){ 
$servername = "*******";
$username = "*********";
$password = "*********";
$dbname = "**********";
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$login = mysqli_real_escape_string($conn, $login);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    exit();
}
$bicho = json_decode($bichos, true);
    $sql = "UPDATE BichoCap SET felicidad = ".$bicho['felicidad'].", 
    hambre = ".$bicho['hambre'].", 
    experiencia = ".$bicho['experiencia']."
    WHERE bc_pk=".$bicho['pk'];
    var_dump($sql);
    
    if (mysqli_query($conn, $sql)) {
        echo "Bicho actualizado";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }


}else{
    echo "Error, faltan parametros";
}
mysqli_close($conn);
?>
