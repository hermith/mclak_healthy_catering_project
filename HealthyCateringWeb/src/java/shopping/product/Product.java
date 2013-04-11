/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping;

/**
 *
 * @author aleksalr
 */
public class Item {
    
    private int id;
    private String name;
    private double price;
    
    public Item(int id, String name, double price){
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public double getPrice(){
        return price;
    }
}
