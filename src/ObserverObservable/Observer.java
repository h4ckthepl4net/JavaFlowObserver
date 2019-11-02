package ObserverObservable;

import java.util.LinkedList;
import java.util.concurrent.Flow;

public class Observer implements Flow.Publisher {

    private boolean isComplete = false;
    private LinkedList<Subscription> subscriptions = new LinkedList<>();

    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        if (!this.isComplete) {
            Subscription subscription = new Subscription(this, (Observable) subscriber);
            this.subscriptions.add(subscription);
            subscriber.onSubscribe(subscription);
        } else {
            throw new IllegalStateException("Observer::subscribe --- Cannot subscribe to completed stream",
                                            new IllegalStateException("Subscribing to completed stream"));
        }
    }

    public void complete() {
        if (this.isComplete) {
            System.out.println("JFO --- Observer::complete ---WARNING: Completing stream that is already completed --- JFO");
        }
        this.isComplete = true;
        for (Subscription $sub : this.subscriptions) {
            $sub.complete();
        }

    }

    public void cancelAll() {
        for (Subscription $sub : this.subscriptions) {
            $sub.cancel();
        }
    }

    public void emit(Object next) {
        if (this.isComplete) {
            System.out.println("JFO --- WARNING: Emitting to completed stream --- JFO");
        }
        for (Subscription $sub : this.subscriptions) {
            $sub.next(next);
        }

    }

    boolean cancelSubscription(int id) {
        return subscriptions.removeIf((Subscription obj) -> obj.get_id() == id);
    }
}
