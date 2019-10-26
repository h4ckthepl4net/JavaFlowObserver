package ObserverObservable;

import java.util.LinkedList;
import java.util.concurrent.Flow;

public class Observer implements Flow.Publisher {

    private LinkedList<Observable> subscribers = new LinkedList<>();
    private LinkedList<Subscription> subscriptions = new LinkedList<>();

    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        this.subscribers.add((Observable)subscriber);
        Subscription subscription = new Subscription(this, subscriber);
        this.subscriptions.add(subscription);
        subscriber.onSubscribe(subscription);
    }

    public void emit(Object next) {
        for(int i = 0, length = this.subscribers.size(); i < length; i++) {
            Subscription $sub = this.subscriptions.get(i);
            if($sub.isValid()) {
                this.subscribers.get(i).onNext(next);
                $sub.request(-1);
            }
        }
    }
}
