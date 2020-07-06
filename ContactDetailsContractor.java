package task8;

public class ContactDetailsContractor {

    // Attributes
    static String name;
    static String surname;
    static int contact;
    static String email;
    static String address;

    public ContactDetailsContractor(String name, String surname, int contact, String email, String address) {
        ContactDetailsContractor.name = name;
        ContactDetailsContractor.surname = surname;
        ContactDetailsContractor.contact = contact;
        ContactDetailsContractor.email = email;
        ContactDetailsContractor.address = address;
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
        String output = "Contractor: " + name + " " + surname;
        output += "\nContact: " + contact;
        output += "\nEmail: " + email;
        output += "\nAddress: " + address;

        return output;
    }
}