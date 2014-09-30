package com.bitbucket.pelenthium.accountservice.client;

import com.bitbucket.pelenthium.accountservice.client.data.Statistic;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by cementovoz on 23.08.14.
 */
public class ClientTask {

    private static  Logger logger = LogManager.getLogger(ClientTask.class);

    public static Runnable getLoadStatTask (final ClientController controller) {
        return new Runnable() {
            @Override
            public void run() {
                AccountConector conector = new AccountConector();
                try {
                    final Statistic statistic = conector.getStatistic();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            logger.debug("Set status to true, clear status message, and set statistic");
                            controller.getStatusProperty().set(true);
                            controller.clearStatus();
                            controller.setStatistic(statistic);
                        }
                    });
                } catch (final Exception exc) {
                    logger.error("Failed load statistic", exc);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            logger.debug("Set status to false, and set status message");
                            controller.getStatusProperty().set(false);
                            controller.setStatus(exc.getMessage());
                        }
                    });
                }
            }
        };
    }

    public static Runnable getLoadAmountTask(final ClientController controller, final int accountId) {
        return new Runnable() {
            @Override
            public void run() {
                AccountConector conector = new AccountConector();
                try {
                    Long amount = conector.getAmount(accountId);
                    logger.info("Account id:#" + accountId + ", amount=" + amount);
                } catch (Exception exc) {
                    logger.error("Failed load amount with id : " + accountId);
                }
            }
        };
    }

    public static Runnable getLoadAddAmountTask(final ClientController controller, final int accountId) {
        return new Runnable() {
            @Override
            public void run() {
                AccountConector conector = new AccountConector();
                Long amount = controller.getAmount();
                try {
                    conector.addAmount(accountId, amount);
                } catch (Exception exc) {
                    logger.error("Failed call add amount with id : " + accountId + " and amount=" + amount);
                }
            }
        };
    }

    public static Runnable getWaitForEndLoadTask(final ClientController controller, final List<Future> futureList, final ClientHandlers handlers) {
        return new Runnable() {
            @Override
            public void run() {
               logger.debug("Wait for all thread is finished");
               for (Future future : futureList) {
                   try {
                       future.get();
                   } catch (Exception e) {
                        //ignored, we waiting finish/interapted this thread
                   }
               }
               handlers.shutdownExecutor();
            }
        };
    }

    public static Runnable clearAndGetStat(final ClientController controller) {
        return new Runnable() {
            @Override
            public void run() {
                AccountConector conector = new AccountConector();
                try {
                    final Statistic statistic = conector.clearStatistic();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.setStatistic(statistic);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
