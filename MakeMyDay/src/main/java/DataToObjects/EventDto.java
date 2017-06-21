/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataToObjects;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Dragos
 */
public class EventDto extends DataDto implements Serializable{
    
    public Long id;
    public String name;
    public String type;
    public String imageUrl;
    public String address;
    public Date date;
    public String hostSite;
    public String price;
    public String description;
    public String link;
    
}
