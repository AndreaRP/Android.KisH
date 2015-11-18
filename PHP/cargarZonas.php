<?php 
    $con = mysqli_connect("*******","*********","*********", "**********"); 
 	#Se comprueba la conexion
 	if (mysqli_connect_errno()) {
 	 echo 'Error de conexion: '.mysqli_connect_error(); 
 	 exit(); 
 	} 
    $lasZonas = mysqli_query($con, "SELECT zona_pk as 'codigo', zona_lat as 'lat',  zona_long as 'longitud' FROM Zona");        
        $zonas = mysqli_fetch_all($lasZonas, MYSQLI_ASSOC);
 	 	#Sacamos el resultado en formato json 
 	 	echo json_encode($zonas);  	
    mysqli_close($con);  
 	 	?>
