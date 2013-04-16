package shopping.customer;

public class PrivateCustomer extends Customer {

    private String firstName;
    private String lastName;

    public PrivateCustomer() {
        super();
        this.firstName = null;
        this.lastName = null;
    }

    public PrivateCustomer(int customerId, String email, String address, String phoneNumber, int zipCode, String city, String firstName, String lastName) {
        super(customerId, email, address, phoneNumber, zipCode, city);
        this.firstName = firstName;
        this.lastName = lastName;
    }

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
    
    @Override
    public String getName() {
        return lastName + ", " + firstName;
    }

    @Override
    public String toString() {
        return "PrivateCustomer{" + "firstName=" + firstName + ", lastName=" + lastName + ", " + super.toString() + '}';
    }
}