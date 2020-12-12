import React from "react";
import Poll from "./Poll";

class ShowPoll extends React.Component{

  constructor(props) {
    super(props);
    this.state = {
      polls: []
    }
  }

  componentDidMount() {
    this.fetchPoll();
  }

  fetchPoll(){
    fetch("/polls").then(function(response){
      if (response.ok) {
        response.json().then(function (data) {
          this.setState({
            polls: data,
          });
        }.bind(this));
      }
      else {
        throw new Error(response.status);
      }
    }.bind(this));
  }

  render() {this.state.polls.map(poll => console.log(  "polls " + poll.name));
    return this.state.polls.map( poll => (<Poll poll={poll}/>))
  }
}

export default ShowPoll;

