import java.io.*;
import java.util.*;
import java.util.stream.IntStream;


class Solution {
    public static void main(String[] args) throws IOException {
        /*File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc22.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }*/

        AOC aoc = new AOC();
        //Part 1
        //789465123
        //simple - 389125467
        System.out.println(aoc.getCupLabels("789465123"));
        //Part 2
        //System.out.println(aoc.winnerScoreRecursiveCombat(input));
    }
}

class AOC {

    static class Cup {
        int number;
        private Cup next;
        public Cup(int number){
            this.number = number;
        }

        public Cup next(){
            return next;
        }
    }

    static class Ring {
        public int size = 0;
        public Cup last = null;

        public void add(int number) {
            Cup newCup = new Cup(number);
            if (last == null) {
                last = newCup;
                newCup.next = last;
            } else {
                newCup.next = last.next;
                last.next = newCup;
                last = newCup;
            }
        }

        public void insertAfter(int cup, int number) {
            if (last == null)
                return;

            Cup newCup, current;
            current = last.next;
            do {
                if (current.number == cup) {
                    newCup = new Cup(number);
                    newCup.next = current.next;
                    current.next = newCup;

                    if (current == last)
                        last = newCup;
                    return;
                }
                current = current.next;
            } while (current != last.next);

        }

        public Cup find(int item) {
            if (last == null)
                return null;

            Cup current = last.next;
            do {
                if (current.number == item) {
                    return current;
                }
                current = current.next;
            } while (current != last.next);

            return null;
        }

        public Cup first() {
            return last.next;
        }

        public Cup remove(int item) {

            if (last == null)
                return null;

            Cup current = last.next, prev = null;
            while (current.number != item) {
                prev = current;
                current = current.next;
            }

            if (current == last && current.next == last) {
                last = null;
                return null;
            }

            if (current == last.next) {
                prev = last.next;
                while (prev.next != last.next) {
                    prev = prev.next;
                }
                last.next = current.next;
                prev.next = last.next;
            } else if (current == last) {
                prev.next = last.next;
            } else {
                prev.next = current.next;
            }
            return last.next;

        }

        public void print(Cup current) {

            if (last == null) {
                System.out.println("Ring is empty");
            } else {
                Cup iteration = last.next;
                System.out.print("Cups : ");
                do {
                    if (current != null && iteration == current) {
                            System.out.print(" (" + iteration.number + ")");
                    } else {
                        System.out.print(" " + iteration.number);
                    }
                    iteration = iteration.next;
                } while (iteration != last.next);
                System.out.println();
            }
        }

        public String getStateLabel(){
            StringBuilder result= new StringBuilder();
            var current = find(1);
            while (current.next().number!=1){
                if(current.number!=1){
                    result.append(current.number);
                }
                current=current.next();
            }
            result.append(current.number);
            return "Result:"+result;
        }
    }

    List<Integer> cups= new ArrayList<>();
    final int TOTAL_MOVES = 100;
    final int CUPS_TO_PICK = 3;
    public String getCupLabels(String s) {
        int n = s.length();
        Ring ring= new Ring();
        IntStream.range(0, n).forEach(i -> ring.add(Integer.parseInt(s.substring(i, i + 1))));
        Cup current = ring.first();
        for (int i = 0; i < TOTAL_MOVES; i++) {
            System.out.printf("-- move %d --%n", i + 1);
            ring.print(current);
            List<Integer> pick = new ArrayList<>();
            Cup currPick = current.next();
            for (int j = 0; j < CUPS_TO_PICK; j++) {
                pick.add(currPick.number);
                currPick = currPick.next();
                ring.remove(pick.get(j));
            }
            System.out.println("pick up: " + pick);


            var destination = 1 + ((n + current.number - 2) % n);

            while (pick.contains(destination)) {
                destination = 1 + ((n + destination - 2) % n);
            }

            System.out.printf("destination: %d%n", destination);
            for (int j = 0; j < CUPS_TO_PICK; j++) {
                ring.insertAfter(destination, pick.get(j));
                destination = pick.get(j);
            }

            ring.print(current);

            current = current.next();
        }

        return ring.getStateLabel();
    }

    private void printCups(int current) {
        StringBuilder print = new StringBuilder("cups:");
        for (int i = 0; i < cups.size(); i++) {
            String format = " %d ";
            if (i == current) {
                format = " (%d) ";
            }

            print.append(String.format(format, cups.get(i)));
        }
        System.out.println(print);
    }
}
