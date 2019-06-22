/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Auxiliars.Pair;
import Components.Repositories.EventTypeWordDao;
import Components.Repositories.KeyWordDao;
import Entities.Event;
import Entities.EventType;
import Entities.EventTypeWord;
import Entities.KeyWord;
import java.util.HashMap;
import java.util.List;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service("keyWordService")
public class KeyWordService {
    
    @Autowired
    public EventTypeWordDao eventTypeWordDao;
    
    @Autowired
    public KeyWordDao keyWordDao;
    
    @Autowired
    public EventService eventService;
    
    @Autowired
    public EventTypeService eventTypeService;
    
    public void updatePercentages(){
        
        Logger.getLogger(KeyWordService.class.getTypeName()).log(Logger.Level.INFO, "Started updating percentages");
        List<Event> events = eventService.getAllDeterminedEvents();
        List<KeyWord> keyWords = getAllKeyWords();
        List<EventType> types = eventTypeService.getAllEventTypes();
        HashMap<EventType, Integer> ET_HASH = new HashMap<>();
        HashMap<KeyWord, Integer> EK_HASH = new HashMap<>();
        HashMap<Pair<EventType,KeyWord>, Integer> ETK_HASH = new HashMap<>();
        for(Event event : events){
            for(KeyWord keyWord : keyWords){
                if(eventService.eventContainsWord(event, keyWord.getWord())){
                    if(EK_HASH.containsKey(keyWord)){
                        EK_HASH.put(keyWord, EK_HASH.get(keyWord) + 1);
                    }else{
                        EK_HASH.put(keyWord, 1);
                    }
                    Pair<EventType,KeyWord> pair = new Pair<>(event.getType(),keyWord);
                    if(ETK_HASH.containsKey(pair)){
                        ETK_HASH.put(pair, ETK_HASH.get(pair)+1);
                    }else{
                        ETK_HASH.put(pair, 1);
                    }
                }
            }
            for(EventType type : types){
                if(event.getType().equals(type)){
                    if(ET_HASH.containsKey(event.getType())){
                        ET_HASH.put(event.getType(), ET_HASH.get(event.getType())+1);
                    }else{
                        ET_HASH.put(event.getType(), 1);
                    }
                }
            }
        }
        
        List<EventTypeWord> eventTypeWords = eventTypeWordDao.getAllEventTypeWord();
        
        for(EventTypeWord etw : eventTypeWords){
            double percent = etw.getPercentage();
            Pair<EventType, KeyWord> pair = new Pair<>(etw.getType(), etw.getWord());
            int ETK = ETK_HASH.get(pair)!=null?ETK_HASH.get(pair):0;
            int EK = EK_HASH.get(etw.getWord())!=null?EK_HASH.get(etw.getWord()):0;
            int ET = ET_HASH.get(etw.getType())!=null?ET_HASH.get(etw.getType()):0;
            Logger.getLogger(KeyWordService.class.getTypeName()).log(Logger.Level.INFO,
                    "evenimente de tip("+etw.getType().getType()+"): " + ET + "\n"
                            + "evenimente ce contin("+etw.getWord().getWord()+"): " + EK + "\n"
                            + "intersectia: " + ETK);
            double newPercent = 0;
            if(ETK != 0.0 && ET != 0.0){
                newPercent = ((double)ETK/(double)ET);
            }
            etw.setPercentage(newPercent);
            Logger.getLogger(KeyWordService.class.getTypeName()).log(Logger.Level.INFO,
                    etw.getType().getType() + " - " + etw.getWord().getWord() + " changed from " + percent + " to " + newPercent);
            eventTypeWordDao.update(etw);
        }
        
        Logger.getLogger(KeyWordService.class.getTypeName()).log(Logger.Level.INFO, "Finished updating percentages");
    }
    
    public List<KeyWord> getAllKeyWords(){
        return keyWordDao.getAllKeyWords();
    }
    
}
