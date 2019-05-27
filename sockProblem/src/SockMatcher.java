import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SockMatcher {
    private BlockingQueue<Sock> unsortedSocksHeap;
    private BlockingQueue<SockPair> sortedSocksHeap;
    private static BlockingQueue<Sock> sockMatcherQueue = new ArrayBlockingQueue<Sock>( 10);
    private AtomicBoolean sockMatcherRunStatus = new AtomicBoolean(true);
    int roboticsArmsCount = 5;
    int matcherCount = 1;

    //construtor
    public SockMatcher(BlockingQueue<Sock> unsortedSocksHeap , BlockingQueue<SockPair> sortedSocksHeap){
        this.unsortedSocksHeap  = unsortedSocksHeap;
        this.sortedSocksHeap  = sortedSocksHeap;
        ExecutorService roboticArmsExecutorPool = Executors.newFixedThreadPool(roboticsArmsCount);
        ExecutorService matcherExecutorPool = Executors.newFixedThreadPool(matcherCount);
        for (int id = 0 ; id < matcherCount ; id++){
            matcherExecutorPool.execute(new Matcher(id ,sockMatcherQueue , sortedSocksHeap) );
        }
        for (int id = 0 ; id < roboticsArmsCount ; id++){
            roboticArmsExecutorPool.execute(new RoboticArms(id , sockMatcherQueue ,unsortedSocksHeap) );
        }

    }

    //Robotic Arms pick the  sock and then put them in the matcher queue
    public class RoboticArms implements Runnable{
        private int id;
        private BlockingQueue<Sock> sockMatcherQueue;
        private BlockingQueue<Sock> unsortedSocksHeap;
        public RoboticArms(int id , BlockingQueue<Sock> sockMatcherQueue ,BlockingQueue<Sock> unsortedSocksHeap ){
            this.id = id;
            this.sockMatcherQueue = sockMatcherQueue;
            this.unsortedSocksHeap = unsortedSocksHeap;
        }

        @Override
        public void run() {
            while(sockMatcherRunStatus.get()){
                try {
                    sockMatcherQueue.put(unsortedSocksHeap.take());
                } catch (InterruptedException e) {
                    System.out.println("unsortedSocksHeap empty sock maker not making enough socks.");
                    e.printStackTrace();
                }

            }
        }
    }


    //Matcher class which basically matches the socks after robotic arms pick them up
    public class Matcher implements Runnable{
        private int id;
        private BlockingQueue<Sock> sockMatcherQueue;
        private BlockingQueue<SockPair> sortedSocksHeap;
        private HashMap <String , Integer > socksCount= new HashMap<String , Integer>();
        public Matcher(int id , BlockingQueue<Sock> sockMatcherQueue ,BlockingQueue<SockPair> sortedSocksHeap ){
            this.id = id;
            this.sockMatcherQueue = sockMatcherQueue;
            this.sortedSocksHeap = sortedSocksHeap;
        }

        @Override
        public void run() {
            while(sockMatcherRunStatus.get()){
                try {
                     Sock sock = sockMatcherQueue.take();
                     if (socksCount.get(sock.getSockColor()) == null){
                         socksCount.put(sock.getSockColor() , 1);
                     }
                     else{
                         socksCount.put(sock.getSockColor() , 1 + socksCount.get(sock.getSockColor()) );
                         if (socksCount.get(sock.getSockColor()) >= 2){
                             int socksPairCount = socksCount.get(sock.getSockColor()) / 2;
                             socksCount.put(sock.getSockColor() , socksCount.get(sock.getSockColor()) % 2 );

                             for (int i = 0 ; i < socksPairCount ; i++){
                                 sortedSocksHeap.put(new SockPair( sock.getSockColor()) );
                             }
                         }
                     }

                } catch (InterruptedException e) {
                    System.out.println("sortedMatcherQueue empty sock maker not making enough socks.");
                    e.printStackTrace();
                }

            }
        }
    }


}
