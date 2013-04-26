/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shopping.customer;

/**
 *
 * @author aleksalr
 */
public abstract class Customer {

    private int customerId;
    private String email;
    private String address;
    private String phoneNumber;
    private int zipCode;
    private String city;

    public Customer() {
        this.customerId = -1;
        this.email = null;
        this.address = null;
        this.phoneNumber = null;
        this.zipCode = -1;
        this.city = null;
    }

    /**
     * Constructor for creating new customer with appropriate information.
     *
     * @param companyId
     * @param email
     * @param address
     * @param phoneNumber
     * @param zipCode
     * @param city
     */
    public Customer(int customerId, String email, String address, String phoneNumber, int zipCode, String city) {
        this.customerId = customerId;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.city = city;
    }
    
    public abstract String getName();

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public String getDriverInformation() {
        return getName().toUpperCase() + ", " + address + ", " + city +  " " + zipCode + ", " + phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", email=" + email + ", address=" + address + ", phoneNumber=" + phoneNumber + ", zipCode=" + zipCode + ", city=" + city + '}';
    }
}