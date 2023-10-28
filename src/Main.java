import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {

        HitCounter test = new HitCounter();
        test.hit(1);
        test.hit(2);
        test.hit(3);
        System.out.println(test.getHits(4));
        test.hit(300);
        System.out.println(test.getHits(300));
        System.out.println(test.getHits(301));
    }
}

// solution. note that this works by deleting content. i was trying to preserve content. would at least be able to ask this
// in an interview although i recognize that my code would still be slow from having to look back repeatedly through
// decrementing until value was found

class HitCounter {
    private Queue<Integer> hits;

    public HitCounter() {
        this.hits = new LinkedList<Integer>();
    }

    public void hit(int timestamp) {
        this.hits.add(timestamp);
    }

    public int getHits(int timestamp) {
        while (!this.hits.isEmpty()) {
            int diff = timestamp - this.hits.peek();
            if (diff >= 300) this.hits.remove();
            else break;
        }
        return this.hits.size();
    }
}


// attempt 2, same issuee with deleting prior hits
class HitCounter {
    Map<Integer,Integer> hitMap = new HashMap<>();

    public HitCounter() {

    }

    public void hit(int timestamp) {
        hitMap.put(timestamp, getHits(timestamp)+ 1);
    }

    public int getHits(int timestamp) {
        int lastKnownHitCount = 0;
        int OOBHits = 0;
        while(timestamp > 300){ // i changed this so only loops 300 times but then got worse results?
            if(hitMap.containsKey(timestamp)){
                lastKnownHitCount = hitMap.get(timestamp);
                break;
            }
            timestamp--;
        }

        for(int i = timestamp; i > 0; i--){
            if (hitMap.containsKey(i)) {
                OOBHits = hitMap.get(i);
                break;
            }
        }

        if(lastKnownHitCount != 0){
            return lastKnownHitCount-OOBHits;
        } else {
            return OOBHits;
        }
    }
}






//
//
//class HitCounter {
//    Map<Integer,Integer> hitMap = new HashMap<>();
//
//    public HitCounter() {
//
//    }
//
//    public void hit(int timestamp) {
//        if(hitMap.containsKey(timestamp)){ // if timestamp exists, update the value
//            hitMap.put(timestamp,hitMap.get(timestamp) + 1);
//        } else { // if timestamp doesn't exist, then assign new hit value based on last known hit value
//            int lastKnownHitCount = getHits(timestamp-1);
//            hitMap.put(timestamp, lastKnownHitCount + 1);
//        }
//
//        if(timestamp > 300){ // revise the new timestamp hit value if timestamp > 300, requiring deduction of OOB hits
//            int OOBHits = getHits(timestamp-300);
//            hitMap.put(timestamp, hitMap.get(timestamp) - OOBHits);
//        }
//    }
//
//    public int getHits(int timestamp) {
//        for(int i = timestamp; i > 0; i--){ // if specific timestamp doesn't exist, decrement until last known hit count found
//            if (hitMap.containsKey(i)) {
//                return hitMap.get(i);
//            } // problem that this decremented more than 300 places so when input 301 it will rturn hitt of 4 at 300. should be 4-1=3
//        }
//        return 0; // if no timestamp found outside of bounds, it means that all hits assigned to current timestamp
//        // fell within 300 seconds of each other since none prior existed.
//    }
//}
//


// two ideas

/*
Idea number one use of queue where a node will house a timestamp, value and a pointer.
This will NOT involve booting nodes once a queue size is reached because even if there are 3,000 elapsed
seconds, will still need to be abe to search for second 10 if i want.
but queue super limited with how i would serach for say timestamp 302 or 50 or whatever and its singly linked so
retrieving other info is tough


option 2: hashmap. time stamp is key, hit count is value where the hit count will be calculated as value at most recent
key + 1 then - any amount of hits that occured outside the 300 second span so would need to take current time stamp
 - 300 and then decrement to find the most recent preciding value and deduct that.

 suppose current stamp is 400, most recent stamp was 5 at 300 and the first 4 stamps occured at 1,2,3,4
 i would say time stamp at 400 is 300.val (5) + 1 - 4 = 2 since 400-300 = 100, and the first found hit value would be
 4 at key of 4 and this calculation only occurs when timestamp > 300
 */