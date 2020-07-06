package task8;

public class ContactDetailsArchitect {

    // Attributes
    static String name;
    static String surname;
    static int contact;
    static String email;
    static String address;

    public ContactDetailsArchitect(String name, String surname, int contact, String email, String address) {
        ContactDetailsArchitect.name = name;
        ContactDetailsArchitect.surname = surname;
        ContactDetailsArchitect.contact = contact;
        ContactDetailsArchitect.email = email;
        ContactDetailsArchitect.address = address;
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
        String output = "Architect: " + name + " " + surname;
        output += "\nContact: " + contact;
        output += "\nEmail: " + email;
        output += "\nAddress: " + address;

        return output;
    }
}