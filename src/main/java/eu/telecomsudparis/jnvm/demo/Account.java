package eu.telecomsudparis.jnvm.demo;

import eu.telecomsudparis.jnvm.offheap.OffHeap;
import eu.telecomsudparis.jnvm.offheap.OffHeapObjectHandle;

public class Account extends OffHeapObjectHandle {

    private static final long CLASS_ID = OffHeap.Klass.registerUserKlass(Account.class);
    private static final long[] offsets = {0L, 4L};
    private static final long SIZE = Integer.SIZE + Long.SIZE;

    Account(int id, long balance) {
        super();
        setIntegerField(offsets[0], id);
        setLongField(offsets[1], balance);
    }

    public Account(Void v, long offset) {
        super(v, offset);
    }

    public long size() { return SIZE; }
    public long classId() { return CLASS_ID; }
    public void descend() { }

    protected int getId() {
        return getIntegerField(offsets[0]);
    }

    protected long getBalance() {
        return getLongField(offsets[1]);
    }

    protected void setBalance(long balance) {
        setLongField(offsets[1], balance);
    }

    protected void incBalance(long delta) {
        setLongField(offsets[1], getLongField(offsets[1])+delta);
    }

    public void transferTo(Account dest, long amount) {
        OffHeap.startRecording();
        this.incBalance(-amount);
        dest.incBalance(+amount);
        OffHeap.stopRecording();
    }

}
