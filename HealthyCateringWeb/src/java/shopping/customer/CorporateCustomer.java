package shopping.customer;

/**
 * A simple storage object that extends the super object Customer.
 *
 * @author linnk
 */
public class CorporateCustomer extends Customer {

    private String companyName;

    /**
     * Standard empty constructor.
     */
    public CorporateCustomer() {
        super();
        this.companyName = null;
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
     * @param companyName Customer company name
     */
    public CorporateCustomer(int customerId, String email, String address, String phoneNumber, int zipCode, String city, String companyName) {
        super(customerId, email, address, phoneNumber, zipCode, city);
        this.companyName = companyName;
    }

    /**
     * Returns the company name
     * 
     * @return Company name
     */
    @Override
    public String getName() {
        return companyName;
    }

    /**
     * Standard generated toString method
     * 
     * @return All variables as a string.
     */
    @Override
    public String toString() {
        return "CorporateCustomer{" + "companyName=" + companyName + ", " + super.toString() + '}';
    }

    // GETTERS AND SETTERS
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}