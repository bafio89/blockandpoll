import ShowPoll from "./ShowPoll";
import React from "react";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";

class Home extends React.Component {

  render() {
    return <div>
      <Grid container>
        <Grid xs={2}/>
        <Grid xs={8}>
          <Typography variant="h1"> Welcome in pollalgorand </Typography>
        </Grid>
        <Grid xs={2}/>
      </Grid>
      <br/>

      <Grid container>
        <Grid xs={3}/>
        <Grid xs={6}>
          <ShowPoll/>
        </Grid>
        <Grid xs={3}/>
      </Grid>

    </div>
  }

}

export default Home;