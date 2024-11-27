package com.gustavo.dummyaxon.command.user.application.usecases.interfaces;

import java.util.concurrent.ExecutionException;

public interface IUseCase<TIn, TOut> {
    TOut execute(TIn input) throws ExecutionException, InterruptedException;
}
