package ObserverObservable;

import ObserverObservable.Interface.ObservableCallBackReceiver;

import java.util.LinkedList;
import java.util.concurrent.Flow;

public class Observable implements Flow.Subscriber {

    private ObservableCallBackReceiver callBackReceiver;
    private Subscription subscription = null;
    private LinkedList<Object> receivedItems = new LinkedList<>();

    private boolean isCanceled = false;

    public Observable(ObservableCallBackReceiver callBackReceiver) {
        this.callBackReceiver = callBackReceiver;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        if(!this.isCanceled) {
            if (this.subscription == null) {
                this.subscription = (Subscription) subscription;
                this.subscription.request(1);
            } else {
                throw new IllegalStateException("Observable::onSubscribe --- Cannot subscribe with observable that is already subscribed to another observer",
                                                new IllegalStateException("Observable already subscribed"));
            }
        } else {
            throw new IllegalStateException("Observable::onSubscribe --- Cannot subscribe to canceled stream",
                                            new IllegalStateException("Subscribing to canceled stream"));
        }
    }

    @Override
    public void onNext(Object o) {
        if(!this.isCanceled) {
            this.receivedItems.add(o);
            this.subscription.request(1);
            this.callBackReceiver.onNext(o);
        } else {
            throw new IllegalStateException("Observable::onNext --- Cannot emit value on canceled subscriptions",
                                            new IllegalStateException("Emitting on closed subscription"));
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("JFO --- Observable::onError --- " + throwable.getCause() + " ---" + throwable.getLocalizedMessage() + " --- JFO");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        if(!this.isCanceled) {
            System.out.println("JFO --- Observable::onComplete --- Stream/subscription with id=" + this.subscription.get_id() + " has been completed! --- JFO");
            this.callBackReceiver.onComplete();
        } else {
            throw new IllegalStateException("Observable::onComplete --- Cannot complete a stream that is canceled",
                                            new IllegalStateException("Completing canceled stream"));
        }
    }

    public void onCancel() {
        if(!this.isCanceled) {
            this.callBackReceiver.onCancel();
            this.callBackReceiver = null;
            this.subscription = null;
        } else {
            throw new IllegalStateException("Observable::onCancel --- Cannot cancel subscription that is already canceled",
                                            new IllegalStateException("Closing canceled subscription"));
        }
    }

    public void cancel() {
        if(!this.isCanceled) {
            isCanceled = true;
            this.subscription.cancel();
        } else {
            throw new IllegalStateException("Observable::cancel --- Cannot cancel subscription that is already canceled",
                    new IllegalStateException("Closing canceled subscription"));
        }
    }

    public LinkedList<Object> getReceivedItems() {
        return this.receivedItems;
    }

}
