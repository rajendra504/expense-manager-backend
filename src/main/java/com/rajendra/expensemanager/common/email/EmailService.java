package com.rajendra.expensemanager.common.email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Async
    public void sendWelcomeEmail(String toEmail) {
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions request = CreateEmailOptions.builder()
                    .from("Expense Tracker <onboarding@resend.dev>")
                    .to(toEmail)
                    .subject("Welcome to Expense Manager ⬡")
                    .html(buildWelcomeEmailHtml(toEmail))
                    .build();

            CreateEmailResponse response = resend.emails().send(request);
            System.out.println("Welcome email sent. ID: " + response.getId());

        } catch (Exception e) {
            System.err.println("Welcome email error: " + e.getMessage());
        }
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions request = CreateEmailOptions.builder()
                    .from("Expense Tracker <onboarding@resend.dev>")
                    .to(toEmail)
                    .subject("Your Password Reset OTP — Expense Manager")
                    .html(buildOtpEmailHtml(toEmail, otp))
                    .build();

            CreateEmailResponse response = resend.emails().send(request);
            System.out.println("OTP email sent. ID: " + response.getId());

        } catch (Exception e) {
            System.err.println("OTP email error: " + e.getMessage());
        }
    }

    private String buildWelcomeEmailHtml(String email) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body style="margin:0;padding:0;background-color:#070b17;
                         font-family:'Helvetica Neue',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" border="0"
                style="background-color:#070b17;padding:40px 16px;">
                <tr><td align="center">
                  <table width="560" cellpadding="0" cellspacing="0" border="0"
                    style="background-color:#0d1526;border-radius:16px;
                           border:1px solid rgba(201,168,76,0.25);
                           box-shadow:0 8px 32px rgba(0,0,0,0.5);">
                    <tr><td style="height:3px;
                      background:linear-gradient(90deg,transparent,#c9a84c,transparent);
                      border-radius:16px 16px 0 0;"></td></tr>
                    <tr><td align="center" style="padding:40px 40px 24px;">
                      <p style="margin:0 0 8px;font-size:11px;letter-spacing:3px;
                                text-transform:uppercase;color:rgba(255,255,255,0.3);">
                        WELCOME ABOARD
                      </p>
                      <h1 style="margin:0;font-size:26px;color:#e8d5a3;
                                 letter-spacing:1px;font-weight:600;">
                        Expense Tracker
                      </h1>
                      <div style="width:40px;height:2px;
                        background:linear-gradient(90deg,transparent,#c9a84c,transparent);
                        margin:16px auto 0;"></div>
                    </td></tr>
                    <tr><td style="padding:0 40px 32px;">
                      <p style="margin:0 0 16px;font-size:15px;
                                color:rgba(255,255,255,0.75);line-height:1.7;">
                        Hi there,
                      </p>
                      <p style="margin:0 0 16px;font-size:15px;
                                color:rgba(255,255,255,0.75);line-height:1.7;">
                        Your account has been successfully created for
                        <strong style="color:#e8d5a3;">%s</strong>.
                        You can now start tracking your income and expenses.
                      </p>
                      <table width="100%%" cellpadding="0" cellspacing="0"
                        style="margin:24px 0;">
                        <tr>
                          <td width="33%%" align="center">
                            <div style="background:rgba(45,212,160,0.1);
                                        border:1px solid rgba(45,212,160,0.25);
                                        border-radius:10px;padding:16px 8px;">
                              <div style="font-size:20px;margin-bottom:6px;">📈</div>
                              <div style="font-size:11px;color:#2dd4a0;
                                          letter-spacing:1px;font-weight:600;">TRACK</div>
                            </div>
                          </td>
                          <td width="4%%"></td>
                          <td width="33%%" align="center">
                            <div style="background:rgba(201,168,76,0.1);
                                        border:1px solid rgba(201,168,76,0.25);
                                        border-radius:10px;padding:16px 8px;">
                              <div style="font-size:20px;margin-bottom:6px;">📊</div>
                              <div style="font-size:11px;color:#c9a84c;
                                          letter-spacing:1px;font-weight:600;">ANALYZE</div>
                            </div>
                          </td>
                          <td width="4%%"></td>
                          <td width="33%%" align="center">
                            <div style="background:rgba(91,143,212,0.1);
                                        border:1px solid rgba(91,143,212,0.25);
                                        border-radius:10px;padding:16px 8px;">
                              <div style="font-size:20px;margin-bottom:6px;">💡</div>
                              <div style="font-size:11px;color:#85b7eb;
                                          letter-spacing:1px;font-weight:600;">GROW</div>
                            </div>
                          </td>
                        </tr>
                      </table>
                      <table width="100%%" cellpadding="0" cellspacing="0"
                        style="margin:24px 0;">
                        <tr><td align="center">
                          <a href="https://expense-manager-em.netlify.app/login"
                            style="display:inline-block;
                                   background:linear-gradient(135deg,#b8932e,#e8d5a3,#b8932e);
                                   color:#0a0e1a;text-decoration:none;font-weight:700;
                                   font-size:13px;letter-spacing:1.5px;
                                   padding:14px 40px;border-radius:10px;">
                            LOGIN TO YOUR ACCOUNT
                          </a>
                        </td></tr>
                      </table>
                      <p style="margin:0;font-size:13px;
                                color:rgba(255,255,255,0.3);line-height:1.7;">
                        If you did not create this account, ignore this email.
                      </p>
                    </td></tr>
                    <tr><td style="padding:20px 40px;
                                   border-top:1px solid rgba(255,255,255,0.07);">
                      <p style="margin:0;font-size:11px;color:rgba(255,255,255,0.2);
                                text-align:center;">
                        © 2026 Expense Tracker · Built with ♥
                      </p>
                    </td></tr>
                    <tr><td style="height:2px;
                      background:linear-gradient(90deg,transparent,rgba(201,168,76,0.4),transparent);
                      border-radius:0 0 16px 16px;"></td></tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(email);
    }

    private String buildOtpEmailHtml(String email, String otp) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head><meta charset="UTF-8"/></head>
            <body style="margin:0;padding:0;background-color:#070b17;
                         font-family:'Helvetica Neue',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" border="0"
                style="background-color:#070b17;padding:40px 16px;">
                <tr><td align="center">
                  <table width="560" cellpadding="0" cellspacing="0" border="0"
                    style="background-color:#0d1526;border-radius:16px;
                           border:1px solid rgba(201,168,76,0.25);">
                    <tr><td style="height:3px;
                      background:linear-gradient(90deg,transparent,#c9a84c,transparent);
                      border-radius:16px 16px 0 0;"></td></tr>
                    <tr><td align="center" style="padding:40px 40px 24px;">
                      <p style="margin:0 0 8px;font-size:11px;letter-spacing:3px;
                                text-transform:uppercase;color:rgba(255,255,255,0.3);">
                        PASSWORD RESET
                      </p>
                      <h1 style="margin:0;font-size:26px;color:#e8d5a3;font-weight:600;">
                        Expense Tracker
                      </h1>
                      <div style="width:40px;height:2px;
                        background:linear-gradient(90deg,transparent,#c9a84c,transparent);
                        margin:16px auto 0;"></div>
                    </td></tr>
                    <tr><td style="padding:0 40px 32px;">
                      <p style="margin:0 0 16px;font-size:15px;
                                color:rgba(255,255,255,0.75);line-height:1.7;">
                        Hi there,
                      </p>
                      <p style="margin:0 0 24px;font-size:15px;
                                color:rgba(255,255,255,0.75);line-height:1.7;">
                        We received a password reset request for
                        <strong style="color:#e8d5a3;">%s</strong>.
                        Use the OTP below — it expires in
                        <strong style="color:#c9a84c;">10 minutes</strong>.
                      </p>
                      <table width="100%%" cellpadding="0" cellspacing="0"
                        style="margin:0 0 24px;">
                        <tr><td align="center">
                          <table cellpadding="0" cellspacing="0" border="0">
                            <tr><td style="background:rgba(201,168,76,0.1);
                                           border:1px solid rgba(201,168,76,0.35);
                                           border-radius:12px;padding:24px 48px;
                                           text-align:center;">
                              <p style="margin:0 0 8px;font-size:11px;letter-spacing:3px;
                                         color:rgba(255,255,255,0.3);text-transform:uppercase;">
                                Your OTP
                              </p>
                              <p style="margin:0;font-size:42px;font-weight:700;
                                         letter-spacing:12px;color:#e8d5a3;
                                         font-family:'Courier New',monospace;">
                                %s
                              </p>
                            </td></tr>
                          </table>
                        </td></tr>
                      </table>
                      <p style="margin:0;font-size:13px;
                                color:rgba(255,255,255,0.3);line-height:1.7;">
                        If you did not request this, ignore this email.
                        Your password will remain unchanged.
                      </p>
                    </td></tr>
                    <tr><td style="padding:20px 40px;
                                   border-top:1px solid rgba(255,255,255,0.07);">
                      <p style="margin:0;font-size:11px;color:rgba(255,255,255,0.2);
                                text-align:center;">
                        © 2026 Expense Tracker · OTP expires in 10 minutes
                      </p>
                    </td></tr>
                    <tr><td style="height:2px;
                      background:linear-gradient(90deg,transparent,rgba(201,168,76,0.4),transparent);
                      border-radius:0 0 16px 16px;"></td></tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(email, otp);
    }
}