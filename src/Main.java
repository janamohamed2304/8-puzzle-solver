import java.util.List;

public class Main {
    public static void main(String[] args) {


        //Successors s = new Successors();
        List<Pair> test = Successors.getSuccessors("283164705");
        List<Pair> test2 = Successors.getSuccessors("203164785");

        for (Pair i : test){
            System.out.println(i.getDirection());
            System.out.println(i.getState());
            System.out.println("\n");
        }
        System.out.println("-----------------");

        for (Pair i : test2){
            System.out.println(i.getDirection());
            System.out.println(i.getState());
            System.out.println("\n");
        }

    }
}