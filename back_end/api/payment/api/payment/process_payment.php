<?php
/**
 * Process Payment
 * POST /api/payment/process_payment.php
 * 
 * Headers: Authorization: Bearer {token}
 * Body: {
 *   "service_request_id": 1,
 *   "amount": 500.00,
 *   "payment_method": "cash" or "razorpay",
 *   "razorpay_order_id": "order_xxx" (optional, for Razorpay),
 *   "razorpay_payment_id": "pay_xxx" (optional, for Razorpay)
 * }
 */

require_once __DIR__ . '/../config/headers.php';
require_once __DIR__ . '/../config/db.php';
require_once __DIR__ . '/../config/jwt.php';

// Only allow POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit();
}

// Validate token and get user info
$tokenData = JWT::validateToken();

if ($tokenData['user_type'] !== 'user') {
    http_response_code(403);
    echo json_encode(['success' => false, 'message' => 'Access denied. Only users can process payments']);
    exit();
}

$userId = $tokenData['user_id'];

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Validate required fields
if (empty($input['service_request_id']) || empty($input['amount'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'service_request_id and amount are required']);
    exit();
}

$requestId = intval($input['service_request_id']);
$amount = floatval($input['amount']);
$paymentMethod = isset($input['payment_method']) ? strtolower($input['payment_method']) : 'cash';
$razorpayOrderId = isset($input['razorpay_order_id']) ? trim($input['razorpay_order_id']) : null;
$razorpayPaymentId = isset($input['razorpay_payment_id']) ? trim($input['razorpay_payment_id']) : null;

// Validate amount
if ($amount <= 0) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Amount must be greater than 0']);
    exit();
}

// Validate payment method
if (!in_array($paymentMethod, ['cash', 'razorpay', 'card', 'upi'])) {
    $paymentMethod = 'cash';
}

try {
    // Verify service request belongs to user and is completed
    $stmt = $db->prepare("SELECT id, user_id, status FROM service_requests WHERE id = ?");
    $stmt->bind_param("i", $requestId);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows === 0) {
        http_response_code(404);
        echo json_encode(['success' => false, 'message' => 'Service request not found']);
        $stmt->close();
        exit();
    }
    
    $request = $result->fetch_assoc();
    $stmt->close();
    
    if ($request['user_id'] != $userId) {
        http_response_code(403);
        echo json_encode(['success' => false, 'message' => 'You can only pay for your own service requests']);
        exit();
    }
    
    if ($request['status'] !== 'completed') {
        http_response_code(400);
        echo json_encode(['success' => false, 'message' => 'Payment can only be processed for completed service requests']);
        exit();
    }
    
    // Check if payment already exists
    $stmt = $db->prepare("SELECT id, status FROM payments WHERE service_request_id = ?");
    $stmt->bind_param("i", $requestId);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows > 0) {
        $existingPayment = $result->fetch_assoc();
        if ($existingPayment['status'] === 'success') {
            http_response_code(400);
            echo json_encode(['success' => false, 'message' => 'Payment already processed for this request']);
            $stmt->close();
            exit();
        }
    }
    $stmt->close();
    
    // Generate transaction ID
    $transactionId = 'TXN' . time() . rand(1000, 9999);
    
    // For now, simulate payment success (dummy logic)
    // In production, integrate with Razorpay API here
    $paymentStatus = 'success';
    
    if ($paymentMethod === 'razorpay' && !empty($razorpayOrderId) && !empty($razorpayPaymentId)) {
        // TODO: Verify Razorpay payment here
        // For now, accept as success
        $paymentStatus = 'success';
    }
    
    // Insert payment record
    $stmt = $db->prepare("
        INSERT INTO payments (
            service_request_id, 
            amount, 
            payment_method, 
            transaction_id, 
            razorpay_order_id, 
            razorpay_payment_id, 
            status
        ) VALUES (?, ?, ?, ?, ?, ?, ?)
    ");
    $stmt->bind_param("idsssss", $requestId, $amount, $paymentMethod, $transactionId, $razorpayOrderId, $razorpayPaymentId, $paymentStatus);
    
    if ($stmt->execute()) {
        $paymentId = $db->getLastInsertId();
        
        echo json_encode([
            'success' => true,
            'message' => 'Payment processed successfully',
            'data' => [
                'payment_id' => $paymentId,
                'service_request_id' => $requestId,
                'amount' => $amount,
                'payment_method' => $paymentMethod,
                'transaction_id' => $transactionId,
                'razorpay_order_id' => $razorpayOrderId,
                'razorpay_payment_id' => $razorpayPaymentId,
                'status' => $paymentStatus,
                'created_at' => date('Y-m-d H:i:s')
            ]
        ]);
    } else {
        $conn = $db->getConnection();
        throw new Exception($conn->error);
    }
    $stmt->close();
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to process payment',
        'error' => $e->getMessage()
    ]);
}

