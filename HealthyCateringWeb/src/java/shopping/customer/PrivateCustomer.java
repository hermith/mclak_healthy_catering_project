package shopping.customer;

/**
 * A simple storage object that extends the super object Customer.
 *
 * @author karl
 */
public class PrivateCustomer extends Customer {

    private String firstName;
    private String lastName;

    /**
     * Standard empty constructor.
     */
    public PrivateCustomer() {
        super();
        this.firstName = null;
        this.lastName = null;
    }

    /**
     * Standard constructor for creating object with data.
     *
     * @param customerId Customer ID
     * @param email Customer email
     * @param address Customer adress
     * @param phoneNumber Customer phone number
     * @param zipCode Customer zip-code
     * @param city Customer city
     * @param firstName Customer first name
     * @param lastName Customer last name
     */
    public PrivateCustomer(int customerId, String email, String address, String phoneNumber, int zipCode, String city, String firstName, String lastName) {
        super(customerId, email, address, phoneNumber, zipCode, city);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the name on format "lastname, firstname"
     * 
     * @return Customer name
     */
    @Override
    public String getName() {
        return lastName + ", " + firstName;
    }

    /** 
     * Standard generated toString
     * 
     * @return All variables as string
     */
    @Override
    public String toString() {
        return "PrivateCustomer{" + "firstName=" + firstName + ", lastName=" + lastName + ", " + super.toString() + '}';
    }

    //GETTERS AND SETTERS
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}