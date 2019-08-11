package io.wayne.demoopentracing.tracing;

public interface QuerySpanNameProvider {

    public interface Builder {
        QuerySpanNameProvider build();
    }

    String querySpanName(String query);
}
