import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc7.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        // System.out.println(aoc.countCombinations(input));
        //Part 2
        System.out.println(aoc.countTotalBags(input));
    }
}

class AOC {
    private HashMap<String, Bag> bags = new HashMap<>();

    public class Bag {
        private String name;
        private List<Bag> contains;

        public Bag(String name) {
            this.name = name;
            this.contains = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Bag> getContains() {
            return contains;
        }

        public void setContains(List<Bag> contains) {
            this.contains = contains;
        }

        public void addBag(Bag bag){
            this.contains.add(bag);
        }
    }

    final String SHINY_GOLD = "shiny gold";
    //Day 7 - Part 2
    long countTotalBags(List<String> bagList) {
        HashMap<String, HashMap<String, Integer>> bagsMap = new HashMap<>();
        Stack<String> containers = new Stack<>();

        for (String bagInfo : bagList) {
            Matcher splitContainMatcher = Pattern.compile("(.*)bags contain(.*)").matcher(bagInfo.replace('.', ' ').trim());
            while (splitContainMatcher.find()) {

                String containerBagName = splitContainMatcher.group(1).trim();
                String bagContains = splitContainMatcher.group(2).trim();
                Bag containerBag = getBag(containerBagName);

                Matcher splitCommaMatcher = Pattern.compile("([^,]+)").matcher(bagContains);
                while (splitCommaMatcher.find()) {
                    String bagDetails = splitCommaMatcher.group()
                            .replaceAll("( bag(s?)|\\\\.)", "")
                            .trim();
                    Matcher countMatcher = Pattern.compile("(\\d+)(.*)").matcher(bagDetails);

                    if(!countMatcher.find()){
                        continue;
                    }

                    int bagCount = Integer.parseInt(countMatcher.group(1));
                    String bagName = countMatcher.group(2).trim();

                    Bag bag = getBag(bagName);
                    for (int i = 0; i < bagCount; i++) {
                        containerBag.addBag(bag);
                    }
                }
            }
        }

        return bagsInside(getBag(SHINY_GOLD),0L);
    }

    private long bagsInside(Bag bag,long bagCount){
        bagCount+=bag.contains.size();
        for(Bag insideBag:bag.contains){
            bagCount =bagsInside(insideBag,bagCount);
        }

        return bagCount;
    }

    private Bag getBag(String containerBagName) {
        if(!this.bags.containsKey(containerBagName)){
            this.bags.put(containerBagName,new Bag(containerBagName));
        }

        return this.bags.get(containerBagName);
    }


    //Day 7 - Part 1
    int countCombinations(List<String> bags){
        HashMap<String,List<String>> bagsMap = new HashMap<>();
        Stack<String> containers = new Stack<>();

        for (String bagInfo:bags) {
            Matcher splitContainMatcher = Pattern.compile("(.*)bags contain(.*)").matcher(bagInfo.replace('.',' ').trim());
            while(splitContainMatcher.find()){
                String containerBag = splitContainMatcher.group(1).trim();
                String bagContains = splitContainMatcher.group(2).trim();

                Matcher splitCommaMatcher = Pattern.compile("([^,]+)").matcher(bagContains);
                if(bagContains.contains("no other bags")){
                    continue;
                }

                while(splitCommaMatcher.find()) {
                    String bag = splitCommaMatcher.group()
                            .replaceAll("bags","")
                            .replaceAll("bag","")
                            .replaceAll("[\\d-]", "").trim();

                    if(!bagsMap.containsKey(bag)) {
                        bagsMap.put(bag,new ArrayList<>());
                    }
                    bagsMap.get(bag).add(containerBag);
                    if (bag.contains(SHINY_GOLD)) {
                        containers.push(containerBag);
                    }
                }
            }
        }

        HashSet<String> set = new HashSet<>();

        while (!containers.empty()){
            String container = containers.pop();
            set.add(container);
            if(!bagsMap.containsKey(container)){
                continue;
            }
            List<String> list = bagsMap.get(container);
            for (String item:list) {
                containers.push(item);
            }
            bagsMap.remove(container);

        }


        return set.size();
    }

}


