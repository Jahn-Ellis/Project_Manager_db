package task8;

public class ContactDetailsCustomer {

    // Attributes
    static String name;
    static String surname;
    static int contact;
    static String email;
    static String address;

    public ContactDetailsCustomer(String name, String surname, int contact, String email, String address) {
        ContactDetailsCustomer.name = name;
        ContactDetailsCustomer.surname = surname;
        ContactDetailsCustomer.contact = contact;
        ContactDetailsCustomer.email = email;
        ContactDetailsCustomer.address = address;
    }

    public static String getSurname() {
        return surname;
    }

    public static String getName() {
        return name;
    }

    public static int getContact() {
        return contact;
    }

    public static String getEmail() {
        return email;
    }

    public static String getAddress() {
        return address;
    }

    public String toString() {
        String output = "Customer: " + name + " " + surname;
        output += "\nContact: " + contact;
        output += "\nEmail: " + email;
        output += "\nAddress: " + address;

        return output;
    }

}