import java.util.Scanner;

public class Main
{
    public static boolean loop = true;
    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);
        
        while (loop) 
        {
            _Skeleton.WriteToConsoleSkeletonOptions();
            String userValue = s.nextLine();
            _Skeleton.GetFromConsoleSkeletonOptions(userValue);
        }
        s.close();
    }
}