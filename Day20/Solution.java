import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class Solution {
    public static void main(String[] args) throws IOException {
        File inputFile = new File(Objects.requireNonNull(Solution.class.getClassLoader().getResource("aoc20_simple.txt")).getFile());
        InputStream inputStream = new FileInputStream(inputFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        List<String> input=new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            input.add(line);
        }

        AOC aoc = new AOC();
        aoc.parseInput(input);
        //Part 1
        //System.out.println(aoc.findCornerSum());
        //Part 2
        System.out.println(aoc.countSeaBits());
        //System.out.println("Part 2 - " + aoc.findValidInputsWithUpdates(input));

    }
}

class AOC {
    final int EDGE_TOP = 0;
    final int EDGE_BOTTOM = 1;
    final int EDGE_LEFT = 2;
    final int EDGE_RIGHT = 3;

    class Tile {

        private String tileId;
        List<List<Boolean>> bits = new ArrayList<>();
        List<List<Boolean>> cropped = new ArrayList<>();

        public List<List<Boolean>> getBits() {
            return bits;
        }

        public void setBits(List<List<Boolean>> bits) {
            this.bits = bits;
        }

        public String getTileId() {
            return tileId;
        }

        public void setTileId(String tileId) {
            this.tileId = tileId;
        }

        public Edge[] getEdges() {
            int top = 0, bottom = 0, left = 0, right = 0;
            for (int i = 0; i < bits.size(); i++) {
                if (bits.get(0).get(i)) {
                    top |= 1 << i;
                }
                if (bits.get(bits.size() - 1).get(i)) {
                    bottom |= 1 << i;
                }

                if (bits.get(i).get(0)) {
                    left |= 1 << i;
                }
                if (bits.get(i).get(bits.size() - 1)) {
                    right |= 1 << i;
                }
            }
            return new Edge[]{new Edge(top), new Edge(bottom), new Edge(left), new Edge(right)};
        }

        public void crop() {
            cropped = new ArrayList<>(bits);
            cropped.remove(0);
            cropped.remove(cropped.size() - 1);
            int n = cropped.get(0).size();
            for (int i = 0; i < cropped.size(); i++) {
                cropped.get(i).remove(0);
                cropped.get(i).remove(n - 1);
            }
        }

        //Rotates clockwise
        public Tile rotate(){
            Tile rotated = new Tile();
            List<List<Boolean>> rotatedBits = new ArrayList<>(bits);
            int n=bits.size()-1;
            for (int r = 0; r < bits.size(); r++) {
                for (int c = 0; c < bits.size() ; c++) {
                    rotatedBits.get(r).set(c,bits.get(n-c).get(r));
                }
            }
            rotated.setTileId(tileId);
            rotated.setBits(rotatedBits);

            return rotated;
        }

        public Tile flip(){
            Tile flipped = new Tile();
            List<List<Boolean>> flippedBits = new ArrayList<>(bits);
            int n=bits.size()-1;
            for (int r = 0; r < bits.size(); r++) {
                flippedBits.set(n-r,new ArrayList<>(bits.get(r)));
            }
            flipped.setTileId(tileId);
            flipped.setBits(bits);

            return flipped;
        }

        @Override
        public String toString() {
            return "{" +
                    "tileId='" + tileId + '\'' +
                    '}';
        }
    }

    class Edge {
        private int checkSum;

        public Edge(int checkSum) {
            this.checkSum = checkSum;
        }

        public int getCheckSum() {
            return checkSum;
        }

        public void setCheckSum(int checkSum) {
            this.checkSum = checkSum;
        }

        @Override
        public String toString() {
            return String.valueOf(checkSum);
        }

        public Edge flip() {
            return new Edge(reverseIntBitwise(checkSum));
        }

        public int reverseIntBitwise(int value) {
            int i = 0, rev = 0, bit;
            while (i++ < 10) {
                bit = (value & 1);
                value = value >> 1;
                rev = rev ^ bit;
                if (i < 10)
                    rev = rev << 1;
            }
            return rev;
        }
    }

    HashMap<String, Tile> tiles = new HashMap<>();

    public void parseInput(List<String> input) {
        String id = "";
        boolean readTile = false;
        Tile tile = new Tile();
        for (String line :
                input) {
            if (!readTile) {
                Matcher m = Pattern.compile("Tile (\\d+):").matcher(line);
                while (m.find()) {
                    id = m.group(1);
                }
                readTile = true;
                tile = new Tile();
                continue;
            }

            if (readTile) {
                if (line.isEmpty()) {
                    readTile = false;
                    tiles.put(id, tile);
                    continue;
                }
                tile.tileId = id;
                List<Boolean> bits = IntStream.range(0, line.length())
                        .mapToObj(b -> Boolean.valueOf(line.charAt(b) == '#' ? true : false))
                        .collect(Collectors.toList());
                tile.getBits().add(bits);
            }
        }

        tiles.put(id, tile);

    }

    public long findCornerSum() {
        HashMap<String, Integer> uniqueEdges = findUniqueEdges();

        long result = 1;
        for (var k :
                uniqueEdges.entrySet()) {
            if (k.getValue() > 2 && k.getValue() <= 4) {
                result *= Integer.parseInt(k.getKey());
            }
        }

        return result;
    }

    public HashMap<String, Integer> findUniqueEdges() {
        HashMap<Integer, List<String>> allEdges = getAllEdges();

        HashMap<String, Integer> uniqueEdges = new HashMap<>();

        for (var e :
                allEdges.entrySet()) {
            if (e.getValue().size() == 1) {
                String key = e.getValue().get(0);
                uniqueEdges.put(key, uniqueEdges.getOrDefault(key, 0) + 1);
            }
        }
        return uniqueEdges;
    }

    private HashMap<Integer, List<String>> getAllEdges() {
        HashMap<Integer, List<String>> allEdges = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, Tile> t :
                tiles.entrySet()) {
            for (Edge edge :
                    t.getValue().getEdges()) {
                allEdges.computeIfAbsent(edge.getCheckSum(), e -> new ArrayList<>()).add(t.getKey());
                allEdges.computeIfAbsent(edge.flip().getCheckSum(), e -> new ArrayList<>()).add(t.getKey());
            }
            i++;
        }
        return allEdges;
    }

    //Part 2
    public long countSeaBits() {
        putTilesInPlace();
        return 0;
    }

    private void putTilesInPlace() {

        HashMap<String, Integer> uniqueEdges = findUniqueEdges();
        List<Tile> corners = new ArrayList<>();
        List<Tile> sides = new ArrayList<>();

        for (var edge :
                uniqueEdges.entrySet()) {
            if (edge.getValue() <= 2) {
                sides.add(tiles.get(edge.getKey()));
            }
            if (edge.getValue() > 2 && edge.getValue() <= 4) {
                corners.add(tiles.get(edge.getKey()));
            }
        }
        System.out.println("Corners : " + corners);
        System.out.println("Sides : " + sides);

        int s = (int) Math.sqrt(tiles.size());
        Tile[][] image = new Tile[s][s];


        for (int r = 0; r * r < tiles.size(); r++) {
            System.out.println(Arrays.toString(image[r]));
        }
        System.out.println("-----------");

        HashMap<Integer, List<String>> allEdges = getAllEdges();
        Tile topLeftTile = tiles.get(corners.get(0));

        for (Tile c :
                corners) {
            boolean found=false;
            for (int r = 0; r < 4; r++) {
                Edge[] edges = c.getEdges();
                if(matchRandB(allEdges,edges)){
                    topLeftTile = c;
                    found=true;
                } else{
                    edges = c.flip().getEdges();
                    if(matchRandB(allEdges,edges)){
                        topLeftTile = c;
                        found=true;
                    }
                }
                System.out.println("Attempt " + c + " Result="+found);

                c=c.rotate();
            }
            if(found){
                break;
            }
        }

        image[0][0] = topLeftTile;
        HashSet<Tile> usedTiles = new HashSet<>();
        usedTiles.add(topLeftTile);

        /*
        HashMap<Tile,String> allTilePermutations = new HashMap<>();
        for (var t:
             tiles.entrySet()) {
            for (int f = 0; f <= 1; f++) {
                for (int r = 0; r < 4; r++) {
                    allTilePermutations.put(t.getValue().rotate(),t.getKey());
                }

            }
        }*/


        for (int r = 0; r * r < tiles.size(); r++) {
            System.out.println(Arrays.toString(image[r]));
        }
    }

    private boolean matchRandB(HashMap<Integer, List<String>> allEdges, Edge[] edges){
        List<String> rEdges = allEdges.get(edges[EDGE_RIGHT].getCheckSum());
        List<String> bEdges = allEdges.get(edges[EDGE_BOTTOM].getCheckSum());

        if (rEdges!=null && bEdges!=null &  rEdges.size() >= 2 && bEdges.size() >= 2) {
            return  true;
        }

        return false;
    }


}
