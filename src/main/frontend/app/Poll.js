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

class Poll extends React.Component {

  constructor(props, context) {
    super(props, context);
    this.state = {selectedOption: '',
      poll: this.props.location.poll
    }
    this.handleChange = this.handleChange.bind(this);
    this.submit = this.submit.bind(this);
  }

  submit(){
    console.log(this.state.selectedOption)
  }

  handleChange(event){
    this.setState({selectedOption: event.target.value});
  };

  render() {
    return <Grid container>
      <Grid item xs={3}/>
      <Grid item xs={6}>
        <Paper>
          <Typography variant="h4" component="h2">
            {this.state.poll.name}
          </Typography>
          <Brightness1Icon style={{ color: green[500] }}/>
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
          </FormControl>
        </Paper>
        <br/>
        <div style={{textAlign:'center'}}>
        <Button onClick={this.submit} variant="contained" color="primary">Vote</Button>
        </div>
      </Grid>
      <Grid item xs={3}/>
    </Grid>
  }
}

export default Poll