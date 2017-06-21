/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Components.Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.jboss.logging.Logger;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Dragos
 */
public class PropertiesService {
    
    public enum PropertiesPath{
        MAIL_PROPERTIES("mail.properties"),
        PHANTOMJS_PROPERTIES("phantomJS.properties");
        private final String PATH;
        private PropertiesPath(String path){
            this.PATH = path;
        }
        public String getPath(){
            return this.PATH;
        }
    }
    
    public static Properties getPropertiesFrom(PropertiesPath fileName){
        Properties prop = new Properties();
	InputStream input = null;
	try {
            input = new FileInputStream((new ClassPathResource(fileName.PATH)).getFile());
            prop.load(input);
	} catch (IOException io) {
            Logger.getLogger(PropertiesService.class.getName()).log(Logger.Level.FATAL, io.getMessage());        
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Logger.getLogger(PropertiesService.class.getName()).log(Logger.Level.FATAL, e.getMessage());
                }
            }
	}
        return prop;
    }
    
}
