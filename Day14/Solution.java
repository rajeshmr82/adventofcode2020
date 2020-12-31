import javax.sound.midi.Soundbank;
import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc14.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        int earliestTime = -1;
        List<String> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println("Part 1 - " + aoc.findSumOfMemoryLeft(input));
        //Part 2
        System.out.println("Part 2 - " + aoc.findSumOfMemoryLeftV2(input));
    }
}

class AOC {
    long findSumOfMemoryLeftV2(List<String> input){
        long sum=0;
        HashMap<String,Long> memory= new HashMap<String, Long>();
        char[] mask = new char[1];
        for (String line: input) {
            if(line.startsWith("mask")){
                mask = line.split("=")[1].trim().toCharArray();
            }else if(line.startsWith("mem")){
                Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(line);
                String key="";
                while(m.find()) {
                    key = m.group(1);
                }
                String binaryKey = String.format("%36s",
                        Long.toBinaryString(Long.parseLong(key)))
                        .replace(" ", "0");
                        //.toCharArray();
                long number = Long.parseLong(line.split("=")[1].trim());
                List<String> addresses = new ArrayList<>();
                addresses.add(binaryKey);
                for (int i = 0; i < mask.length; i++) {
                    for (int j = addresses.size()-1; j >=0; j--) {
                        char[] edit = addresses.remove(j).toCharArray();
                        if(mask[i]=='1'){
                            edit[i]=mask[i];
                            addresses.add(String.valueOf(edit));
                        }else if(mask[i]=='0'){
                            addresses.add(String.valueOf(edit));
                        }else if(mask[i]=='X'){
                            edit[i]='0';
                            addresses.add(String.valueOf(edit));
                            char[] copy = edit.clone();
                            copy[i]='1';
                            addresses.add(String.valueOf(copy));
                        }
                    }
                }
                for (String address: addresses) {
                    if(memory.containsKey(address)){
                        sum -=memory.get(address);
                    }
                    sum +=number;
                    memory.put(address,number);
                }
            }
        }

        return sum;
    }

    long findSumOfMemoryLeft(List<String> input){
        HashMap<String,Long> memory= new HashMap<String, Long>();
        long sum=0;
        char[] mask = new char[1];

        for (String line: input) {
            if(line.startsWith("mask")){
                mask = line.split("=")[1].trim().toCharArray();
            }else if(line.startsWith("mem")){
                Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(line);
                String key="";
                while(m.find()) {
                    key = m.group(1);
                }
                String binary = String.format("%36s",
                        Long.toBinaryString(Long.parseLong(line.split("=")[1].trim())))
                            .replace(" ", "0");
                long number = getMaskedValue(mask,binary);
                if(memory.containsKey(key)){
                    sum -=memory.get(key);
                }
                sum +=number;
                memory.put(key,number);
            }
        }

        return sum;
    }

    long getMaskedValue(char[] mask,String binary){
        long result = 0;
        for (int i = 0; i < mask.length; i++) {
            if(mask[i]!='X'){
                result = (result << 1) | Integer.parseInt(String.valueOf(mask[i]));
            }else {
                result = (result << 1) | Integer.parseInt(String.valueOf(binary.charAt(i)));
            }
        }

        return result;
    }
}
