package eu.telecomsudparis.jnvm.demo;

import eu.telecomsudparis.jnvm.offheap.OffHeap;
import eu.telecomsudparis.jnvm.offheap.OffHeapString;
import eu.telecomsudparis.jnvm.util.persistent.RecoverableStrongHashMap;

import java.util.Map;

public class Bank {

    private Map<OffHeapString, Account> accounts;

    public Bank() {
    }

    public void createAccount(String id, long initialDeposit) {
        checkAccountNotExists(id);

        accounts.put(new OffHeapString(id), new Account(Integer.parseInt(id), initialDeposit));
    }

    public void createAccount(String id) {
        createAccount(id, 0);
    }

    public long getBalance(String id) {
        checkAccountExists(id);

        return accounts.get(id).getBalance();
    }

    public void credit(String id, long amount) {
        checkAccountExists(id);

        accounts.get(id).incBalance(amount);
    }

    public void performTransfer(String from, String to, long amount) {
        Account fromAccount = accounts.get(from);
        Account toAccount = accounts.get(to);

        try {
            fromAccount.transferTo(toAccount, amount);
        } catch (NullPointerException e) {
            String[] accs = {from, to};
            for (String acc : accs) {
                checkAccountExists(acc);
            }
        }
    }

    private void checkAccountExists(String account) throws IllegalArgumentException {
        if (!this.accounts.containsKey(account)) {
            throw new IllegalArgumentException("account does not exist: " + account);
        }
    }

    private void checkAccountNotExists(String account) throws IllegalArgumentException {
        if (this.accounts.containsKey(account)) {
            throw new IllegalArgumentException("account does exist: " + account);
        }
    }

    public void clear() {
        this.accounts.clear();
    }

    public long getTotal() {
        return this.accounts.keySet().stream()
                .map(accounts::get)
                .mapToLong(Account::getBalance)
                .sum();
    }

    public void open() {
        OffHeap.finishInit();
        this.accounts = RecoverableStrongHashMap.recover("bank", 10000000);
    }

    public void close() {
    }

}
