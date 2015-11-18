<?php 
	#Se mira que el campo login no ste vacio
 if (isset($_POST['campoLogin']) && $_POST['campoLogin'] != ""){ 
 	$login = $_POST['campoLogin']; 
 	#Conexion a la bd
 	$con = mysqli_connect("**********","*********","*********", "***********"); 
 	#Se comprueba la conexion
 	if (mysqli_connect_errno()) {
 	 echo 'Error de conexion: '.mysqli_connect_error(); 
 	 exit(); 
 	} 
 	 #Se quitan posibles caracteres especiales [NUL (ASCII 0), \n, \r, \, ', ", y Control-Z] (sql injection)
 	 $login = mysqli_real_escape_string($con, $login); 
     $query="SELECT bc_pk as 'pk',
     bicho_fk as 'nPokemon', 
     zona_desc as 'zonaCapturado',
     BichoCap.nombre, 
     felicidad, 
     hambre, 
     Bicho.nombre as 'nombreGenerico', 
     experiencia 
     FROM BichoCap  
     inner join Bicho on Bicho.bicho_pk = BichoCap.bicho_fk 
     inner join Punto on punto_pk = BichoCap.punto_fk
     inner join Zona on Punto.zona_fk = Zona.zona_pk
     where usuario_fk = (select usuario_pk from Usuario where login = '".$login."')";
 	 	$misBichos = mysqli_query($con, $query);          
        $bichos = mysqli_fetch_all($misBichos, MYSQLI_ASSOC);
 	 	#Sacamos el resultado en formato json 
 	 	echo json_encode($bichos);  	  
 }else{
 		echo "Error de conexion, faltan parametros"; 
 	} 
 	 	?>
