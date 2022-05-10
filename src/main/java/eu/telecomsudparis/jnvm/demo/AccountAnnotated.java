package eu.telecomsudparis.jnvm.demo;

import eu.telecomsudparis.jnvm.transformer.annotations.Persistent;

@Persistent(fa="non-private")
public class AccountAnnotated {
    private final int id;
    private long balance;

    AccountAnnotated(int id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    protected int getId() {
        return this.id;
    }

    protected long getBalance() {
        return this.balance;
    }

    protected void setBalance(long balance) {
        this.balance = balance;
    }

    protected void incBalance(long delta) {
        this.balance += delta;
    }

    public void transferTo(AccountAnnotated dest, long amount) {
        this.balance -= amount;
        dest.balance += amount;
    }

}
