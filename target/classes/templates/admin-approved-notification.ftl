<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Admin Account Has Been Approved</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            background-color: #28a745;
            color: white;
            padding: 20px;
            text-align: center;
            border-radius: 5px 5px 0 0;
        }
        .content {
            background-color: #f9f9f9;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 0 0 5px 5px;
        }
        .button {
            display: inline-block;
            padding: 12px 24px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin: 20px 0;
        }
        .footer {
            text-align: center;
            margin-top: 20px;
            color: #666;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>🎉 Congratulations!</h1>
        <h2>Your Admin Account Has Been Approved</h2>
    </div>
    
    <div class="content">
        <p>Dear ${adminName},</p>
        
        <p>We are pleased to inform you that your admin account registration has been approved by <strong>${superAdminName}</strong>.</p>
        
        <p>You now have full access to the Academy Management System with administrative privileges.</p>
        
        <h3>What's Next?</h3>
        <ul>
            <li>You can now log in to the system using your registered email address and password</li>
            <li>Access the admin dashboard to manage users, courses, and other system features</li>
            <li>Configure your academy settings and preferences</li>
        </ul>
        
        <p style="text-align: center;">
            <a href="#" class="button">Login to Your Account</a>
        </p>
        
        <p><strong>Important:</strong> If you have any questions or need assistance, please don't hesitate to contact our support team.</p>
        
        <p>We look forward to having you as part of our academy administration team!</p>
        
        <p>Best regards,<br>Academy Management System</p>
    </div>
    
    <div class="footer">
        <p>This is an automated message. Please do not reply to this email.</p>
    </div>
</body>
</html>
