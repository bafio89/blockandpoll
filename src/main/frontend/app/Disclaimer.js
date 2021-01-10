import Typography from "@material-ui/core/Typography";
import * as React from "react";

class Disclaimer extends React.Component{

  render() {
    return (
        <Typography align={'left'}
                    style={{
                      fontSize: '12px',
                      marginTop: '-14px',
                      marginLeft: '15px'
                    }}
                    color="textSecondary">
          Block'n'Poll does not hold your keys for you. We cannot access
          accounts, recover keys, reset passwords, nor reverse transactions.
          Protect your keys and you are responsible for your
          security. </Typography>
    )
  }
}

export default Disclaimer;