import * as React from "react";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControl from "@material-ui/core/FormControl";
import FormLabel from "@material-ui/core/FormLabel";
import Radio from "@material-ui/core/Radio";
import Button from "@material-ui/core/Button";
import Brightness1Icon from '@material-ui/icons/Brightness1';
import {green, red, yellow} from '@material-ui/core/colors';
import TextField from "@material-ui/core/TextField";
import withStyles from "@material-ui/core/styles/withStyles";
import Divider from "@material-ui/core/Divider";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import {getIconColor} from "./PollCard";
import MenuBar from "./MenuBar";
import OptionVoteChart from "./OptionVoteChart";
import {Alert} from "@material-ui/lab";
import {CircularProgress, LinearProgress} from "@material-ui/core";

const useStyles = () => ({
  spaces: {
    margin: '15px'
  },
  question: {
    marginTop: '-17px',
    marginLeft: '20px'
  },
  size: {
    margin: '15px',
    display: 'flex'
  },
  verticalBar: {
    display: 'inline-block'
  },
  greenIcon: {
    margin: '15px',
    verticalAlign: 'top',
    position: 'absolute',
    color: green[500]
  },
  redIcon: {
    margin: '15px',
    verticalAlign: 'top',
    position: 'absolute',
    color: red[500]
  },
  yellowIcon: {
    margin: '15px',
    verticalAlign: 'top',
    position: 'absolute',
    color: yellow[500]
  },
  circularProgress: {
    display: 'flex',
    // '& > * + *': {
    //   marginLeft: '2px',
    // },
    position: 'absolute',
    top: '34%',
    left: '50%',
    marginTop: '-50px',
    marginLeft: '-50px',
  }
});

class Poll extends React.Component {

  constructor(props, context) {
    super(props, context);
    this.state = {
      appId: this.props.location.pathname.replace("/poll/", ""),
      selectedOption: '',
      poll: '',
      optionsVotes: '',
      errorMnemonickey: false,
      alert: {display: 'none', text: '', severity: ''},
      linearBarDisplay: 'none'
    }

    this.handleChange = this.handleChange.bind(this);
    this.handleMnemonicKeyChange = this.handleMnemonicKeyChange.bind(this);
    this.submitOptin = this.submitOptin.bind(this);
    this.submitVote = this.submitVote.bind(this);
    this.getPoll = this.getPoll.bind(this);
  }

  componentDidMount() {
    this.getPoll()
  }

  getPoll() {
    fetch("/poll/" + this.state.appId)
    .then(function (response) {
      if (response.ok) {
        response.json().then(function (data) {
          console.log(data)
          this.setState({
            poll: data.blockchainPoll,
            optionsVotes: data.optionsVotes
          });
        }.bind(this));
      } else {
        throw new Error(response.status);
      }
    }.bind(this));
  }

  submitOptin() {
    this.setState({linearBarDisplay: 'flex'})
    if (this.validateParams()) {
      fetch("/optin/poll/" + this.state.poll.appId,
          {
            method: 'POST',
            headers: {
              "Accept": "application/json",
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              mnemonicKey: this.state.mnemonicKey,
            })
          }).then(this.handleResponse());
    }else{
      this.setState({linearBarDisplay: 'none'})
    }
  }

  submitVote() {
    console.log(this.state.poll.appId)
    this.setState({linearBarDisplay: 'flex'})
    if (this.validateParams()) {
      fetch("/vote/poll/" + this.state.poll.appId,
          {
            method: 'POST',
            headers: {
              "Accept": "application/json",
              "Content-Type": "application/json"
            },
            body: JSON.stringify({
              mnemonicKey: this.state.mnemonicKey,
              selectedOption: this.state.selectedOption
            })
          }).then(this.handleResponse());
    }else{
      this.setState({linearBarDisplay: 'none'})
    }
  }

  handleResponse() {
    return (response) => {
      this.setState({linearBarDisplay: 'none'})
      if (response.ok) {
        this.setState({
          alert: {
            display: 'flex',
            text: 'Success! Wait some seconds for page reloading',
            severity: 'success'
          }
        })
        setTimeout(function () {
          window.location.reload();
        }.bind(this), 5000)
      } else {
        console.log(this.state)
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
    if (this.state.selectedOption === '') {
      return false
    }
    if (this.state.mnemonicKey === '' || this.state.mnemonicKey === undefined) {
      this.setState({errorMnemonickey: true})
      return false
    } else {
      return true
    }
  }

  handleChange(event) {
    this.setState({selectedOption: event.target.value});
  };

  handleMnemonicKeyChange(event) {
    this.setState({mnemonicKey: event.target.value})
  }

  render() {
    const {classes} = this.props;
    return <div>
      <MenuBar/>
      <br/>
      {this.state.poll === '' ?
          <div className={classes.circularProgress} >
            <CircularProgress size={100}/>
          </div> :

          <Grid container>
            <Grid item xs={3}/>
            <Grid item xs={6}>
              <Paper>
                <Grid container>
                  <Grid item xs={7}>
                    <Typography variant="h4" component="h2"
                                className={classes.spaces}>
                      {this.state.poll.name}
                    </Typography>
                    <Typography variant="h5" component="h2"
                                color="textSecondary"
                                className={classes.question}>
                      {this.state.poll.question}
                    </Typography>
                  </Grid>
                  <Grid item xs={7}>
                    <Divider/>
                    <Typography variant="body1" className={classes.spaces}>
                      {this.state.poll.description}
                    </Typography>
                    <Typography variant="overline" className={classes.spaces}>
                      Number of people subscribed to this
                      poll: {this.state.optionsVotes
                        ? this.state.optionsVotes.subscribedAccountNumber : ''}
                    </Typography>
                    <br/>
                    <TableContainer component={Paper}>
                      <Table className={classes.table}
                             aria-label="customized table">
                        <TableHead>
                          <TableRow>
                            <TableCell>Options</TableCell>
                            <TableCell align="right">Votes</TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {this.state.optionsVotes ? Object.keys(
                              this.state.optionsVotes.optionsVotes).map(
                              (row) => (
                                  <TableRow key={row}>
                                    <TableCell component="th" scope="row">
                                      {row}
                                    </TableCell>
                                    <TableCell
                                        align="right">{this.state.optionsVotes.optionsVotes[row]}</TableCell>
                                  </TableRow>
                              )) : ''}
                        </TableBody>
                      </Table>
                    </TableContainer>
                    <OptionVoteChart optionsVotes={this.state.optionsVotes
                        ? this.state.optionsVotes.optionsVotes : ''}/>
                  </Grid>
                  <Grid item xs={1}>
                    <Divider/>
                    <Divider orientation={'vertical'}
                             className={classes.verticalBar}/>
                    <Brightness1Icon
                        className={this.state.poll ? getIconColor.call(this,
                            classes, this.state.poll.pollStatus) : ''}/>
                  </Grid>
                  <Grid item xs={4}>
                    <Divider/>
                    <div style={{textAlign: 'left'}}>
                      <Typography variant='overline' className={classes.spaces}>
                        The poll is {this.state.poll.pollStatus}
                      </Typography>
                    </div>
                    <
                        div style={{textAlign: 'left'}}>
                      <Typography variant='overline' className={classes.spaces}>
                        Subscription interval
                      </Typography>
                    </div>
                    <div style={{textAlign: 'left'}}>
                      <Typography variant='overline' className={classes.spaces}>
                        {this.state.poll ? new Date(
                            this.state.poll.startSubscriptionTime).toLocaleDateString()
                            : ''} - {this.state.poll ? new Date(
                          this.state.poll.endSubscriptionTime).toLocaleDateString()
                          : ''}
                      </Typography>
                    </div>
                    <div style={{textAlign: 'left'}}>
                      <Typography variant='overline' className={classes.spaces}>
                        Vote interval
                      </Typography>
                    </div>
                    <div style={{textAlign: 'left'}}>
                      <Typography variant='overline' className={classes.spaces}>
                        {this.state.poll ? new Date(
                            this.state.poll.startVotingTime).toLocaleDateString()
                            : ''} - {this.state.poll ? new Date(
                          this.state.poll.endVotingTime).toLocaleDateString()
                          : ''}
                      </Typography>
                    </div>
                  </Grid>
                </Grid>
                <Divider/>
                <FormControl component="fieldset" className={classes.size}>
                  <FormLabel component="legend">Express your vote</FormLabel>
                  <RadioGroup aria-label="gender" name="gender1"
                              value={this.state.selectedOption}
                              onChange={this.handleChange}>
                    {this.state.poll ? this.state.poll.options.map((option) =>
                        <FormControlLabel
                            value={option}
                            control={<Radio/>}
                            label={option}/>
                    ) : ''}
                  </RadioGroup>
                  <TextField id="mnemonicKey" label="Passphrase"
                             error={this.state.errorMnemonickey}
                             multiline
                             rows={4}
                             defaultValue="Default Value"
                             variant="outlined"
                             value={this.state.mnemonicKey || ''}
                             onChange={this.handleMnemonicKeyChange}
                             className={classes.size}
                  />
                </FormControl>
                <Alert style={{display: this.state.alert.display}}
                       severity={this.state.alert.severity}>{this.state.alert.text}</Alert>
                <LinearProgress style={{display: this.state.linearBarDisplay}}/>
                <div style={{textAlign: 'center'}}>
                  <Button onClick={this.submitOptin} variant="contained"
                          color="primary"
                          className={classes.spaces}>Opt In</Button>
                  <Button onClick={this.submitVote} variant="contained"
                          color="primary"
                          className={classes.spaces}>Vote</Button>
                </div>
              </Paper>
            </Grid>
            <Grid item xs={3}/>
          </Grid>
      }
    </div>
  }
}

export default withStyles(useStyles)(Poll);