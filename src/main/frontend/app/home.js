import ShowPoll from "./ShowPoll";
import React from "react";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import MenuBar from "./MenuBar";

class Home extends React.Component {

  render() {
    return <div>
      <MenuBar/>
      <Grid container>
        <Grid item xs={2}/>
        <Grid item xs={8}>
          <Typography align={'center'} variant="h1"> Welcome in pollalgorand </Typography>
        </Grid>
        <Grid item xs={2}/>
      </Grid>
      <br/>

      <Grid container>
        <Grid item xs={2}/>
        <Grid item xs={8} style={{'textAlign':'center'}}>
          <Button variant="contained" color="primary" href={"#/createpoll"}>Create a poll</Button>
        </Grid>
        <Grid item xs={2}/>
      </Grid>
      <br/>


      <Grid container>
        <Grid xs={3}/>
        <Grid xs={6}>
          <Grid container>
          <ShowPoll/>
          </Grid>
        </Grid>
        <Grid xs={3}/>
      </Grid>

    </div>
  }

}

export default Home;