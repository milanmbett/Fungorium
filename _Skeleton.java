
public class _Skeleton 
{
 

    public static void WriteToConsoleSkeletonOptions()
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
    }
    public static void GetFromConsoleSkeletonOptions(String s)
    {
        switch (s.toLowerCase()) 
        {
            case "1": _SkeletonMethods.skeleton_1();
                break;
            case "2": _SkeletonMethods.skeleton_2();
                break;
            case "3": _SkeletonMethods.skeleton_3();
                break;
            case "4": _SkeletonMethods.skeleton_4();
                break;
            case "5": _SkeletonMethods.skeleton_5();
                break;
            case "6": _SkeletonMethods.skeleton_6();
                break;
            case "7": _SkeletonMethods.skeleton_7();
                break;
            case "8": _SkeletonMethods.skeleton_8();
                break;
            case "9": _SkeletonMethods.skeleton_9();
                break;
            case "10": _SkeletonMethods.skeleton_10();
                break;
            case "11": _SkeletonMethods.skeleton_11();
                break;
            case "12": _SkeletonMethods.skeleton_12();
                break;
            case "13": _SkeletonMethods.skeleton_13();
                break;
            case "14": _SkeletonMethods.skeleton_14();
                break;
            case "15": _SkeletonMethods.skeleton_15();
                break;
            case "exit":
                System.out.println("Goodbye!");
                Main.loop = false;
                break;
            default:
                System.out.println("Ismeretlen parancs!");
                break;
        }
    }
    
}
