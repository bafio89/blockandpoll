import * as React from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";

class CreatePoll extends React.Component{

  constructor(props) {
    super(props);
    this.state = {sender: ''}
    this.state = {mnemonicKey: ''}
    this.state = {name: ''}
    this.state = {option1: ''}
    this.state = {option2: ''}
    this.state = {startSubDate: ''}
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

  createPoll(event) {
    console.log("babab " + this.state.sender);
  }

  render() {
    return (
        <div>
          <form className="createPoll" noValidate autoComplete="off" onSubmit={this.createPoll}>
            <TextField id="sender" label="Creator address" variant="outlined" value={this.state.sender || ''} onChange={this.handleSenderChange}/>
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
                defaultValue=""
                className= "start subscription"
                InputLabelProps={{
                  shrink: true,
                }}
                value={this.state.startSubDate || ''}
                onChange={this.handleStartSubDateChange}
            />
            <TextField
                id="date"
                label="end subscription date"
                type="date"
                defaultValue=""
                className= "end subscription"
                InputLabelProps={{
                  shrink: true,
                }}
                value={this.state.endSubDate || ''}
                onChange={this.handleEndSubDateChange}
            />
            <TextField
                id="date"
                label="start voting date"
                type="date"
                defaultValue=""
                className= "start voting"
                InputLabelProps={{
                  shrink: true,
                }}
                value={this.state.startVotingDate || ''}
                onChange={this.handleStartVotingDateChange}
            />
            <TextField
                id="date"
                label="end voting date"
                type="date"
                defaultValue=""
                className= "end voting"
                InputLabelProps={{
                  shrink: true,
                }}
                value={this.state.endVotingDate || ''}
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