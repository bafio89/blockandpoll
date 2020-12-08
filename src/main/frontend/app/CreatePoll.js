import * as React from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import moment from "moment";

const nowValues = {
  now: moment().format("yyyy-MM-DD")
};

class CreatePoll extends React.Component{

  constructor(props) {
    super(props);
    this.state = {sender: ''}
    this.state = {mnemonicKey: ''}
    this.state = {name: ''}
    this.state = {option1: ''}
    this.state = {option2: ''}
    this.state = {startSubDate: nowValues}
    this.state = {endSubDate: ''}
    this.state = {startVotingDate: ''}
    this.state = {endVotingDate: ''}

    this.handleSenderChange = this.handleSenderChange.bind(this);
    this.handleMnemonicKeyChange = this.handleMnemonicKeyChange.bind(this);
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleOption1Change = this.handleOption1Change.bind(this);
    this.handleOption2Change = this.handleOption2Change.bind(this);
    this.handleStartSubDateChange = this.handleStartSubDateChange.bind(this);
    this.handleEndSubDateChange = this.handleEndSubDateChange.bind(this);
    this.handleStartVotingDateChange = this.handleStartVotingDateChange.bind(this);
    this.handleEndVotingDateChange = this.handleEndVotingDateChange.bind(this);

    this.createPoll = this.createPoll.bind(this);
  }

  handleSenderChange(event) {
    console.log(event.target.value)
    this.setState({sender: event.target.value})
  }

  handleMnemonicKeyChange(event){
    this.setState({mnemonicKey: event.target.value})
  }

  handleNameChange(event){
    this.setState({name: event.target.value})
  }

  handleOption1Change(event){
    this.setState({option1: event.target.value})
  }

  handleOption2Change(event){
    this.setState({option2: event.target.value})
  }

  handleStartSubDateChange(event){
    this.setState({startSubDate: event.target.value})
  }

  handleEndSubDateChange(event){
    this.setState({endSubDate: event.target.value})
  }

  handleStartVotingDateChange(event){
    this.setState({startVotingDate: event.target.value})
  }

  handleEndVotingDateChange(event){
    console.log(event.target.value)
    this.setState({endVotingDate: event.target.value})
  }

  createPoll(event) {console.log(this.state.startSubDate)
    return fetch("/createpoll/signedtx",
        {
          method: 'POST',
          headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            sender: this.state.sender,
            mnemonicKey: this.state.mnemonicKey,
            name: this.state.name,
            startSubscriptionTime: this.state.startSubDate + "T00:00:00",
            endSubscriptionTime: this.state.endSubDate + "T00:00:00",
            startVotingTime: this.state.startVotingDate + "T00:00:00",
            endVotingTime: this.state.endVotingDate + "T00:00:00",
            options: [this.state.option1, this.state.option2]
          })
        }).then(function (response) {console.log(response.body)
      if (response.ok) {
        response.text().then(function (data) {
          this.setState({
            txt: data
          });
        }.bind(this));
  }});
  }

  render() {
    return (
        <div>
          <form className="createPoll" noValidate autoComplete="off" onSubmit={this.createPoll}>
            <TextField id="sender" label="Creator address" variant="outlined" value={this.state.sender || ''} onChange={this.handleSenderChange} />
            <TextField id="mnemonicKey" label="Passphrase" variant="outlined" value={this.state.mnemonicKey || ''} onChange={this.handleMnemonicKeyChange}/>
            <br/>
            <TextField id="pollName" label="Name" variant="outlined" value={this.state.name || ''} onChange={this.handleNameChange}/>
            <br/>
            <TextField id="option1" label="Option 1" variant="outlined" value={this.state.option1 || ''} onChange={this.handleOption1Change}/>
            <br/>
            <TextField id="option2" label="Option 2" variant="outlined" value={this.state.option2 || ''} onChange={this.handleOption2Change}/>
            <br/>
            <TextField
                id="date"
                label="start subscription date"
                type="date"
                defaultValue={nowValues.now}
                className= "start subscription"
                InputLabelProps={{
                  shrink: true,
                }}
                onChange={this.handleStartSubDateChange}
            />
            <TextField
                id="date"
                label="end subscription date"
                type="date"
                defaultValue={nowValues.now}
                className= "end subscription"
                InputLabelProps={{
                  shrink: true,
                }}
                onChange={this.handleEndSubDateChange}
            />
            <TextField
                id="date"
                label="start voting date"
                type="date"
                defaultValue={nowValues.now}
                className= "start voting"
                InputLabelProps={{
                  shrink: true,
                }}
                onChange={this.handleStartVotingDateChange}
            />
            <TextField
                id="date"
                label="end voting date"
                type="date"
                defaultValue={nowValues.now}
                className= "end voting"
                InputLabelProps={{
                  shrink: true,
                }}
                onChange={this.handleEndVotingDateChange}
            />
            <Button variant="contained" color="primary" type="submit">
              Create poll
            </Button>
          </form>
        </div>
    );
  }

}

export default CreatePoll;