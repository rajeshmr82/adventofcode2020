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
        System.out.println(aoc.getCupLabel("389125467",100,9));
        //Part 2
        //System.out.println(aoc.getCupLabel("389125467",100,9));
        //test - 389125467
        //System.out.println(aoc.getCupLabelMillion("389125467",10000000,1000000));
        System.out.println(aoc.getCupLabelMillion("789465123",10000000,1000000));
        //aoc.ringTest();
    }


}

class AOC {

    public void ringTest() {
        Ring ring = new Ring();
        ring.add(3);
        ring.add(8);
        ring.add(1);
        ring.add(2);
        ring.add(5);
        ring.add(4);
        ring.add(6);
        ring.add(7);
        ring.print(null);
        System.out.println(ring.find(4).next().number);
        System.out.println(ring.find(4).previous().number);
        ring.insertAfter(8,9);
        System.out.println(ring.find(9).next().number);
        System.out.println(ring.find(9).previous().number);
        ring.remove(9);
        System.out.println(ring.find(8).next().number);
        System.out.println(ring.find(1).previous().number);
        ring.remove(7);
        System.out.println("After removing 7 ");
        ring.print( null);
        System.out.println(ring.find(6).next().number);
        System.out.println(ring.find(3).previous().number);
        ring.remove(3);
        ring.print(null);
        System.out.println(ring.find(6).next().number);
        System.out.println(ring.find(8).previous().number);
    }

    static class Cup {
        long number;
        private Cup next;
        private Cup prev;
        public Cup(long number){
            this.number = number;
        }

        public Cup next(){
            return next;
        }

        public Cup previous(){
            return prev;
        }
    }

    static class Ring {
        public int size = 0;
        public Cup last = null;
        private final HashMap<Long,Cup> cache = new HashMap<>();

        public void add(long number) {
            Cup newCup = new Cup(number);
            if (last == null) {
                last = newCup;
                newCup.next = last;
                newCup.prev = last;
            } else {
                newCup.next = last.next;
                last.next = newCup;
                newCup.prev = last;
                last = newCup;
                last.next.prev = newCup;
            }
            cache.put(number, newCup);
        }

        public void insertAfter(long cup, long number) {
            if (last == null)
                return;

            Cup target = cache.get(cup);
            Cup newCup = new Cup(number);
            newCup.next = target.next;
            newCup.prev = target;
            target.next = newCup;
            newCup.next.prev = newCup;
            cache.put(number,newCup);

            /*Cup newCup, current;
            current = last.next;
            do {
                if (current.number == cup) {
                    newCup = new Cup(number);
                    newCup.next = current.next;
                    current.next = newCup;

                    if (current == last){
                        last = newCup;
                    }
                    cache.put(number,newCup);
                    return;
                }
                current = current.next;
            } while (current != last.next);*/

        }

        public Cup find(long item) {
            if(cache.containsKey(item)){
                return cache.get(item);
            }

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

        public Cup remove(long item) {

            if (last == null){
                return null;
            }

            var current = cache.get(item);
            var prev = current.prev;

            if (current == last && current.next == last) {
                last = null;
                return null;
            }

            if (current == last.next) {
                last.next = current.next;
                current.next.prev = last;

            } else if (current == last) {
                prev.next = last.next;
                last.next.prev = prev;
                last = prev;
            } else {
                current.next.prev = prev;
                prev.next = current.next;
            }

            cache.remove(item);

            return new Cup(item);
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

        public long getProduct() {
            var one = cache.get(1L);
            var two = one.next();
            var three = two.next();
            System.out.println("One:"+one.number);
            System.out.println("Two:"+two.number);
            System.out.println("three:"+three.number);
            return two.number * three.number;
        }
    }

    final int CUPS_TO_PICK = 3;

    public long getCupLabelMillion(String s,int totalMoves, int totalCount) {
        Ring ring = prepareRing(s, totalCount);
        shuffleCups(totalMoves, totalCount, ring);

        return ring.getProduct();
    }

    public String getCupLabel(String s,int totalMoves, int totalCount) {
        Ring ring = prepareRing(s, totalCount);

        shuffleCups(totalMoves, totalCount, ring);

        return ring.getStateLabel();
    }

    private Ring prepareRing(String s, int totalCount) {
        Ring ring= new Ring();
        IntStream.range(0, s.length()).forEach(i -> ring.add(Integer.parseInt(s.substring(i, i + 1))));

        for (int i = s.length(); i < totalCount; i++) {
            ring.add(i+1);
        }
        return ring;
    }

    private void shuffleCups(int totalMoves, int totalCount, Ring ring) {
        Cup current = ring.first();
        for (int i = 0; i < totalMoves; i++) {
            if((i+1) % 10000==0){
                System.out.printf("-- move %d --%n", i + 1);
            }

            List<Long> pick = new ArrayList<>();
            Cup currPick = current.next();
            for (int j = 0; j < CUPS_TO_PICK; j++) {
                pick.add(currPick.number);
                currPick = currPick.next();
                ring.remove(pick.get(j));
            }
            var destination = 1 + ((totalCount + current.number - 2) % totalCount);

            while (pick.contains(destination)) {
                destination = 1 + ((totalCount + destination - 2) % totalCount);
            }

            for (int j = 0; j < CUPS_TO_PICK; j++) {
                ring.insertAfter(destination, pick.get(j));
                destination = pick.get(j);
            }

            current = current.next();
        }
    }

}
