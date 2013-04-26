
package image;

/**
 *
 * @author Maria
 */

import java.util.ArrayList;  
import java.util.List;  
import javax.annotation.PostConstruct;  
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
public class GalleriaBean {
    private List<String> images;  
    
    @PostConstruct  
    public void init() {  
        images = new ArrayList<String>();  
   
        images.add("food1.jpg");
        images.add("food2.jpg");
        images.add("food3.jpg");
        images.add("food4.jpg");
        images.add("food5.jpg");
    }
    
    public List<String> getImages() {  
        return images;  
    } 
}
