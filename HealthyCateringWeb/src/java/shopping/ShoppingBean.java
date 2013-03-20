/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author aleksalr
 */
@ManagedBean(name = "shopping")
@SessionScoped
public class ShoppingBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Item[] items = new Item[]{
        new Item(1, "Pizza", 90),
        new Item(2, "Coke", 20),
        new Item(3, "Blowjob", 50),
        new Item(4, "Karl", 2.50),
        new Item(5, "Maria", 1.50)
    };

    public Item[] getItems() {
        return items;
    }
}