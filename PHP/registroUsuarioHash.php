<?php 
include 'PasswordHash.php';
	#Se mira que los campos login y pass no estén vacíos
 
 	#Se asginan las variables
 	$login = 'prueba'; 
 	$pass = 'prueba';

    //hasheo la pass
    $passHash=create_hash($pass);

 	#Conexion a la bd
	$con = mysqli_connect("mysql13.000webhost.com","a5018682_root","admin1", "a5018682_KisHDB");

    //insercion en BD	
    $sql = "INSERT INTO Usuario (login, pwd) VALUES ('".$login."', '".$passHash."')";
    if (mysqli_query($con, $sql)) {
        echo "Usuario dado de alta correctamente";
    } else {
        echo "Error: " . $sql . "<br>" . $con->error;
    }
 ?>