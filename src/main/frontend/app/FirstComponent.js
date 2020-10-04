import * as React from "react";

class FirstComponent extends React.Component {


  constructor(props) {
    super(props);
    this.state = {
      txt: ""
    }
  }

  componentDidMount() {

     fetch("/bo/helloworld").then(function (response) {console.log(" ciao " + response)
      if (response.ok) {
        response.text().then(function (data) {console.log(" a tutti" + data)
          this.setState({
            txt: data
          });
        }.bind(this));
      }
      else {
        throw new Error(response.status);
      }
    }.bind(this))
    .catch(function () {
      this.setState({
        txt: "Oops, something goes wrong!"
      });
    }.bind(this));
  }

  render() {
    return <h1>Algo App: {this.state.txt}</h1>
  }
}

export default FirstComponent;