package io.saku.aws.rdsreplicator.checker;

/**
 * Created by sakura on 2016/02/25.
 */
public final class CheckerResult<T> {
    final private boolean changed;
    final private T oldValue;
    final private T newValue;
    final private boolean firstTime;

    CheckerResult(boolean changed, T oldValue, T newValue){
        this(changed, oldValue, newValue, false);
    }

    CheckerResult(boolean changed, T oldValue, T newValue, boolean firstTime){
        this.changed = changed;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.firstTime = firstTime;
    }

    public boolean isChanged(){
        return this.changed;
    }

    public T getOldValue(){
        return this.oldValue;
    }

    public T getNewValue(){
        return this.newValue;
    }

    public boolean isFirstTime(){
        return this.firstTime;
    }
}
