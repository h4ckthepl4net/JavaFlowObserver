package ObserverObservable;

import java.util.concurrent.Flow;

class Subscription implements Flow.Subscription {

    private Observer observer;
    private Observable observable;

    private long requestedCount = 0;
    private boolean isCanceled = false;

    Subscription(Flow.Publisher observer, Flow.Subscriber observable) {
        this.observer = (Observer)observer;
        this.observable =  (Observable)observable;
    }

    @Override
    public void request(long l) {
        if(l > 0) {
            this.requestedCount += l;
        } else if(!this.isCanceled) {
            this.observable.onError(new IllegalArgumentException("Argument of function Subscription::request cannot be less than 1",
                                                                 new IllegalArgumentException()));
        }
    }

    @Override
    public void cancel() {
        this.isCanceled = true;
    }

    void next(Object next) {
        if(this.isValid()) {
            this.requestedCount--;
            this.observable.onNext(next);
        }
    }

    public void complete() {
        if(!this.isCanceled) {
            this.observable.onComplete();
        }
    }

    private boolean isValid() {
        return this.isCanceled && this.requestedCount > 0;
    }
}
