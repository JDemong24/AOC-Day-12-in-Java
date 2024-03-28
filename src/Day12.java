import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Day12{
    private static final String FILE_NAME = "DataDay12.dat";

    /**
     * Reads the data from a CSV file and prints it to the console.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        Day12 solver = new Day12();
        
        try
        {
            InputStream stream =
                Day12.class.getClassLoader().getResourceAsStream(FILE_NAME);
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(stream));


            int total = 0;

            
            while (reader.ready())  
            {  
                String line = reader.readLine();
                String[] elements = line.split(" ");

                String pattern = elements[0];

                String[] numString = elements[1].split(",");
                int size = numString.length;

                int[] nums = new int[size];
                for(int i = 0; i < size; i++){
                    nums[i] = Integer.parseInt(numString[i]);
                }
                
                total = total + solver.countPatterns(pattern, nums);

                System.out.println(total);
            }
            
            reader.close();
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println("File not found: " + fnfe.getMessage());
        }
        catch (IOException ioe)
        {
            System.out.println("Error reading line: " + ioe.getMessage());
        }        
    }


    public int countPatterns(String pattern, int[] groups){
        int location = pattern.indexOf("?");
        if(location == -1){
            if(validatePattern2(pattern, groups)){
                return 1;
            }
            return 0;
        }

        String start = pattern.substring(0, location);
        String end = pattern.substring(location + 1);

        return countPatterns(start + "#" + end, groups) + countPatterns(start + "." + end, groups);
    }

    private boolean validatePattern2(String string, int[] groups){
        char[] charArray = string.toCharArray();

        int count = 0;

        ArrayList<Integer> stringGroups = new ArrayList<Integer>();

        boolean addCountAgain = true;


        for(int i = 0; i < charArray.length; i++){
            if(charArray[i] == '#'){
                count++;
                addCountAgain = true;
            }else if(charArray[i] == '.'){
                if(count > 0){
                    stringGroups.add(count);
                }
                addCountAgain = false;

                count = 0;
            }
        }
        if(addCountAgain){
            stringGroups.add(count);
        }

        if(stringGroups.size() != groups.length){
            return false;
        }

        for(int i = 0; i < stringGroups.size(); i++){
            if(stringGroups.get(i) != groups[i]){
                return false;
            }
        }
        return true;
    }

    private boolean validatePattern(String pattern, int[] groups, int groupIndex){
        int groupsLength = groups.length;
        // int groupIndex = 0;
        int firstHash = pattern.indexOf("#");
        String changedPattern = pattern.substring(firstHash);
        int hashCounter = 0;
        int index = 0;

        if(firstHash == -1){
            return false;
        }

        while(index < changedPattern.length() && changedPattern.charAt(index) == '#'){
            hashCounter ++;
            index ++;
        }

        while(groupIndex <= groupsLength){
            if(hashCounter != groups[groupIndex]){
                return false;
            }

            if((firstHash + hashCounter) < changedPattern.length()-1 && hashCounter == groups[groupIndex]){
                changedPattern = changedPattern.substring(firstHash + hashCounter);
                if(groups.length-1 > groupIndex){
                    validatePattern(changedPattern,groups,groupIndex+1);
                }else{
                    return false;
                }
                
            }else{
                if(groups[groups.length - 1] == hashCounter){
                    return true;
                }else{
                    return false;
                }
            }
        }

        return true;
    }
}