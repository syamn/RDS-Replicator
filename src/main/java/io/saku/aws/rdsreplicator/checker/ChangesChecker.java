package io.saku.aws.rdsreplicator.checker;

/**
 * Created by sakura on 2016/02/25.
 */
public class ChangesChecker<T> {
    final private ChangesCheckHandler handler;

    private T lastObject = null;
    private boolean isStarted = false;

    public ChangesChecker(){
        this(null);
    }

    public ChangesChecker(final ChangesCheckHandler handler){
        this.handler = handler;
    }

    public CheckerResult<T> check(final T newValue){
        final CheckerResult<T> result = this.checkImpl(newValue);

        if (this.handler != null){
            this.handler.handleCheckerResult(result);
        }

        return result;
    }

    private CheckerResult<T> checkImpl(final T newValue){
        final T oldValue = this.lastObject;
        this.lastObject = newValue;

        if (!this.isStarted){
            this.isStarted = true;
            return new CheckerResult<T>(true, oldValue, newValue, true); // returns as changed
        }

        T compareBase = oldValue;
        T compareTarget = newValue;

        if (compareBase == null){
            if (compareTarget == null){
                return new CheckerResult<T>(false, null, null); // no changed (null pair)
            }

            compareBase = compareTarget;
            compareTarget = null;
        }

        return new CheckerResult<T>(!compareBase.equals(compareTarget), oldValue, newValue);
    }
}
