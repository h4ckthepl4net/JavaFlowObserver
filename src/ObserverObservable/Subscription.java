package ObserverObservable;

import java.util.concurrent.Flow;

class Subscription implements Flow.Subscription {

    private static int id = 0;

    private int currentId;
    private Observer observer;
    private Observable observable;

    private long requestedCount = 0;
    private boolean isCanceled = false;

    Subscription(Observer observer, Observable observable) {
        this.observer = observer;
        this.observable =  observable;
        currentId = id++;
    }

    @Override
    public void request(long l) {
        if(l > 0) {
            this.requestedCount += l;
        } else if (!this.isCanceled) {
            this.observable.onError(new IllegalArgumentException("Subscription::request --- Argument of function Subscription::request cannot be less than 1",
                                                                 new IllegalArgumentException("Invalid argument of Subscription::request")));
        } else {
            throw new IllegalArgumentException("Subscription::request --- Argument of function Subscription::request cannot be less than 1",
                                                new IllegalArgumentException("Invalid argument of Subscription::request"));
        }
    }

    @Override
    public void cancel() {
        if(!this.isCanceled) {
            this.isCanceled = true;
            this.observable.onCancel();
            this.observer.cancelSubscription(this.currentId);
            this.observable = null;
            this.observer = null;
        } else {
            throw new IllegalStateException("Subscription::cancel --- Cannot cancel subscription that is already canceled",
                                            new IllegalStateException("Closing canceled subscription"));
        }
    }

    void next(Object next) {
        if(!this.isCanceled) {
            if (this.requestedCount > 0) {
                this.requestedCount--;
                this.observable.onNext(next);
            }
        } else {
            throw new IllegalStateException("Subscription::next --- Cannot emit value on canceled subscriptions",
                                            new IllegalStateException("Emitting on canceled subscription"));
        }
    }

    public void complete() {
        if(!this.isCanceled) {
            this.observable.onComplete();
        } else {
            throw new IllegalStateException("Subscription::complete --- Cannot complete subscriptions that are canceled",
                                            new IllegalStateException("Completing canceled subscriptions"));
        }
    }

    int get_id() {
        return this.currentId;
    }
}
