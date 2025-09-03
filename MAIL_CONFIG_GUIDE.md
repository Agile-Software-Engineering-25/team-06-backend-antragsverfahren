# Mail Configuration Guide

This file contains instructions for configuring email functionality in the application.

## Production Configuration

To use real email functionality, update the following properties in `application.properties`:

```properties
# For Gmail
mail.host=smtp.gmail.com
mail.port=587
mail.username=your-email@gmail.com
mail.password=your-app-password
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.smtp.ssl.trust=smtp.gmail.com
```

## Gmail App Password Setup

1. Enable 2-Factor Authentication on your Google account
2. Go to Google Account settings > Security > App passwords
3. Generate a new app password for "Mail"
4. Use this generated password (not your regular Gmail password) in the configuration

## Alternative Email Providers

### Outlook/Hotmail
```properties
mail.host=smtp-mail.outlook.com
mail.port=587
mail.username=your-email@outlook.com
mail.password=your-password
mail.smtp.auth=true
mail.smtp.starttls.enable=true
```

### Yahoo Mail
```properties
mail.host=smtp.mail.yahoo.com
mail.port=587
mail.username=your-email@yahoo.com
mail.password=your-app-password
mail.smtp.auth=true
mail.smtp.starttls.enable=true
```

## Environment Variables

For production deployment, it's recommended to use environment variables instead of hardcoding credentials:

```bash
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
```

Then reference them in application.properties:
```properties
mail.username=${MAIL_USERNAME}
mail.password=${MAIL_PASSWORD}
```

## Testing Email

To test email functionality with a real address:
1. Update the credentials in application.properties
2. Use the endpoint: POST /studienbescheinigung/send
3. Or use: GET /studienbescheinigung/generate?sendEmail=true
