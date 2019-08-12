package io.wayne.demoopentracing.tracing;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TracingCluster extends Cluster {

    private final Tracer tracer;
    private final ExecutorService executorService;
    private final QuerySpanNameProvider querySpanNameProvider;

    public TracingCluster(Initializer initializer){
        this(initializer, GlobalTracer.get());
    }

    public TracingCluster(Initializer initializer, Tracer tracer) {
        this(initializer, tracer, CustomStringSpanName.newBuilder().build("execute"));
    }

    public TracingCluster(Initializer initializer, QuerySpanNameProvider querySpanNameProvider) {
        this(initializer, GlobalTracer.get(), querySpanNameProvider);
    }

    public TracingCluster(Initializer initializer, Tracer tracer, QuerySpanNameProvider querySpanNameProvider) {
        this(initializer, tracer, querySpanNameProvider, Executors.newCachedThreadPool());
    }

    public TracingCluster(Initializer initializer, Tracer tracer, QuerySpanNameProvider querySpanNameProvider, ExecutorService executorService) {
        super(initializer);
        this.tracer = tracer;
        this.querySpanNameProvider = querySpanNameProvider;
        this.executorService = executorService;
    }

    @Override
    public Session newSession() {
        return new TracingSession(super.newSession(), tracer, querySpanNameProvider, executorService);
    }

    @Override
    public Session connect() {
        return super.connect();
    }

    @Override
    public Session connect(String keyspace) {
        return super.connect(keyspace);
    }

    @Override
    public ListenableFuture<Session> connectAsync() {
        return super.connectAsync();
    }

    @Override
    public ListenableFuture<Session> connectAsync(String keyspace) {
        return Futures.transform(super.connectAsync(keyspace), new Function<Session, Session>() {
            @Override
            public Session apply(Session session) {
                return new TracingSession(session, tracer,querySpanNameProvider, executorService);
            }
        });
    }
}
