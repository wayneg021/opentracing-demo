package io.wayne.demoopentracing;

import com.datastax.driver.core.Cluster;
import io.jaegertracing.internal.JaegerTracer;
import io.opentracing.Tracer;
import io.wayne.demoopentracing.tracing.TracingCluster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.AbstractClusterConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraCqlSessionFactoryBean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = { "io.wayne.demoopentracing" })
public class CassandraConfiguration {

    Tracer tracer = initTracer("cassandra");

    @Bean
    public TracingCluster cluster() {
        Cluster.Builder builder = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).withoutJMXReporting();

        TracingCluster cluster = new TracingCluster(builder, tracer);

        return cluster;
    }


    @Bean
    public CassandraCqlSessionFactoryBean session() {
        CassandraCqlSessionFactoryBean session = new CassandraCqlSessionFactoryBean();
        session.setCluster(cluster());
        session.setKeyspaceName("testkeyspace");

        return session;
    }

    private static JaegerTracer initTracer(String service){
        io.jaegertracing.Configuration.SamplerConfiguration samplerConfiguration =
                io.jaegertracing.Configuration.SamplerConfiguration.fromEnv().withType("const").withParam(1);
        io.jaegertracing.Configuration.ReporterConfiguration reporterConfiguration =
                io.jaegertracing.Configuration.ReporterConfiguration.fromEnv().withLogSpans(true);
        io.jaegertracing.Configuration config = new io.jaegertracing.Configuration(service).withSampler(samplerConfiguration).withReporter(reporterConfiguration);

        return config.getTracer();
    }
}
