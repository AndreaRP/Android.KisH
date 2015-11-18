<?php 
//include 'comprobarLogin.php';
//include 'Conexion.php';
//if (isset($_POST['campoLogin']) && $_POST['campoLogin'] != "" && isset($_POST['campoPass']) && $_POST['campoPass'] != ""){ 
//  $login = $_POST['campoLogin']; 
// 	$pass = $_POST['campoPass'];
//  $correcto = comprobarLogin($login, $pass);
//if($correcto){
    $con = mysqli_connect("sql100.byethost6.com","b6_16108246","thebeatles1", "b6_16108246_KishBD"); 
 	#Se comprueba la conexion
 	if (mysqli_connect_errno()) {
 	 echo 'Error de conexion: '.mysqli_connect_error(); 
 	 exit(); 
 	} 
 	$zonaActual = $_POST['zonaActual'];
    //$zonaActual = '6';
    $losBichosxZona = mysqli_query($con, "select bicho_fk as 'nPokemon', Bicho.nombre as 'nombreGenerico' 
from BichoXZona 
inner join Bicho
on BichoXZona.bicho_fk = Bicho.bicho_pk
where zona_fk = '".$zonaActual."'");          
        $bichosZona = mysqli_fetch_all($losBichosxZona, MYSQLI_ASSOC);
 	 	#Sacamos el resultado en formato json 
 	 	echo json_encode($bichosZona);  	  
 	//}else{
      //echo "Login o Pass incorrectos";   
     //} 
//}else{
//    echo "Error de conexion, faltan parametros"
//}
  mysqli_close($con);
 	 	?>