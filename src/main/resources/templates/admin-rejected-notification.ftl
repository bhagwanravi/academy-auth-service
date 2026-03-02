<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Your Admin Registration Request</title>
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
            background-color: #dc3545;
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
        <h1>Registration Update</h1>
        <h2>Your Admin Registration Request</h2>
    </div>
    
    <div class="content">
        <p>Dear ${adminName},</p>
        
        <p>We regret to inform you that your admin account registration has been reviewed and <strong>rejected</strong> by <strong>${superAdminName}</strong>.</p>
        
        <p>This decision could be due to various reasons such as:</p>
        <ul>
            <li>Information provided during registration could not be verified</li>
            <li>The academy has reached its admin capacity</li>
            <li>Your registration does not meet the current requirements</li>
            <li>Other administrative reasons</li>
        </ul>
        
        <h3>What Can You Do?</h3>
        <p>If you believe this decision was made in error or would like to understand the specific reasons for the rejection, please contact the academy administration directly.</p>
        
        <p>You may also consider submitting a new registration request with updated information if the circumstances have changed.</p>
        
        <p>We appreciate your interest in joining our academy management team.</p>
        
        <p>Best regards,<br>Academy Management System</p>
    </div>
    
    <div class="footer">
        <p>This is an automated message. Please do not reply to this email.</p>
    </div>
</body>
</html>
