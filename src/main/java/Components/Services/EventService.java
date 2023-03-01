/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Auxiliars.Pair;
import Components.Repositories.EventDao;
import Entities.Event;
import Entities.EventType;
import Entities.EventTypeWord;
import Enums.Messages.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service("eventService")
public class EventService {

    @Autowired
    public EventDao eventDao;

    @Autowired
    public EventTypeService eventTypeService;

    private static final HashMap<Character, Character> DIACRITICS_MAP;

    static {
        DIACRITICS_MAP = new HashMap<>();
        DIACRITICS_MAP.put('ă', 'a');
        DIACRITICS_MAP.put('â', 'a');
        DIACRITICS_MAP.put('î', 'i');
        DIACRITICS_MAP.put('ș', 's');
        DIACRITICS_MAP.put('ț', 't');
        DIACRITICS_MAP.put('Ă', 'A');
        DIACRITICS_MAP.put('Â', 'A');
        DIACRITICS_MAP.put('Î', 'I');
        DIACRITICS_MAP.put('Ș', 'S');
        DIACRITICS_MAP.put('Ț', 'T');
    }

    public List<Event> getEvents(int day, int month, int year, int start, int finish, String type) {
        EventType typeEvent;
        try {
            typeEvent = eventTypeService.getEventType(type);
        } catch (IllegalArgumentException ex) {
            typeEvent = null;
        }
        Calendar auxDate = Calendar.getInstance();
        auxDate.set(year, month, day);
        auxDate.set(Calendar.MINUTE, 0);
        auxDate.set(Calendar.SECOND, 0);
        auxDate.set(Calendar.MILLISECOND, 0);
        auxDate.set(Calendar.HOUR_OF_DAY, start);
        Date startD = auxDate.getTime();
        auxDate.set(Calendar.HOUR_OF_DAY, finish);
        Date finishD = auxDate.getTime();
        List<Event> events = eventDao.getEventByFilter(typeEvent, startD, finishD);
        return events;
    }

    public List<Event> getEventsToday(String type) {
        EventType typeEvent;
        List<Event> events;
        try {
            Calendar today = Calendar.getInstance();
            Date startD = today.getTime();
            today.add(Calendar.DAY_OF_MONTH, 1);
            Date finishD = today.getTime();
            typeEvent = eventTypeService.getEventType(type);
            events = eventDao.getEventByFilter(typeEvent, startD, finishD);
        } catch (IllegalArgumentException ex) {
            events = eventDao.getAllEvents().subList(0, 10);
        }
        return events;
    }

    public List<Event> getEvents(String type) {
        EventType typeEvent;
        List<Event> events;
        try {
            typeEvent = eventTypeService.getEventType(type);
            events = eventDao.getEventByType(typeEvent);
        } catch (IllegalArgumentException ex) {
            events = eventDao.getAllEvents();
        }
        return events;
    }

    public Event getEventById(Long id) {
        return (Event) eventDao.getById(id);
    }

    public Event getEventByName(String name) {
        return (Event) eventDao.getEventByName(name);
    }

    public Event getEventByType(String type) {
        return (Event) eventDao.getEventByType(eventTypeService.getEventType(type));
    }

    public List<Event> getAllEvents() {
        return eventDao.getAllEvents();
    }

    public List<Event> getAllDeterminedEvents() {
        return eventDao.getAllDetermentEvents();
    }

    public boolean saveEvents(List<Event> events) {
        for (Event event : events) {
            if (event.isUsable()) {
                eventDao.save(event);
            }
        }
        return true;
    }

    public ArrayList<List<Event>> groupEventsByDate(List<Event> events) {
        events.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        ArrayList<List<Event>> eventGroups = new ArrayList<>();
        ArrayList<Event> groupEvent = new ArrayList<>();
        for (int index = 0; index < events.size() - 1; index++) {
            if (events.get(index).getDate().compareTo(events.get(index + 1).getDate()) == 0) {
                groupEvent.add(events.get(index));
            } else {
                groupEvent.add(events.get(index));
                eventGroups.add((ArrayList<Event>) groupEvent.clone());
                groupEvent.clear();
            }
        }
        if (events.size() > 0) {
            groupEvent.add(events.get(events.size() - 1));
            eventGroups.add((ArrayList<Event>) groupEvent.clone());
        }
        return eventGroups;
    }

    public boolean deleteAllEvents() {
        return eventDao.deleteAllEvents();
    }

    public boolean deleteAllExpiredEvents() {
        return eventDao.deleteAllExpiredEvents();
    }

    public Response deleteEvent(Long id) {
        boolean succes = eventDao.delete(Event.class, id);
        if (succes) {
            return Response.SUCCESFULL_DELETE;
        }
        return Response.UNSUCCESFULL_DELETE;
    }

    public void determineEventTypes(List<Event> events) {
        Logger.getLogger(EventService.class.getTypeName()).log(Level.INFO, "Started event type determination");
        List<EventType> eventTypes = eventTypeService.getAllEventTypes();
        List<Event> filteredEvents = new LinkedList<>();
        for (Event e : events) {
            if (e.getType().getType().equals("ALTELE"))
                filteredEvents.add(e);
        }
        for (Event event : filteredEvents) {
            determineEventType(event, eventTypes);
        }
        Logger.getLogger(EventService.class.getTypeName()).log(Level.INFO, "Finished event type determination");
    }

    private void determineEventType(Event event, List<EventType> eventTypes) {
        HashMap<EventType, Pair<Integer, Double>> percentageSum = new HashMap<>();
        for (EventType type : eventTypes) {
            for (EventTypeWord etw : type.getPercentages()) {
                String word = etw.getWord().getWord();
                double percentage = etw.getPercentage();
                if (stringContainsWord(event.getContent(), word)) {
                    if (percentageSum.containsKey(type)) {
                        Integer numSum = percentageSum.get(type).getKey() + 1;
                        Double per = percentageSum.get(type).getValue() + percentage;
                        percentageSum.put(type, new Pair<>(numSum, per));
                    } else {
                        percentageSum.put(type, new Pair<>(1, percentage));
                    }
                }
            }
        }
        EventType bestType = null;
        double max = 0.0;
        Random random = new Random();
        for (EventType type : percentageSum.keySet()) {
            double percentage = percentageSum.get(type).getValue() / percentageSum.get(type).getKey();
            if (percentage > max) {
                bestType = type;
                max = percentage;
            } else if (percentage == max) {
                if (random.nextBoolean()) {
                    bestType = type;
                }
            }
        }
        if (max != 0) {
            event.setType(bestType);
            Logger.getLogger(EventService.class.getTypeName()).log(Level.INFO, "Event {0} type is changed to {1}",
                    new Object[] { event.getName(), bestType.getType() });
        } else {
            Logger.getLogger(EventService.class.getTypeName()).log(Level.INFO, "Event {0} type is left to ALTELE",
                    event.getName());
        }
    }

    public boolean eventContainsWord(Event event, String word) {
        return stringContainsWord(event.getContent(), word);
    }

    private boolean stringContainsWord(String eventContent, String word) {
        Pattern pattern = Pattern.compile("^(.*?(\\b" + word + "\\b)[^$]*)$");
        Matcher matcher = pattern.matcher(eventContent);
        return matcher.find();
    }

    /**
     * This function replaces all the diacritics of an event with there proper characters (ă = a)
     *
     * @param events
     */
    public void replaceDiacritics(List<Event> events) {
        for (Event event : events) {
            event.setName(replaceDiacritics(event.getName()));
            event.setAddress(replaceDiacritics(event.getAddress()));
            event.setDescription(replaceDiacritics(event.getDescription()));
            event.setPrice(replaceDiacritics(event.getPrice()));
        }
    }

    private String replaceDiacritics(String string) {
        if (string == null)
            return "";
        for (Character diacritic : DIACRITICS_MAP.keySet()) {
            string = string.replaceAll(diacritic.toString(), DIACRITICS_MAP.get(diacritic).toString());
        }
        return string;
    }
}
