/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

/**
 *
 * @author aleksalr
 */
public class Customer {
    
    String email;
    String address;
    int phone_number;
    int zip_code;
    String city;
    
    /**
     * Constructor for creating new customer with appropriate information.
     * @param email
     * @param address
     * @param phone_number
     * @param zip_code
     * @param city 
     */

    public Customer(String email, String address, int phone_number, int zip_code, String city) {
        this.email = email;
        this.address = address;
        this.phone_number = phone_number;
        this.zip_code = zip_code;
        this.city = city;
    }
}