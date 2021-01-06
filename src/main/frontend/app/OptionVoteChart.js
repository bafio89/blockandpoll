import * as React from "react";
import {CanvasJSChart} from "canvasjs-react-charts";

class OptionVoteChart extends React.Component{

  constructor(props, context) {
    super(props, context);
    this.state = {
      optionsVotes: this.props.optionsVotes,
      options: ''
    }
  }

  render() {

    let dataPoints = []
    this.props.optionsVotes ? Object.keys(
         this.props.optionsVotes).map( (row) =>
            dataPoints.push({label: row, y: this.props.optionsVotes[row]})
        ) : '';

    const options = {
      data: [
          {
            type: "column",
            dataPoints: dataPoints
          }]
    }
    return (
          <CanvasJSChart containerProps={{height:'300px', width: '150%'}} options = {options}/>
    );
  }
}

export default OptionVoteChart;