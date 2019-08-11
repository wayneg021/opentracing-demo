package io.wayne.demoopentracing.tracing;

import com.datastax.driver.core.Cluster;
import io.opentracing.Tracer;

import java.util.concurrent.ExecutorService;

public class TracingCluster extends Cluster {

    private final Tracer tracer;
    private final ExecutorService executorService;

    public TracingCluster(Initializer initializer, Tracer tracer, ExecutorService executorService) {
        super(initializer);
        this.tracer = tracer;
        this.executorService = executorService;
    }


}
