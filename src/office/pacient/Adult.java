package office.pacient;

public class Adult extends Patient{

    private boolean healthInsurance;

    public Adult(String LastName, String FirstName, int BirthYear, String CNP, String Tel, boolean HealthInsurance) {
        super(LastName, FirstName, BirthYear, CNP, Tel);
        healthInsurance = HealthInsurance;
    }

    @Override
    public void displayPatient() {
        System.out.println("Name: " + lastName + " " + firstName + " -> Insurance: " + healthInsurance);
    }
}