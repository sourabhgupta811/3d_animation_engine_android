package org.rajawali3d.materials;

import android.content.Context;

/**
 * Created by Sourabh Gupta on 20/5/19.
 */
public final class RajawaliContext {
    public static RajawaliContext instance;
    private Context context;
    public static RajawaliContext getInstance(){
        if(instance == null) {
            instance = new RajawaliContext();
        }
        return instance;
    }

    public Context getContext() {
        if(context==null){
            throw new java.lang.RuntimeException("Please set rajawali to current context");
        }
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
