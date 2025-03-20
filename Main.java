import java.util.Scanner;

public class Main
{
    public static Scanner _scanner = new Scanner(System.in);
    public static boolean loop = true;
    public static void main(String[] args)
    {
        while (loop) 
        {
            _Skeleton.WriteToConsoleSkeletonOptions();
            String userValue = _scanner.nextLine();
            _Skeleton.GetFromConsoleSkeletonOptions(userValue);
        }
        _scanner.close();
    }
}