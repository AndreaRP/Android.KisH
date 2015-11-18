<?php 
include 'PasswordHash.php';
	#Se mira que los campos login y pass no estÃ©n vacÃ­os
 if (isset($_POST['campoLogin']) && $_POST['campoLogin'] != "" && isset($_POST['campoPass']) && $_POST['campoPass'] != ""){ 
 	#Se asginan las variables
 	$login = $_POST['campoLogin']; 
 	$pass = $_POST['campoPass'];
 	#Conexion a la bd
 	$con = mysqli_connect("**********","**********","***********", "*********"); 
 	#Se comprueba la conexion
 	if (mysqli_connect_errno()) {
 	 echo 'Error de conexion: '.mysqli_connect_error(); 
 	 exit(); 
 	} 
 	 #Se quitan posibles caracteres especiales [NUL (ASCII 0), \n, \r, \, ', ", y Control-Z] (sql injection)
 	 $login = mysqli_real_escape_string($con, $login); 
 	 $pass = mysqli_real_escape_string($con, $pass);
 	 #Se comprueban login y pass hasheada
 	 $usuario = mysqli_query($con, "SELECT login, pwd, usuario_pk FROM Usuario WHERE login = '".$login."'"); 
 	 #Se comprueba que se han devuelto resultados 
 	 if (!$usuario) { 
 	 	echo 'Error en la consulta: '. mysqli_error($con); 
 	 	exit(); 
 	 }
 	 #Se coge el resultado 
 	 $result = mysqli_fetch_row($usuario);   
 	 #Si coincide, se devuelve la lista de bichos de ese usuario 
if ($result[0] == $login && validate_password($pass, $result[1])) {
 	 	#indicamos que el login es correcto
 	 	echo 'true';	  
 	}else{
      echo "Login o Pass incorrectos";   
     }
 }else{
 		echo "Error de conexion, faltan parametros"; 
 	} 
 	 	?>
