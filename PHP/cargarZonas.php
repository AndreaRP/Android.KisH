<?php 
//include 'comprobarLogin.php';
//include 'Conexion.php';
//if (isset($_POST['campoLogin']) && $_POST['campoLogin'] != "" && isset($_POST['campoPass']) && $_POST['campoPass'] != ""){ 
  //  if(true){
  //  $login = $_POST['campoLogin']; 
 //	$pass = $_POST['campoPass'];
     //$login = "root"; 
    //$pass = "root";
  // $correcto = validarLogin($login, $pass);
//if($correcto){
    $con = mysqli_connect("sql100.byethost6.com","b6_16108246","thebeatles1", "b6_16108246_KishBD"); 
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
 	//}else{
    //  echo "Login o Pass incorrectos";   
     //} 
//}else{
//    echo "Error de conexion, faltan parametros";
//}
 	 	?>