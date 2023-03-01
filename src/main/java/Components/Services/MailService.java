package Components.Services;

import Entities.Event;
import Entities.Plan;
import Entities.User;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service("mailService")
public class MailService {

    @Autowired
    public PlanService planService;

    @Autowired
    public SecurityService securityService;

    public enum MailType {
        USER_AUTHENTIFICATION, NEXT_WEEK, NEXT_DAY;
    }

    private final Properties PROPS;
    private final Session SESSION;

    public MailService() {
        PROPS = PropertiesService.getPropertiesFrom(PropertiesService.PropertiesPath.MAIL_PROPERTIES);
        SESSION = Session.getInstance(PROPS, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(PROPS.getProperty("server_email"),
                        PROPS.getProperty("server_password"));
            }
        });
    }

    public void checkForNotifications() {
        Logger.getLogger(MailService.class.getName()).log(Level.INFO, "Started notification check");
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        Calendar nextWeek = Calendar.getInstance();
        nextWeek.add(Calendar.WEEK_OF_MONTH, 1);
        List<Plan> listTomorrow = planService.getPlansByDate(tomorrow);
        List<Plan> listNextWeek = planService.getPlansByDate(nextWeek);
        sendNotifications(listTomorrow, MailType.NEXT_DAY);
        sendNotifications(listNextWeek, MailType.NEXT_WEEK);
        Logger.getLogger(MailService.class.getName()).log(Level.INFO, "Finished notification check");
    }

    public boolean sendNotifications(List<Plan> plans, MailType type) {

        for (Plan plan : plans) {
            try {
                if (plan.getEvents().isEmpty()) {
                    continue;
                }
                Logger.getLogger(MailService.class.getName()).log(Level.INFO, "Preparing email for {0}",
                        plan.getUser().getEmail());
                Message message = new MimeMessage(SESSION);
                message.setFrom(new InternetAddress(PROPS.getProperty("server_email")));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(plan.getUser().getEmail()));

                String contentStart = PROPS.getProperty(type.name() + ".content_start");
                String contentFinish = PROPS.getProperty(type.name() + ".content_finish");
                for (Event event : plan.getEvents()) {
                    contentStart += "<br><a href=\"" + event.getLink() + "\">" + event.getName() + "</a>";
                }
                contentStart += "<br>" + contentFinish;

                message.setSubject(PROPS.getProperty(type.name() + ".subject"));
                message.setContent(contentStart, "text/html; charset=utf-8");
                Transport.send(message);
                Logger.getLogger(MailService.class.getName()).log(Level.INFO, "Finished sending email");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public boolean sendConfirmation(User user) {
        try {
            Logger.getLogger(MailService.class.getName()).log(Level.INFO, "Preparing email for {0}", user.getEmail());
            Message message = new MimeMessage(SESSION);
            message.setFrom(new InternetAddress(PROPS.getProperty("server_email")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));

            String contentStart = PROPS.getProperty(MailType.USER_AUTHENTIFICATION.name() + ".content");
            String link = new String(securityService.hashPassword(user.getEmail()));
            try {
                link = URLEncoder.encode(link, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
            }
            String linkConfirmare = "<a href=\"" + PROPS.getProperty("server_url") + "?link=" + link + "\">link</a>";

            message.setSubject(PROPS.getProperty(MailType.USER_AUTHENTIFICATION.name() + ".subject"));
            message.setContent(contentStart + ": " + linkConfirmare, "text/html; charset=utf-8");
            Transport.send(message);
            Logger.getLogger(MailService.class.getName()).log(Level.INFO, "Finished sending email");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
