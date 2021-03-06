package com.blockandpoll.rest.adapter.repository;

import static java.util.stream.Collectors.toList;

import com.blockandpoll.rest.PollEntity;
import com.blockandpoll.rest.adapter.exceptions.SavingToDbException;
import com.blockandpoll.rest.domain.model.BlockchainPoll;
import com.blockandpoll.rest.domain.repository.PollRepository;
import java.util.List;
import java.util.Optional;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class PostgresPollRepository implements PollRepository {

  private Logger logger = LoggerFactory.getLogger(PostgresPollRepository.class);

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public static final String RETRIEVE_POLLS_JOIN = "SELECT id, name, p.start_subscription_time, p.end_subscription_time,p.start_voting_time, p.end_voting_time, p.question,p.description, p.app_id, po.option FROM blockandpoll.poll p join blockandpoll.poll_options po on p.id = po.id_poll order by p.id ASC";

  private final String INSERT_POLL = "INSERT INTO blockandpoll.poll "
      + "(name, start_subscription_time, end_subscription_time,"
      + "start_voting_time, end_voting_time, description, app_id, question) "
      + "VALUES (:NAME, :START_SUBSCRIPTION_TIME, :END_SUBSCRIPTION_TIME,"
      + ":START_VOTING_TIME, :END_VOTING_TIME, :DESCRIPTION, :APP_ID, :QUESTION)";

  private final String RETRIEVE_POLL_BY_APP_ID = "SELECT id, name, p.start_subscription_time, p.end_subscription_time,p.start_voting_time, p.end_voting_time, p.question,p.description, p.app_id, po.option FROM blockandpoll.poll p join blockandpoll.poll_options po on p.id = po.id_poll WHERE app_id = ";

  private final String INSERT_POLL_OPTIONS = "INSERT INTO blockandpoll.poll_options "
      + "(id_poll, option)"
      + "VALUES (:ID_POLL, :OPTION)";

  public PostgresPollRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void save(BlockchainPoll poll) {

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    try {
      jdbcTemplate.update(INSERT_POLL, pollParams(poll), keyHolder);

      Integer pollId = (Integer) keyHolder.getKeys().get("ID");

      poll.getOptions().forEach(
          option -> jdbcTemplate.update(INSERT_POLL_OPTIONS, pollOptionParam(option, pollId)));
    } catch (Exception e) {
      logger.error("An error occours trying to save the poll in the DB", e);
      throw new SavingToDbException(poll.getName(), e.getMessage());
    }

  }

  @Override
  public List<BlockchainPoll> find() {

    return fromEntityToDomain(jdbcTemplate.query(RETRIEVE_POLLS_JOIN, blockchainPollMapper()));
  }

  @Override
  public Optional<BlockchainPoll> findBy(long appId) {

    return fromEntityToDomain(
        jdbcTemplate.query(RETRIEVE_POLL_BY_APP_ID + "'" + appId + "'", blockchainPollMapper()))
        .stream().findFirst();
  }

  private ResultSetExtractor<List<PollEntity>> blockchainPollMapper() {
    return JdbcTemplateMapperFactory
        .newInstance()
        .addKeys("id")
        .newResultSetExtractor(PollEntity.class);
  }

  private List<BlockchainPoll> fromEntityToDomain(List<PollEntity> pollEntities) {

    return pollEntities.stream().map(
        pollEntity -> new BlockchainPoll(pollEntity.getAppId(), pollEntity.getName(),
            pollEntity.getQuestion(),
            pollEntity.getStartSubscriptionTime(), pollEntity.getEndSubscriptionTime(),
            pollEntity.getStartVotingTime(), pollEntity.getEndVotingTime(),
            pollEntity.getOptions(), "", pollEntity.getDescription())).collect(toList());
  }

  private MapSqlParameterSource pollOptionParam(String option, Integer pollId) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue("OPTION", option);
    params.addValue("ID_POLL", pollId);

    return params;
  }

  private MapSqlParameterSource pollParams(BlockchainPoll poll) {

    MapSqlParameterSource params = new MapSqlParameterSource();

    params.addValue("NAME", poll.getName());
    params.addValue("START_SUBSCRIPTION_TIME", poll.getStartSubscriptionTime());
    params.addValue("END_SUBSCRIPTION_TIME", poll.getEndSubscriptionTime());
    params.addValue("START_VOTING_TIME", poll.getStartVotingTime());
    params.addValue("END_VOTING_TIME", poll.getEndVotingTime());
    params.addValue("DESCRIPTION", poll.getDescription());
    params.addValue("APP_ID", poll.getAppId());
    params.addValue("QUESTION", poll.getQuestion());

    return params;
  }
}
