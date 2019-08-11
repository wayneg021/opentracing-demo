package io.wayne.demoopentracing.tracing;

import com.datastax.driver.core.*;
import com.google.common.util.concurrent.ListenableFuture;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;

import java.util.Map;

public class TracingSession implements Session {

    static final String COMPONENT_NAME = "java_cassandra";

    private final Session session;
    private final Tracer tracer;
    private final QuerySpanNameProvider querySpanNameProvider;

    public TracingSession(Session session, Tracer tracer, QuerySpanNameProvider querySpanNameProvider) {
        this.session = session;
        this.tracer = tracer;
        this.querySpanNameProvider = querySpanNameProvider;
    }

    @Override
    public String getLoggedKeyspace() {
        return session.getLoggedKeyspace();
    }

    @Override
    public Session init() {
        return new TracingSession(session.init(), tracer, querySpanNameProvider);
    }

    @Override
    public ListenableFuture<Session> initAsync() {
        return null;
    }

    @Override
    public ResultSet execute(String s) {
        return null;
    }

    @Override
    public ResultSet execute(String s, Object... objects) {
        return null;
    }

    @Override
    public ResultSet execute(String s, Map<String, Object> map) {
        return null;
    }

    @Override
    public ResultSet execute(Statement statement) {
        return null;
    }

    @Override
    public ResultSetFuture executeAsync(String s) {
        return null;
    }

    @Override
    public ResultSetFuture executeAsync(String s, Object... objects) {
        return null;
    }

    @Override
    public ResultSetFuture executeAsync(String s, Map<String, Object> map) {
        return null;
    }

    @Override
    public ResultSetFuture executeAsync(Statement statement) {
        return null;
    }

    @Override
    public PreparedStatement prepare(String s) {
        return null;
    }

    @Override
    public PreparedStatement prepare(RegularStatement regularStatement) {
        return null;
    }

    @Override
    public ListenableFuture<PreparedStatement> prepareAsync(String s) {
        return null;
    }

    @Override
    public ListenableFuture<PreparedStatement> prepareAsync(RegularStatement regularStatement) {
        return null;
    }

    @Override
    public CloseFuture closeAsync() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public Cluster getCluster() {
        return null;
    }

    @Override
    public State getState() {
        return null;
    }

    private Span buildSpan(String query) {
        String spanName = querySpanNameProvider.querySpanName(query);
        Tracer.SpanBuilder spanBuilder = tracer.buildSpan(spanName)
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT);

        Span span = spanBuilder.start();

        Tags.COMPONENT.set(span, COMPONENT_NAME);
        Tags.DB_STATEMENT.set(span, query);
        Tags.DB_TYPE.set(span, "cassandra");

        String keyspace = getLoggedKeyspace();
        if(keyspace != null) {
            Tags.DB_INSTANCE.set(span, keyspace);
        }

        return span;
    }
}
