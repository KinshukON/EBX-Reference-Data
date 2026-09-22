package com.onebx.ebx.fasttrack.referencedata;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.LoggingCategory;
import com.orchestranetworks.service.Session;

/** Bounded startup retries for repository services that become available asynchronously. */
final class ReferenceDataBootstrap {
    private static final int MAX_ATTEMPTS = 10;
    private static final AtomicBoolean RUNNING = new AtomicBoolean();

    private ReferenceDataBootstrap() { }

    static void install(final Repository repository, final Session session, final LoggingCategory log) {
        if (!RUNNING.compareAndSet(false, true)) return;
        if (attempt(repository, session, log)) {
            RUNNING.set(false);
            return;
        }
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(task -> {
            final Thread thread = new Thread(task, "ebx-reference-data-bootstrap");
            thread.setDaemon(true);
            return thread;
        });
        executor.schedule(new Runnable() {
            private int attempts = 1;

            @Override
            public void run() {
                final boolean complete = attempt(repository, session, log);
                attempts++;
                if (complete || attempts >= MAX_ATTEMPTS) {
                    if (!complete) {
                        log.error("[reference-data] automatic installation did not complete after "
                            + attempts + " attempts; resolve the reported cause and restart EBX");
                    }
                    RUNNING.set(false);
                    executor.shutdown();
                } else {
                    executor.schedule(this, 30, TimeUnit.SECONDS);
                }
            }
        }, 30, TimeUnit.SECONDS);
    }

    /** True only when the foundation and perspective are complete (or perspective is disabled). */
    private static synchronized boolean attempt(final Repository repository, final Session session,
        final LoggingCategory log) {
        try {
            ReferenceDataFoundationInstaller.install(repository, session, log);
            return ReferenceDataPerspectiveInstaller.install(repository, session, log);
        } catch (final Exception failure) {
            log.warn("[reference-data] bootstrap incomplete; will retry within the startup limit: "
                + failure.getMessage());
            return false;
        }
    }
}
