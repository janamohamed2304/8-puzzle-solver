import java.util.ArrayList;
import java.util.List;

public class Successors {

    private static String swap(String s, int i, int j) {
        char[] charArray = s.toCharArray();
        char temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return new String(charArray);
    }

    public static List<Pair> getSuccessors(String state){
        List<Pair> successors = new ArrayList<Pair>();
        int emptyIndex = state.indexOf("0");

        // UP
        if(emptyIndex-3 >= 0){
            Pair succ = new Pair();
            succ.setDirection("UP");
            succ.setState(swap(state, emptyIndex, emptyIndex-3));
            successors.add(succ);
        }

        // DOWN
        if(emptyIndex+3 <= 8){
            Pair succ = new Pair();
            succ.setDirection("DOWN");
            succ.setState(swap(state, emptyIndex, emptyIndex+3));
            successors.add(succ);
        }

        // LEFT
        if(emptyIndex != 0 && emptyIndex != 3 && emptyIndex != 6 ){
            Pair succ = new Pair();
            succ.setDirection("LEFT");
            succ.setState(swap(state, emptyIndex, emptyIndex-1));
            successors.add(succ);
        }

        // RIGHT
        if(emptyIndex != 2 && emptyIndex != 5 && emptyIndex != 8 ){
            Pair succ = new Pair();
            succ.setDirection("RIGHT");
            succ.setState(swap(state, emptyIndex, emptyIndex+1));
            successors.add(succ);
        }

        return successors;
    }
}
