import java.io.*;
import java.util.*;

public class FinalPro {
    static ArrayList<Person> people=new ArrayList<>();
    public static Calendar calendar = Calendar.getInstance();

    public static void main(String[] args)throws IOException {
        Scanner scanner=new Scanner(System.in);
        AdminPanel adminPanel = new AdminPanel();
        PersonPanel personPanel = new PersonPanel();

        try{
            adminPanel.adminManageCivil.readFile();
            personPanel.bankManager.read();
            adminPanel.adminManageDocument.readFile();
        }
        catch (FileNotFoundException e){
            System.out.println("1-Admin Panel");
            System.out.println("2-Person Panel");
            int oprationCode=scanner.nextInt();

            while (true) {
                switch (oprationCode) {
                    case 1: {
                        adminPanel.adminMenu();
                        System.out.println("1-Admin Panel");
                        System.out.println("2-Person Panel");
                        oprationCode = scanner.nextInt();
                        break;
                    }
                    case 2: {
                        personPanel.personMenu();
                        System.out.println("1-Admin Panel");
                        System.out.println("2-Person Panel");
                        oprationCode = scanner.nextInt();
                        break;

                    }
                }
            }
        }
        System.out.println("1-Admin Panel");
        System.out.println("2-Person Panel");
        int oprationCode=scanner.nextInt();

        while (true) {
            switch (oprationCode) {
                case 1: {
                    adminPanel.adminMenu();
                    System.out.println("1-Admin Panel");
                    System.out.println("2-Person Panel");
                    oprationCode = scanner.nextInt();
                    break;
                }
                case 2: {
                    personPanel = new PersonPanel();
                    personPanel.personMenu();
                    System.out.println("1-Admin Panel");
                    System.out.println("2-Person Panel");
                    oprationCode = scanner.nextInt();
                    break;

                }
            }
        }
    }
}

class AdminManageBank {
    public void movingDays() throws IOException, InvalidNationalCode {

        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).loans.size(); j++) {
                try {
                    loanPayments(FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).loans.get(j).getInitialLoanAmount(), j);
                } catch (InvalidNationalCode e) {
                    System.err.println(FinalPro.people.get(i).getName() + " " + FinalPro.people.get(i).getNationalCode());
                    e.printStackTrace();
                } catch (NotEnoughMoney e) {
                    System.err.println(FinalPro.people.get(i).getName() + " " + FinalPro.people.get(i).getNationalCode());
                    e.printStackTrace();
                }
            }
        }

        FinalPro.calendar.add(Calendar.DATE, 1);


        boolean deposit = false;
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                if (FinalPro.people.get(i).bankAccounts.get(j) instanceof SavingsAccount) {
                    ((SavingsAccount) FinalPro.people.get(i).bankAccounts.get(j)).countDays++;
                    if (((SavingsAccount) FinalPro.people.get(i).bankAccounts.get(j)).countDays == ((SavingsAccount) FinalPro.people.get(i).bankAccounts.get(j)).getDesignatedTime()) {
                        deposit(FinalPro.people.get(i).getNationalCode(), ((SavingsAccount) FinalPro.people.get(i).bankAccounts.get(j)).getBankInterestPercentage(), j);
                        deposit = true;
                    }
                }
            }
        }

        if (deposit == true) {
            for (int i = 0; i < FinalPro.people.size(); i++) {
                for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                    if (j == 0 && i == 0)
                        BankManager.write(FinalPro.people.get(i).bankAccounts.get(i).getAccountNumber(), FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).bankAccounts.get(j).getStock(), FinalPro.people.get(i).bankAccounts.get(j).getProductionDate(), FinalPro.people.get(i).bankAccounts.get(j).getNegativePoint(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartPassword(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartNumber(), false);
                    else
                        BankManager.write(FinalPro.people.get(i).bankAccounts.get(i).getAccountNumber(), FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).bankAccounts.get(j).getStock(), FinalPro.people.get(i).bankAccounts.get(j).getProductionDate(), FinalPro.people.get(i).bankAccounts.get(j).getNegativePoint(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartPassword(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartNumber(), true);

                }
            }

        }

    }


    public void loanPayments(int nationalCode, int initialLoanAmount, int numberOFLoan) throws InvalidNationalCode, NotEnoughMoney, IOException {
        float InstallmentAmount;
        int wallet;
        int numberOfInstallments = FinalPro.people.get(BankManager.searchPeople(nationalCode)).loans.get(numberOFLoan).getFixeNumberOfInstallments();
        if (numberOfInstallments == 6) {
            float interestRates = 0.05f;
            InstallmentAmount = (initialLoanAmount * (interestRates + 1)) / 6;
            wallet = FinalPro.people.get(BankManager.searchPeople(nationalCode)).getWallet();
            if (wallet < InstallmentAmount) {
                FinalPro.people.get(BankManager.searchPeople(nationalCode)).bankAccounts.get(0).setNegativePoint();
                throw new NotEnoughMoney();
            } else {
                FinalPro.people.get(BankManager.searchPeople(nationalCode)).setWallet((int) (wallet - InstallmentAmount));
                FinalPro.people.get(BankManager.searchPeople(nationalCode)).loans.get(numberOFLoan).setNumberOfInstallments(FinalPro.people.get(BankManager.searchPeople(nationalCode)).loans.get(numberOFLoan).getNumberOfInstallments() - 1);


                for (int i = 0; i < FinalPro.people.size(); i++) {
                    if (i == 0)
                        AdminManageCivil.write(FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).getName(), FinalPro.people.get(i).getAge(), FinalPro.people.get(i).getGender(), FinalPro.people.get(i).getWallet(), false);
                    else
                        AdminManageCivil.write(FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).getName(), FinalPro.people.get(i).getAge(), FinalPro.people.get(i).getGender(), FinalPro.people.get(i).getWallet(), true);
                }
            }
        }
        if (numberOfInstallments == 12) {
            float interestRates = 0.1f;
            InstallmentAmount = (initialLoanAmount * (interestRates + 1)) / 12;
            wallet = FinalPro.people.get(BankManager.searchPeople(nationalCode)).getWallet();
            if (wallet < InstallmentAmount) {
                FinalPro.people.get(BankManager.searchPeople(nationalCode)).bankAccounts.get(0).setNegativePoint();
                throw new NotEnoughMoney();
            } else {
                FinalPro.people.get(BankManager.searchPeople(nationalCode)).setWallet((int) (wallet - InstallmentAmount));
                FinalPro.people.get(BankManager.searchPeople(nationalCode)).loans.get(numberOFLoan).setNumberOfInstallments(FinalPro.people.get(BankManager.searchPeople(nationalCode)).loans.get(numberOFLoan).getNumberOfInstallments() - 1);

                for (int i = 0; i < FinalPro.people.size(); i++) {
                    if (i == 0)
                        AdminManageCivil.write(FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).getName(), FinalPro.people.get(i).getAge(), FinalPro.people.get(i).getGender(), FinalPro.people.get(i).getWallet(), false);
                    else
                        AdminManageCivil.write(FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).getName(), FinalPro.people.get(i).getAge(), FinalPro.people.get(i).getGender(), FinalPro.people.get(i).getWallet(), true);
                }
            }
        }
    }


    public static void deposit(int nationalCode, float BankInterestPercentage, int numberOfBankAccount) throws InvalidNationalCode {
        int stok = FinalPro.people.get(BankManager.searchPeople(nationalCode)).bankAccounts.get(numberOfBankAccount).getStock();
        FinalPro.people.get(BankManager.searchPeople(nationalCode)).bankAccounts.get(numberOfBankAccount).setStock((int) (stok + (stok * BankInterestPercentage)));

    }


    public void recovery() {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                System.out.println(FinalPro.people.get(i).bankAccounts.get(j).getNationalCodeOwner() + " " + FinalPro.people.get(i).bankAccounts.get(j).getStock() + " " + FinalPro.people.get(i).bankAccounts.get(j).getProductionDate() + " " + FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber() + " " + FinalPro.people.get(i).bankAccounts.get(j).cart.getCartPassword());
            }
        }
    }

    public void scenario() throws FileNotFoundException {

        for (int i = 0; i < FinalPro.people.size(); i++) {
            FinalPro.people.get(i).setWallet(0);
        }
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                FinalPro.people.get(i).bankAccounts.get(j).setStock(0);
            }
        }

        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                System.out.println("Name:" + FinalPro.people.get(i).getName() + "/" + "National code:" + FinalPro.people.get(i).getNationalCode() + "/Stoke" + FinalPro.people.get(i).bankAccounts.get(j).getStock()+"/Wallet:"+FinalPro.people.get(i).getWallet());
            }
        }

        for (int i = 0; i < FinalPro.people.size(); i++) {
            FinalPro.people.remove(i);
        }
        AdminManageCivil.readFile();
        FinalPro.people.remove(0);
        BankManager.read();


        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                System.out.println("Name:" + FinalPro.people.get(i).getName() + "/" + "National code:" + FinalPro.people.get(i).getNationalCode() + "/Stoke" + FinalPro.people.get(i).bankAccounts.get(j).getStock()+"/Wallet:"+FinalPro.people.get(i).getWallet());
            }
        }
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j=0 ; j<FinalPro.people.get(i).estates.size() ; j++){
                System.out.println("National code:" + FinalPro.people.get(i).getNationalCode() +"/Address:"+FinalPro.people.get(i).estates.get(j).getEstateAddress());
            }
        }


    }
}


class AdminManageCivil {
    static File file = new File(".\\Final project");
    static File file2 = new File(file, "inf.txt");


    public static void write(int nationalCode, String name, int age, String gender ,int wallet ,boolean append) throws IOException {
        file2.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file2,append);
        Formatter formatter = new Formatter(fileOutputStream);
        formatter.format("%d %s %d %s %d\n", nationalCode, name, age, gender,wallet);
        formatter.flush();
    }
    public static void readFile() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file2);
        Scanner scanner = new Scanner(fileInputStream);
        while (scanner.hasNext()) {
            FinalPro.people.add(new Person(scanner.nextInt(), scanner.next(), scanner.nextInt(), scanner.next(),scanner.nextInt()));
        }
    }

    public static void recovery() {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            System.out.println(FinalPro.people.get(i).getName()+" "+FinalPro.people.get(i).getNationalCode());
        }
    }


    public void register(int nationalCode, String name, int age, String gender,int wallet ) throws IOException {
        FinalPro.people.add(new Person(nationalCode, name, age, gender,wallet));
        write(nationalCode, name, age, gender,wallet,true);
    }

    public <T> void editInfo(int nationalCod, String type, T t) throws InvalidNationalCode, IOException {

        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (FinalPro.people.get(i).getNationalCode() == nationalCod) {

                if (type.equals("name")) {
                    FinalPro.people.get(i).setName((String) t);
                }
                if (type.equals("age")) {
                    FinalPro.people.get(i).setAge((Integer) t);
                }
                if (type.equals("gender")) {
                    FinalPro.people.get(i).setGender((String) t);
                }

            }

        }

    }



    public void deleteInfo(int nationalCode) throws IOException {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (nationalCode == FinalPro.people.get(i).getNationalCode()) {
                FinalPro.people.remove(i);

            }
        }
        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (i==0)
                write(FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).getName(), FinalPro.people.get(i).getAge(), FinalPro.people.get(i).getGender(), FinalPro.people.get(i).getWallet(),false);
            else
                write(FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).getName(), FinalPro.people.get(i).getAge(), FinalPro.people.get(i).getGender(), FinalPro.people.get(i).getWallet(),true);

        }


    }


}


class AdminManageDocument {

    static File file = new File(".\\Final project");
    static File file2 = new File(file, "estate.txt");

    public void write(int nationalCodeOwner, String estateAddress, String purchaseDate, int value, boolean append,int ducRegistrationCode) throws IOException {
        file2.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file2, append);
        Formatter formatter = new Formatter(fileOutputStream);
        formatter.format(" %d %s %s %d %d \n", nationalCodeOwner, estateAddress, purchaseDate, value, ducRegistrationCode);
        formatter.flush();
    }

    public void readFile() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file2);
        Scanner scanner = new Scanner(fileInputStream);
        for (int i = 0; i < FinalPro.people.size(); i++) {
            while (scanner.hasNext()) {
                FinalPro.people.get(i).estates.add(new Estate(scanner.nextInt(), scanner.next(), scanner.next(), scanner.nextInt()));
                FinalPro.people.get(i).estates.get(FinalPro.people.get(i).estates.size() - 1).setDucRegistrationCode(scanner.nextInt());
            }
        }
    }


    public int register(int nationalCodeOwner, String estateAddress, String purchaseDate, int value) throws InvalidNationalCode, IOException {
        FinalPro.people.get(BankManager.searchPeople(nationalCodeOwner)).estates.add(new Estate(nationalCodeOwner, estateAddress, purchaseDate, value));
        int ducRegistrationCode = FinalPro.people.get(BankManager.searchPeople(nationalCodeOwner)).estates.get(FinalPro.people.get(BankManager.searchPeople(nationalCodeOwner)).estates.size() - 1).getDucRegistrationCode();
        write(nationalCodeOwner, estateAddress, purchaseDate, value, true, ducRegistrationCode);
        return ducRegistrationCode;

    }

    public void recovery() {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).estates.size(); j++) {
                System.out.println(FinalPro.people.get(i).estates.get(j).getDucRegistrationCode() + " " + FinalPro.people.get(i).estates.get(j).getEstateAddress());
            }
        }
    }


    public <T> void editInfo(int nationalCod, int ducRegistrationCode, String type, T t) throws InvalidNationalCode, IOException, MyExeption {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (FinalPro.people.get(i).getNationalCode() == nationalCod) {


                if (type.equals("estateAddress")) {
                    FinalPro.people.get(BankManager.searchPeople(nationalCod)).estates.get(searchEstate(nationalCod, ducRegistrationCode)).setEstateAddress((String) t);
                }
                if (type.equals("purchaseDate")) {
                    FinalPro.people.get(BankManager.searchPeople(nationalCod)).estates.get(searchEstate(nationalCod, ducRegistrationCode)).setPurchaseDate((String) t);
                }
                if (type.equals("value")) {
                    FinalPro.people.get(BankManager.searchPeople(nationalCod)).estates.get(searchEstate(nationalCod, ducRegistrationCode)).setValue((Integer) t);
                }

            }

        }


    }

    public int searchEstate(int nationalCod, int ducRegistrationCode) throws MyExeption {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).estates.size(); j++) {
                if (nationalCod == FinalPro.people.get(i).getNationalCode()) {
                    if (ducRegistrationCode == FinalPro.people.get(i).estates.get(j).getDucRegistrationCode()) {
                        return j;
                    }
                }
            }
        }
        throw new MyExeption("Invalid nationalCod or ducRegistrationCode");
    }

    public void deleteInfo(int nationalCode, int ducRegistrationCode) throws IOException {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).estates.size(); j++) {
                if (nationalCode == FinalPro.people.get(i).getNationalCode()) {
                    if (ducRegistrationCode == FinalPro.people.get(i).estates.get(j).getDucRegistrationCode())
                        FinalPro.people.get(i).setWallet(FinalPro.people.get(i).estates.get(j).getValue());
                    FinalPro.people.get(i).estates.remove(j);
                }
            }
        }
        for (int j = 0; j < FinalPro.people.size(); j++) {
            for (int k = 0; k < FinalPro.people.get(j).estates.size(); k++) {
                if (j == 0 && k == 0)
                    write(FinalPro.people.get(j).getNationalCode(), FinalPro.people.get(j).estates.get(k).getEstateAddress(), FinalPro.people.get(j).estates.get(k).getPurchaseDate(), FinalPro.people.get(j).estates.get(k).getValue(), false, FinalPro.people.get(j).estates.get(k).getDucRegistrationCode());
                else
                    write(FinalPro.people.get(j).getNationalCode(), FinalPro.people.get(j).estates.get(k).getEstateAddress(), FinalPro.people.get(j).estates.get(k).getPurchaseDate(), FinalPro.people.get(j).estates.get(k).getValue(), true, FinalPro.people.get(j).estates.get(k).getDucRegistrationCode());
            }
        }

        for (int i=0 ;i<FinalPro.people.size() ;i++){
            if (i==0)
                AdminManageCivil.write(FinalPro.people.get(i).getNationalCode(),FinalPro.people.get(i).getName(),FinalPro.people.get(i).getAge(),FinalPro.people.get(i).getGender(),FinalPro.people.get(i).getWallet(),false);
            else
                AdminManageCivil.write(FinalPro.people.get(i).getNationalCode(),FinalPro.people.get(i).getName(),FinalPro.people.get(i).getAge(),FinalPro.people.get(i).getGender(),FinalPro.people.get(i).getWallet(),true);

        }
    }

}


class AdminPanel {
    AdminManageDocument adminManageDocument=new AdminManageDocument();
    AdminManageCivil adminManageCivil = new AdminManageCivil();
    AdminManageBank adminManageBank = new AdminManageBank();

    public void adminMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Login to the:");
        System.out.println("1-Document registration system ");
        System.out.println("2-Civil registration system");
        System.out.println("3-Admin Manage Bank");
        System.out.println("4-Project scenario");
        int selectedCode = scanner.nextInt();

        switch (selectedCode) {
            case 1: {
                System.out.println("1-Register");
                System.out.println("2-Edit Information");
                System.out.println("3-Delete Information");
                System.out.println("4-Recovery Information");

                int ducCode=scanner.nextInt();

                if (ducCode==1){
                    System.out.println("Enter 1-nationalCodeOwner , 2-estateAddress , 3-purchaseDate , 4-value");
                    try {
                        System.out.println("ducRegistrationCode:"+adminManageDocument.register(scanner.nextInt(),scanner.next(),scanner.next(),scanner.nextInt()));
                    } catch (InvalidNationalCode e) {
                        e.printStackTrace();
                    }

                }
                if (ducCode==2){

                    System.out.println("Enter nationalCode , ducRegistrationCode");
                    int nationalCode = scanner.nextInt();
                    int ducRegistrationCode=scanner.nextInt();

                    boolean search = false;
                    for (int i = 0; i < FinalPro.people.size(); i++) {
                        if (FinalPro.people.get(i).getNationalCode() == nationalCode) {
                            search = true;

                            int editCode ;

                            System.out.println(" Do you want to Edit estateAddress?(If you do not want to enter zero)");
                            editCode = scanner.nextInt();

                            if (editCode != 0) {
                                System.out.println("Enter estateAddress");
                                try {
                                    try {
                                        adminManageDocument.editInfo(nationalCode,ducRegistrationCode,"estateAddress",scanner.next());
                                    } catch (MyExeption e) {
                                        e.printStackTrace();
                                    }
                                } catch (InvalidNationalCode e) {
                                    e.printStackTrace();
                                }

                            }

                            System.out.println(" Do you want to Edit purchaseDate?(If you do not want to enter zero)");
                            editCode = scanner.nextInt();
                            if (editCode != 0) {
                                System.out.println("Enter new purchaseDate");
                                try {
                                    adminManageDocument.editInfo(nationalCode,ducRegistrationCode,"purchaseDate",scanner.next());
                                } catch (InvalidNationalCode e) {
                                    e.printStackTrace();
                                } catch (MyExeption e) {
                                    e.printStackTrace();
                                }
                            }


                            System.out.println(" Do you want to Edit value?(If you do not want to enter zero)");
                            editCode = scanner.nextInt();
                            if (editCode != 0) {
                                System.out.println("Enter new value");
                                try {
                                    adminManageDocument.editInfo(nationalCode,ducRegistrationCode,"value",scanner.nextInt());
                                } catch (InvalidNationalCode e) {
                                    e.printStackTrace();
                                } catch (MyExeption e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    for (int j = 0; j < FinalPro.people.size(); j++) {
                        for (int k=0 ;k<FinalPro.people.get(j).estates.size() ;k++) {
                            if (j==0 && k==0)
                                adminManageDocument.write(FinalPro.people.get(j).getNationalCode(),FinalPro.people.get(j).estates.get(k).getEstateAddress(),FinalPro.people.get(j).estates.get(k).getPurchaseDate(),FinalPro.people.get(j).estates.get(k).getValue(),false,FinalPro.people.get(j).estates.get(k).getDucRegistrationCode());
                            else
                                adminManageDocument.write(FinalPro.people.get(j).getNationalCode(),FinalPro.people.get(j).estates.get(k).getEstateAddress(),FinalPro.people.get(j).estates.get(k).getPurchaseDate(),FinalPro.people.get(j).estates.get(k).getValue(),true,FinalPro.people.get(j).estates.get(k).getDucRegistrationCode());
                        }
                    }

                    if (search == false) {
                        try {
                            throw new InvalidNationalCode();
                        } catch (InvalidNationalCode invalidNationalCode) {
                            invalidNationalCode.getStackTrace();
                        }
                    }
                }
                if (ducCode==3){
                    System.out.println("Enter your national code & ducRegistrationCode");
                    adminManageDocument.deleteInfo(scanner.nextInt(),scanner.nextInt());
                }
                if (ducCode==4){
                    adminManageDocument.recovery();
                }
                break;
            }

            case 2: {
                System.out.println("1-Register");
                System.out.println("2-Edit Information");
                System.out.println("3-Delete Information");
                System.out.println("4-Recovery Information");


                int civilCod = scanner.nextInt();
                switch (civilCod) {
                    case 1: {
                        System.out.println("Enter:nationalCode, name, age,gender");
                        int nationalCode = scanner.nextInt();
                        String name = scanner.next();
                        int age = scanner.nextInt();
                        String gender = scanner.next();
                        adminManageCivil.register(nationalCode, name, age, gender, 0);

                        break;

                    }

                    case 2: {
                        System.out.println("Enter nationalCode");
                        int nationalCode = scanner.nextInt();

                        boolean search = false;
                        for (int i = 0; i < FinalPro.people.size(); i++) {
                            if (FinalPro.people.get(i).getNationalCode() == nationalCode) {
                                search = true;

                                int editCode ;

                                System.out.println(" Do you want to Edit name?(If you do not want to enter zero)");
                                editCode = scanner.nextInt();

                                if (editCode != 0) {
                                    System.out.println("Enter name");
                                    try {
                                        adminManageCivil.editInfo(nationalCode, "name", scanner.next());
                                    } catch (InvalidNationalCode e) {
                                        e.printStackTrace();
                                    }
                                }

                                System.out.println(" Do you want to Edit age?(If you do not want to enter zero)");
                                editCode = scanner.nextInt();
                                if (editCode != 0) {
                                    try {
                                        adminManageCivil.editInfo(nationalCode, "age", scanner.nextInt());
                                    } catch (InvalidNationalCode e) {
                                        e.printStackTrace();
                                    }
                                }


                                System.out.println(" Do you want to Edit gender?(If you do not want to enter zero)");
                                editCode = scanner.nextInt();
                                if (editCode != 0) {
                                    try {
                                        adminManageCivil.editInfo(nationalCode, "gender", scanner.nextInt());
                                    } catch (InvalidNationalCode e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
//                            for (int k=0 ;k<FinalPro.people.size();k++){
//                                System.out.println(FinalPro.people.get(k).getName());
//                        }
                        for (int j = 0; j < FinalPro.people.size(); j++) {
                            if (j == 0)
                                AdminManageCivil.write(FinalPro.people.get(j).getNationalCode(), FinalPro.people.get(j).getName(), FinalPro.people.get(j).getAge(), FinalPro.people.get(j).getGender(), FinalPro.people.get(j).getWallet(), false);
                            else
                                AdminManageCivil.write(FinalPro.people.get(j).getNationalCode(), FinalPro.people.get(j).getName(), FinalPro.people.get(j).getAge(), FinalPro.people.get(j).getGender(), FinalPro.people.get(j).getWallet(), true);
                        }
                        if (search == false) {
                            try {
                                throw new InvalidNationalCode();
                            } catch (InvalidNationalCode invalidNationalCode) {
                                invalidNationalCode.getStackTrace();
                            }
                        }

                        break;
                    }

                    case 3: {
                        System.out.println("Enter your national code:");
                        adminManageCivil.deleteInfo(scanner.nextInt());
                    }

                    case 4: {
                        adminManageCivil.recovery();
                        break;
                    }
                }
                break;
            }
            case 3: {
                System.out.println("1-moving Days");
                System.out.println("2-recovery");
                int selectedCod = scanner.nextInt();
                if (selectedCod == 1) {
                    System.out.println(FinalPro.calendar.get(Calendar.DATE));
                    try {
                        adminManageBank.movingDays();
                    } catch (InvalidNationalCode e) {
                        e.printStackTrace();
                    }
                }
                if (selectedCod == 2) {
                    adminManageBank.recovery();
                }
                break;
            }

            case 4: {
                adminManageBank.scenario();
                break;
            }
        }

    }
}


class BankAccount {
    private long accountNumber = 1000000000000000L;
    private int nationalCodeOwner;
    private int stock;
    private int productionDate;
    private int negativePoint = 0;
    Cart cart;
    static int counter;
    private boolean getAloan = true;

    public BankAccount(/*long accountNumber*/int nationalCodeOwner, int stock, int productionDate, int negativePoint) {
        //    this.accountNumber = 1000000000;
        this.nationalCodeOwner = nationalCodeOwner;
        this.stock = stock;
        this.productionDate = productionDate;
        this.negativePoint = negativePoint;
        cart = new Cart();
        counter++;

    }

    public BankAccount(int nationalCodeOwner, int stock, int productionDate) {
        this.nationalCodeOwner = nationalCodeOwner;
        this.stock = stock;
        this.productionDate = productionDate;
        cart = new Cart();
    }

    public boolean isGetAloan() {
        return getAloan;
    }

    public void setGetAloan(boolean getAloan) {
        this.getAloan = getAloan;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        long maxAccountNumber = 0;
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                if (maxAccountNumber < FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber()) {
                    maxAccountNumber = FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber();
                }
            }
        }
        maxAccountNumber -= 1000000000000000L;
        maxAccountNumber++;
        counter = (int) maxAccountNumber;
        this.accountNumber = accountNumber;
    }

    public int getNationalCodeOwner() {
        return nationalCodeOwner;
    }

    public void setNationalCodeOwner(int nationalCodeOwner) {
        this.nationalCodeOwner = nationalCodeOwner;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(int productionDate) {
        this.productionDate = productionDate;
    }

    public int getNegativePoint() {
        return negativePoint;
    }

    public void setNegativePoint() {
        this.negativePoint += 1;
        if (negativePoint >= 5)
            getAloan = false;
    }
}


class BankManager {

    static File file = new File(".\\Final project");
    static File file2 = new File(file, "bank.txt");

    public static void write(long accountNumber, int nationalCodeOwner, int stock, int productionDate, int negativePoint, int password, long curtNumber, boolean append) throws IOException {
        file2.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file2, append);
        Formatter formatter = new Formatter(fileOutputStream);
        formatter.format("%d %d %d %d %d %d \n", nationalCodeOwner, stock, productionDate, accountNumber, password, curtNumber);
        formatter.flush();
    }

    public static void read() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file2);
        Scanner scanner = new Scanner(fileInputStream);
        int i = 0;
        while (scanner.hasNext()) {
            for (; i < FinalPro.people.size(); i++) {
//            for (int j=0 ; j<FinalPro.people.size() ; j++){
//                if (i>0 &&  FinalPro.people.get(i).getNationalCode()==FinalPro.people.get(j).getNationalCode()){
//                    FinalPro.people.get(i).bankAccounts.add(new BankAccount(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
//                    FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).setAccountNumber((long) (scanner.nextLong()));
//                    FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).cart.setCartPassword(scanner.nextInt());
//                    FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).cart.setCartNumber(scanner.nextLong());
//                }
//            }


                FinalPro.people.get(i).bankAccounts.add(new BankAccount(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
                FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).setAccountNumber((scanner.nextLong()));
                FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).cart.setCartPassword(scanner.nextInt());
                FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).cart.setCartNumber(scanner.nextLong());

            }
//            if (FinalPro.people.size()!=1) {
//                BankAccount bankAccount = new BankAccount(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
//                bankAccount.setAccountNumber((scanner.nextLong()));
//                bankAccount.cart.setCartPassword(scanner.nextInt());
//                bankAccount.cart.setCartNumber(scanner.nextLong());
//
//                for (int j = 0; j < FinalPro.people.size(); j++) {
//                    for (int k = 0; k < FinalPro.people.get(j).bankAccounts.size(); k++) {
//                        if (i != 0) {
//                            if (bankAccount.getNationalCodeOwner() == FinalPro.people.get(j).bankAccounts.get(k).getNationalCodeOwner()) {
//                                FinalPro.people.get(j).bankAccounts.add(bankAccount);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }

        }

    }


    public long buildCurrentAccount(int nationalCodeOwner, int stock, int productionDate, int negativePoint) throws InvalidNationalCode, IOException {

        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (nationalCodeOwner == FinalPro.people.get(i).getNationalCode()) {
                FinalPro.people.get(i).bankAccounts.add(new CurrentAccount(nationalCodeOwner, stock, productionDate, negativePoint));
                FinalPro.people.get(i).setWallet(FinalPro.people.get(i).getWallet() + stock);
                long accountNumber = FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).getAccountNumber();

                for (int k = 0; k < FinalPro.people.size(); k++) {
                    if (k == 0)
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), false);
                    else
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), true);

                }
                return accountNumber;
            }
        }
        throw new InvalidNationalCode();
    }

    public long buildLendAccount(int nationalCodeOwner, int stock, int productionDate, int negativePoint) throws InvalidNationalCode, IOException {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (nationalCodeOwner == FinalPro.people.get(i).getNationalCode()) {
                FinalPro.people.get(i).bankAccounts.add(new LendAccount(nationalCodeOwner, stock, productionDate, negativePoint));
                FinalPro.people.get(i).setWallet(FinalPro.people.get(i).getWallet() + stock);
                long accountNumber = FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).getAccountNumber();

                for (int k = 0; k < FinalPro.people.size(); k++) {
                    if (k == 0)
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), false);
                    else
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), true);

                }

                return accountNumber;

            }
        }
        throw new InvalidNationalCode();
    }

    public long buildSavingsAccount(String type, int nationalCodeOwner, int stock, int productionDate, int negativePoint) throws InvalidType, InvalidNationalCode, IOException {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (nationalCodeOwner == FinalPro.people.get(i).getNationalCode()) {
                FinalPro.people.get(i).bankAccounts.add(new SavingsAccount(type, nationalCodeOwner, stock, productionDate, negativePoint));
                FinalPro.people.get(i).setWallet(FinalPro.people.get(i).getWallet() + stock);
                long accountNumber = FinalPro.people.get(i).bankAccounts.get(FinalPro.people.get(i).bankAccounts.size() - 1).getAccountNumber();

                for (int k = 0; k < FinalPro.people.size(); k++) {
                    if (k == 0)
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), false);
                    else
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), true);

                }
                return accountNumber;

            }
        }
        throw new InvalidNationalCode();
    }

    public void receiveCheckbook(int nationalCode, long accountNumber, String name, String family) throws InvalidNationalCode, InvalidAccount {
        boolean searchPeople = false;
        boolean searchCurrentAccount = false;
        boolean searchAccountNumber = false;
        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (nationalCode == FinalPro.people.get(i).getNationalCode()) {
                searchPeople = true;
                for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                    if (FinalPro.people.get(i).bankAccounts.get(j) instanceof CurrentAccount) {
                        searchCurrentAccount = true;
                        if (accountNumber == FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber()) {
                            searchAccountNumber = true;
                            ((CurrentAccount) FinalPro.people.get(i).bankAccounts.get(j)).checkbook = new Checkbook(nationalCode, name, family);
                        }
                    }
                }
            }
        }
        if (searchPeople == false)
            throw new InvalidNationalCode();
        if (searchCurrentAccount == false)
            throw new InvalidAccount("Invalid Current Account");
        if (searchAccountNumber == false)
            throw new InvalidAccount("Invalid Account Number");

    }

    public void cardToCard(long cartNumber_Receiver, long cardPassword_Sender, int money) throws InsufficientInventory, InvalidAccount, IOException {
        int numberOfReceiver = -1;
        int numberOfSender = -1;
        int numberOfAccountReceiver = -1, numberOfAccountSender = -1;
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {

                if (cartNumber_Receiver == FinalPro.people.get(i).bankAccounts.get(j).cart.getCartNumber()) {
                    numberOfReceiver = i;
                    numberOfAccountReceiver = j;
                }
                if (cardPassword_Sender == FinalPro.people.get(i).bankAccounts.get(j).cart.getCartPassword()) {
                    numberOfSender = i;
                    numberOfAccountSender = j;
                }
            }
        }
        if (numberOfAccountReceiver != -1 && numberOfAccountSender != -1 && numberOfReceiver != -1 && numberOfSender != -1) {
            if (FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).getStock() - 10 > money) {
                FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).setStock(FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).getStock() - money);
                FinalPro.people.get(numberOfSender).setWallet(FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).getStock());
                FinalPro.people.get(numberOfReceiver).bankAccounts.get(numberOfAccountReceiver).setStock(FinalPro.people.get(numberOfReceiver).bankAccounts.get(numberOfAccountReceiver).getStock() + money);
                FinalPro.people.get(numberOfReceiver).setWallet(FinalPro.people.get(numberOfReceiver).bankAccounts.get(numberOfAccountReceiver).getStock());

                for (int k = 0; k < FinalPro.people.size(); k++) {
                    if (k == 0)
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), false);
                    else
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), true);
                }
                for (int f = 0; f < FinalPro.people.size(); f++) {
                    for (int l = 0; l < FinalPro.people.get(f).bankAccounts.size(); l++) {
                        if (f == 0 && l == 0)
                            BankManager.write(FinalPro.people.get(f).bankAccounts.get(l).getAccountNumber(), FinalPro.people.get(f).getNationalCode(), FinalPro.people.get(f).bankAccounts.get(l).getStock(), FinalPro.people.get(f).bankAccounts.get(l).getProductionDate(), FinalPro.people.get(f).bankAccounts.get(l).getNegativePoint(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartPassword(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartNumber(), false);
                        else
                            BankManager.write(FinalPro.people.get(f).bankAccounts.get(l).getAccountNumber(), FinalPro.people.get(f).getNationalCode(), FinalPro.people.get(f).bankAccounts.get(l).getStock(), FinalPro.people.get(f).bankAccounts.get(l).getProductionDate(), FinalPro.people.get(f).bankAccounts.get(l).getNegativePoint(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartPassword(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartNumber(), true);
                    }
                }


            } else {
                throw new InsufficientInventory();//موجودی ناکافی
            }
        } else
            throw new InvalidAccount("Invalid Account");
    }


    public void receiptCheck(int day, int month, int year, int amount, long accountNumberSender, long accountNumberReceiver) throws InvalidAccount, InsufficientInventory, IOException {
        int numberOfSender = -1, numberOfAccountSender = -1;
        int numberOfReceiver = -1, numberOfAccountReceiver = -1;
        System.out.println(FinalPro.calendar.get(Calendar.DAY_OF_WEEK) + " " + FinalPro.calendar.get(Calendar.MONTH) + " " + FinalPro.calendar.get(Calendar.YEAR));
        if (day == FinalPro.calendar.get(Calendar.DAY_OF_WEEK) && month == FinalPro.calendar.get(Calendar.MONTH) && year == FinalPro.calendar.get(Calendar.YEAR)) {
            for (int i = 0; i < FinalPro.people.size(); i++) {
                for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                    //        if (FinalPro.people.get(i).bankAccounts.get(j) instanceof CurrentAccount) {
                    if (FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber() == accountNumberSender) {
                        numberOfSender = i;
                        numberOfAccountSender = j;
                    }
                    //     }
                }
            }

            for (int i = 0; i < FinalPro.people.size(); i++) {
                for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                    //              if (FinalPro.people.get(i).bankAccounts.get(j) instanceof CurrentAccount) {
                    if (FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber() == accountNumberReceiver) {
                        numberOfReceiver = i;
                        numberOfAccountReceiver = j;
                    }
                    //             }
                }
            }
        }

        depositMoney(/*FinalPro.people.get(numberOfReceiver).bankAccounts.get(numberOfAccountReceiver).getAccountNumber(), FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).getAccountNumber(), */amount, numberOfSender, numberOfReceiver, numberOfAccountSender, numberOfAccountReceiver);


    }


    public void depositMoney(/*long accountNumber_Receiver, long accountNumber_Sender,*/ int money, int numberOfSender, int numberOfReceiver, int numberOfAccountSender, int numberOfAccountReceiver) throws InsufficientInventory, InvalidAccount, IOException {
        if (numberOfAccountReceiver != -1 && numberOfAccountSender != -1 && numberOfReceiver != -1 && numberOfSender != -1) {
            if (FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).getStock() - 10 > money) {
                FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).setStock(FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).getStock() - money);
                FinalPro.people.get(numberOfSender).setWallet(FinalPro.people.get(numberOfSender).bankAccounts.get(numberOfAccountSender).getStock());
                FinalPro.people.get(numberOfReceiver).bankAccounts.get(numberOfAccountReceiver).setStock(FinalPro.people.get(numberOfReceiver).bankAccounts.get(numberOfAccountReceiver).getStock() + money);
                FinalPro.people.get(numberOfReceiver).setWallet(FinalPro.people.get(numberOfReceiver).bankAccounts.get(numberOfAccountReceiver).getStock());

                for (int k = 0; k < FinalPro.people.size(); k++) {
                    if (k == 0)
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), false);
                    else
                        AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), true);
                }

                for (int f = 0; f < FinalPro.people.size(); f++) {
                    for (int l = 0; l < FinalPro.people.get(f).bankAccounts.size(); l++) {
                        if (f == 0 && l == 0)
                            BankManager.write(FinalPro.people.get(f).bankAccounts.get(l).getAccountNumber(), FinalPro.people.get(f).getNationalCode(), FinalPro.people.get(f).bankAccounts.get(l).getStock(), FinalPro.people.get(f).bankAccounts.get(l).getProductionDate(), FinalPro.people.get(f).bankAccounts.get(l).getNegativePoint(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartPassword(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartNumber(), false);
                        else
                            BankManager.write(FinalPro.people.get(f).bankAccounts.get(l).getAccountNumber(), FinalPro.people.get(f).getNationalCode(), FinalPro.people.get(f).bankAccounts.get(l).getStock(), FinalPro.people.get(f).bankAccounts.get(l).getProductionDate(), FinalPro.people.get(f).bankAccounts.get(l).getNegativePoint(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartPassword(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartNumber(), true);
                    }
                }
            } else {
                throw new InsufficientInventory();//موجودی ناکافی
            }
        } else
            throw new InvalidAccount("Invalid Account");

    }


    public long receiveCard(String name, String lastName, long accountNumber) throws InvalidAccount {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                if (accountNumber == FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber()) {
                    FinalPro.people.get(i).bankAccounts.get(j).cart = new Cart(name, lastName);
                    return FinalPro.people.get(i).bankAccounts.get(j).cart.getCartNumber();
                }
            }
        }
        throw new InvalidAccount("Invalid Account");
    }

    public int getPassword(long accountNumber) throws InvalidAccount {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                if (accountNumber == FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber()) {
                    return FinalPro.people.get(i).bankAccounts.get(j).cart.getCartPassword();
                }
            }
        }
        throw new InvalidAccount("Invalid Account");
    }

    public void getLoan(int nationalCode, int numberOfInstallments, int loanAmount) throws InvalidNationalCode, MyExeption, IOException {
        boolean searchNationalCode = false;
        if (numberOfInstallments == 6 || numberOfInstallments == 12) {
            if (loanAmount > 100 && loanAmount < 1000000) {
                for (int i = 0; i < FinalPro.people.size(); i++) {
                    if (nationalCode == FinalPro.people.get(i).getNationalCode()) {
                        searchNationalCode = true;
                        FinalPro.people.get(i).loans.add(new Loan(loanAmount, numberOfInstallments, numberOfInstallments));
                        FinalPro.people.get(i).setWallet(FinalPro.people.get(i).getWallet() + loanAmount);


                        for (int k = 0; k < FinalPro.people.size(); k++) {
                            if (k == 0)
                                AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), false);
                            else
                                AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), true);

                        }

                    }
                }
            } else
                throw new MyExeption("Loan amount is not allowed");
        } else
            throw new MyExeption("The number of installments is not allowed");

        if (searchNationalCode == false)
            throw new InvalidNationalCode();

    }


    public static int searchPeople(int nationalCode) throws InvalidNationalCode {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            if (nationalCode == FinalPro.people.get(i).getNationalCode())
                return i;
        }
        throw new InvalidNationalCode();
    }


    public boolean takingDeposit(int nationalCode) throws InvalidNationalCode, IOException {
        boolean deposit = false;
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                if (nationalCode == FinalPro.people.get(i).getNationalCode()) {
                    if (FinalPro.people.get(i).bankAccounts.get(j) instanceof SavingsAccount) {
                        if (((SavingsAccount) FinalPro.people.get(i).bankAccounts.get(j)).countDays == ((SavingsAccount) FinalPro.people.get(i).bankAccounts.get(j)).getDesignatedTime()) {
                            AdminManageBank.deposit(FinalPro.people.get(i).getNationalCode(), ((SavingsAccount) FinalPro.people.get(i).bankAccounts.get(j)).getBankInterestPercentage(), j);
                            deposit = true;
                        } else {
                            deposit = false;
                        }

                    }
                }
            }
        }
        if (deposit == true) {
            for (int i = 0; i < FinalPro.people.size(); i++) {
                for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                    if (j == 0 && i == 0)
                        BankManager.write(FinalPro.people.get(i).bankAccounts.get(i).getAccountNumber(), FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).bankAccounts.get(j).getStock(), FinalPro.people.get(i).bankAccounts.get(j).getProductionDate(), FinalPro.people.get(i).bankAccounts.get(j).getNegativePoint(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartPassword(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartNumber(), false);
                    else
                        BankManager.write(FinalPro.people.get(i).bankAccounts.get(i).getAccountNumber(), FinalPro.people.get(i).getNationalCode(), FinalPro.people.get(i).bankAccounts.get(j).getStock(), FinalPro.people.get(i).bankAccounts.get(j).getProductionDate(), FinalPro.people.get(i).bankAccounts.get(j).getNegativePoint(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartPassword(), FinalPro.people.get(i).bankAccounts.get(j).cart.getCartNumber(), true);

                }
            }

        }
        return deposit;
    }

    public void depositFromWallet(int nationalCode, long accountNumber, int money) throws IOException {
        for (int i = 0; i < FinalPro.people.size(); i++) {
            for (int j = 0; j < FinalPro.people.get(i).bankAccounts.size(); j++) {
                if (nationalCode == FinalPro.people.get(i).getNationalCode()) {
                    if (accountNumber == FinalPro.people.get(i).bankAccounts.get(j).getAccountNumber()) {
                        FinalPro.people.get(i).bankAccounts.get(j).setStock(FinalPro.people.get(i).bankAccounts.get(j).getStock()+money);
                        FinalPro.people.get(i).setWallet(FinalPro.people.get(i).getWallet()-money);
                    }
                }
            }
        }
        for (int f = 0; f < FinalPro.people.size(); f++) {
            for (int l = 0; l < FinalPro.people.get(f).bankAccounts.size(); l++) {
                if (f == 0 && l == 0)
                    write(FinalPro.people.get(f).bankAccounts.get(l).getAccountNumber(), FinalPro.people.get(f).getNationalCode(), FinalPro.people.get(f).bankAccounts.get(l).getStock(), FinalPro.people.get(f).bankAccounts.get(l).getProductionDate(), FinalPro.people.get(f).bankAccounts.get(l).getNegativePoint(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartPassword(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartNumber(), false);
                else
                    write(FinalPro.people.get(f).bankAccounts.get(l).getAccountNumber(), FinalPro.people.get(f).getNationalCode(), FinalPro.people.get(f).bankAccounts.get(l).getStock(), FinalPro.people.get(f).bankAccounts.get(l).getProductionDate(), FinalPro.people.get(f).bankAccounts.get(l).getNegativePoint(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartPassword(), FinalPro.people.get(f).bankAccounts.get(l).cart.getCartNumber(), true);
            }
        }


        for (int k = 0; k < FinalPro.people.size(); k++) {
            if (k == 0)
                AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), false);
            else
                AdminManageCivil.write(FinalPro.people.get(k).getNationalCode(), FinalPro.people.get(k).getName(), FinalPro.people.get(k).getAge(), FinalPro.people.get(k).getGender(), FinalPro.people.get(k).getWallet(), true);

        }

    }
}


class Cart {
    private String name;
    private String lastname;
    private long cartNumber=1000000000000105l;
    private int cartPassword;
    private Random random = new Random();
    private int cv2;
    static int counter=0;

    public Cart(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
        setCartNumber(getCartNumber()+counter);
        counter++;
        setCv2();
        setCartPassword();
    }
    public Cart(){}

    public int getCartPassword() {
        return cartPassword;
    }

    public void setCartPassword() {
        cartPassword = random.nextInt(10000)+1000;
    }
    public void setCartPassword(int cartPassword){
        this.cartPassword=cartPassword;
    }

    public void setCv2(int cv2) {
        this.cv2 = cv2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public long getCartNumber() {
        return cartNumber;
    }

    public void setCartNumber(long cartNumber) {
        this.cartNumber = cartNumber;
    }



    public int getCv2() {
        return cv2;
    }

    public void setCv2() {
        cv2 = random.nextInt(1000) + 100;
    }
}


class Check {
    private int day;
    private int month;
    private int year;
    private int amount;
    private int nationalCodeReceiver;
    private int accountNumberSender;

    public Check(int day, int month, int year, int amount, int nationalCodeReceiver, int accountNumberSender) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.nationalCodeReceiver = nationalCodeReceiver;
        this.accountNumberSender = accountNumberSender;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getNationalCodeReceiver() {
        return nationalCodeReceiver;
    }

    public void setNationalCodeReceiver(int nationalCodeReceiver) {
        this.nationalCodeReceiver = nationalCodeReceiver;
    }

    public int getAccountNumberSender() {
        return accountNumberSender;
    }

    public void setAccountNumberSender(int accountNumberSender) {
        this.accountNumberSender = accountNumberSender;
    }
}


class Checkbook {
    private int nationalCode;
    private String name;
    private String lastName;
    ArrayList<Check>checks;

    public Checkbook(int nationalCode, String name, String lastName) {
        this.nationalCode = nationalCode;
        this.name = name;
        this.lastName = lastName;
        checks=new ArrayList<>();
    }

    public int getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(int nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}


class CurrentAccount extends BankAccount {
    Checkbook checkbook;
    private int bankCardPassword;


    public CurrentAccount(int nationalCodeOwner, int stock, int productionDate, int negativePoint) {
        super(nationalCodeOwner, stock, productionDate, negativePoint);
        setAccountNumber(getAccountNumber() + counter);
    }


    public int getBankCardPassword() {
        return bankCardPassword;
    }

    public void setBankCardPassword(int bankCardPassword) {
        this.bankCardPassword = bankCardPassword;
    }

}


class Estate {
    private int ducRegistrationCode;
    private int nationalCodeOwner;
    private String estateAddress;
    private String purchaseDate;
    private int value;
    private Random random = new Random();


    public Estate(int nationalCodeOwner, String estateAddress, String purchaseDate, int value) {
        setDucRegistrationCode();
        this.nationalCodeOwner = nationalCodeOwner;
        this.estateAddress = estateAddress;
        this.purchaseDate = purchaseDate;
        this.value = value;
    }

    public int getDucRegistrationCode() {
        return ducRegistrationCode;
    }

    public void setDucRegistrationCode() {
        ducRegistrationCode = random.nextInt(100000000)+10000000;
    }
    public void setDucRegistrationCode(int ducRegistrationCode) {
        this.ducRegistrationCode=ducRegistrationCode;
    }

    public int getNationalCodeOwner() {
        return nationalCodeOwner;
    }

    public void setNationalCodeOwner(int nationalCodeOwner) {
        this.nationalCodeOwner = nationalCodeOwner;
    }

    public String getEstateAddress() {
        return estateAddress;
    }

    public void setEstateAddress(String estateAddress) {
        this.estateAddress = estateAddress;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}


class InsufficientInventory extends Exception{
    public InsufficientInventory(){
        super("Insufficient Inventory");
    }
}


class InvalidAccount extends Exception{
    public InvalidAccount(String massage){
        super(massage);
    }
}


class InvalidNationalCode extends Exception{
    public InvalidNationalCode(){
        super("InvalidNationalCode");
    }
}


class InvalidType extends Exception{
    public InvalidType(){
        super("Invalid Type");
    }
}


class LendAccount extends BankAccount {


    public LendAccount(int nationalCodeOwner, int stock, int productionDate, int negativePoint) {
        super(nationalCodeOwner, stock, productionDate, negativePoint);
        setAccountNumber(getAccountNumber()+counter);
    }


}


class Loan {
    private int initialLoanAmount;
    private int numberOfInstallments;
    private int fixeNumberOfInstallments;

    public Loan(int initialLoanAmount, int numberOfInstallments, int fixeNumberOfInstallments) {
        this.initialLoanAmount = initialLoanAmount;
        this.numberOfInstallments = numberOfInstallments;
        this.fixeNumberOfInstallments = fixeNumberOfInstallments;
    }

    public int getInitialLoanAmount() {
        return initialLoanAmount;
    }

    public void setInitialLoanAmount(int initialLoanAmount) {
        this.initialLoanAmount = initialLoanAmount;
    }

    public int getFixeNumberOfInstallments() {
        return fixeNumberOfInstallments;
    }

    public void setFixeNumberOfInstallments(int fixeNumberOfInstallments) {
        this.fixeNumberOfInstallments = fixeNumberOfInstallments;
    }

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }
}


class MyExeption extends Exception{
    public MyExeption(String massage){
        super(massage);
    }
}


class NotEnoughMoney extends Exception{
    public NotEnoughMoney(){
        super(" NotEnoughMoney");
    }
}


class Person {
    private int nationalCode;
    private String name;
    private int age;
    private String gender;
    private int wallet;
    ArrayList<Loan>loans;

    ArrayList<BankAccount> bankAccounts;
    ArrayList<Estate>estates;


    public Person(int nationalCode, String name, int age, String gender,int wallet) {
        this.nationalCode = nationalCode;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.wallet=wallet;
        bankAccounts = new ArrayList<>();
        loans=new ArrayList<>();
        estates=new ArrayList<>();
    }





    public int getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(int nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}


class PersonPanel {
    BankManager bankManager = new BankManager();

    public void personMenu() throws IOException {
        Scanner scanner = new Scanner(System.in);


        System.out.println("1-Building account ");
        System.out.println("2-Deposit money");
        System.out.println("3-Receive Checkbook");
        System.out.println("4-Check receipt");
        System.out.println("5-Get a loan");
        System.out.println("6-Taking a deposit");
        int selectedCode = scanner.nextInt();

        switch (selectedCode) {
            case 1: {
                System.out.println("Specify account type");
                System.out.println("1-Current account");
                System.out.println("2-Lend account");
                System.out.println("3-Savings account");
                int accountCod = scanner.nextInt();
                switch (accountCod) {
                    case 1: {
                        System.out.println("Enter nationalCodeOwner,  stock,  productionDate,  negativePoint");
                        try {
                            int nationalCode = scanner.nextInt(), stock = scanner.nextInt(), productionDate = scanner.nextInt(), negativePoint = scanner.nextInt();

                            long accountNumber = bankManager.buildCurrentAccount(nationalCode, stock, productionDate, negativePoint);
                            System.out.println("Account Number:" + accountNumber);
                            System.out.println("Enter 1-Name & 2-Last Name to receive checkbook and cart");
                            String name = scanner.next(), lastName = scanner.next();
                            try {
                                bankManager.receiveCheckbook(nationalCode, accountNumber, name, lastName);
                                long cartNumber=bankManager.receiveCard(name, lastName, accountNumber);
                                System.out.println("Cart number:" +cartNumber);
                                int password=bankManager.getPassword(accountNumber);
                                System.out.println("Cart Password:" + password);
                                bankManager.write(accountNumber,nationalCode,stock,productionDate,negativePoint,password,cartNumber,true);
                            } catch (InvalidAccount e) {
                                e.printStackTrace();
                            }
                        } catch (InvalidNationalCode e) {
                            e.getStackTrace();
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("Enter nationalCodeOwner,  stock,  productionDate,  negativePoint");
                        try {
                            int nationalCode = scanner.nextInt(), stock = scanner.nextInt(), productionDate = scanner.nextInt(), negativePoint = scanner.nextInt();

                            long accountNumber = bankManager.buildLendAccount(nationalCode, stock, productionDate, negativePoint);
                            System.out.println("Account Number:" + accountNumber);
                            System.out.println("Enter 1-Name & 2-Last Name to receive  cart");
                            String name = scanner.next(), lastName = scanner.next();
                            try {
                                long cartNumber=bankManager.receiveCard(name, lastName, accountNumber);
                                System.out.println("Cart number:" +cartNumber);
                                int password=bankManager.getPassword(accountNumber);
                                System.out.println("Cart Password:" +password);
                                bankManager.write(accountNumber,nationalCode,stock,productionDate,negativePoint,password,cartNumber,true);

                            } catch (InvalidAccount e) {
                                e.printStackTrace();
                            }
                        } catch (InvalidNationalCode e) {
                            System.out.println(e.getStackTrace());
                        }
                        break;
                    }

                    case 3: {
                        System.out.println("Enter type nationalCodeOwner,  stock,  productionDate,  negativePoint");
                        try {
                            String type=scanner.next();
                            int nationalCode = scanner.nextInt(), stock = scanner.nextInt(), productionDate = scanner.nextInt(), negativePoint = scanner.nextInt();

                            long accountNumber = bankManager.buildSavingsAccount(type,nationalCode, stock, productionDate, negativePoint);
                            System.out.println("Account Number:" + accountNumber);
                            System.out.println("Enter 1-Name & 2-Last Name to receive  cart");
                            String name = scanner.next(), lastName = scanner.next();
                            try {
                                long cartNumber=bankManager.receiveCard(name, lastName, accountNumber);
                                System.out.println("Cart number:" +cartNumber);
                                int password=bankManager.getPassword(accountNumber);
                                System.out.println("Cart Password:" +password);
                                bankManager.write(accountNumber,nationalCode,stock,productionDate,negativePoint,password,cartNumber,true);
                            } catch (InvalidAccount e) {
                                e.printStackTrace();
                            }
                        } catch (InvalidType e) {
                            e.printStackTrace();
                        } catch (InvalidNationalCode e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                break;
            }

            case 2: {
                System.out.println("1- Via Bank Cart");
                System.out.println("2-via wallet");
                int depositCode = scanner.nextInt();
                if (depositCode == 1) {
                    System.out.println("Enter cartNumber_Receiver,  cardPassword_Sender,  money");
                    try {
                        bankManager.cardToCard(scanner.nextLong(), scanner.nextLong(), scanner.nextInt());
                    } catch (InsufficientInventory e) {
                        e.printStackTrace();
                    } catch (InvalidAccount e) {
                        e.printStackTrace();
                    }
                } else if (depositCode == 2) {
                    System.out.println("Enter nationalCode,  accountNumber,  money");
                    bankManager.depositFromWallet(scanner.nextInt(),scanner.nextLong(),scanner.nextInt());
                }
                break;
            }

            case 3: {
                System.out.println("Enter 1-nationalCode , 2-accountNumber , 3-name , 4-family");
                try {
                    bankManager.receiveCheckbook(scanner.nextInt(), scanner.nextLong(), scanner.next(), scanner.next());
                } catch (InvalidNationalCode e) {
                    e.printStackTrace();
                } catch (InvalidAccount e) {
                    e.printStackTrace();
                }
                break;
            }
            case 4: {
                System.out.println("Enter date (1401/2/20)");
                int year = scanner.nextInt();
                System.out.println("/");
                int minth = scanner.nextInt();
                System.out.println("/");
                int day = scanner.nextInt();
                try {
                    System.out.println("Enter amount,  accountNumberSender,  accountNumberReceiver");
                    bankManager.receiptCheck(day, minth, year, scanner.nextInt(), scanner.nextLong(), scanner.nextLong());
                } catch (InvalidAccount e) {
                    e.printStackTrace();
                } catch (InsufficientInventory e) {
                    e.printStackTrace();
                }
                break;
            }
            case 5: {
                System.out.println("Enter  nationalCode, numberOfInstallments(6 or 12) , loanAmount(100-1000000 Tomans)");
                try {
                    bankManager.getLoan(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
                } catch (InvalidNationalCode e) {
                    e.printStackTrace();
                } catch (MyExeption e) {
                    e.printStackTrace();
                }
                break;
            }
            case 6:{
                System.out.println("Enter  nationalCode");
                try {
                    if (bankManager.takingDeposit(scanner.nextInt())) {
                        System.out.println("The interest on your deposit has been credited to your account");
                    }
                    else
                        System.out.println("The principal amount of your deposit has been credited to your account");
                } catch (InvalidNationalCode e) {
                    e.printStackTrace();
                }
            }

        }

    }
}


class SavingsAccount extends BankAccount {
    private float BankInterestPercentage;
    private String type;
    private int designatedTime;

    int countDays=0;

    public SavingsAccount(String type, int nationalCodeOwner, int stock, int productionDate, int negativePoint) throws InvalidType {
        super(nationalCodeOwner, stock, productionDate, negativePoint);
        setType(type);
        setBankInterestPercentage();
        setDesignatedTime();
        setAccountNumber(getAccountNumber()+counter);
    }

    public void setType(String type) throws InvalidType {
        if (type.equals("shortTerm") || type.equals("longTerm") || type.equals("special"))
            this.type = type;
        else
            throw new InvalidType();
    }

    public void setBankInterestPercentage() {
        if (type.equals("shortTerm"))
            BankInterestPercentage = 0.1f;

        else if (type.equals("longTerm"))
            BankInterestPercentage = 0.3f;

        else if (type.equals("special"))
            BankInterestPercentage = 0.5f;

    }

    public void setDesignatedTime() {
        if (type.equals("shortTerm"))
            designatedTime = 10;

        else if (type.equals("longTerm"))
            designatedTime = 30;

        else if (type.equals("special"))
            designatedTime = 50;

    }

    public float getBankInterestPercentage() {
        return BankInterestPercentage;
    }


    public String getType() {
        return type;
    }

    public int getDesignatedTime() {
        return designatedTime;
    }


}

