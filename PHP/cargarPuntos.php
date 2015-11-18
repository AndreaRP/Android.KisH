<?php 
    $con = mysqli_connect("********","***********","**********", "************"); 
 	#Se comprueba la conexion
 	if (mysqli_connect_errno()) {
 	 echo 'Error de conexion: '.mysqli_connect_error(); 
 	 exit(); 
 	} 
 	$zonaActual = $_POST['zonaActual'];
	$zonaActual = mysqli_real_escape_string($con, $zonaActual);
    $losPuntos = mysqli_query($con, "SELECT punto_pk as 'codigo', zona_fk as 'zonaCodigo', punto_lat as 'lat', punto_long as 'lon' FROM Punto 
        where zona_fk = '".$zonaActual."'
        and
        activo = '1'");         
        $puntos = mysqli_fetch_all($losPuntos, MYSQLI_ASSOC);
 	 	#Sacamos el resultado en formato json 
 	 	echo json_encode($puntos);  	  
mysqli_close($con);
 	 	?>
