import React from "react";
import PollCard from "./PollCard";
import Grid from "@material-ui/core/Grid";

class ShowPoll extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      polls: []
    }
  }

  componentDidMount() {
    this.fetchPoll();
  }

  fetchPoll() {
    fetch("/polls").then(function (response) {
      if (response.ok) {
        response.json().then(function (data) {
          this.setState({
            polls: data,
          });
        }.bind(this));
      } else {
        throw new Error(response.status);
      }
    }.bind(this));
  }

  render() {
    this.state.polls.map(poll => console.log("polls " + poll.appId));
    return this.state.polls.map(poll => (
        <Grid key={poll.appId} item xs={4}>
          <PollCard poll={poll}/>
        </Grid>
    ))
  }
}

export default ShowPoll;

