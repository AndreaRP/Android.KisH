<?php 
    $con = mysqli_connect("*********","********","**********", "***********"); 
 	#Se comprueba la conexion
 	if (mysqli_connect_errno()) {
 	 echo 'Error de conexion: '.mysqli_connect_error(); 
 	 exit(); 
 	} 
 	$zonaActual = $_POST['zonaActual'];
    $losBichosxZona = mysqli_query($con, "select bicho_fk as 'nPokemon', Bicho.nombre as 'nombreGenerico' 
from BichoXZona 
inner join Bicho
on BichoXZona.bicho_fk = Bicho.bicho_pk
where zona_fk = '".$zonaActual."'");          
        $bichosZona = mysqli_fetch_all($losBichosxZona, MYSQLI_ASSOC);
 	 	#Sacamos el resultado en formato json 
 	 	echo json_encode($bichosZona);  	  
  mysqli_close($con);
 	 	?>
