import * as React from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import moment from "moment";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";
import Paper from "@material-ui/core/Paper";
import MenuBar from "./MenuBar";

const nowValues = {
  now: moment().format("yyyy-MM-DD")
};

const useStyles = () => ({
  spaces: {
    margin: '15px'
  },
  size: {
    margin: '15px',
    display: 'flex'
  },
});

class CreatePoll extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      question: '',
      mnemonicKey: '',
      name: '',
      option1: '',
      option2: '',
      description: '',
      startSubDate: '',
      endSubDate: '',
      startVotingDate: '',
      endVotingDate: ''
    }

    this.handleQuestionChange = this.handleQuestionChange.bind(this);
    this.handleMnemonicKeyChange = this.handleMnemonicKeyChange.bind(this);
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleOption1Change = this.handleOption1Change.bind(this);
    this.handleOption2Change = this.handleOption2Change.bind(this);
    this.handleDescriptionChange = this.handleDescriptionChange.bind(this)
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

  handleQuestionChange(event) {
    console.log(event.target.value)
    this.setState({question: event.target.value})
  }

  handleMnemonicKeyChange(event) {
    this.setState({mnemonicKey: event.target.value})
  }

  handleDescriptionChange(event) {
    this.setState({description: event.target.value})
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
    this.setState({startSubDate: event.target.value})
  }

  handleEndSubDateChange(event) {
    this.setState({endSubDate: event.target.value})
  }

  handleStartVotingDateChange(event) {
    this.setState({startVotingDate: event.target.value})
  }

  handleEndVotingDateChange(event) {
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
            question: this.state.question,
            mnemonicKey: this.state.mnemonicKey,
            name: this.state.name,
            startSubscriptionTime: this.state.startSubDate + "T00:00:10",
            endSubscriptionTime: this.state.endSubDate + "T00:00:10",
            startVotingTime: this.state.startVotingDate + "T00:00:10",
            endVotingTime: this.state.endVotingDate + "T00:00:10",
            options: [this.state.option1, this.state.option2],
            description: this.state.description
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
          <MenuBar/>
          <Grid container>
            <Grid item xs={2}/>
            <Grid item xs={8}>
              <Typography align={'center'} variant="h1"> Create your
                poll </Typography>
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
                      style={{'textAlign': 'left'}}>
                  <TextField id="question" label="Poll question"
                             variant="outlined" value={this.state.question}
                             onChange={this.handleQuestionChange}
                             className={classes.size}/>
                  <TextField id="pollName" label="Name" variant="outlined"
                             value={this.state.name || ''}
                             onChange={this.handleNameChange}
                             className={classes.size}/>
                  <br/>
                  <TextField id="description" label="Description"
                             multiline
                             rows={4}
                             defaultValue="Default Value"
                             variant="outlined"
                             value={this.state.description || ''}
                             onChange={this.handleDescriptionChange}
                             className={classes.size}
                  />
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
                  <TextField id="mnemonicKey" label="Passphrase"
                             multiline
                             rows={3}
                             defaultValue="Default Value"
                             variant="outlined"
                             value={this.state.mnemonicKey || ''}
                             onChange={this.handleMnemonicKeyChange}
                             className={classes.size}
                  />
                  <br/>
                  <div style={{textAlign: 'center'}}>
                    <Button variant="contained" color="primary" type="submit"
                            className={classes.spaces}>
                      Create poll
                    </Button>
                  </div>
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