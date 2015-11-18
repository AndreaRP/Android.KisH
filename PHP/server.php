<?php 
include 'PasswordHash.php';
	#Se mira que los campos login y pass no estÃ©n vacÃ­os
 if (isset($_POST['campoLogin']) && $_POST['campoLogin'] != "" && isset($_POST['campoPass']) && $_POST['campoPass'] != ""){ 
 	#Se asginan las variables
 	$login = $_POST['campoLogin']; 
 	$pass = $_POST['campoPass'];
 	#Conexion a la bd
 	$con = mysqli_connect("*********","********","*********", "**********"); 
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
 	 	$misBichos = mysqli_query($con, "SELECT bicho_fk as 'nPokemon', zona_desc as 'zonaCapturado',BichoCap.nombre, felicidad, hambre, Bicho.nombre as 'nombreGenerico', experiencia
         FROM BichoCap 
         inner join Zona
         on BichoCap.zona_fk=Zona.zona_pk
         inner join Bicho
         on Bicho.bicho_pk = BichoCap.bicho_fk
         where `usuario_fk`= ".$result[2]);
          
        $bichos = mysqli_fetch_all($misBichos, MYSQLI_ASSOC);
 	 	#Sacamos el resultado en formato json 
 	 	echo json_encode($bichos);  	  
 	}else{
      echo "Login o Pass incorrectos";   
     }
 }else{
 		echo "Error de conexion, faltan parametros"; 
 	} 
 	 	?>
