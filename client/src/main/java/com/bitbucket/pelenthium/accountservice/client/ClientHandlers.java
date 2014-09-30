package com.bitbucket.pelenthium.accountservice.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by cementovoz on 22.08.14.
 */
public class ClientHandlers {

    private static final Logger LOG = LogManager.getLogger(ClientHandlers.class);

    private ClientController controller;
    private ExecutorService executorService;

    public ClientHandlers (ClientController controller) {
        this.controller = controller;
    }

    public EventHandler<ActionEvent> getRefreshHandler () {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent event) {
                new Thread(ClientTask.getLoadStatTask(controller)).start();
            }
        };
    }

    public EventHandler<ActionEvent> getStartHandler () {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final int readerThreadCount = controller.getReaderThreadCount();
                final int writerThreadCount = controller.getWriterThreadCount();
                if ((readerThreadCount + writerThreadCount) > 0) {
                    controller.showProgress();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            submitAllTask(readerThreadCount, writerThreadCount);
                        }
                    }).start();
                } else {
                    LOG.error("Threads count is zero");
                    ClientHandlers.this.shutdownExecutor();
                }
            }
        };
    }

    private void submitAllTask( int readerThreadCount,  int writerThreadCount) {
        executorService = Executors.newFixedThreadPool(readerThreadCount + writerThreadCount);
        LOG.info("Create executor for " + (readerThreadCount + writerThreadCount) + " threads for call getAmount and addAmount");
        List<Future> futureList = new ArrayList<>(readerThreadCount + writerThreadCount);
        int[] ids = controller.getIds();
        LOG.debug("Found " + ids.length + " ids");
        if (ids.length > 0) {
            futureList.addAll(submitTaskToThread(readerThreadCount, ids, new ThreadTask() {
                @Override
                public Runnable getTask(int id) {
                    return ClientTask.getLoadAmountTask(controller, id);
                }
            }));
            futureList.addAll(submitTaskToThread(writerThreadCount, ids, new ThreadTask() {
                @Override
                public Runnable getTask( int id) {
                    return ClientTask.getLoadAddAmountTask(controller, id);
                }
            }));
            new Thread(ClientTask.getWaitForEndLoadTask(controller, futureList, ClientHandlers.this)).start();
        }
        else {
            LOG.error("You don't set id!");
        }
    }

    private List<Future> submitTaskToThread(int threadCount, int[] ids, ThreadTask task) {
        List<Future> futureList = new ArrayList<>(threadCount);
        if (threadCount > 0) {
            int threadPerId = threadCount / ids.length;
            threadPerId = threadPerId > 0 ? threadPerId : 1;
            LOG.debug("For one id call " + threadPerId + " thread.");
            if (threadCount == 1) {
                Future<?> submit = executorService.submit(task.getTask(ids[0]));
                futureList.add(submit);
            }
            else {
                for (int i=0; i < ids.length ; i++) {
                    for (int j = 0; j < threadPerId; j++) {
                        Future<?> submit = executorService.submit(task.getTask(ids[i]));
                        futureList.add(submit);
                    }
                }
            }
        }
        return futureList;
    }



    public EventHandler<ActionEvent> getStopHandler () {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                shutdownExecutor();
            }
        };
    }

    public void shutdownExecutor() {
        if (executorService != null && !executorService.isShutdown()) {
            LOG.debug("Shutdown executor");
            executorService.shutdownNow();
        }
        LOG.debug("Executor reset to null");
        executorService = null;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.hideProgress();
                controller.setStartIsActive();
            }
        });
    }

    public EventHandler<ActionEvent> getClearHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new Thread(ClientTask.clearAndGetStat(controller)).start();
            }
        };
    }

    private static interface ThreadTask {
        public Runnable getTask (int id);
    }
}
