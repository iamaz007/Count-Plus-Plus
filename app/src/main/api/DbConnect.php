
<?php
// ============== Parho Likho Computer Science ///
///www.youtube.com/ParholikhoCS
///www.Facebook.com/ParholikhoCS
///www.twitter.com/ParholikhoCS
//----------------------------------------------------

 
class DbConnect
{
    //Variable to store database link
    private $con;
 
    //Class constructor
    function __construct()
    {
 
    }
 
    //This method will connect to the database
    function connect()
    {
        //Including the constants.php file to get the database constants
        include('Constants.php');
 
        //connecting to mysql database
        $this->con = mysqli_connect(DB_HOST,DB_USERNAME,DB_PASSWORD,DB_NAME);

        if (mysqli_connect_errno()) {
            echo "Failed to connect to MySQL: " . mysqli_connect_error();
        exit();
        }
        // $this->con = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
 
        // //Checking if any error occured while connecting
        // if (mysqli_connect_errno()) {
        //     echo "Failed to connect to MySQL: " . mysqli_connect_error();
        //     return null;
        // }
 
        //finally returning the connection link
        return $this->con;
    }
 
}