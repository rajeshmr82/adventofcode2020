import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc24.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        //Part 1
        System.out.println(aoc.countBlackTiles(input));
    }


}

class AOC {
    static class Grid {
        HashSet<Pair<Integer,Integer>> blackTiles = new HashSet<>();

        public void flip(Pair<Integer,Integer> tile){
            System.out.println("Flipping tile: "+tile.toString() + " " + blackTiles.contains(tile));
            if(blackTiles.contains(tile)){
                blackTiles.remove(tile);
            }else{
                blackTiles.add(tile);
            }
        }

        public int getFlippedCount(){
            System.out.println(blackTiles);
            return blackTiles.size();
        }
    }

    Grid grid = new Grid();

    final String NORTH_EAST = "ne";
    final String NORTH_WEST = "nw";
    final String SOUTH_EAST = "se";
    final String SOUTH_WEST = "sw";
    final String EAST = "e";
    final String WEST = "w";

    public int countBlackTiles(List<String> input) {
        for (var line:
             input) {
            Pair<Integer, Integer> tile = Pair.of(0, 0);
            while (line.length() > 0) {
                if (line.startsWith(EAST)) {
                    tile = Pair.of(tile.getLeft() + 1, tile.getRight());
                    line = line.substring(1);
                } else if (line.startsWith(WEST)) {
                    tile = Pair.of(tile.getLeft() - 1, tile.getRight());
                    line = line.substring(1);
                } else if (line.startsWith(NORTH_EAST)) {
                    tile = Pair.of(tile.getLeft(), tile.getRight() + 1);
                    line = line.substring(2);
                } else if (line.startsWith(NORTH_WEST)) {
                    tile = Pair.of(tile.getLeft() - 1, tile.getRight() + 1);
                    line = line.substring(2);
                } else if (line.startsWith(SOUTH_EAST)) {
                    tile = Pair.of(tile.getLeft() + 1, tile.getRight() - 1);
                    line = line.substring(2);
                } else if (line.startsWith(SOUTH_WEST)) {
                    tile = Pair.of(tile.getLeft(), tile.getRight() - 1);
                    line = line.substring(2);
                }
            }
            System.out.println(tile);
            grid.flip(tile);
        }

        return grid.getFlippedCount();
    }
}
