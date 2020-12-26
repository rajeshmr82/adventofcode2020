import java.io.*;
import java.util.*;
import java.util.stream.IntStream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc11.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<String> input = new ArrayList<>();
        int max=0;
        while ((line = reader.readLine()) != null) {
            input.add(line.replaceAll("L","#"));
        }

        AOC aoc = new AOC();
        //Part 1
        //System.out.println("Part 1 - " + aoc.countOccupiedSeats(input));
        //Part 2
        System.out.println("Part 2 - " + aoc.countOccupiedSeatsBasedOnVisibility(input));
    }
}

class AOC {
    final Character EMPTY = 'L';
    final Character OCCUPIED = '#';
    final Character FLOOR ='.';

    //Part 2
    int countOccupiedSeatsBasedOnVisibility(List<String> map){
        List<String> newMap = new ArrayList<>();
        int n=map.size();
        int w = map.get(0).length();

        int seatCount = 0;
        while (true) {
            boolean hasChanged=false;
            for (int i = 0; i < n; i++) {
                char[] newLine = map.get(i).toCharArray();
                for (int j = 0; j < w; j++) {
                    if(newLine[j]==FLOOR){
                        continue;
                    }
                    int adjCount = adjacentVisibleCount(map, n, w, i, j);
                    if (newLine[j]==OCCUPIED && adjCount>=5) {
                        newLine[j] = EMPTY;
                        hasChanged=true;
                    }else if(newLine[j]==EMPTY && adjCount==0){
                        newLine[j] = OCCUPIED;
                        hasChanged=true;
                    }
                }
                newMap.add(i, String.valueOf(newLine));

                seatCount+=  IntStream.range(0, newLine.length)
                        .mapToObj(c -> newLine[c])
                        .filter(s -> s==OCCUPIED)
                        .count();
            }
            if(!hasChanged){
                break;
            }else{
                seatCount=0;
                map =new ArrayList<>(newMap);
                newMap.clear();
            }

        }

        return seatCount;
    }

    //Part 1
    int countOccupiedSeats(List<String> map){
        List<String> newMap = new ArrayList<>();
        int n=map.size();
        int w = map.get(0).length();

        int seatCount = 0;
        while (true) {
            boolean hasChanged=false;
            for (int i = 0; i < n; i++) {
                char[] newLine = map.get(i).toCharArray();
                for (int j = 0; j < w; j++) {
                    if(newLine[j]==FLOOR){
                        continue;
                    }
                    int adjCount = adjacentCount(map, n, w, i, j);
                    if (newLine[j]==OCCUPIED && adjCount>=4) {
                        newLine[j] = EMPTY;
                        hasChanged=true;
                    }else if(newLine[j]==EMPTY && adjCount==0){
                        newLine[j] = OCCUPIED;
                        hasChanged=true;
                    }
                }
                newMap.add(i, String.valueOf(newLine));

                seatCount+=  IntStream.range(0, newLine.length)
                        .mapToObj(c -> newLine[c])
                        .filter(s -> s==OCCUPIED)
                        .count();
            }
            if(!hasChanged){
                break;
            }else{
                seatCount=0;
                map =new ArrayList<>(newMap);
                newMap.clear();
            }

        }

        return seatCount;
    }

    private int adjacentCount(List<String> map, int n, int w, int i, int j) {
        int count = 0;
        for (int r = -1; r <= 1; r++) {
            if(i +r<0){
                continue;
            }else if(i +r>= n){
                break;
            }
            for (int c = -1; c <=1 ; c++) {
                if(j +c<0 || (r==0 && c==0)){
                    continue;
                }else if(j +c>= w){
                    break;
                }

                if(map.get(i +r).charAt(j +c)==OCCUPIED){
                    count++;
                }
            }
        }

        return count;
    }

    private int adjacentVisibleCount(List<String> map, int n, int w, int i, int j) {
        int count = 0;
        int h=i;
        int v=j;
        while(--h>=0){
            if(map.get(h).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }
        h=i;
        v=j;
        while(++h<n){
            if(map.get(h).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }
        h=i;
        v=j;
        while (--v>=0){
            if(map.get(h).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }
        v=j;
        while (++v<w){
            if(map.get(i).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }

        h=i;
        v=j;
        while(--h>=0 && --v>=0) {
            if(map.get(h).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }
        h=i;
        v=j;
        while(++h<n && ++v<w) {
            if(map.get(h).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }

        h=i;
        v=j;
        while(--h>=0 && ++v<w) {
            if(map.get(h).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }

        h=i;
        v=j;
        while(++h<n && --v>=0) {
            if(map.get(h).charAt(v)==EMPTY){
                break;
            }
            if(map.get(h).charAt(v)==OCCUPIED){
                count++;
                break;
            }
        }

        return count;
    }

}


