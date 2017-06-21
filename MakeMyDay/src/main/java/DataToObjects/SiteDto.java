/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataToObjects;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Dragos
 */
public class SiteDto extends DataDto implements Serializable{
    public Long id;
    public String name;
    public String eventUrlType;
    public String contentType;
    public String link;
    public String dateFormat;
    public ItemTemplateDto itemTemplate;
    public List<EventDto> events;
}
