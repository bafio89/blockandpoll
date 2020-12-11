package com.pollalgorand.rest.adapter;

import com.pollalgorand.rest.adapter.repository.PostgresPollRepository;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class AdapterConfig {

  @Bean
  public PostgresPollRepository postgresPollRepository(DataSource algoproject){
    return new PostgresPollRepository(new NamedParameterJdbcTemplate(algoproject));
  }
}
