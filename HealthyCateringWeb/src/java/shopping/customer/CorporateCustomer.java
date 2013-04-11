package shopping.customer;

public class CorporateCustomer extends Customer {
    private String companyName;

    public CorporateCustomer() {
        super();
        this.companyName = null;
    }

    public CorporateCustomer(int customerId, String email, String address, String phoneNumber, int zipCode, String city, String companyName) {
        super(customerId, email, address, phoneNumber, zipCode, city);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "CorporateCustomer{" + "companyName=" + companyName + ", " + super.toString() + '}';
    }
}