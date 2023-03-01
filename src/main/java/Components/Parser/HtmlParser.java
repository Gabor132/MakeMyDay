/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Parser;

import Components.Services.EventService;
import Components.Services.KeyWordService;
import Components.Services.PropertiesService;
import Components.Services.SiteService;
import Entities.Event;
import Entities.ItemClass;
import Entities.ItemTemplate;
import Entities.SiteTemplate;
import Enums.EventUrlType;
import Enums.ValueType;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * This class takes a SiteTemplate and returnes the events it finds on the website based on the ItemTemplate-ItemClasses
 * defined in the SiteTemplate
 *
 * @author Dragos
 */
@Component
@Service("htmlParser")
public class HtmlParser {

    private final static String DATES_IN_ROMAINIAN[] = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie",
            "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie", "Mâine", "Poimâine", "Luni",
            "Marți", "Miercuri", "Joi", "Vineri", "Sâmbată", "Duminică", "Maine", "Poimaine", "Luni", "Marti",
            "Miercuri", "Joi", "Vineri", "Sambata", "Duminica" };
    private final static String DATES_IN_ENGLISH[] = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "Octomber", "November", "December", "Tomorrow", "After tomorrow", "Monday",
            "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Tomorrow", "After tomorrow", "Monday",
            "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

    @Autowired
    public SiteService siteService;

    @Autowired
    public EventService eventService;

    @Autowired
    public KeyWordService keyWordService;

    public HtmlParser() {
        PROPS = PropertiesService.getPropertiesFrom(PropertiesService.PropertiesPath.PHANTOMJS_PROPERTIES);
    }

    private final Properties PROPS;

    public void scheduledSearch() {
        Logger.getLogger(HtmlParser.class.getName()).log(Level.INFO, "Started cleaning up events");
        eventService.deleteAllExpiredEvents();
        Logger.getLogger(HtmlParser.class.getName()).log(Level.INFO, "Finished cleaning up events");
        Logger.getLogger(HtmlParser.class.getName()).log(Level.INFO, "Started parsing events");
        List<SiteTemplate> siteList = siteService.getAllSites();
        List<Event> eventList = new LinkedList<>();
        for (SiteTemplate site : siteList) {
            eventList.addAll(parse(site));
        }
        Logger.getLogger(HtmlParser.class.getName()).log(Level.INFO, "Finished parsing events");
        eventService.determineEventTypes(eventList);
        eventService.saveEvents(eventList);
        keyWordService.updatePercentages();
    }

    public List<Event> parse(SiteTemplate site) {
        List<Event> listEvents = new LinkedList<>();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(Boolean.parseBoolean(PROPS.getProperty("javascript_enabled")));
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                PROPS.getProperty("driver_path"));
        PhantomJSDriverService driverService = new PhantomJSDriverService.Builder()
                .usingPhantomJSExecutable(new File(PROPS.getProperty("driver_path"))).build();
        try {
            if (!driverService.isRunning()) {
                driverService.start();
            }
            RemoteWebDriver driver = new RemoteWebDriver(driverService.getUrl(), capabilities);
            String url = site.getLink();
            driver.get(url);
            Document doc = Jsoup.parse(driver.getPageSource());
            Elements el = doc.select(site.getItemTemplate().getName());
            for (Element e : el) {
                Event event = constructEvent(e, site.getItemTemplate());
                event.setHostSite(site);
                event.setType(site.getContentType());
                listEvents.add(event);
            }
            driver.close();
            if (driverService.isRunning()) {
                driverService.stop();
            }
        } catch (IOException | ScreenshotException | NoSuchWindowException ex) {
            Logger.getLogger(HtmlParser.class.getName()).log(Level.SEVERE, null, ex);
            if (driverService.isRunning()) {
                driverService.stop();
            }
            return listEvents;
        }
        eventService.replaceDiacritics(listEvents);
        return listEvents;
    }

    /**
     * This function receives an Element from the JSoup library and the ItemTemplate of the Site and returns an event
     * completed with all the information it could find
     *
     * @param jsoupElement
     * @param item
     *
     * @return {Event}
     */
    public Event constructEvent(Element jsoupElement, ItemTemplate item) {
        Event event = new Event();
        LinkedList<ItemClass> queue = new LinkedList<>();
        for (ItemClass ic : item.getClasses()) {
            queue.add(ic);
        }
        while (!queue.isEmpty()) {
            ItemClass currentIC = queue.pop();
            for (ItemClass ic : currentIC.getChildren()) {
                queue.add(ic);
            }
            switch (currentIC.getClassType()) {
            case NAME: {
                String name;
                if (currentIC.getValueType().equals(ValueType.VALUE)) {
                    name = jsoupElement.select(currentIC.getClassName()).text();
                } else {
                    name = jsoupElement.select(currentIC.getClassName()).attr(currentIC.getValueLocation());
                }
                event.setName(name);
            }
                break;
            case IMAGE: {
                String imageUrl;
                if (currentIC.getValueType().equals(ValueType.VALUE)) {
                    imageUrl = jsoupElement.select(currentIC.getClassName()).text();
                } else {
                    imageUrl = jsoupElement.select(currentIC.getClassName()).attr(currentIC.getValueLocation());
                }
                event.setImageUrl(imageUrl);
            }
                break;
            case DESCRIPTION: {
                String description;
                if (currentIC.getValueType().equals(ValueType.VALUE)) {
                    description = jsoupElement.select(currentIC.getClassName()).text();
                } else {
                    description = jsoupElement.select(currentIC.getClassName()).attr(currentIC.getValueLocation());
                }
                event.setDescription(description);
            }
                break;
            case LOCATION: {
                String address;
                if (currentIC.getValueType().equals(ValueType.VALUE)) {
                    address = jsoupElement.select(currentIC.getClassName()).text();
                } else {
                    address = jsoupElement.select(currentIC.getClassName()).attr(currentIC.getValueLocation());
                }
                event.setAddress(address);
            }
                break;
            case DATE: {
                String date;
                if (currentIC.getValueType().equals(ValueType.VALUE)) {
                    date = jsoupElement.select(currentIC.getClassName()).text();
                } else {
                    date = jsoupElement.select(currentIC.getClassName()).attr(currentIC.getValueLocation());
                }
                String dateFormat;
                if (item.getHostSite().getDateFormat().isEmpty()) {
                    dateFormat = "yyyy-MM-dd HH:mm:ss";
                } else {
                    dateFormat = item.getHostSite().getDateFormat();
                }
                SimpleDateFormat simple = new SimpleDateFormat(dateFormat);
                try {
                    event.setDate(simple.parse(date.substring(0, Math.min(dateFormat.length(), date.length()))));
                } catch (ParseException ex) {
                    Logger.getLogger(HtmlParser.class.getName()).log(Level.WARNING, "Failed to parse the date " + date);
                    Date parsedDate = parseUnparsableDateFormat(date, dateFormat);
                    event.setDate(parsedDate);
                }
            }
                break;
            case PRICE: {
                String price;
                if (currentIC.getValueType().equals(ValueType.VALUE)) {
                    price = jsoupElement.select(currentIC.getClassName()).text();
                } else {
                    price = jsoupElement.select(currentIC.getClassName()).attr(currentIC.getValueLocation());
                }
                event.setPrice(price);
            }
                break;
            case TYPE: {

            }
                break;
            case URL: {
                String link = "";
                if (currentIC.getValueType().equals(ValueType.VALUE)) {
                    link += jsoupElement.select(currentIC.getClassName()).text();
                } else {
                    link += jsoupElement.select(currentIC.getClassName()).attr(currentIC.getValueLocation());
                }
                if (item.getHostSite().getEventUrlType() == EventUrlType.CONCATENATION) {
                    if (link.startsWith("/")) {
                        link = link.substring(1, link.length());
                    }
                    link = item.getHostSite().getLink() + link;
                }
                event.setLink(link);
            }
                break;
            case OTHER: {

            }
            }
        }
        return event;
    }

    public Date parseUnparsableDateFormat(String date, String pattern) {
        String translatedDate = translateDate(date, pattern);
        System.out.println("Translated date: " + translatedDate);
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(translatedDate);
        Date parsedDate = Calendar.getInstance().getTime();
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
            if (!dates.isEmpty()) {
                parsedDate = dates.get(0);
            }
        }
        return parsedDate;
    }

    private String translateDate(String date, String pattern) {
        String finalDate;
        String splitter = " ";
        for (char c : pattern.toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                splitter = "" + c;
                break;
            }
        }
        String[] dates = date.split(splitter);
        boolean foundTranslation = false;
        for (int i = 0; i < dates.length && !foundTranslation; i++) {
            String piece = dates[i];
            if (!piece.isEmpty() && Character.isAlphabetic(piece.charAt(0))) {
                String newPiece = piece.toUpperCase();
                for (int j = 0; j < DATES_IN_ROMAINIAN.length; j++) {
                    String romanianMonth = DATES_IN_ROMAINIAN[j];
                    String oldMonth = romanianMonth.toUpperCase().substring(0,
                            Math.min(romanianMonth.length(), newPiece.length()));
                    if (oldMonth.equals(newPiece)) {
                        String newMonth = DATES_IN_ENGLISH[j].toUpperCase().substring(0,
                                Math.min(DATES_IN_ENGLISH[j].length(), newPiece.length()));
                        dates[i] = newMonth;
                        foundTranslation = true;
                        break;
                    }
                }
            }
        }
        finalDate = dates[0];
        for (int i = 1; i < dates.length; i++) {
            finalDate += splitter + dates[i];
        }
        return finalDate;
    }

}
