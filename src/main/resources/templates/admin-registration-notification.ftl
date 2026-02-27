<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>New Admin Registration Request</title>
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
        .button {
            display: inline-block;
            padding: 12px 24px;
            background-color: #28a745;
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
        <h1>New Admin Registration Request</h1>
    </div>
    
    <div class="content">
        <p>Dear Super Admin,</p>
        
        <p>A new admin has registered for your academy and is awaiting your approval:</p>
        
        <table style="width: 100%; border-collapse: collapse; margin: 20px 0;">
            <tr>
                <td style="padding: 8px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Admin Name:</strong></td>
                <td style="padding: 8px; border: 1px solid #ddd;">${adminName}</td>
            </tr>
            <tr>
                <td style="padding: 8px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Email Address:</strong></td>
                <td style="padding: 8px; border: 1px solid #ddd;">${adminEmail}</td>
            </tr>
            <tr>
                <td style="padding: 8px; border: 1px solid #ddd; background-color: #f2f2f2;"><strong>Tenant ID:</strong></td>
                <td style="padding: 8px; border: 1px solid #ddd;">${tenantId}</td>
            </tr>
        </table>
        
        <p>Please review this request and take appropriate action. You can approve or reject this admin registration through the admin portal.</p>
        
        <p><strong>Important:</strong> This admin will not be able to access the system until you approve their registration.</p>
        
        <p>Thank you for your attention to this matter.</p>
        
        <p>Best regards,<br>Academy Management System</p>
    </div>
    
    <div class="footer">
        <p>This is an automated message. Please do not reply to this email.</p>
    </div>
</body>
</html>
