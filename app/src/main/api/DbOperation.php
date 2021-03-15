<?php

// ============== Parho Likho Computer Science ///
///www.youtube.com/ParholikhoCS
///www.Facebook.com/ParholikhoCS
///www.twitter.com/ParholikhoCS
//----------------------------------------------------

 
class DbOperation
{
    private $con;
	private $id;
    function __construct($email)
    {
        include('DbConnect.php');
        $db = new DbConnect();
		$this->con = $db->connect();
		
		$sqlquery = "SELECT id FROM users where email='".$email."'";
		if ($result=mysqli_query($this->con,$sqlquery)) {
			$json = mysqli_fetch_all ($result, MYSQLI_ASSOC);
			// print_r($json);
			// return true; 
			$this->id = $json[0]['id'];
		} else {
			echo mysqli_error($this->con); 
		}
		// $stmt = $this->con->prepare($sqlquery);
		// // $stmt->bind_result($id);
		// $stmt->execute(array(":email"=>$email));
		// while($stmt->fetch()){
		// 	$this->id = $stmt->id; 
		// }
    }

	public function createPayment($sender_name, $sender_email, $amount, $due_date){
		// return true; 
		$sql = "INSERT INTO `payments` 
		(`sender_name`, `sender_email`, `receiver_id`, `amount`, `due_date`) 
		VALUES ('".$sender_name."', '".$sender_email."', $this->id, ".$amount.", '".$due_date."');";
		if (mysqli_query($this->con,$sql)) {
			return true; 
		} else {
			return false; 
		}
		
		// $stmt = $this->con->prepare();
		// $stmt->bind_param($sender_name, $sender_email, 1, $amount, $due_date);
		// if($stmt->execute())
		// 	return true; 
		// return false; 
	}
	
	public function getPayments(){
		$sqlquery = "SELECT * FROM  payments where receiver_id='".$this->id."'";
		if ($result=mysqli_query($this->con,$sqlquery)) {
			$json = mysqli_fetch_all ($result, MYSQLI_ASSOC);
			// print_r($json);
			return $json; 
		} else {
			echo mysqli_error($this->con); 
		}
		
		// $stmt = $this->con->prepare("SELECT * FROM  payments where receiver_id=:id");
		// $stmt->bind_result($id, $sender_name, $sender_email, $amount, $due_date);
        // $stmt->execute(array(":id"=>$this->id));
		// $payments = array();
		
		// while($stmt->fetch()){
		// 	$temp = array(); 
		// 	$temp['id'] = $id; 
		// 	$temp['sender_name'] = $sender_name; 
		// 	$temp['sender_email'] = $sender_email; 
		// 	$temp['amount'] = $amount; 
		// 	$temp['due_date'] = $due_date; 
		// 	array_push($payments, $temp);
		// }
		// return $payments; 
	}
}

