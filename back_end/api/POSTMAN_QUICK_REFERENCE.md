# Postman Quick Reference - Copy & Paste

## 🔗 Base URL
```
http://localhost/easymechanic/api
```

---

## 1️⃣ User Registration

**URL:** `POST http://localhost/easymechanic/api/auth/user_register.php`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "password": "password123",
  "vehicle_type": "Car",
  "vehicle_number": "ABC123"
}
```

---

## 2️⃣ Mechanic Registration

**URL:** `POST http://localhost/easymechanic/api/auth/mechanic_register.php`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "name": "Mechanic Name",
  "email": "mechanic@example.com",
  "phone": "9876543210",
  "password": "password123",
  "specialization": "Engine Repair",
  "experience_years": 5
}
```

---

## 3️⃣ User Login

**URL:** `POST http://localhost/easymechanic/api/auth/user_login.php`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**💡 Copy token from response!**

---

## 4️⃣ Mechanic Login

**URL:** `POST http://localhost/easymechanic/api/auth/mechanic_login.php`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "mechanic@example.com",
  "password": "password123"
}
```

**💡 Copy token from response!**

---

## 5️⃣ Update Mechanic Location

**URL:** `POST http://localhost/easymechanic/api/mechanic/update_location.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_MECHANIC_TOKEN_HERE
```

**Body:**
```json
{
  "latitude": 28.6139,
  "longitude": 77.2090
}
```

---

## 6️⃣ Find Nearby Mechanics

**URL:** `GET http://localhost/easymechanic/api/mechanic/find_mechanics.php?latitude=28.6139&longitude=77.2090&radius=10`

**Headers:** (None)

---

## 7️⃣ Create Service Request

**URL:** `POST http://localhost/easymechanic/api/requests/create_request.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

**Body:**
```json
{
  "issue_description": "Engine not starting, makes clicking sound when turning key",
  "latitude": 28.6139,
  "longitude": 77.2090,
  "address": "123 Main Street, New Delhi"
}
```

---

## 8️⃣ Accept Service Request

**URL:** `POST http://localhost/easymechanic/api/requests/accept_request.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_MECHANIC_TOKEN_HERE
```

**Body:**
```json
{
  "request_id": 1
}
```

---

## 9️⃣ Complete Service Request

**URL:** `POST http://localhost/easymechanic/api/requests/complete_request.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_MECHANIC_TOKEN_HERE
```

**Body:**
```json
{
  "request_id": 1
}
```

---

## 🔟 Get Service Requests

**URL:** `GET http://localhost/easymechanic/api/requests/get_requests.php?type=user&status=pending`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

**Query Params:**
- `type`: user or mechanic (optional)
- `status`: pending, accepted, in_progress, completed, cancelled (optional)

---

## 1️⃣1️⃣ Process Payment

**URL:** `POST http://localhost/easymechanic/api/payment/process_payment.php`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

**Body (Cash):**
```json
{
  "service_request_id": 1,
  "amount": 500.00,
  "payment_method": "cash"
}
```

**Body (Razorpay):**
```json
{
  "service_request_id": 1,
  "amount": 500.00,
  "payment_method": "razorpay",
  "razorpay_order_id": "order_xxx",
  "razorpay_payment_id": "pay_xxx"
}
```

---

## 1️⃣2️⃣ Get Payment History

**URL:** `GET http://localhost/easymechanic/api/payment/get_payments.php`

**Headers:**
```
Authorization: Bearer YOUR_USER_TOKEN_HERE
```

**Query Params:**
- `service_request_id`: (optional)

---

## 1️⃣3️⃣ AI Troubleshooting

**URL:** `POST http://localhost/easymechanic/api/ai/troubleshoot.php`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "problem_description": "My car engine won't start"
}
```

**More Examples:**
```json
{"problem_description": "Battery is dead"}
```
```json
{"problem_description": "Car is overheating"}
```
```json
{"problem_description": "Brakes are squeaking"}
```
```json
{"problem_description": "Tire is flat"}
```

---

## 1️⃣4️⃣ Health Check

**URL:** `GET http://localhost/easymechanic/api/index.php`

**Headers:** (None)

---

## 📥 Import Postman Collection

1. Open Postman
2. Click **"Import"**
3. Select **"Upload Files"**
4. Choose: `POSTMAN_COLLECTION.json`
5. Click **"Import"**

Collection includes all endpoints with pre-configured requests!

---

## 🔑 How to Use Tokens

1. **Login** (User or Mechanic)
2. **Copy token** from response
3. **In Postman:**
   - Click collection → Variables tab
   - Set `user_token` or `mechanic_token`
   - Or manually add to Authorization header:
     ```
     Authorization: Bearer YOUR_TOKEN_HERE
     ```

---

## ✅ Testing Order

1. Health Check
2. Register User
3. Register Mechanic
4. User Login (get token)
5. Mechanic Login (get token)
6. Update Mechanic Location
7. Find Nearby Mechanics
8. Create Service Request
9. Accept Request (as mechanic)
10. Complete Request (as mechanic)
11. Process Payment (as user)
12. Get Payment History

---

**Quick Copy-Paste Ready!** 🚀

