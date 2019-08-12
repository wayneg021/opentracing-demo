package io.wayne.demoopentracing.tracing;

public class CustomStringSpanName implements QuerySpanNameProvider {

    private String customString;

    public static class Builder implements QuerySpanNameProvider.Builder {

        @Override
        public QuerySpanNameProvider build() {
            return new CustomStringSpanName("execute");
        }

        public QuerySpanNameProvider build(String customString){
            return new CustomStringSpanName(customString);
        }

    }

    CustomStringSpanName(String customString){
        if(customString == null){
            this.customString = "execute";
        } else {
            this.customString = customString;
        }
    }

    @Override
    public String querySpanName(String query) {
        return customString;
    }

    public static Builder newBuilder(){
        return new Builder();
    }
}
