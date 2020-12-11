package com.pollalgorand.rest.adapter.repository;

import com.pollalgorand.rest.adapter.exceptions.SavingToDbException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.PollRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class PostgresPollRepository implements PollRepository {

  private Logger logger = LoggerFactory.getLogger(PostgresPollRepository.class);

  private final NamedParameterJdbcTemplate jdbcTemplate;

  private final String INSERT_POLL = "INSERT INTO pollgorand.poll "
      + "(name, mnemonic_key, start_subscription_time, end_subscription_time,"
      + "start_voting_time, end_voting_time, description, app_id) "
      + "VALUES (:NAME, :MNEMONIC_KEY, :START_SUBSCRIPTION_TIME, :END_SUBSCRIPTION_TIME,"
      + ":START_VOTING_TIME, :END_VOTING_TIME, :DESCRIPTION, :APP_ID)";

  private final String INSERT_POLL_OPTIONS = "INSERT INTO pollgorand.poll_options "
      + "(id_poll, option)"
      + "VALUES (:ID_POLL, :OPTION)";

  public PostgresPollRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void save(BlockchainPoll poll){

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    try {
      jdbcTemplate.update(INSERT_POLL, pollParams(poll), keyHolder);
      Integer pollId = (Integer) keyHolder.getKeys().get("ID");
      poll.getOptions().forEach(
          option -> jdbcTemplate.update(INSERT_POLL_OPTIONS, pollOptionParam(option, pollId)));
    }catch (Exception e){
      logger.info("An error occours trying to save the poll in the DB", e);
      throw new SavingToDbException(poll.getName(), e);
    }

  }

  private MapSqlParameterSource pollOptionParam(String option, Integer pollId) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue("OPTION",option);
    params.addValue("ID_POLL",pollId);


    return params;
  }

  private MapSqlParameterSource pollParams(BlockchainPoll poll) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue("NAME", poll.getName());
    params.addValue("MNEMONIC_KEY", poll.getMnemonicKey());
    params.addValue("START_SUBSCRIPTION_TIME", poll.getStartSubscriptionTime());
    params.addValue("END_SUBSCRIPTION_TIME", poll.getEndSubscriptionTime());
    params.addValue("START_VOTING_TIME", poll.getStartVotingTime());
    params.addValue("END_VOTING_TIME", poll.getEndVotingTime());
    params.addValue("DESCRIPTION", poll.getDescription());
    params.addValue("APP_ID", poll.getAppId());

    return params;
  }
}
