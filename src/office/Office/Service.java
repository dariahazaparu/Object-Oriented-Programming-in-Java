package office.Office;

import office.Appointment.Appointment;
import office.doctor.Doctor;
import office.doctor.FamilyDoctor;
import office.doctor.Nurse;
import office.doctor.Pediatrician;
import office.pacient.Adult;
import office.pacient.Child;
import office.pacient.Patient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Service {
    public static Service serviceInstance = null;

    private ArrayList<Patient> patients = new ArrayList<>();
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();


    private Service() {}

    public static Service getInstance() {
        if (serviceInstance == null) {
            serviceInstance = new Service();
        }

        return serviceInstance;
    }

    public void addDoctor() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.println("Doctor's personal data");
        System.out.print("\tLast name: ");
        String lastname = scanner.next();
        System.out.print("\tFirst name: ");
        String firstname = scanner.next();
        System.out.print("\tEmail: ");
        String email = scanner.next();
        System.out.print("\tHire year: ");
        int hire = scanner.nextInt();
        System.out.print("\tIs he/she a family doctor (1), nurse(2) or a pediatrician(3)?");
        int typeOfDoctor = scanner.nextInt();
        if (typeOfDoctor == 1) {
            System.out.print("\tHow many families does he/she has?");
            int nrFamilies = scanner.nextInt();
            Doctor familyDoctor = new FamilyDoctor(lastname, firstname, email, hire, nrFamilies);
            doctors.add(familyDoctor);
        } else if (typeOfDoctor == 2) {
            System.out.print("\tHow many hours does he/she work in a week?");
            int nrHours = scanner.nextInt();
            Doctor nurse = new Nurse(lastname, firstname, email, hire, nrHours);
            doctors.add(nurse);
        } else if (typeOfDoctor == 3) {
            System.out.print("\tHow high is his/her bonus?");
            int bonus = scanner.nextInt();
            Doctor peds = new Pediatrician(lastname, firstname, email, hire, bonus);
            doctors.add(peds);
        } else {
            System.out.println("Invalid type of doctor. Addition aborted.");
        }
        System.out.println("Doctor added successfully.");
    }

    public void addPatient(){
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.println("Patient's personal data");
        System.out.print("\tLast name: ");
        String lastname = scanner.next();
        System.out.print("\tFirst name: ");
        String firstname = scanner.next();
        System.out.print("\tBirth year: ");
        int birth = scanner.nextInt();
        System.out.print("\tCNP: ");
        String cnp = scanner.next();

        LocalDateTime time = LocalDateTime.now();
        if (time.getYear() - birth < 18) {
            System.out.print("\tParent ID:");
            int id = scanner.nextInt();
            Patient parent = searchParent(id);
            if (parent == null) {
                System.out.println("ID not found or parent not actually valid parent. Registration aborted.");
                return;
            }
            else {
                Patient child = new Child(lastname, firstname, birth, cnp, parent.getTel(), parent.getLastName());
                patients.add(child);
            }
        } else {
            System.out.print("\tPhone number: ");
            String tel = scanner.next();
            System.out.print("\tDoes he/she have health insurance (0/1)?");
            byte hi = scanner.nextByte();
            Patient adult = new Adult(lastname, firstname, birth, cnp, tel, hi == 1);
            patients.add(adult);
        }

        System.out.println("Patient successfully registered.");
    }

    private Patient searchParent(int id) {
        Patient sch = null;
        for(var i: patients)
            if (i.getID() == id && i.computeAge() >= 18)
                sch = i;
        return sch;
    }

    public FamilyDoctor randomFamilyDoctor() {
        ArrayList<FamilyDoctor> fds = new ArrayList<>();
        for (var i: doctors) {
            if (i.getClass().toString().equals("class office.doctor.FamilyDoctor")){
                FamilyDoctor fd = (FamilyDoctor) i;
                fds.add(fd);
            }
        }
        Random rand = new Random();
        int given = rand.nextInt(fds.size());
        return fds.get(given);
    }

    private Pediatrician randomPediatrician() {
        ArrayList<Pediatrician> fds = new ArrayList<>();
        for (var i: doctors) {
            if (i.getClass().toString().equals("class office.doctor.Pediatrician")){
                Pediatrician fd = (Pediatrician) i;
                fds.add(fd);
            }
        }
        Random rand = new Random();
        int given = rand.nextInt(fds.size());
        return fds.get(given);
    }

    private Nurse randomNurse() {
        ArrayList<Nurse> fds = new ArrayList<>();
        for (var i: doctors) {
            if (i.getClass().toString().equals("class office.doctor.Nurse")){
                Nurse fd = (Nurse) i;
                fds.add(fd);
            }
        }
        Random rand = new Random();
        int given = rand.nextInt(fds.size());
        return fds.get(given);
    }

    public Patient findPatient(int id) {
        Patient found = null;
        for (var i: patients)
            if (i.getID() == id)
                found = i;
        return found;
    }

    public Doctor findDoctor (int id) {
        Doctor found = null;
        for (var i: doctors)
            if(i.getID() == id)
                found = i;
        return found;
    }

    public Appointment findAppointment (int id) {
        Appointment found = null;
        for (var i: appointments)
            if(i.getID() == id)
                found = i;
        return found;
    }

    public void makeAppointment() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.println("Appointment data");
        System.out.println("Patient's ID:");
        int id = scanner.nextInt();
        System.out.println("Does the patient need a consult(1), pediatric consult(2) or a session of questions(3)?");
        byte consult = scanner.nextByte();
        System.out.println("Date (dd-mm-yyyy--hh-mm): ");
        String data = scanner.next();
        int day = Integer.parseInt(data.substring(0,2));
        int month = Integer.parseInt(data.substring(3, 5));
        int year = Integer.parseInt(data.substring(6, 10));
        int hour = Integer.parseInt(data.substring(12, 14));
        int minute = Integer.parseInt(data.substring(15, 17));
        LocalDateTime timeOfApp = LocalDateTime.of(year, month, day, hour, minute);

        Patient patient = findPatient(id);
        if (consult == 1) {
            FamilyDoctor familyDoctor = randomFamilyDoctor();
            if (familyDoctor != null && patient != null) {
                Appointment ap = new Appointment(findPatient(id), familyDoctor, timeOfApp);
                appointments.add(ap);
            } else {
                System.out.println("Appointment invalid.");
                return;
            }

        } else if (consult == 2) {
            Pediatrician pediatrician = randomPediatrician();
            if (pediatrician != null && patient != null) {
                Appointment ap = new Appointment(patient, pediatrician, timeOfApp);
                appointments.add(ap);
            } else{
                System.out.println("Appointment invalid.");
                return;
            }
        } else if (consult == 3) {
            Nurse nurse = randomNurse();
            if (nurse != null && patient != null) {
                Appointment ap = new Appointment(patient, nurse, timeOfApp);
                appointments.add(ap);
            } else {
                System.out.println("Appointment invalid.");
                return;
            }
        } else {
            System.out.println("Type of appointment invalid. Addition aborted.");
            return;
        }
        System.out.println("Appointment made successfully.");
    }

    public void deleteDoctor() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.print("Doctor ID for deletion:");
        int id = scanner.nextInt();
        Doctor doctor = findDoctor(id);
        if (doctor == null) {
            System.out.println("Invalid doctor ID.");
            return;
        }

        doctors.remove(doctor);
    }

    public void deletePatient() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.print("Patient ID for deletion:");
        int id = scanner.nextInt();
        Patient patient = findPatient(id);
        if (patient == null) {
            System.out.println("Invalid patient ID.");
            return;
        }
        patients.remove(patient);
    }

    public void deleteAppointment() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.print("Appointment ID for deletion:");
        int id = scanner.nextInt();
        Appointment app = findAppointment(id);
        if (app == null) {
            System.out.println("Invalid appointment ID.");
            return;
        }
        appointments.remove(app);
    }

    public void displayDoctors() {
        System.out.println("These are the doctors who work here:");
        for (var i: doctors)
            i.displayDoctor();
    }

    public void displayPatients () {
        System.out.println("These are the patients registered here:");
        for (var i: patients)
            i.displayPatient();
    }

    public void displayAppointments(){
        System.out.println("These are all appointments made at this clinic: ");
        for (var i :appointments)
            i.displayAppointment();
    }

    public void editDoctor() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.print("Doctor ID for editing:");
        int id = scanner.nextInt();
        Doctor doctor = findDoctor(id);
        if (doctor == null) {
            System.out.println("Invalid doctor ID.");
            return;
        }
        System.out.println("Old doctor's data");
        doctor.show();

        System.out.println("What do you want to edit?");
        System.out.println("1. Last name");
        System.out.println("2. First name");
        System.out.println("3. Email");
        System.out.println("4. Hire year");
//        if (doctor.getClass().toString().equals("class office.doctor.FamilyDoctor")) {
//            System.out.println("5. Number of families");
//        } else if (doctor.getClass().toString().equals("class office.doctor.Nurse")) {
//            System.out.println("5. Working hours per week");
//        } else if (doctor.getClass().toString().equals("class office.doctor.Pediatrician")) {
//            System.out.println("5. Salary bonus");
//        }

        int opt = scanner.nextInt();
        if(opt == 1) {
            System.out.print("New last name: ");
            String name = scanner.next();
            doctor.setLastName(name);
        } else if (opt == 2) {
            System.out.print("New first name: ");
            String name = scanner.next();
            doctor.setFirstName(name);
        } else if (opt == 3) {
            System.out.print("New email: ");
            String email = scanner.next();
            doctor.setEmail(email);
        } else if (opt == 4) {
            System.out.print("New hire year: ");
            int hire = scanner.nextInt();
            doctor.setHireYear(hire);
            System.out.println("New salary: " + doctor.computeSalary());
        } else {
            System.out.println("Invalid option. Abort editing.");
            return;
        }
//        else if (opt == 5) {
//            if (doctor.getClass().toString().equals("class office.doctor.FamilyDoctor")) {
//            } else if (doctor.getClass().toString().equals("class office.doctor.Nurse")) {
//            } else if (doctor.getClass().toString().equals("class office.doctor.Pediatrician")) {
//            }
//        }
        System.out.println("Successfully edited.");

    }

    public void editPatient() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.print("Patient ID for editing:");
        int id = scanner.nextInt();
        Patient patient = findPatient(id);
        if (patient == null) {
            System.out.println("Invalid patient ID.");
            return;
        }
        System.out.println("Old patient's data");
        patient.show();

        System.out.println("What do you want to edit?");
        System.out.println("1. Last name");
        System.out.println("2. First name");
        System.out.println("3. Birth year");
        System.out.println("4. CNP");
        System.out.println("5. Phone number");
        int opt = scanner.nextInt();
        if(opt == 1) {
            System.out.print("New last name: ");
            String name = scanner.next();
            patient.setLastName(name);
        } else if (opt == 2) {
            System.out.print("New first name: ");
            String name = scanner.next();
            patient.setFirstName(name);
        } else if (opt == 3) {
            System.out.print("New birth year: ");
            int birth = scanner.nextInt();
            patient.setBirthYear(birth);
        } else if (opt == 4) {
            System.out.print("New CNP: ");
            String cnp = scanner.next();
            patient.setCNP(cnp);
        }  else if (opt == 5) {
            System.out.print("New phone number: ");
            String tel = scanner.next();
            patient.setTel(tel);
        } else {
            System.out.println("Invalid option. Abort editing.");
            return;
        }
        System.out.println("Successfully edited.");
    }

    public void editAppointment() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.print("Appointment ID for editing:");
        int id = scanner.nextInt();
        Appointment app = findAppointment(id);
        if (app == null) {
            System.out.println("Invalid appointment ID.");
            return;
        }
        System.out.println("Old doctor's data");
        app.displayAppointment();

        System.out.println("What do you want to edit?");
        System.out.println("1. Patient");
        System.out.println("2. Doctor");
        System.out.println("3. Date");
        System.out.println("4. Status");

        int opt = scanner.nextInt();
        if(opt == 1) {
            System.out.print("New patient id: ");
            int pid = scanner.nextInt();
            Patient patient = findPatient(pid);
            app.setPatient(patient);
        } else if (opt == 2) {
            System.out.print("New doctor id: ");
            int pid = scanner.nextInt();
            Doctor doctor = findDoctor(pid);
            app.setDoctor(doctor);
        } else if (opt == 3) {
            System.out.print("New date: ");
            String data = scanner.next();
            int day = Integer.parseInt(data.substring(0,2));
            int month = Integer.parseInt(data.substring(3, 5));
            int year = Integer.parseInt(data.substring(6, 10));
            int hour = Integer.parseInt(data.substring(12, 14));
            int minute = Integer.parseInt(data.substring(15, 17));
            LocalDateTime timeOfApp = LocalDateTime.of(year, month, day, hour, minute);
            app.setTimeOfAppointment(timeOfApp);
        } else if (opt == 4) {
            System.out.print("New status (0/1): ");
            boolean status = scanner.nextBoolean();
            app.setStatus(status);
        } else {
            System.out.println("Invalid option. Abort editing.");
            return;
        }
        System.out.println("Successfully edited.");
    }

    public void goToAppointment() {
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");

        System.out.println("Appointment ID attended:");
        int id = scanner.nextInt();
        Appointment app = findAppointment(id);
        if (app == null) {
            System.out.println("Invalid appointment ID.");
            return;
        }
        app.setStatus(!app.isStatus());
        System.out.println("Appointment attended.");
    }
}
