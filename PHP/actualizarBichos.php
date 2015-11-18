<?php
//$login = $_POST['campoLogin']; 
$bichos = $_POST['bichos']; 
//$login='bla';
//$bichos="[{'pk':'1','nPokemon':'151','zonaCapturado':'Unknown','nombre':'Mew','felicidad':'0','hambre':'0','nombreGenerico':'Mew','experiencia':'0'},{'pk':'2','nPokemon':'147','zonaCapturado':'Unknown','nombre':'Dratini','felicidad':'0','hambre':'0','nombreGenerico':'Dratini','experiencia':'0'},{'pk':'3','nPokemon':'149','zonaCapturado':'Unknown','nombre':'Antenilla','felicidad':'0','hambre':'10','nombreGenerico':'Dragonite','experiencia':'0']";

if (isset($bichos) && $bichos != ""){ 
$servername = "sql100.byethost6.com";
$username = "b6_16108246";
$password = "thebeatles1";
$dbname = "b6_16108246_KishBD";
// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$login = mysqli_real_escape_string($conn, $login);
//$bichos = mysqli_real_escape_string($conn, $bichos);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    exit();
}
$bicho = json_decode($bichos, true);
    $sql = "UPDATE BichoCap SET felicidad = ".$bicho['felicidad'].", 
    hambre = ".$bicho['hambre'].", 
    experiencia = ".$bicho['experiencia']."
    WHERE bc_pk=".$bicho['pk'];
    var_dump($sql);
    
    if (mysqli_query($conn, $sql)) {
        echo "Bicho actualizado";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }


}else{
    echo "Error, faltan parametros";
}
mysqli_close($conn);
?>