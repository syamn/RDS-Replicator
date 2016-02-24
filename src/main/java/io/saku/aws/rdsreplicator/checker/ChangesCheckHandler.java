package io.saku.aws.rdsreplicator.checker;

/**
 * Created by sakura on 2016/02/25.
 */
public interface ChangesCheckHandler<T> {
    void handleCheckerResult(CheckerResult result);
}
