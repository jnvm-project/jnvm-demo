package eu.telecomsudparis.jnvm.demo;

public class Account {
    private final int id;
    private long balance;

    Account(int id, long balance) {
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

    public void transferTo(Account dest, long amount) {
        this.balance -= amount;
        dest.balance += amount;
    }

}
