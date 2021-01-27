import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc25.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println(aoc.findEncryptionKey(input));
        //Part 2
        //System.out.println(aoc.countBlackTilesAfter100Days(input));
    }


}

class AOC {
    long cardPubKey =0;
    long doorPubKey =0;
    final long DIVIDER = 20201227L;
    final long SUBJECT_NUMBER=7L;

    public long findEncryptionKey(List<String> input) {
        cardPubKey = Long.parseLong(input.get(0));
        doorPubKey = Long.parseLong(input.get(1));

        var doorLoopSize = loop(cardPubKey);
        System.out.println(doorLoopSize);
        var cardLoopSize = loop(doorPubKey);
        System.out.println(cardLoopSize);

        long cardEncryption =loopAndTransform(cardLoopSize,cardPubKey);
        System.out.println(cardEncryption);
        long doorEncryption =loopAndTransform(doorLoopSize,doorPubKey);
        System.out.println(doorEncryption);
        return 0;
    }

    private long loopAndTransform(long loopSize,long pubKey) {
        long encryption =1;
        for (int i = 0; i < loopSize; i++) {
            encryption *= pubKey;
            encryption %= DIVIDER;
        }

        return encryption;
    }

    private long loop(long pubKey) {
        long value=1,i=1;
        while (true){
            value *=SUBJECT_NUMBER;
            value %=DIVIDER;
            if(value==pubKey){
                return i;
            }
            i++;
        }
    }

}
