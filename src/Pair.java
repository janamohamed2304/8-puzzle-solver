public class Pair {
    String direction;
    String state;

    public Pair(){

    }

    public Pair(String d, String s) {
        state = s;
        direction = d;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
