<#include "main.ftl" />
<@mainPage title="Account Verification">
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <!-- LOGO -->
        <tr>
            <td bgcolor="#165d21" align="center">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                    <tr>
                        <td align="center" valign="top" style="padding: 40px 10px 40px 10px;"> </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td bgcolor="#165d21" align="center" style="padding: 0px 10px 0px 10px;">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                    <tr>

                        <td bgcolor="#ffffff" align="center" valign="top" style="padding: 40px 20px 0px 20px; border-radius: 4px 4px 0px 0px; color: #111111; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 48px; font-weight: bolder; letter-spacing:2px; line-height: 48px;">
                            <img src="https://api.pcloud.com/getpubthumb?code=XZAx0qXZITrz7GAmLPHkDShM6KRyqQtVxec7&linkpassword=undefined&size=300x100&crop=0&type=auto">
                            <h1 style="font-size: 48px; font-weight: bolder;margin-top: -1%;">Value Added Service <span style="font-weight: 800;font-size: 35px;color: green;">(VAS Registration)</span></h1>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td bgcolor="#f4f4f4" align="center" style="padding: 0px 10px 0px 10px;">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                    <tr>
                        <td bgcolor="#ffffff" align="left" style="padding: 0px 30px 10px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 600; line-height: 25px;">
                            <p style="margin: 0;">We're excited to have you get started. First, you need to complete your registration. &nbsp; Just press the button below.</p>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#ffffff" align="left">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td bgcolor="#ffffff" align="center" style="padding: 0px 30px 10px 30px;">
                                        <table border="0" cellspacing="0" cellpadding="0">

                                            <tr>
                                                <img src="https://api.pcloud.com/getpubthumb?code=XZnx0qXZGlUug4j5gO46GEC3E296q5QDmiHV&linkpassword=undefined&size=512x512&crop=0&type=auto" width="125" height="120" style="display: block; border: 0px;" /><br>
                                                <td align="center" style="border-radius: 3px;" bgcolor="#00ad56"><a href="${domainUrl}${url}?code=${code}&user_id=${userId}" target="_blank" style="font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 2px; border: 1px solid #00ad56; display: inline-block;">Complete Registration</a></td>
                                            </tr>

<#--                                            <tr>-->
<#--                                                <img src="https://api.pcloud.com/getpubthumb?code=XZnx0qXZGlUug4j5gO46GEC3E296q5QDmiHV&linkpassword=undefined&size=512x512&crop=0&type=auto" width="125" height="120" style="display: block; border: 0px;" /><br>-->
<#--                                                <td align="center" style="border-radius: 3px;" bgcolor="#00ad56">-->
<#--                                                    <a href="${domainUrl}${url}?code=${code}" target="_blank" style="font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 2px; border: 1px solid #00ad56; display: inline-block;">-->
<#--                                                        Complete Registration-->
<#--                                                    </a>-->
<#--                                                </td>-->
<#--                                            </tr>-->
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr> <!-- COPY -->
                    <tr>
                        <td bgcolor="#ffffff" align="left" style="padding: 0px 30px 0px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 600; line-height: 25px;">
                            <p style="margin: 0;">If that doesn't work, copy and paste the following link in your browser:</p>
                        </td>
                    </tr>

                    <tr>
                        <td bgcolor="#ffffff" align="left" style="padding: 0px 30px 0px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 600; line-height: 25px;">
                            <p style="margin: 0;">Link valid for ${live_duration} minutes</p>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#ffffff" align="left" style="padding: 5px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight:600; line-height: 25px;">
                            <p style="margin: 0;"><a href="#" target="_blank" style="color: #00ad56;">${domainUrl}${url}?code=${code}</a></p>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#ffffff" align="left" style="padding: 0px 30px 20px 30px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 600; line-height: 25px;">
                            <p style="margin: 0;">The link can only be used once, and valid for ${live_duration} minutes. </p>
                        </td>
                    </tr>
                    <tr>
                        <td bgcolor="#ffffff" align="left" style="padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 600; line-height: 25px;">
                            <p style="margin: 0;">If you did not request this email please ignore it.</p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td bgcolor="#f4f4f4" align="center" style="padding: 30px 10px 0px 10px;">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                    <tr>
                        <td bgcolor="#8bd9b2" align="center" style="padding: 30px 30px 30px 30px; border-radius: 4px 4px 4px 4px; color: #666666; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;">
                            <h2 style="font-size: 20px; font-weight: 600; color: #111111; margin: 0;">Need more help?</h2>
                            <p style="margin: 0;color: black;font-weight: 500;">Call: <a href="#" target="_blank" style="color: #165d21;">234 818 213 4332</a><br>or email <a href="mailto:tommygogd@gmail.com" style="color: #165d21;">tommygogd@gmail.com</a></p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td bgcolor="#f4f4f4" align="center" style="padding: 0px 10px 0px 10px;">
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                    <tr>
                        <td bgcolor="#165d21" align="left" style="padding: 0px 30px 30px 30px; color: white; font-family: 'Lato', Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 600; line-height: 18px;"> <br>
                            <p style="margin: 0;">This email was sent from a notification-only email address which cannot accept incoming mail. Please do not reply directly to this message.</p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</@mainPage>