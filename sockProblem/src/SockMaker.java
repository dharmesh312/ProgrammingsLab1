import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SockMaker{
    private BlockingQueue<Sock> unsortedSocksHeap;
    private AtomicBoolean sockMakerRunStatus = new AtomicBoolean(true);
    int numThread = 10  ;
    public SockMaker (BlockingQueue<Sock> queue){
        this.unsortedSocksHeap = queue;
        ExecutorService sockMakerExecutorPool = Executors.newFixedThreadPool(numThread);
        for (int id = 0 ; id < numThread ; id++){
            SockGenerator sockGenerator = new SockGenerator(id);
            sockMakerExecutorPool.execute(sockGenerator);
        }
    }

    public class SockGenerator implements Runnable {
        private int id;
        public SockGenerator(int id){
            this.id = id;
        }
        int i = 20;

        @Override
        public void run() {
            while(true) {

                Sock sock = new Sock();
                try {
                    unsortedSocksHeap.put(sock);
//                    System.out.println( " generating socks : " + sock.getSockColor().toString()  +  this.id);
                } catch (InterruptedException e) {
                    System.out.println("Exception thrown at SockMaker.java in run()");
                    e.printStackTrace();
                }
            }

        }
    }


}
