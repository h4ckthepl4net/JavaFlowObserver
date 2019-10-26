package ObserverObservable;

import java.util.concurrent.Flow;

class Subscription implements Flow.Subscription {

    private Flow.Publisher observer;
    private Flow.Subscriber observable;

    private long requestedCount = 0;
    private boolean isCanceled = false;

    Subscription(Flow.Publisher observer, Flow.Subscriber observable) {
        this.observer = observer;
        this.observable = observable;
    }

    @Override
    public void request(long l) {
        this.requestedCount += l;
    }

    @Override
    public void cancel() {
        this.isCanceled = true;
    }

    boolean isValid() {
        return this.isCanceled && this.requestedCount > 0;
    }
}
