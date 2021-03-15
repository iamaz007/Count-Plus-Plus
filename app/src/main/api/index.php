<?php 
	// ============== Parho Likho Computer Science ///
///www.youtube.com/ParholikhoCS
///www.Facebook.com/ParholikhoCS
///www.twitter.com/ParholikhoCS
//----------------------------------------------------


	require_once 'DbOperation.php';
	
	$response = array(); 

	//// http://----Ur IP Address ---/heroapi/HeroApi/v1/?op=addPayment
	
	if(isset($_GET['op'])){
		
		switch($_GET['op']){
			

				/// Check URL and testing API
				/// http://=======Enter your IP Address------ /heroapi/HeroApi/v1/?op=addPayment
				/// Require POST
			case 'addPayment':
				// if(isset($_POST['sender_name']) 
				// && isset($_POST['sender_email']) 
				// && isset($_POST['amount']) 
				// && isset($_POST['due_date']) 
				// && isset($_POST['email'])
				// ){
					$db = new DbOperation($_POST['email']); 
					if($db->createPayment($_POST['sender_name'], $_POST['sender_email']
					, $_POST['amount'], $_POST['due_date']
					))
					if($db)
					{
						$response['error'] = false;
						$response['message'] = 'Payment added successfully';
					}else{
						$response['error'] = true;
						$response['message'] = 'Could not add payment';
					}
				// }else{
				// 	$response['error'] = true; 
				// 	$response['message'] = 'Required Parameters are missing';
				// }
			break; 
			
			////http://----Enter your IP Address -----/heroapi/HeroApi/v1/?op=getPayments
			////Require GET
			case 'getPayments':
				$db = new DbOperation($_POST['email']);
				$payments = $db->getPayments();
				if(count($payments)<=0){
					$response['error'] = true; 
					$response['message'] = 'Nothing found in the database';
				}else{
					$response['error'] = false; 
					$response['payments'] = $payments;
				}
			break; 
			
			default:
				$response['error'] = true;
				$response['message'] = 'No operation to perform';
			
		}
		
	}else{
		$response['error'] = false; 
		$response['message'] = 'Invalid Request';
	}
	
	echo json_encode($response);