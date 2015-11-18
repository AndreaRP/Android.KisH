<?php 
//include 'comprobarLogin.php';
//include 'Conexion.php';
//if (isset($_POST['campoLogin']) && $_POST['campoLogin'] != "" && //isset($_POST['campoPass']) && $_POST['campoPass'] != ""){ 
  //  $login = $_POST['campoLogin']; 
 	//$pass = $_POST['campoPass'];
//   $correcto = comprobarLogin($login, $pass);
//if($correcto){
    $con = mysqli_connect("sql100.byethost6.com","b6_16108246","thebeatles1", "b6_16108246_KishBD"); 
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
 //	}else{
   //   echo "Login o Pass incorrectos";   
     //} 
//}else{
  //  echo "Error de conexion, faltan parametros"
//}
mysqli_close($con);
 	 	?>