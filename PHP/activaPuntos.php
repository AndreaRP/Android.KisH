<?php
include 'PasswordHash.php';
$pass = $_POST['pass']; 
if (isset($pass) && $pass != ""){ 
$servername = "sql100.byethost6.com";
$username = "b6_16108246";
$password = "thebeatles1";
$dbname = "b6_16108246_KishBD";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    exit();
}
//quitamos caracteres raros
$pass = mysqli_real_escape_string($conn, $pass);
//hasheamos
$clave = create_hash('c');
//comprobamos que la pass está correcta
#Se comprueba la pass hasheada   
if (validate_password($pass, $clave)) {
        #se llama a la bd y se actualizan los datos
$sel="SELECT punto_pk FROM Punto 
      where activo = 0
      ORDER BY RAND()
      LIMIT 3";
      $puntos = mysqli_query($conn, $sel);        
    $puntosRandom = mysqli_fetch_all($puntos, MYSQLI_ASSOC);
      //var_dump($puntosRandom);  
if(!empty($puntosRandom)){
    foreach ($puntosRandom as $punto) {
    $sql = "UPDATE Punto   
            SET activo = 1 
            where punto_pk =".$punto['punto_pk'];
            
        if (mysqli_query($conn, $sql)) {
        echo "Punto ".$punto['punto_pk']." activado ";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}
}else{
    echo "No hay puntos inactivos";
}
mysqli_close($conn);
    }else{
      echo "Login o Pass incorrectos";   
     }

}else{
    echo "Error, faltan parametros";
}

?>