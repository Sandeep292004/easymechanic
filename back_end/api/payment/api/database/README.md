# EASY MECHANIC Database Documentation

## Database Files

### 1. `schema.sql` - Complete Database Schema (Version 2.0)
**Use this for fresh installations**

This file contains the complete database schema with all tables and enhancements:
- Enhanced users table with address, profile images, verification status
- Enhanced mechanics table with shop details, location, ratings
- Service requests with priority, costs, ratings
- Payments with Razorpay integration fields
- Reviews/ratings system
- Service request images
- Notifications system
- Improved indexes for better performance

**How to use:**
1. Open phpMyAdmin: http://localhost/phpmyadmin/
2. Click on "SQL" tab
3. Copy and paste the entire content of `schema.sql`
4. Click "Go" to execute
5. Verify all tables are created

### 2. `ALTER_TABLES.sql` - Database Migration Script
**Use this if you already have the database and want to upgrade**

This file contains ALTER TABLE statements to add new fields to existing tables.

**Important Notes:**
- Some MySQL versions don't support `IF NOT EXISTS` in ALTER TABLE
- If you get errors, remove `IF NOT EXISTS` from the statements
- Run this script carefully on production databases
- Always backup your database before running migrations

**How to use:**
1. Backup your existing database
2. Open phpMyAdmin: http://localhost/phpmyadmin/
3. Select the `easymechanic` database
4. Click on "SQL" tab
5. Copy and paste sections of `ALTER_TABLES.sql` one at a time
6. Or run the entire file if you're sure

## Schema Enhancements (Version 2.0)

### Users Table Enhancements
- ✅ `address`, `city`, `state`, `pincode` - Location information
- ✅ `profile_image` - User profile picture
- ✅ `is_active` - Account status (active/inactive)
- ✅ `email_verified`, `phone_verified` - Verification status
- ✅ `last_login` - Track last login time

### Mechanics Table Enhancements
- ✅ `shop_name` - Workshop/shop name
- ✅ `address`, `city`, `state`, `pincode` - Shop location
- ✅ `latitude`, `longitude` - Shop coordinates
- ✅ `profile_image`, `shop_image` - Profile and shop images
- ✅ `is_active` - Account status
- ✅ `email_verified`, `phone_verified` - Verification status
- ✅ `total_ratings` - Count of ratings received
- ✅ `last_login` - Track last login time

### Service Requests Enhancements
- ✅ `city`, `state`, `pincode` - Request location details
- ✅ `vehicle_type`, `vehicle_number` - Vehicle info for request
- ✅ `priority` - Request priority (low, medium, high, urgent)
- ✅ `estimated_cost`, `actual_cost` - Cost tracking
- ✅ `notes` - Additional notes from mechanic
- ✅ `user_rating`, `user_feedback` - Post-service feedback
- ✅ `started_at` - When work started
- ✅ `cancelled_at`, `cancelled_by`, `cancellation_reason` - Cancellation tracking

### Payments Enhancements
- ✅ `user_id`, `mechanic_id` - Quick access fields
- ✅ `razorpay_signature` - Payment signature for verification
- ✅ `failure_reason` - Payment failure details
- ✅ `refund_amount`, `refund_reason` - Refund tracking

### New Tables

#### 1. `mechanic_reviews`
- Store reviews and ratings for mechanics
- One review per service request
- Visible/hidden status

#### 2. `service_request_images`
- Store images related to service requests
- Types: issue, before, after, other
- Track who uploaded (user or mechanic)

#### 3. `notifications`
- Push notification system
- Track read/unread status
- Link to related entities

### Token Management Enhancements
- ✅ `device_info` - Device information
- ✅ `ip_address` - IP address tracking

## Indexes Added

All tables now have optimized indexes for:
- Email and phone lookups
- Status filtering
- Location-based queries
- Date-based sorting
- Foreign key relationships

## Migration Guide

### For Fresh Installation
1. Use `schema.sql` - Complete and ready to use

### For Existing Database
1. Backup your database first!
2. Review `ALTER_TABLES.sql`
3. Run sections one by one
4. Test your application after each section
5. If errors occur, check MySQL version compatibility

### Common Issues

**Issue**: `IF NOT EXISTS` not supported
**Solution**: Remove `IF NOT EXISTS` from ALTER statements

**Issue**: Foreign key constraint errors
**Solution**: Ensure referenced tables exist and data is consistent

**Issue**: Column already exists
**Solution**: Skip that ALTER statement or use `IF NOT EXISTS` (if supported)

## Database Structure

```
easymechanic/
├── users (Vehicle owners)
├── mechanics (Service providers)
├── mechanic_locations (GPS tracking)
├── service_requests (Service requests)
├── payments (Payment transactions)
├── user_tokens (JWT tokens)
├── mechanic_reviews (Reviews & ratings)
├── service_request_images (Request images)
└── notifications (Push notifications)
```

## Performance Tips

1. **Indexes**: All frequently queried columns are indexed
2. **Foreign Keys**: Properly set up for data integrity
3. **Partitioning**: Consider partitioning large tables by date
4. **Archiving**: Archive old completed requests periodically
5. **Backups**: Set up regular database backups

## Backup Commands

### Export Database
```bash
mysqldump -u root -p easymechanic > easymechanic_backup.sql
```

### Import Database
```bash
mysql -u root -p easymechanic < easymechanic_backup.sql
```

## Version History

- **Version 2.0** (Current): Enhanced schema with new fields and tables
- **Version 1.0**: Initial schema with basic tables

## Support

For issues or questions about the database schema, refer to:
- Main API documentation: `../README.md`
- Setup guide: `../SETUP.md`

