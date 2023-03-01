/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataToObjects;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Dragos
 */
public class ItemClassDto extends DataDto implements Serializable {
    public Long id;
    public String className;
    public String classType;
    public String valueType;
    public String valueLocation;
    public Long hostItem;
    public Set<ItemClassDto> children;
    public Long parent;
}
