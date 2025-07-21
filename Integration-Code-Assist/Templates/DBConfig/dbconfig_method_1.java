import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DBConfig {
		
	@Bean(name = "{bean_name}")
	@ConfigurationProperties(prefix = "property_prefix")
	public DataSource deliniaDS() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "DbNameJdbcTemplate")
	public JdbcTemplate deliniatransjdbcTemplate(@Qualifier("{bean_name}") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}	
}
