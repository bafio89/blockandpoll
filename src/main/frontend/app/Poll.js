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

const useStyles = () => ({
  spaces: {
    margin: '15px'
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
  }
});

class Poll extends React.Component {

  constructor(props, context) {
    super(props, context);
    this.state = {
      appId: this.props.location.pathname.replace("/poll/", ""),
      selectedOption: '',
      poll: '',
      optionsVotes: ''
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
    return fetch("/optin/poll/" + this.state.poll.appId,
        {
          method: 'POST',
          headers: {
            "Accept": "application/json",
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            mnemonicKey: this.state.mnemonicKey,
          })
        });
  }

  submitVote() {
    console.log(this.state.poll.appId)

    return fetch("/vote/poll/" + this.state.poll.appId,
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
        });
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
                <Table className={classes.table} aria-label="customized table">
                  <TableHead>
                    <TableRow>
                      <TableCell>Options</TableCell>
                      <TableCell align="right">Votes</TableCell>
                    </TableRow>
                  </TableHead>
                  <OptionVoteChart optionsVotes={this.state.optionsVotes ? this.state.optionsVotes.optionsVotes : ''}/>
                  <TableBody>
                    {this.state.optionsVotes ? Object.keys(
                        this.state.optionsVotes.optionsVotes).map((row) => (
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
            </Grid>
            <Grid item xs={1}>
              <Divider/>
              <Divider orientation={'vertical'}
                       className={classes.verticalBar}/>
              <Brightness1Icon className={this.state.poll ? getIconColor.call(this, classes, this.state.poll.pollStatus) : ''}/>
            </Grid>
            <Grid item xs={4}>
              <Divider/>
              <div style={{textAlign: 'left'}}>
                <Typography variant='overline' className={classes.spaces}>
                  The poll is {this.state.poll.pollStatus}
                </Typography>
              </div><
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
                    this.state.poll.endVotingTime).toLocaleDateString() : ''}
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
              {this.state.poll ? this.state.poll.options.map( (option) =>
                  <FormControlLabel
                      value={option}
                      control={<Radio/>}
                      label={option}/>
              ) : ''}
            </RadioGroup>
            <TextField id="mnemonicKey" label="Passphrase"
                       multiline
                       rows={4}
                       defaultValue="Default Value"
                       variant="outlined"
                       value={this.state.mnemonicKey || ''}
                       onChange={this.handleMnemonicKeyChange}
                       className={classes.size}
            />
          </FormControl>
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
    </div>
  }
}

export default withStyles(useStyles)(Poll);