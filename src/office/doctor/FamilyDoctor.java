package office.doctor;

import java.time.LocalDateTime;

public class FamilyDoctor extends Doctor {

    private int noOfFamilies;

    public FamilyDoctor(String LastName, String FirstName, String Email, int HireYear, int NoOfFamilies) {
        super(LastName, FirstName, Email, HireYear);
        noOfFamilies = NoOfFamilies;
    }

    public int getNoOfFamilies() {
        return noOfFamilies;
    }

    public void setNoOfFamilies(int noOfFamilies) {
        this.noOfFamilies = noOfFamilies;
    }

    @Override
    public int computeSalary() {
        LocalDateTime time = LocalDateTime.now();
        return 1000 * (time.getYear() - hireYear) + 5 * noOfFamilies;
    }

    @Override
    public void displayDoctor() {
        System.out.println("\tDoctor " + lastName + " " + firstName + " (" + email + ") has been working here since "
                + hireYear + " as a family doctor.");
    }

    public void show() {
        super.show();
        System.out.println("\tNumber of families: " + noOfFamilies);
    }
}
