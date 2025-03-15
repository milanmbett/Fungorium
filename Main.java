import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);


        
        boolean loop = true;

        while (loop) 
        {
            System.out.println("------------------------");
            System.out.println("Opciók: ");
            System.out.println("1:  Gomba lerakása");
            System.out.println("2:  Rovar mozgatása");
            System.out.println("3:  Rovar lerakása");
            System.out.println("4:  Gomba fejlesztése ");
            System.out.println("5:  Játék incializálása");
            System.out.println("6:  Játék vége és kiértékelés");
            System.out.println("7:  Rovar támad gombát");
            System.out.println("8:  Gomba támad rovart");
            System.out.println("9:  Spóra generálása");
            System.out.println("10: Spóra megevése");
            System.out.println("11: Gombafonal megevése");
            System.out.println("12: Tekton kettétörése");
            System.out.println("13: Tekton elpuszítása");
            System.out.println("14: Gombafonal terjedése");
            System.out.println("15: Fonál terjedése");
            System.out.println("EXIT: Kilépés");
            System.out.println("------------------------");
            String userValue = s.nextLine();
            switch (userValue.toLowerCase()) 
            {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    break;
                case "8":
                    break;
                case "9":
                    break;
                case "10":
                    break;
                case "11":
                    break;
                case "12":
                    break;
                case "13":
                    break;
                case "14":
                    break;
                case "15":
                    break;
                case "exit":
                    loop = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Ismeretlen parancs!");
                    break;
            }
        }
        s.close();
    }
}