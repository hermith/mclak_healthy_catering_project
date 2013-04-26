
package image;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;

/**
 *
 * @author Maria
 */

@Named
public class ImageBean {
    private List<String> images;  
  
    public ImageBean(){  
        images = new ArrayList<String>();  
        images.add("beef.jpg");  
        images.add("fish.jpg");  
        images.add("greek.jpg");
        images.add("chicken.jpg");
        images.add("vegetables.jpg"); 
    }  
  
    public List<String> getImages() {  
        return images;  
    }  
}
