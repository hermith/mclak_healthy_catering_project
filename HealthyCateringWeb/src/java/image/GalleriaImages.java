
package image;

/**
 * This file is about each images
 *
 * @author Maria
 */
public class GalleriaImages {
    
    public String name;
    public String path;
    
    public GalleriaImages(String name, String path){
        this.name = name;
        this.path = path;
    }
    
    // Getters and setters
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getPath(){
        return path;
    }
    
    public void setPath(String path){
        this.path=path;
    }
    
    
}
