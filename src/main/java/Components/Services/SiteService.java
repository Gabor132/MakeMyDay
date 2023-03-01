/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import Components.Repositories.ItemClassDao;
import Components.Repositories.ItemTemplateDao;
import Components.Repositories.SiteDao;
import Entities.ItemClass;
import Entities.ItemTemplate;
import Entities.SiteTemplate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dragos
 */
@Service("siteService")
public class SiteService {

    @Autowired
    public SiteDao siteDao;

    @Autowired
    public ItemTemplateDao itemTemplateDao;

    @Autowired
    public ItemClassDao itemClassDao;

    public SiteTemplate getById(long id) {
        return (SiteTemplate) siteDao.getById(id);
    }

    public List<SiteTemplate> getAllSites() {
        return siteDao.getAllSites();
    }

    public boolean saveSite(SiteTemplate site) {
        siteDao.save(site);
        return true;
    }

    public boolean updateSite(SiteTemplate site) {
        boolean success = true;
        success = success && updateItemTemplate(site.getItemTemplate());
        success = success && siteDao.update(site);
        return success;
    }

    private boolean updateItemTemplate(ItemTemplate item) {
        boolean success = true;
        for (ItemClass classItem : item.getClasses()) {
            success = success && updateItemClass(classItem);
        }
        success = success && itemTemplateDao.update(item);
        return success;
    }

    private boolean updateItemClass(ItemClass itemClass) {
        boolean success = true;
        for (ItemClass classItem : itemClass.getChildren()) {
            success = success && updateItemClass(classItem);
        }

        success = success
                && (itemClass.getId() == null ? itemClassDao.save(itemClass) : itemClassDao.update(itemClass));
        return success;
    }

    public boolean deleteSite(long id) {
        siteDao.delete(SiteTemplate.class, id);
        return true;
    }

}
