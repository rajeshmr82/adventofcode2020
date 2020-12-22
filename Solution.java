import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile=new File(Solution.class.getClassLoader().getResource("aoc4.txt").getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        String record="";
        while ((line = reader.readLine()) != null) {
            if(!line.isEmpty()){
                record+=" " + line;
            }else {
                input.add(record);
                record="";
            }
        }

        AOC aoc= new AOC();
       System.out.println(aoc.countValidPassport(input));
    }
}

class AOC {
    //Day 4
    int countValidPassport(List<String> passports){
        int count=0;
        for (String passport:
             passports) {
            if(isValidPassport(passport)
            )
                if(isValidPassportFields(passport)){
                    count++;
                }

        }

        return count;
    }

    private boolean isValidPassportFields(String passport) {
        Map<String, String> map = new HashMap<String, String>();
        Pattern pattern = Pattern.compile("(\\w+):?([^ ]+)?");
        Matcher matcher = pattern.matcher(passport);
        while (matcher.find()) {
            map.put(matcher.group(1),matcher.group(2));
        }

        String byr= map.get("byr");
        Pattern byrPattern = Pattern.compile("^(19[2-9][0-9]|2[0][0][0-2])$");
        if(!byrPattern.matcher(byr).matches())
            return false;

        String iyr= map.get("iyr");
        Pattern iyrPattern = Pattern.compile("^(2[0][1][0-9]|2[0][2][0])$");
        if(!iyrPattern.matcher(iyr).matches())
            return false;

        String eyr= map.get("eyr");
        Pattern eyrPattern = Pattern.compile("^(2[0][2][0-9]|2[0][3][0])$");
        if(!eyrPattern.matcher(eyr).matches())
            return false;

        String hgt= map.get("hgt");
        Pattern hgtPattern = Pattern.compile("^(1[5-9][0-9]cm|[5][9]in|[6-7][0-9]in)$");
        if(!hgtPattern.matcher(hgt).matches())
            return false;

        String hcl= map.get("hcl");
        Pattern hclPattern = Pattern.compile("^(#[0-9a-f]{6})$");
        if(!hclPattern.matcher(hcl).matches())
            return false;

        String ecl= map.get("ecl");
        Pattern eclPattern = Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$");
        if(!eclPattern.matcher(ecl).matches())
            return false;

        String pid= map.get("pid");
        Pattern pidPattern = Pattern.compile("^(\\d{9})$");
        if(!pidPattern.matcher(pid).matches())
            return false;

        return true;
    }

    private boolean isValidPassport(String passport) {
        return passport.contains("byr") &&
                passport.contains("iyr") &&
                passport.contains("eyr") &&
                passport.contains("hgt") &&
                passport.contains("hcl") &&
                passport.contains("ecl") &&
                passport.contains("pid");
    }

}


