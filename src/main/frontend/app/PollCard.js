import React from "react";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import {green, red} from "@material-ui/core/colors";
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
    fontSize: 14,
  },
  status: {
    marginBottom: 12,
    display: 'inline-block',
    verticalAlign: 'super',
    marginLeft: 6

  },
  icon: {
    display: 'inline-block',
    color: green[500]
  },
  redIcon: {
    display: 'inline-block',
    color: red[500]
  }
});

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
            <Typography variant="h6" gutterBottom>
              {this.props.poll.name}
            </Typography>
            <div className={useStyles.display}>
              <Brightness1Icon className={this.props.poll.pollStatus === 'Open'? classes.icon : classes.redIcon}/>
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