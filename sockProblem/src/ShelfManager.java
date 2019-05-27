import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShelfManager{
    BlockingQueue<SockPair> sortedSocksHeap;
    private AtomicBoolean shelfManagerRunStatus = new AtomicBoolean(true);
    private HashMap <String , Integer> sockPairDict = new HashMap<String, Integer>();

    public ShelfManager(BlockingQueue<SockPair> sortedSocksHeap){
        this.sortedSocksHeap = sortedSocksHeap;
        Thread shelfManagerThread = new Thread(new ShelfManagerThread());
        shelfManagerThread.start();
    }

    public class ShelfManagerThread implements Runnable {
        @Override
        public void run() {
            while (shelfManagerRunStatus.get()) {
                try {
                    SockPair sockPair = sortedSocksHeap.take();
                    if (sockPairDict.get(sockPair.getSockColor()) == null){
                        sockPairDict.put(sockPair.getSockColor() , 1 );
                    }else
                        sockPairDict.put(sockPair.getSockColor() , sockPairDict.get(sockPair.getSockColor()) + 1);
                    System.out.println(sockPair.getSockColor());
                } catch (InterruptedException e) {
                    System.out.println("sortedSocksHeap empty erro in Shelfmanager");
                    e.printStackTrace();
                }
            }

        }
    }


    public void printMap (){
        for ( Map.Entry<String , Integer> entry : sockPairDict.entrySet()){
            System.out.println(entry.getKey()  + " "+   entry.getValue().toString());
        }
    }
}
