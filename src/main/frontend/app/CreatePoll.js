import * as React from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import moment from "moment";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import withStyles from "@material-ui/core/styles/withStyles";
import Paper from "@material-ui/core/Paper";

import MenuBar from "./MenuBar";
import {Alert} from "@material-ui/lab";

class CreatePoll extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      question: '',
      mnemonicKey: '',
      name: '',
      options: [{id: 0, idElement: 'Option 0', val: '', error: false},
        {id: 1, idElement: 'Option 1', val: '', error: false}],
      description: '',
      startSubDate: '',
      endSubDate: '',
      startVotingDate: '',
      endVotingDate: '',
      errorName: false,
      errorQuestion: false,
      errorDescription: false,
      errorOptions: false,
      errorMnemonicKey: false,
      alert: {display: 'none', text:'', severity: ''}
    }

    this.handleQuestionChange = this.handleQuestionChange.bind(this);
    this.handleMnemonicKeyChange = this.handleMnemonicKeyChange.bind(this);
    this.handleNameChange = this.handleNameChange.bind(this);
    this.handleOptionChange = this.handleOptionChange.bind(this);
    this.handleDescriptionChange = this.handleDescriptionChange.bind(this)
    this.handleStartSubDateChange = this.handleStartSubDateChange.bind(this);
    this.handleEndSubDateChange = this.handleEndSubDateChange.bind(this);
    this.handleStartVotingDateChange = this.handleStartVotingDateChange.bind(
        this);
    this.handleEndVotingDateChange = this.handleEndVotingDateChange.bind(this);
    this.appendInput = this.appendInput.bind(this);
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
    console.log(event.target);
    this.setState({name: event.target.value})
  }

  handleOptionChange(event) {
    let optionsArray = []
    this.state.options.map(option => option.idElement === event.target.id ?
        optionsArray.push({
          id: option.id,
          idElement: option.idElement,
          val: event.target.value,
          error: option.error
        }) : optionsArray.push(option))
    this.setState({options: optionsArray})
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

  appendInput() {
    let options = this.state.options
    options.push({
      id: this.state.options.length,
      idElement: "Option " + this.state.options.length,
      value: '',
      error: false
    })
    this.setState({options: options});

  }

  createPoll() {
    let optionsArray = []

    this.state.options.map(
        option => option.id < this.state.options.length - 1 && option.val !== ''
            ? optionsArray.push(option.val) : '')

    if(this.validateParams()){
      fetch("/createpoll/signedtx",
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
              options: optionsArray,
              description: this.state.description
            })
          }).then(this.handleResponse());
    }
  }

  handleResponse() {
    return (response) => {
      if (response.ok) {
        this.setState({
          alert: {
            display: 'flex',
            text: 'Success! Poll created correctly',
            severity: 'success'
          }
        })
      } else {
        this.setState({
          alert: {
            display: 'flex',
            text: 'Something goes wrong! Please retry',
            severity: 'error'
          }
        })
      }
    };
  }

  validateParams() {
    let canSubmit = true
    if (this.state.question === '') {
      this.setState({errorQuestion: true})
      canSubmit = false
    }
    if (this.state.name === '') {
      this.setState({errorName: true})
      canSubmit = false
    }
    if (this.state.description === '') {
      this.setState({errorDescription: true})
      canSubmit = false
    }
    if (this.state.mnemonicKey === '') {
      this.setState({errorMnemonicKey: true})
      canSubmit = false
    }
    let optionsArray = []

    if (this.state.options.length === 2) {
      this.state.options.map(option => {
        if (option.val === '') {
          optionsArray.push(this.buildErrorOption(option))
          canSubmit = false
        } else {
          optionsArray.push(this.buildValidOption(option))
        }
      })
    } else {
      this.state.options.map(option => {
        if (option.id < this.state.options.length - 1 && (option.val
            === '' || option.val === undefined)) {
          optionsArray.push(this.buildErrorOption(option))
          canSubmit = false
        } else {
          optionsArray.push(this.buildValidOption(option))
        }
      })
    }
    this.setState({options: optionsArray})
    return canSubmit
  }

  buildErrorOption(option) {
    return {
      id: option.id,
      idElement: option.idElement,
      val: option.val,
      error: true
    };
  }

  buildValidOption(option) {
    return {
      id: option.id,
      idElement: option.idElement,
      val: option.val,
      error: false
    };
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
                  <TextField error={this.state.errorQuestion} required
                             id="question" label="Poll question"
                             variant="outlined" value={this.state.question}
                             onChange={this.handleQuestionChange}
                             className={classes.size}/>
                  <TextField error={this.state.errorName} required id="pollName"
                             label="Name" variant="outlined"
                             value={this.state.name || ''}
                             onChange={this.handleNameChange}
                             className={classes.size}/>
                  <br/>
                  <TextField error={this.state.errorDescription} required
                             id="description" label="Description"
                             multiline
                             rows={4}
                             defaultValue="Default Value"
                             variant="outlined"
                             value={this.state.description || ''}
                             onChange={this.handleDescriptionChange}
                             className={classes.size}
                  />
                  {this.state.options ? this.state.options.map(option =>
                      option.id === this.state.options.length - 1 ?
                          <TextField required error={option.error}
                                     id={option.idElement}
                                     label={option.idElement} variant="outlined"
                                     value={option.val}
                                     onClick={this.appendInput}
                                     onChange={this.handleOptionChange}
                                     className={classes.spaces}/> :
                          <TextField required error={option.error}
                                     id={option.idElement}
                                     label={option.idElement} variant="outlined"
                                     value={option.val}
                                     onChange={this.handleOptionChange}
                                     className={classes.spaces}/>
                  ) : ''}
                  <br/>
                  <TextField
                      required
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
                      required
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
                      required
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
                      required
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
                  <TextField
                      error={this.state.errorMnemonicKey}
                      required
                      id="mnemonicKey" label="Passphrase"
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
                    <Alert style={{display: this.state.alert.display}} severity={this.state.alert.severity}>{this.state.alert.text}</Alert>
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

export default withStyles(useStyles)(CreatePoll);