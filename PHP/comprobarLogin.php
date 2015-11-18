<?php	
function validarLogin($login, $pass){
    $con = mysqli_connect("*********","**********","**********", "************"); 
 	#Se comprueba la conexion
 	if (mysqli_connect_errno()) {
 	 echo 'Error de conexion: '.mysqli_connect_error(); 
 	 exit(); 
 	} 
    
    #Se quitan posibles caracteres especiales [NUL (ASCII 0), \n, \r, \, ', ", y Control-Z] (sql injection)
 	 $login = mysqli_real_escape_string($con, $login); 
 	 $pass = mysqli_real_escape_string($con, $pass);
 	 #Se comprueban login y pass hasheada
 	 $usuario = mysqli_query($con, "SELECT login, pwd FROM Usuario WHERE login = '".$login."'"); 
 	 #Se comprueba que se han devuelto resultados 
 	 if (!$usuario) { 
 	 	echo 'Error en la consulta: '. mysqli_error($con); 
 	 	exit(); 
 	 }
 	 #Se coge el resultado 
 	 $result = mysqli_fetch_row($usuario); 
 	 #Si coincide, se devuelve la lista de bichos de ese usuario 
 	 if ($result[0] == $login && $result[1] == $pass) {
 	 	return true; 
 	}else{
      return false;  
     }   
}
?>
