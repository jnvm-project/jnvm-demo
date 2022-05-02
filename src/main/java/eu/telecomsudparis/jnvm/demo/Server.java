package eu.telecomsudparis.jnvm.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.function.Supplier;

import static spark.Spark.*;

public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        new Server().doMain();
    }

    public void doMain() {

        final Bank bank = new Bank();
        bank.open();
        LOG.info("Bank opened");

        port(8080);

        get("/balance/:id", (req, res) -> {
            String id = req.params("id");
            LOG.debug("getBalance(" + id + ")");

            return executorService(req, res, () -> {
                long balance = bank.getBalance(id);
                return Long.toString(balance);
            });
        });

        post("/create/:id", (req, res) -> {
            String id = req.params("id");
            LOG.debug("createAccount(" + id + ")");

            return executorService(req, res, () -> {
                bank.createAccount(id);
                return "OK";
            });
        });

        post("/create/:start/:end", (req, res) -> {
            int start = Integer.parseInt(req.params("start"));
            int end = Integer.parseInt(req.params("end"));
            LOG.info("createAccounts(" + start + "," + end + ")");

            return executorService(req, res, () -> {
                for (int i = start; i <= end; i++) {
                    bank.createAccount(Integer.toString(i));
                }
                return "OK";
            });
        });

        put("/credit/:id/:amount", (req, res) -> {
            String id = req.params("id");
            long amount = Long.parseLong(req.params("amount"));
            LOG.debug("credit(" + id + "," + amount + ")");

            return executorService(req, res, () -> {
                bank.credit(id, amount);
                return "OK";
            });
        });

        put("/transfer/:from/:to/:amount", (req, res) -> {
            String from = req.params("from");
            String to = req.params("to");
            long amount = Long.parseLong(req.params("amount"));
            LOG.debug("performTransfer(" + from + "," + to + "," + amount + ")");

            return executorService(req, res, () -> {
                bank.performTransfer(from, to, amount);
                return "OK";
            });
        });

        post("/clear/all", (req, res) -> {
            LOG.debug("clear()");

            return executorService(req, res, () -> {
                bank.clear();
                return "OK";
            });
        });

        post("/total", (req, res) -> {
            LOG.debug("total()");

            return executorService(req, res, () -> {
                long total = bank.getTotal();
                return Long.toString(total);
            });
        });

        SignalHandler sh = s -> {
            LOG.info("Shutting down ..");

            bank.close();

            System.exit(0);
            stop();
        };

        Signal.handle(new Signal("INT"), sh);
        Signal.handle(new Signal("TERM"), sh);

        Thread.currentThread().interrupt();
    }

    private String executorService(Request req, Response res, Supplier<String> f) {
        try {
            return f.get();
        } catch (IllegalArgumentException e) {
            String errorString = e.toString();
            LOG.error(errorString);
            res.status(401);
            return errorString;
        }
    }

}

