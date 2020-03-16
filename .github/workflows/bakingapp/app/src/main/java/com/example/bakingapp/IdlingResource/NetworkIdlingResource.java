package com.example.bakingapp.IdlingResource;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;
    private AtomicBoolean mIsIdle = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setmIsIdle(boolean isIdle) {
        mIsIdle.set(isIdle);
        if (isIdle && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
