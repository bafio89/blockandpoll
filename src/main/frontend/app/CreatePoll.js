import * as React from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import moment from "moment";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";
import Paper from "@material-ui/core/Paper";

const nowValues = {
  now: moment().format("yyyy-MM-DD")
};

const useStyles = () => ({
  spaces: {
    margin: '15px'
  }
});

class CreatePoll extends React.Component {

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
    this.handleStartVotingDateChange = this.handleStartVotingDateChange.bind(
        this);
    this.handleEndVotingDateChange = this.handleEndVotingDateChange.bind(this);
    this.createPoll = this.createPoll.bind(this);

  }

  componentDidMount() {
    this.setDefault()
  }

  setDefault() {
    this.setState({startSubDate: nowValues.now})
    this.setState({endSubDate: nowValues.now})
    this.setState({startVotingDate: nowValues.now})
    this.setState({endVotingDate: nowValues.now})
  }

  handleSenderChange(event) {
    console.log(event.target.value)
    this.setState({sender: event.target.value})
  }

  handleMnemonicKeyChange(event) {
    this.setState({mnemonicKey: event.target.value})
  }

  handleNameChange(event) {
    this.setState({name: event.target.value})
  }

  handleOption1Change(event) {
    this.setState({option1: event.target.value})
  }

  handleOption2Change(event) {
    this.setState({option2: event.target.value})
  }

  handleStartSubDateChange(event) {
    console.log("onload")
    this.setState({startSubDate: event.target.value})
  }

  handleEndSubDateChange(event) {
    this.setState({endSubDate: event.target.value})
  }

  handleStartVotingDateChange(event) {
    this.setState({startVotingDate: event.target.value})
  }

  handleEndVotingDateChange(event) {
    console.log(event.target.value)
    this.setState({endVotingDate: event.target.value})
  }

  createPoll() {
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
        }).then(function (response) {
      console.log(response.body)
      if (response.ok) {
        response.text().then(function (data) {
          this.setState({
            txt: data
          });
        }.bind(this));
      }
    });
  }

  render() {

    const {classes} = this.props;
    return (
        <div>
          <Grid container>
            <Grid item xs={2}/>
            <Grid item xs={8}>
              <Typography align={'center'} variant="h1"> Create your poll </Typography>
            </Grid>
            <Grid item xs={2}/>
          </Grid>
          <br/>
          <Grid container>
            <Grid item xs={3}/>
            <Grid item xs={6}>
              <Paper>
                <form className="createPoll" noValidate autoComplete="off"
                      onSubmit={this.createPoll}
                      style={{'textAlign': 'center'}}>
                  <TextField id="sender" label="Creator address"
                             variant="outlined" value={this.state.sender}
                             onChange={this.handleSenderChange}
                             className={classes.spaces}/>
                  <TextField id="mnemonicKey" label="Passphrase"
                             variant="outlined"
                             value={this.state.mnemonicKey || ''}
                             onChange={this.handleMnemonicKeyChange}
                             className={classes.spaces}/>
                  <TextField id="pollName" label="Name" variant="outlined"
                             value={this.state.name || ''}
                             onChange={this.handleNameChange}
                             className={classes.spaces}/>
                  <br/>
                  <TextField id="option1" label="Option 1" variant="outlined"
                             value={this.state.option1 || ''}
                             onChange={this.handleOption1Change}
                             className={classes.spaces}/>
                  <TextField id="option2" label="Option 2" variant="outlined"
                             value={this.state.option2 || ''}
                             onChange={this.handleOption2Change}
                             className={classes.spaces}/>
                  <br/>
                  <TextField
                      id="date"
                      label="start subscription date"
                      type="date"
                      InputLabelProps={{
                        shrink: true,
                      }}
                      value={this.state.startSubDate || ''}
                      onChange={this.handleStartSubDateChange}
                      className={classes.spaces}
                  />
                  <TextField
                      id="date"
                      label="end subscription date"
                      type="date"
                      className={classes.spaces}
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
                      className={classes.spaces}
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
                      className={classes.spaces}
                      InputLabelProps={{
                        shrink: true,
                      }}
                      value={this.state.endVotingDate || ''}
                      onChange={this.handleEndVotingDateChange}
                  />

                  <br/>
                  <Button variant="contained" color="primary" type="submit" className={classes.spaces}>
                    Create poll
                  </Button>
                  <br/>
                </form>
              </Paper>
            </Grid>
            <Grid item xs={3}/>
          </Grid>
        </div>
    );
  }

}

export default withStyles(useStyles)(CreatePoll);