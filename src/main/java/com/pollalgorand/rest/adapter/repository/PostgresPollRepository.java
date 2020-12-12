package com.pollalgorand.rest.adapter.repository;

import static java.util.stream.Collectors.toList;

import com.pollalgorand.rest.PollEntity;
import com.pollalgorand.rest.adapter.exceptions.SavingToDbException;
import com.pollalgorand.rest.domain.model.BlockchainPoll;
import com.pollalgorand.rest.domain.repository.PollRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class PostgresPollRepository implements PollRepository {

  private Logger logger = LoggerFactory.getLogger(PostgresPollRepository.class);

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public static final String RETRIEVE_POLLS = "SELECT * FROM pollgorand.poll p order by p.id ASC";

  public static final String RETRIEVE_POLLS_OPTION = "SELECT * FROM pollgorand.poll_options where id_poll=";

  private final String INSERT_POLL = "INSERT INTO pollgorand.poll "
      + "(name, start_subscription_time, end_subscription_time,"
      + "start_voting_time, end_voting_time, description, app_id, sender) "
      + "VALUES (:NAME, :START_SUBSCRIPTION_TIME, :END_SUBSCRIPTION_TIME,"
      + ":START_VOTING_TIME, :END_VOTING_TIME, :DESCRIPTION, :APP_ID, :SENDER)";

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
      logger.error("An error occours trying to save the poll in the DB", e);
      throw new SavingToDbException(poll.getName(), e);
    }

  }

  @Override
  public List<BlockchainPoll> find() {

    List<PollEntity> entityPolls = jdbcTemplate.query(RETRIEVE_POLLS, new PollMapper());

    List<PollEntity> pollEntities = entityPolls.stream().map(this::getPoll)
        .collect(toList());

    return fromEntityToDomain(pollEntities);
  }

  private List<BlockchainPoll> fromEntityToDomain(List<PollEntity> pollEntities) {

    return pollEntities.stream().map(pollEntity -> new BlockchainPoll(pollEntity.getAppId(), pollEntity.getName(), pollEntity.getSender(),
        pollEntity.getStartSubscriptionTime(), pollEntity.getEndSubscriptionTime(), pollEntity.getStartVotingTime(), pollEntity.getEndVotingTime(),
        pollEntity.getOptions(), "", pollEntity.getDescription())).collect(toList());
  }

  private PollEntity getPoll(PollEntity entityPoll) {

    List<String> options = jdbcTemplate
        .query(RETRIEVE_POLLS_OPTION + entityPoll.getId(), new PollOptionMapper());

    entityPoll.setOptions(options);
    return entityPoll;
  }

  private static final class PollOptionMapper implements RowMapper<String> {
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    return rs.getString("option");
    }
  }

  private static final class PollMapper implements RowMapper<PollEntity> {
    public PollEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new PollEntity(
          rs.getLong("id"),
          rs.getString("name"),
          rs.getTimestamp("start_subscription_time").toLocalDateTime(),
          rs.getTimestamp("end_subscription_time").toLocalDateTime(),
          rs.getTimestamp("start_voting_time").toLocalDateTime(),
          rs.getTimestamp("end_voting_time").toLocalDateTime(),
          null,
          rs.getString("sender"),
          rs.getString("description"),
          Long.valueOf(rs.getString("app_id"))
          );
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
    params.addValue("START_SUBSCRIPTION_TIME", poll.getStartSubscriptionTime());
    params.addValue("END_SUBSCRIPTION_TIME", poll.getEndSubscriptionTime());
    params.addValue("START_VOTING_TIME", poll.getStartVotingTime());
    params.addValue("END_VOTING_TIME", poll.getEndVotingTime());
    params.addValue("DESCRIPTION", poll.getDescription());
    params.addValue("APP_ID", poll.getAppId());
    params.addValue("SENDER", poll.getSender());

    return params;
  }
}
