import React from "react";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import {green, red, yellow} from "@material-ui/core/colors";
import Brightness1Icon from "@material-ui/icons/Brightness1";
import withStyles from "@material-ui/core/styles/withStyles";

const useStyles = () => ({
  root: {
    minWidth: 275,
  },
  bullet: {
    display: 'inline-block',
    margin: '0 2px',
    transform: 'scale(0.8)',
  },
  title: {
    marginBottom: 0,
  },
  status: {
    marginBottom: 12,
    display: 'inline-block',
    verticalAlign: 'super',
    marginLeft: 6

  },
  greenIcon: {
    display: 'inline-block',
    color: green[500]
  },
  redIcon: {
    display: 'inline-block',
    color: red[500]
  },
  yellowIcon: {
    display: 'inline-block',
    color: yellow[500]
  }
});

export function getIconColor(classes, pollStatus) {
  return pollStatus === 'Expired' ? classes.redIcon
      : pollStatus === 'Subscription open' ? classes.yellowIcon
          : classes.greenIcon;
}

class PollCard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {classes: ''}
  }

  render() {
    const {classes} = this.props;
    return <div style={{'alignText': 'left'}}>
      <a href={'#/poll/' + this.props.poll.appId}
         style={{'textDecoration': 'none'}}>
        <Card className={classes.root} variant="outlined"
              style={{'margin': '15px', 'height': '180px'}}>
          <CardContent>
            <Typography variant="h6" gutterBottom className={classes.title}>
              {this.props.poll.name}
            </Typography>
            <Typography color="textSecondary">
              {this.props.poll.question}
            </Typography>
            <div>
              <Brightness1Icon className={getIconColor.call(this, classes, this.props.poll.pollStatus)}/>
              <Typography className={classes.status} color="textSecondary">
                {this.props.poll.pollStatus}
              </Typography>
            </div>
            <Typography variant="body2" component="p">
              {this.props.poll.description}
            </Typography>
          </CardContent>
        </Card>
      </a>
    </div>

  }
}

export default withStyles(useStyles)(PollCard);