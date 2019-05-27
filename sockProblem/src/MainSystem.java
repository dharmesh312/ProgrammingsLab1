import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainSystem {
    private static BlockingQueue<Sock> unsortedSocksHeap = new ArrayBlockingQueue<Sock>( 10);
    private static BlockingQueue<SockPair> sortedSocksHeap = new ArrayBlockingQueue<SockPair>( 10);

    public static void main(String[] args) {
        SockMaker sockMaker = new SockMaker(unsortedSocksHeap);
        SockMatcher sockMatcher = new SockMatcher(unsortedSocksHeap , sortedSocksHeap);
        ShelfManager shelfManager = new ShelfManager(sortedSocksHeap);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shelfManager.printMap();
    }
}
