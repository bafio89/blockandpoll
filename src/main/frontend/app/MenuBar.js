import * as React from "react";
import {AppBar, IconButton, Toolbar} from "@material-ui/core";
import {MenuIcon} from "@material-ui/data-grid";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import withStyles from "@material-ui/core/styles/withStyles";

const useStyles = () => ({
  root: {
    flexGrow: 1,
  },
  title: {
    flexGrow: 1,
  }
});

class MenuBar extends React.Component {

  constructor(props) {
    super(props);
    this.state = {classes: ''}
  }

  render() {
    const {classes} = this.props;
    return <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <IconButton edge="start" color="inherit" aria-label="menu">
            <MenuIcon/>
          </IconButton>
          <a href={'/#'} style={{'textDecoration': 'none', 'color':'white', 'display':'contents'}}>
          <Typography variant="h6" className={classes.title}>
            Block 'n' Poll
          </Typography>
          </a>
            <Button color="inherit">Login</Button>
        </Toolbar>
      </AppBar>
    </div>
  }

}

export default withStyles(useStyles)(MenuBar);