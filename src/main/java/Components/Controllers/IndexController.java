/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Controllers;

import Components.Mappers.EventTypeMapper;
import Components.Services.EventTypeService;
import DataToObjects.DataDto;
import DataToObjects.ResponseDto;
import Entities.EventType;
import Enums.Messages.Response;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Dragos
 */
@RestController
public class IndexController {

    @Autowired
    public EventTypeService eventTypeService;

    @Autowired
    public EventTypeMapper eventTypeMapper;

    @RequestMapping(value = "/*")
    public ModelAndView getIndex() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/references", method = RequestMethod.GET)
    public ResponseDto getAllEventTypes() {
        List<DataDto> list = new LinkedList<>();
        for (EventType type : eventTypeService.getAllEventTypes()) {
            list.add(eventTypeMapper.toDto(type));
        }
        return new ResponseDto(Response.REFERENCES_SUCCESS, list);
    }

}
