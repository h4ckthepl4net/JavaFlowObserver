package ObserverObservable;

import ObserverObservable.Interface.ObservableCallBackReceiver;

import java.util.LinkedList;
import java.util.concurrent.Flow;

public class Observable implements Flow.Subscriber {

    private ObservableCallBackReceiver callBackReceiver;
    private Subscription subscription = null;
    private LinkedList<Object> receivedItems = new LinkedList<>();

    public Observable(ObservableCallBackReceiver callBackReceiver) {
        this.callBackReceiver = callBackReceiver;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = (Subscription)subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(Object o) {
        this.receivedItems.add(o);
        this.subscription.request(1);
        this.callBackReceiver.onNext(o);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("JFO---" + throwable.getCause() + "---" + throwable.getLocalizedMessage() + "---JFO");
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("JFO---Stream/subscription with id="+this.subscription.get_id()+" has been completed!---JFO");
        this.callBackReceiver.onComplete();
    }

    public void close() {
        this.subscription.cancel();
    }
}
