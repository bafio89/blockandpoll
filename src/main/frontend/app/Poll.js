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
import {green} from '@material-ui/core/colors';
import TextField from "@material-ui/core/TextField";
import withStyles from "@material-ui/core/styles/withStyles";

const useStyles = () => ({
  spaces: {
    margin: '15px'
  }
});

class Poll extends React.Component {

  constructor(props, context) {
    super(props, context);
    this.state = {selectedOption: '',
      poll: this.props.location.poll
    }
    this.handleChange = this.handleChange.bind(this);
    this.handleSenderChange = this.handleSenderChange.bind(this);
    this.handleMnemonicKeyChange = this.handleMnemonicKeyChange.bind(this);
    this.submitOptin = this.submitOptin.bind(this);
    this.submitVote = this.submitVote.bind(this);
  }

  submitOptin(){
    console.log(this.state.poll.appId)

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

  submitVote(){
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

  handleChange(event){
    this.setState({selectedOption: event.target.value});
  };

  handleSenderChange(event) {
    console.log(event.target.value)
    this.setState({sender: event.target.value})
  }

  handleMnemonicKeyChange(event) {
    this.setState({mnemonicKey: event.target.value})
  }

  render() {
    const {classes} = this.props;
    return <Grid container>
      <Grid item xs={3}/>
      <Grid item xs={6}>
        <Paper>
          <Grid container>
          <Grid item xs={7}>
          <Typography variant="h4" component="h2">
            {this.state.poll.name}
          </Typography>
          </Grid>
          <Grid item xs={5}>
            <Brightness1Icon style={{ color: green[500] }}/>
          </Grid>
          </Grid>
          <br/>
          <Typography variant="h6" component="h3">
            {this.state.poll.description}
          </Typography>
          <FormControl component="fieldset">
            <FormLabel component="legend">Express your vote</FormLabel>
            <RadioGroup aria-label="gender" name="gender1" value={this.state.selectedOption} onChange={this.handleChange}>
              <FormControlLabel value={this.state.poll.options[0]} control={<Radio />} label={this.state.poll.options[0]} />
              <FormControlLabel value={this.state.poll.options[1]} control={<Radio />} label={this.state.poll.options[1]} />
            </RadioGroup>
            <TextField id="sender" label="Creator address"
                       variant="outlined" value={this.state.sender}
                       onChange={this.handleSenderChange}
                       className={classes.spaces}/>
            <TextField id="mnemonicKey" label="Passphrase"
                       variant="outlined"
                       value={this.state.mnemonicKey || ''}
                       onChange={this.handleMnemonicKeyChange}
                       className={classes.spaces}/>
          </FormControl>
        </Paper>
        <br/>
        <div style={{textAlign:'center'}}>
        <Button onClick={this.submitOptin} variant="contained" color="primary" className={classes.spaces}>Opt In</Button>
        <Button onClick={this.submitVote} variant="contained" color="primary" className={classes.spaces}>Vote</Button>
        </div>
      </Grid>
      <Grid item xs={3}/>
    </Grid>
  }
}

export default withStyles(useStyles)(Poll);