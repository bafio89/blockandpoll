import React from "react";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import CardActions from "@material-ui/core/CardActions";
import makeStyles from "@material-ui/core/styles/makeStyles";

const useStyles = makeStyles({
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
  pos: {
    marginBottom: 12,
  },
});

class Poll extends React.Component{

  constructor(props) {
    super(props);
    this.state = { classes: ''}
  }

  render(){
    return <div>
      <Card className={useStyles.root} variant="outlined" style={{'maxWidth': '300px'}}>
        <CardContent>
          <Typography className={useStyles.title} color="textSecondary" gutterBottom>
            Permissionless
          </Typography>
          <Typography variant="h5" component="h2">
            {this.props.poll.name}
          </Typography>
          <Typography className={useStyles.pos} color="textSecondary">
            ongoing
          </Typography>
          <Typography variant="body2" component="p">
            {this.props.poll.description}
          </Typography>
        </CardContent>
        <CardActions>
          <Button size="small">Vote</Button>
        </CardActions>
      </Card>
    </div>


  }
}

export default Poll;