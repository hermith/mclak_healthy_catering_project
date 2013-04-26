


package image;

/**
 * This file adds images into a galley
 * 
 * 
 * @author Maria
 */

import java.util.ArrayList;  
import java.util.List;  
import javax.annotation.PostConstruct;  
import javax.inject.Named;

@Named
public class GalleriaBean {
    private List<String> images;  
    
    
    /**
     *  Adds images
     */
    @PostConstruct  
    public void init() {  
        images = new ArrayList<String>();  
   
        images.add("food4.jpg");
        images.add("food2.jpg");
        images.add("food3.jpg");
        images.add("food1.jpg");
        images.add("food5.jpg");
    }
    
    /**
     * Get images
     * @return images
     */
    public List<String> getImages() {  
        return images;  
    } 
}
