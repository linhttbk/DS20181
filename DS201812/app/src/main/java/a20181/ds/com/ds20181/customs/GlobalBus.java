package a20181.ds.com.ds20181.customs;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class GlobalBus {
    private static Bus sBus;

    GlobalBus() {
    }

    public static Bus getBus() {
        if (sBus == null)
            sBus = new Bus(ThreadEnforcer.ANY);
        return sBus;
    }
}
