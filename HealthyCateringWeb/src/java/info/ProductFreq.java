/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

/**
 *
 * @author aleksalr
 */
public class ProductFreq {

    int id;
    int frequency;

    public ProductFreq(int id, int frequency) {
        this.id = id;
        this.frequency = frequency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    
    
}
