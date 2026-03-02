<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Account Approved</title>
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
            background-color: #007bff;
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
        <h1>Admin Account Approved</h1>
    </div>
    
    <div class="content">
        <p>Dear Super Admin,</p>
        
        <p>This is to confirm that you have successfully approved the admin account for:</p>
        
        <table style="width: 100%; border-collapse: collapse; margin: 20px 0;">
            <tr>
                <td style="padding: 8px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Admin Name:</strong></td>
                <td style="padding: 8px; border: 1px solid #ddd;">${adminName}</td>
            </tr>
            <tr>
                <td style="padding: 8px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Email Address:</strong></td>
                <td style="padding: 8px; border: 1px solid #ddd;">${adminEmail}</td>
            </tr>
        </table>
        
        <p>The admin has been notified of their approval and can now access the system with their administrative privileges.</p>
        
        <p><strong>Action Completed:</strong> Admin account activation successful</p>
        
        <p>Thank you for your prompt attention to this request.</p>
        
        <p>Best regards,<br>Academy Management System</p>
    </div>
    
    <div class="footer">
        <p>This is an automated message. Please do not reply to this email.</p>
    </div>
</body>
</html>
