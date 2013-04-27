package shopping.customer;

/**
 * Storage object for Customers.
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

    /**
     * Standard empty constructor
     */
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
     * @param companyId Customer ID
     * @param email Customer email
     * @param address Customer address
     * @param phoneNumber Customer phone number
     * @param zipCode Customer zip-code
     * @param city Customer city
     */
    public Customer(int customerId, String email, String address, String phoneNumber, int zipCode, String city) {
        this.customerId = customerId;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.city = city;
    }

    /**
     * Will return the combined name of the private customer or the company
     * name.
     *
     * @return Customer name
     */
    public abstract String getName();

    /**
     * A "toString" method used in the drivers UI, to give relevant customer
     * information to the drivers.
     *
     * @return
     */
    public String getDriverInformation() {
        return getName().toUpperCase() + ", " + address + ", " + city + " " + zipCode + ", " + phoneNumber;
    }

    /**
     * Standard generated toString
     * 
     * @return All variables as string
     */
    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId + ", email=" + email + ", address=" + address + ", phoneNumber=" + phoneNumber + ", zipCode=" + zipCode + ", city=" + city + '}';
    }

    // GETTERS AND SETTERS
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
}