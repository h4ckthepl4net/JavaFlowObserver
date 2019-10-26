package ObserverObservable;

import java.util.LinkedList;
import java.util.concurrent.Flow;

public class Observable implements Flow.Subscriber {

    private Subscription subscription = null;
    private LinkedList<Object> receivedItems = new LinkedList<>();

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = (Subscription)subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(Object o) {
        this.receivedItems.add(o);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("JFO---" + throwable.getCause() + "---" + throwable.getLocalizedMessage() + "---JFO");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        //TODO implement method onComplete
    }

    public void close() {
        this.subscription.cancel();
    }
}
