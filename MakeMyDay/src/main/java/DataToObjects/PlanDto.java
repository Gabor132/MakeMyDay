/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataToObjects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dragos
 */
public class PlanDto extends DataDto implements Serializable {
    public Long id;
    public Long user;
    public List<EventDto> events;
    public Date day;
}
