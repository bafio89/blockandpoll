import * as React from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";

class CreatePoll extends React.Component{

  render() {
    return (
        <div>
          <form className="createPoll" noValidate autoComplete="off" onSubmit={this.createPoll()}>
            <TextField id="sender" label="Creator address" variant="outlined" />
            <TextField id="mnemonicKey" label="Passphrase" variant="outlined" />
            <br/>
            <TextField id="pollName" label="Name" variant="outlined" />
            <br/>
            <TextField id="option1" label="Option 1" variant="outlined" />
            <br/>
            <TextField id="option2" label="Option 2" variant="outlined" />
            <br/>
            <TextField
                id="date"
                label="start subscription date"
                type="date"
                defaultValue=""
                className= "start subscription"
                InputLabelProps={{
                  shrink: true,
                }}
            />
            <TextField
                id="date"
                label="end subscription date"
                type="date"
                defaultValue=""
                className= "end subscription"
                InputLabelProps={{
                  shrink: true,
                }}
            />
            <TextField
                id="date"
                label="start voting date"
                type="date"
                defaultValue=""
                className= "start voting"
                InputLabelProps={{
                  shrink: true,
                }}
            />
            <TextField
                id="date"
                label="end voting date"
                type="date"
                defaultValue=""
                className= "end voting"
                InputLabelProps={{
                  shrink: true,
                }}
            />
            <Button variant="contained" color="primary" type="submit">
              Create poll
            </Button>
          </form>
        </div>
    );
  }

  createPoll() {
    return alert("ciao");
  }
}

export default CreatePoll;