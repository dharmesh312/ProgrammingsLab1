import java.util.Random;

public class Sock {
    private  String sockColor;
    private enum Color
    {
        WHITE, BLACK, BLUE , GREY;
    }

    public Sock(){
        this.sockColor = (Color.values()[new Random().nextInt(Color.values().length)]).toString();
    }

    public String getSockColor(){
        return (this.sockColor).toString();
    }
}
