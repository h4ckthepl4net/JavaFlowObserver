package ObserverObservable;

import java.util.LinkedList;
import java.util.concurrent.Flow;

public class Observer implements Flow.Publisher {

    private LinkedList<Subscription> subscriptions = new LinkedList<>();

    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        Subscription subscription = new Subscription(this, subscriber);
        this.subscriptions.add(subscription);
        subscriber.onSubscribe(subscription);
    }

    public void emit(Object next) {
        for(Subscription $sub : this.subscriptions) {
            $sub.next(next);
        }
    }
}
